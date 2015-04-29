package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/21/2015.
 */

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class UpdateLocationsActivity extends ActionBarActivity {

    //Initialize views and variables
    EditText editText_last_name;
    EditText editText_phone;
    EditText editText_email;
    EditText editText_fname;
    EditText editText_flag;
    String _id;
    String name;
    String longitude;
    String latitude;
    String type;
    String incorrect;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set content view and setup toolbar
        setContentView(R.layout.activity_update);
        mToolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Update Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Record");

        //Bind views
        editText_fname = (EditText) findViewById(R.id.editText_fname);
        editText_last_name = (EditText) findViewById(R.id.editText_last_name);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_phone = (EditText) findViewById(R.id.editText_phone);
        editText_flag = (EditText) findViewById(R.id.editText_flag);

        //Obtain details of the clicked contact
        Bundle getBundle = null;
        getBundle = this.getIntent().getExtras();
        _id = getBundle.getString("_id");
        name = getBundle.getString("name");
        longitude = getBundle.getString("longitude");
        latitude = getBundle.getString("latitude");
        type = getBundle.getString("type");
        incorrect = getBundle.getString("incorrect");

        editText_fname.setText(name);
        editText_last_name.setText(longitude);
        editText_email.setText(latitude);
        editText_phone.setText(type);
        editText_flag.setText(incorrect);
    }

    public void updateContact(View v) throws UnknownHostException {
        //New MyLocation object
        MyLocation location = new MyLocation();
        //Set up MongoLab updating
        location.setDoc_id(_id);
        location.setName(editText_fname.getText().toString());
        location.setLongitude(Double.parseDouble(editText_last_name.getText().toString()));
        location.setLatitude(Double.parseDouble(editText_email.getText().toString()));
        location.setType(editText_phone.getText().toString());
        location.setFlag(editText_flag.getText().toString());
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
}