package localvicinity.localvicinity.com.localvicinity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class AddLocationDetails extends ActionBarActivity {

    EditText editText_name, editText_longitude, editText_latitude;
    Button submit;
    Spinner location_type;
    Toolbar mToolbar;
    MarkerOptions marker;
    private GoogleMap mMap;
    private String[] arraySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_details);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Add Location Details");
        getSupportActionBar().setTitle("Add Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Bundle extras = getIntent().getExtras();
        double passLong = extras.getDouble("long");
        double passLat = extras.getDouble("lat");
        submit = (Button) findViewById(R.id.submit_button);
        editText_name = (EditText) findViewById(R.id.place_name);
        editText_longitude = (EditText) findViewById(R.id.longitude);
        editText_longitude.setText(Double.toString(passLong));
        editText_latitude = (EditText) findViewById(R.id.latitude);
        editText_latitude.setText(Double.toString(passLat));
        this.arraySpinner = new String[]{"Bank", "Bar", "Bathroom", "Bus Stop", "Computer Lab", "Hospital", "Parking", "Restaurant"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, arraySpinner);
        location_type = (Spinner) findViewById(R.id.location_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location_type.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    saveContact();

                    GetLocationsAsyncTask task = new GetLocationsAsyncTask();
                    try {
                        ArrayList<MyLocation> returnValues = new ArrayList<>();
                        returnValues = task.execute().get();
                        //System.out.println("Testing: " + returnValues.get(0).getName());
                        PointsList p = ((PointsList) getApplicationContext());
                        p.setPoints(returnValues);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(getApplicationContext(), "Added " + editText_name.getText().toString() + " to database!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddLocationDetails.this, ViewLocationsActivity.class);
                    startActivity(intent);
                    finish();

                } catch (UnknownHostException e) {
                    System.out.println("OH NO");
                }


            }
        });


    }
    public void saveContact() throws UnknownHostException {

        MyLocation saveMyLocation = new MyLocation();
        saveMyLocation.name = editText_name.getText().toString();
        saveMyLocation.latitude = Double.parseDouble(editText_latitude.getText().toString());
        saveMyLocation.longitude = Double.parseDouble(editText_longitude.getText().toString());
        saveMyLocation.location_type = location_type.getSelectedItem().toString();
        SaveAsyncTask tsk = new SaveAsyncTask();
        tsk.execute(saveMyLocation);
    }
}
