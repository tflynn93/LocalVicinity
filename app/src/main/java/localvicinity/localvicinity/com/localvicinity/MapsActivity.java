package localvicinity.localvicinity.com.localvicinity;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ArrayList<MyLocation> points = new ArrayList<>(); //All of the points
    List<Bus> busList; //List of busses
    List<BusStop> busStopList; //Lists of bus stops
    LocationType lt;
    LocationTypeDescriptor ltd;
    Toolbar toolbar;
    private Handler mHandler;
    Location user;
    int bigcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        PointsList p = ((PointsList) getApplicationContext());
        points = p.getPoints();
        Bundle extras = getIntent().getExtras();
        //category = extras.getString("category");
        lt = (LocationType) extras.getSerializable("category");
        ltd = new LocationTypeDescriptor();
        getSupportActionBar().setTitle(ltd.typeDescription(lt));
        setUpMapIfNeeded();

        if (lt == LocationType.BUS_STOP) {
            this.mHandler = new Handler();
            m_Runnable.run();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (lt == LocationType.BUS_STOP) {
                    SitesDownloadTask s = new SitesDownloadTask();
                    s.execute();
                } else {
                    user.setLongitude(mMap.getMyLocation().getLongitude());
                    user.setLatitude(mMap.getMyLocation().getLatitude());
                    mMap.clear();
                    setUpMap();
                }
                return true;
            case R.id.action_settings:
                ;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setTrafficEnabled(true);

        int count = 0;
        double latitude = 0;
        double longitude = 0;

        BitmapDescriptor icon;

        if (lt == LocationType.COMPUTER_LAB) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.labicon);
        } else if (lt == LocationType.RESTAURANT) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.restauranticon);
        } else if (lt == LocationType.BUS_STOP) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.busstopicon);
        } else if (lt == LocationType.BANK_ATM) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.bankicon);
        } else if (lt == LocationType.BATHROOM) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.bathicon);
        } else if (lt == LocationType.PARKING_LOT) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.parkingicon);
        } else if (lt == LocationType.BAR) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.baricon);
        } else if (lt == LocationType.HOSPITAL) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.hospitalicon);
        } else {
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        }


        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

        //Toast.makeText(MapsActivity.this, count + " being displayed", Toast.LENGTH_SHORT).show();

        //mMap.addMarker(new MarkerOptions().position(nac).title(title).snippet(desc));

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, false);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        try {
            myLocation = mMap.getMyLocation();
        } catch (NullPointerException e) {

        }

        // set map type
        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        try {
            latitude = myLocation.getLatitude();
        } catch (NullPointerException e) {
            //latitude = 0.0;
            latitude = 40.793643;
        }

        // Get longitude of the current location
        try {
            longitude = myLocation.getLongitude();
        } catch (NullPointerException e) {
            //longitude = 0.0;
            longitude = -77.86826296;
        }

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        if (bigcount == 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        // Zoom in the Google Map
        if (bigcount == 0) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        LatLng myCoordinates = new LatLng(latLng.latitude, latLng.longitude);

        if (bigcount == 0) {
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myCoordinates, 15);
        }
        //mMap.animateCamera(yourLocation);


        user = new Location("User");
        user.setLongitude(latLng.longitude);
        user.setLatitude(latLng.latitude);


        Location l = new Location("Points");
        for (MyLocation loc : points) {
            try {
                l.setLatitude(loc.getLatitude());
                l.setLongitude(loc.getLongitude());

                loc.setDistance((int) user.distanceTo(l));
            } catch (NullPointerException e) {
                System.out.println("Null caught");
            }
        }

        Collections.sort(points, new DistanceComporator());
        count = 0;

        if (lt != LocationType.BUS_STOP) {
            for (MyLocation loc : points) {
                if (loc.getLocationType() == lt) {
                    if (count == 0) {
                        if (bigcount != 0) {
                            try {
                                System.out.println(Double.toString(loc.getLatitude()) + ": " + Double.toString(loc.getLongitude()));
                                Polyline line = mMap.addPolyline(new PolylineOptions()
                                        .add(new LatLng(loc.getLatitude(), loc.getLongitude()), new LatLng(user.getLatitude(), user.getLongitude()))
                                        .width(5)
                                        .color(Color.RED));
                            } catch (NullPointerException e) {
                                System.out.println("NULL HERE");
                            }
                        }
                    }
                    System.out.println("Add marker time!!!!!!!!");
                    mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).title(loc.getName()).snippet(loc.getType()).icon(icon));
                    count += 1;
                    bigcount += 1;
                }
            }
        }

    }


    private final Runnable m_Runnable = new Runnable() {
        public void run()

        {

            SitesDownloadTask s = new SitesDownloadTask();
            s.execute();
            MapsActivity.this.mHandler.postDelayed(m_Runnable, 30000);

        }

    };

    public class SitesDownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                Downloader.DownloadFromUrl("http://realtime.catabus.com/InfoPoint/rest/vehicles/getallvehicles", openFileOutput("busses.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mMap.clear();
            busList = GetBusLocations.getStackSitesFromFile(getApplicationContext());
            busStopList = GetBusStops.getStackSitesFromFile(getApplicationContext());


            //Location myLocation = mMap.getMyLocation();
            try {
                user.setLongitude(mMap.getMyLocation().getLongitude());
                user.setLatitude(mMap.getMyLocation().getLatitude());
            } catch (NullPointerException e) {
                System.out.println("Oopsies");
            }

            BitmapDescriptor icon2;
            icon2 = BitmapDescriptorFactory.fromResource(R.drawable.busstopicon);

            BitmapDescriptor icon;
            icon = BitmapDescriptorFactory.fromResource(R.drawable.busicon2);

            Location l3 = new Location("Busses");
            for (Bus bus : busList) {
                try {
                    l3.setLatitude(bus.getLatitude());
                    l3.setLongitude(bus.getLongitude());
                    bus.setDistance((int) user.distanceTo(l3));
                } catch (NullPointerException e) {
                }

            }

            Collections.sort(busList, new DistanceComporator());
            int count2 = 0;

            for (Bus loc : busList) {
                try {

                    if (count2 == 0 && bigcount != 0) {
                        System.out.println("DISTANCE: " + loc.getDistance());
                        Polyline line = mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(loc.getLatitude(), loc.getLongitude()), new LatLng(user.getLatitude(), user.getLongitude()))
                                .width(5)
                                .color(Color.GREEN));
                    }
                    count2 += 1;
                    if (loc.getName().equals("Invalid Route Number")) {

                    } else {
                        //System.out.println("Inside MapsActivity: " + loc.name);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).title(loc.getRoute_number(loc.getRoute_number())).snippet("Passengers: " + loc.getOnBoard()).icon(icon));
                    }
                } catch (NullPointerException e) {
                    System.out.println("Null caught");
                }
            }

            Location l2 = new Location("Stops");
            for (BusStop bs : busStopList) {
                try {
                    l2.setLatitude(bs.getLatitude());
                    l2.setLongitude(bs.getLongitude());
                    bs.setDistance((int) user.distanceTo(l2));
                } catch (NullPointerException e) {

                }
            }

            Collections.sort(busStopList, new DistanceComporator());

            int count = 0;
            for (BusStop loc : busStopList) {
                if (count == 0 && bigcount != 0) {
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(loc.getLatitude(), loc.getLongitude()), new LatLng(user.getLatitude(), user.getLongitude()))
                            .width(5)
                            .color(Color.RED));
                }
                //System.out.println("Inside MapsActivity: " + loc.name);
                mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).title(loc.getName()).snippet("Bus Stop").icon(icon2));
                count += 1;
                bigcount += 1;
                //System.out.println(loc.getName());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
