package localvicinity.localvicinity.com.localvicinity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LocationListActivity extends ActionBarActivity {

    ArrayList<MyLocation> points = new ArrayList<>();
    ArrayList<MyLocation> pointsListView = new ArrayList<>();
    Toolbar toolbar;
    LocationType lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        ListView listview = (ListView) findViewById(R.id.listView);
        final PointsList p = ((PointsList) getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Locations");

        Bundle extras = getIntent().getExtras();
        //category = extras.getString("category");

        try {
            lt = (LocationType) extras.getSerializable("category");
            p.setLocationType(lt);
        }
        catch(NullPointerException e) {
            lt = p.getLocationType();
        }

        points = p.getPoints();


        if(lt.toString().equalsIgnoreCase("Bus_Stop"))
        {
            List<BusStop> bsList = p.getBusStops();
            try {
                for (BusStop b : bsList) {
                    pointsListView.add(b);
                }
            }
            catch(NullPointerException e)
            {
                System.out.println("Null caught");
            }

        }

        for(MyLocation point: points)
        {
            if(point.getLocationType().toString().equalsIgnoreCase(lt.toString()))
            {
                pointsListView.add(point);
            }
        }
        Collections.sort(pointsListView, new DistanceComporator());

        LocationListAdapter adapter = new LocationListAdapter(getApplicationContext(), R.layout.list_item, pointsListView);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    Intent i = new Intent(LocationListActivity.this, LocationDetailActivity.class);
                    i.putExtra("information", pointsListView.get(position).getName());
                    i.putExtra("latitude", Double.toString(pointsListView.get(position).getLatitude()));
                    i.putExtra("longitude",Double.toString(pointsListView.get(position).getLongitude()));
                    i.putExtra("incorrect", pointsListView.get(position).getFlag());
                    i.putExtra("_id", pointsListView.get(position).getDoc_id());
                    i.putExtra("type", pointsListView.get(position).getType());
                    LocationListActivity.this.startActivity(i);
                    //MainActivity.this.finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LocationListActivity.this, MapsActivity.class);
        intent.putExtra("category", lt);
        startActivity(intent);
        return;
    }
}
