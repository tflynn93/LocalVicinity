package localvicinity.localvicinity.com.localvicinity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class LocationDetailActivity extends ActionBarActivity {

    String information;
    String longitude, latitude;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        Bundle extras = getIntent().getExtras();
        longitude = (String) extras.getSerializable("longitude");
        latitude = (String) extras.getSerializable("latitude");
        information = (String) extras.getSerializable("information");

        toolbar = (Toolbar) findViewById(R.id.include2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(information);


        TextView longitudeTextView = (TextView) findViewById(R.id.longitude);
        TextView lat = (TextView) findViewById(R.id.latitude);
        longitudeTextView.setText(longitude);
        lat.setText(latitude);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
