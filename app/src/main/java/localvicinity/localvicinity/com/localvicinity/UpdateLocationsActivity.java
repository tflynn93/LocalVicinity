package localvicinity.localvicinity.com.localvicinity;

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

/**
 * This activity updates a given contact record
 * @author KYAZZE MICHAEL
 *
 */

public class UpdateLocationsActivity extends ActionBarActivity {

    EditText editText_last_name;
    EditText editText_phone;
    EditText editText_email;
    EditText editText_fname;

    String _id;
    String name;
    String longitude;
    String latitude;
    String type;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mToolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Update Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Record");
        editText_fname = (EditText) findViewById(R.id.editText_fname);
        editText_last_name = (EditText) findViewById(R.id.editText_last_name);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_phone = (EditText) findViewById(R.id.editText_phone);

        //Obtain details of the clicked contact
        Bundle getBundle = null;
        getBundle = this.getIntent().getExtras();
        _id = getBundle.getString("_id");
        name = getBundle.getString("name");
        longitude = getBundle.getString("longitude");
        latitude = getBundle.getString("latitude");
        type = getBundle.getString("type");

        editText_fname.setText(name);
        editText_last_name.setText(longitude);
        editText_email.setText(latitude);
        editText_phone.setText(type);
    }

    /**
     * Method that updates a given cloud contact
     * @param v
     * @throws UnknownHostException
     */
    public void updateContact(View v) throws UnknownHostException {
        MyLocation location = new MyLocation();
        location.setDoc_id(_id);
        location.setName(editText_fname.getText().toString());
        location.setLongitude(Double.parseDouble(editText_last_name.getText().toString()));
        location.setLatitude(Double.parseDouble(editText_email.getText().toString()));
        location.setType(editText_phone.getText().toString());
        MongoLabUpdateContact tsk = new MongoLabUpdateContact();
        tsk.execute(location);
        Intent i = new Intent(this, ViewLocationsActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * AsyncTask to update a given contact
     * @author KYAZZE MICHAEL
     *
     */
    final class MongoLabUpdateContact extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {
            MyLocation myLocation = (MyLocation) params[0];

            try {

                QueryBuilder qb = new QueryBuilder();
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
                if(connection.getResponseCode() <205)
                {

                    return true;
                }
                else
                {
                    return false;

                }

            } catch (Exception e) {
                e.getMessage();
                return false;

            }

        }

    }

}