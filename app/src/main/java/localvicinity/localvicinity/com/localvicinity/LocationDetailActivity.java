package localvicinity.localvicinity.com.localvicinity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class LocationDetailActivity extends ActionBarActivity {

    String information;
    String longitude, latitude;
    String flag;
    String type;
    String _id;
    Toolbar toolbar;
    private GoogleMap map;
    public ArrayList<MyLocation> returnValues = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        Bundle extras = getIntent().getExtras();
        longitude = (String) extras.getSerializable("longitude");
        latitude = (String) extras.getSerializable("latitude");
        information = (String) extras.getSerializable("information");
        flag = (String) extras.getSerializable("incorrect");
        type = (String) extras.getSerializable("type");
        _id = (String) extras.getSerializable("_id");

        toolbar = (Toolbar) findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(information);

        final LatLng current_location = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.setMyLocationEnabled(true);
        Marker hamburg = map.addMarker(new MarkerOptions().position(current_location)
                .title(information));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(current_location, 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


        TextView longitudeTextView = (TextView) findViewById(R.id.longitude);
        TextView lat = (TextView) findViewById(R.id.latitude);
        TextView flagTextView = (TextView) findViewById(R.id.flag);
        longitudeTextView.setText("Longitude: " + longitude);
        lat.setText("Latitude: " + latitude);
        try {
            if (flag.equalsIgnoreCase("false")) {
                flagTextView.setText("This location is up to date!");
            }
            else
            {
                flagTextView.setTextColor(Color.RED);
                flagTextView.setText("This location has been marked as incorrect!");
            }
        }
        catch(NullPointerException e)
        {
            flagTextView.setText("This location is up to date!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_location_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Location marked as inaccurate",
                        Toast.LENGTH_SHORT).show();
                try {
                    updateContact(R.layout.activity_location_detail);
                }
                catch(UnknownHostException e)
                {
                    System.out.println("UnknownHostException Caught");
                }
        }
        return true;
    }

    public void updateContact(int activity_location_detail) throws UnknownHostException {
        //New MyLocation object
        MyLocation location = new MyLocation();
        //Set up MongoLab updating
        location.setDoc_id(_id);
        System.out.println(_id);
        location.setName(information);
        System.out.println(information);
        location.setLongitude(Double.parseDouble(longitude));
        System.out.println(longitude);
        location.setLatitude(Double.parseDouble(latitude));
        System.out.println(latitude);
        location.setType(type);
        System.out.println(type);
        location.setFlag("true");
        System.out.println(location.getFlag());
        MongoLabUpdateContact tsk = new MongoLabUpdateContact();
        tsk.execute(location);
        //Intent i = new Intent(this, ViewLocationsActivity.class);
        //startActivity(i);
        finish();
    }

    final class MongoLabUpdateContact extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            MyLocation myLocation = (MyLocation) params[0];

            try {
                //New QueryBuilder object
                QueryBuilder qb = new QueryBuilder();
                //Set up HttpURLConnection object and properties
                URL url = new URL(qb.buildLocationUpdateURL(myLocation.getDoc_id()));
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type",
                        "application/json");
                connection.setRequestProperty("Accept", "application/json");

                OutputStreamWriter osw = new OutputStreamWriter(
                        connection.getOutputStream());

                osw.write(qb.setLocationData(myLocation));
                osw.flush();
                osw.close();
                if (connection.getResponseCode() < 205) {

                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                e.getMessage();
                return false;
            }
        }
    }


    public void startDownloads() {
        //New instance of GetLocationAsyncTask
        GetLocationsAsyncTask task = new GetLocationsAsyncTask();
        //Try to set points
        try {
            returnValues = task.execute().get();
            PointsList p = ((PointsList) getApplicationContext());
            p.setPoints(returnValues);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //New SitesDownloadTask instance
        SitesDownloadTask sites = new SitesDownloadTask();
        //Execute this to download files
        sites.execute();
    }

    public class SitesDownloadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                //Download both files from CATA (one for stops and one for vehicles)
                Downloader.DownloadFromUrl("http://realtime.catabus.com/InfoPoint/rest/vehicles/getallvehicles", openFileOutput("busses.xml", Context.MODE_PRIVATE));
                Downloader.DownloadFromUrl("http://realtime.catabus.com/InfoPoint/rest/stops/getallstops", openFileOutput("stops.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
