package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/13/2015.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class GetLocationsAsyncTask extends AsyncTask<MyLocation, Void, ArrayList<MyLocation>> {
    static BasicDBObject user = null;
    static String OriginalObject = "";
    static String server_output = null;
    static String temp_output = null;

    @Override
    protected ArrayList<MyLocation> doInBackground(MyLocation... arg0) {

        ArrayList<MyLocation> mylocations = new ArrayList<MyLocation>();

        QueryBuilder qb = new QueryBuilder();
        URL url = null;
        try {
            url = new URL(qb.buildLocationGetURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url
                    .openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Accept", "application/json");

        try {
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create a basic db list
        String mongoarray = "{ testdb: " + server_output + "}";
        Object o = com.mongodb.util.JSON.parse(mongoarray);
        DBObject dbObj = (DBObject) o;
        BasicDBList locations = (BasicDBList) dbObj.get("testdb");

        for (Object obj : locations) {

            DBObject userObj = (DBObject) obj;
            MyLocation temp = new MyLocation();

            try {
                temp.setDoc_id(userObj.get("_id").toString());
                temp.setName(userObj.get("name").toString());
                temp.setLongitude(Double.parseDouble(userObj.get("longitude").toString()));
                temp.setLatitude(Double.parseDouble(userObj.get("latitude").toString()));
                temp.setType(userObj.get("type").toString());
                if (userObj.get("type").toString().equalsIgnoreCase("Bus Stop")) {
                    temp.setLocationType(LocationType.BUS_STOP);
                } else if (userObj.get("type").toString().equalsIgnoreCase("Computer Lab")) {
                    temp.setLocationType(LocationType.COMPUTER_LAB);
                } else if (userObj.get("type").toString().equalsIgnoreCase("Restaurant")) {
                    temp.setLocationType(LocationType.RESTAURANT);
                } else if (userObj.get("type").toString().equalsIgnoreCase("Hospital")) {
                    temp.setLocationType(LocationType.HOSPITAL);
                } else if (userObj.get("type").toString().equalsIgnoreCase("Parking")) {
                    temp.setLocationType(LocationType.PARKING_LOT);
                } else if (userObj.get("type").toString().equalsIgnoreCase("Bar")) {
                    temp.setLocationType(LocationType.BAR);
                } else if (userObj.get("type").toString().equalsIgnoreCase("Bank")) {
                    temp.setLocationType(LocationType.BANK_ATM);
                } else if (userObj.get("type").toString().equalsIgnoreCase("Bathroom")) {
                    temp.setLocationType(LocationType.BATHROOM);
                }
            } catch (NullPointerException e) {
                System.out.println("Null Pointer Caught");
            }
            mylocations.add(temp);
        }
        return mylocations;
    }
}