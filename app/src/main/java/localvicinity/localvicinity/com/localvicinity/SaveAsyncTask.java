package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/4/2015.
 */

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class SaveAsyncTask extends AsyncTask<MyLocation, Void, Boolean> {

    @Override
    protected Boolean doInBackground(MyLocation... arg0) {
        try {
            //New myLocation object
            MyLocation myLocation = arg0[0];

            //New QueryBuilder object
            QueryBuilder qb = new QueryBuilder();

            //HttpClient setup
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(qb.buildLocationSaveURL());

            //Set up json parsing
            StringEntity params = new StringEntity(qb.createLocation(myLocation));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() < 205) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            String val = e.getMessage();
            String val2 = val;
            return false;
        }
    }

}