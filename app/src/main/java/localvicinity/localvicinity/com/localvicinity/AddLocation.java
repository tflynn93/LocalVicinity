package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 3/6/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddLocation extends ActionBarActivity {

    //Declare variables
    Button submit;
    TextView instructions;
    Toolbar mToolbar;
    MarkerOptions marker;
    private GoogleMap mMap;
    double latitude = 0;
    double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        //Set up toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Pick Location");
        getSupportActionBar().setTitle("Pick Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Hide keyboard by default
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Set up map for picking
        setUpMapIfNeeded();

        //Declare and setup instructions text
        instructions = (TextView) findViewById(R.id.instructions);

        //Declare submit button and set listener
        submit = (Button) findViewById(R.id.submit_button);
        submit.setVisibility(View.INVISIBLE);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Go to addlocationdetails class and pass long and lat
                Intent intent = new Intent(AddLocation.this, AddLocationDetails.class);
                intent.putExtra("long", marker.getPosition().longitude);
                intent.putExtra("lat", marker.getPosition().latitude);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //initialize variables
        double latitude = 0;
        double longitude = 0;

        //Set GoogleMap settings
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);

        //Declare LocationManager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);

        //Get last known location
        Location myLocation = locationManager.getLastKnownLocation(provider);
        try {
            latitude = myLocation.getLatitude();
        } catch (NullPointerException e) {
            //If last location not found default to State College
            latitude = 40.793643;
        }
        try {
            longitude = myLocation.getLongitude();
        } catch (NullPointerException e) {
            //If last location not found default to State College
            longitude = -77.86826296;
        }

        //Create new LatLng object and move camera there
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        LatLng myCoordinates = new LatLng(latLng.latitude, latLng.longitude);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 15);

        //Set up onclicklistener for the map to place points
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("New Location");
                //Hide instructions and set button visible
                instructions.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
                //Clear any existing markers and add new one
                mMap.clear();
                mMap.addMarker(marker);
            }
        });

    }
}
