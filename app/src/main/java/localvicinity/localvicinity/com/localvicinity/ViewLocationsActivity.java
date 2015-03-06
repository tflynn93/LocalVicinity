package localvicinity.localvicinity.com.localvicinity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This activity retrieves the mongolab contacts and displays them in a listview.
 * @author KYAZZE MICHAEL
 *
 */

public class ViewLocationsActivity extends ListActivity{
    ArrayList<MyLocation> returnValues = new ArrayList();
    ArrayList<String> listItems = new ArrayList();
    Toolbar mToolbar;
    String valueTOUpdate_id;
    String valueTOUpdate_name;
    String valueTOUpdate_longitude;
    String valueTOUpdate_latitude;
    String valueTOUpdate_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_contacts);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("View Locations in Database");

        //Get your cloud contacts
        GetLocationsAsyncTask task = new GetLocationsAsyncTask();
        try {
            returnValues = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(MyLocation x: returnValues){
            //listItems.add(x.getDoc_id());
            listItems.add(x.getName() + " " + x.getType());
        }

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listItems));


    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String selectedValue = (String) getListAdapter().getItem(position);
        //Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
        selectedContact(selectedValue);

        Bundle dataBundle = new Bundle();
        dataBundle.putString("_id", valueTOUpdate_id);
        dataBundle.putString("name", valueTOUpdate_name);
        dataBundle.putString("longitude", valueTOUpdate_longitude);
        dataBundle.putString("latitude", valueTOUpdate_latitude);
        dataBundle.putString("type", valueTOUpdate_type);
        Intent moreDetailsIntent = new Intent(this,UpdateLocationsActivity.class);
        moreDetailsIntent.putExtras(dataBundle);
        startActivity(moreDetailsIntent);
    }

    /*
     * Retrieves the full details of a selected contact.
     * The details are then passed onto the Update Contacts Record.
     *
     * This is a quick way for demo purposes.
     * You should consider storing this data in a database, shared preferences or text file
     */

    public void selectedContact(String selectedValue){
        for(MyLocation x: returnValues){
            if(selectedValue.contains(x.getName())){
                valueTOUpdate_id = x.getDoc_id();
                valueTOUpdate_name = x.getName();
                valueTOUpdate_longitude = Double.toString(x.getLongitude());
                valueTOUpdate_latitude = Double.toString(x.getLatitude());
                valueTOUpdate_type = x.getType();
            }
        }

    }


}