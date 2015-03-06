package localvicinity.localvicinity.com.localvicinity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardGridView;


public class MainActivity extends ActionBarActivity{

    Toolbar toolbar;
    public ArrayList<MyLocation> returnValues = new ArrayList<>();
    ArrayList<Card> cards = new ArrayList<Card>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the layout
        setContentView(R.layout.activity_main);

        //Define the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Set supportActionBar and title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Local Vicinity");

        //If network connection exists then download new files
        if(isNetworkAvailable() == true) {
            startDownloads();
        }
        else//If no connection display error message to the user
        {
            Toast.makeText(getApplicationContext(), "No network connection available, results may be incorrect",
                    Toast.LENGTH_LONG).show();
        }

        //Create the 8 different category cards
        createMainCards();

        //Create the card for adding a location
        createAddCard();

        //Set the card grid array adapter
        CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(this,cards);

        //Bind the gridview to the adapter
        CardGridView gridView = (CardGridView) this.findViewById(R.id.myGrid);
        if (gridView!=null){
            gridView.setAdapter(mCardArrayAdapter);
        }
    }


    private void createMainCards()
    {
        //Create new CustomCard instances for the different categories
        CustomCard bus = new CustomCard(this, LocationType.BUS_STOP, R.drawable.bus, R.color.darkpurple);
        CustomCard computer = new CustomCard(this, LocationType.COMPUTER_LAB, R.drawable.lab, R.color.darkblue);
        CustomCard bathroom = new CustomCard(this, LocationType.BATHROOM, R.drawable.bath, R.color.green);
        CustomCard hospital = new CustomCard(this, LocationType.HOSPITAL, R.drawable.hospital, R.color.red);
        CustomCard bar = new CustomCard(this, LocationType.BAR, R.drawable.bar, R.color.orange);
        CustomCard bank = new CustomCard(this, LocationType.BANK_ATM, R.drawable.bank, R.color.blue);
        CustomCard parking = new CustomCard(this, LocationType.PARKING_LOT, R.drawable.parking, R.color.darkgreen);
        CustomCard restaurant = new CustomCard(this, LocationType.RESTAURANT, R.drawable.restaurant, R.color.purple);

        //Add all the cards to the arraylist
        cards.add(bus);
        cards.add(computer);
        cards.add(bathroom);
        cards.add(hospital);
        cards.add(bar);
        cards.add(bank);
        cards.add(parking);
        cards.add(restaurant);
    }

    private void createAddCard()
    {
        //Create new header and set the title
        CardHeader addLocationHeader = new CardHeader(this);
        addLocationHeader.setTitle("Add New Location");

        //Create new card instance
        Card addLocation = new Card(this);

        //Set thumbnail and drawable resource
        CardThumbnail addLocationThumb = new CardThumbnail(this);
        addLocationThumb.setDrawableResource(R.drawable.plus);

        //Set color
        addLocation.setBackgroundResourceId(R.color.darkred);

        //Add header
        addLocation.addCardHeader(addLocationHeader);

        //Add thumbnail
        addLocation.addCardThumbnail(addLocationThumb);

        //Set clickable
        addLocation.setClickable(true);

        //Add to arraylist
        cards.add(addLocation);

        //Set onClick listener
        addLocation.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                //Set intent to AddLocation screen
                Intent intent = new Intent(MainActivity.this, AddLocation.class);
                startActivity(intent);
            }
        });
    }

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void startDownloads()
    {
        //New instance of GetLocationAsyncTask
        GetLocationsAsyncTask task = new GetLocationsAsyncTask();
        //Try to set points
        try {
            returnValues = task.execute().get();
            PointsList p = ((PointsList)getApplicationContext());
            p.setPoints(returnValues);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //New SitesDownloadTask instance
        SitesDownloadTask sites = new SitesDownloadTask();
        //Execute this to download files
        sites.execute();
    }

    public class SitesDownloadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                //Download both files from CATA (one for stops and one for vehicles)
                Downloader.DownloadFromUrl("http://realtime.catabus.com/InfoPoint/rest/vehicles/getallvehicles", openFileOutput("busses.xml", Context.MODE_PRIVATE));
                Downloader.DownloadFromUrl("http://realtime.catabus.com/InfoPoint/rest/stops/getallstops", openFileOutput("stops.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
