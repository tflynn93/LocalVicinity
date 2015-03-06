package localvicinity.localvicinity.com.localvicinity;


import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tim on 2/13/2015.
 */
public class GetBusLocations {

    static final String KEY_LIST = "ArrayOfVehicleLocation";
    static final String KEY_BUS = "VehicleLocation";
    static final String KEY_DEST = "Destination";
    static final String KEY_LONG = "Longitude";
    static final String KEY_LAT = "Latitude";
    static final String KEY_ROUTE = "RouteId";
    static final String KEY_ONBOARD = "OnBoard";
    static final String KEY_LASTUPDATE = "LastUpdated";

    public static List<Bus> getStackSitesFromFile(Context ctx) {

        // List of bars that we will return
        List<Bus> specials;
        specials = new ArrayList<Bus>();

        //current StackSite while parsing
        Bus currentLocation = null;
        //current text value while parsing
        String curText = "";

        try {
            // Get our factory and parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            //System.out.println("Starting XML Parser");

            //Create file stream and buffered reader
            FileInputStream fis = ctx.openFileInput("busses.xml");

            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            //System.out.println("After fis and reader");

            //point to the parser of the file
            xpp.setInput(reader);

            //get eventType
            int eventType = xpp.getEventType();

            //Loop until we reach the end
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Get the current tagname
                String tagname = xpp.getName();
                //System.out.println(xpp.toString());
                //System.out.println("inside while loop + tagname =" + tagname);

                //React to different event types appropriately
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase(KEY_BUS)) {
                            //If we are starting a new <bar> block we need a new Bar object to represent it
                            currentLocation = new Bus();
                            currentLocation.setType("Bus Stop");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        //Parse the current text so we can use it in END_TAG event
                        curText = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(KEY_BUS)) {
                            //if </bar> then we are done with current bar add it to the list.
                            specials.add(currentLocation);
                        }
                        else if (tagname.equalsIgnoreCase(KEY_DEST)) {
                            currentLocation.setName(curText);
                            //System.out.println("Destination: " + curText);
                        } else if (tagname.equalsIgnoreCase(KEY_LAT)) {
                            currentLocation.setLatitude(Double.parseDouble(curText));
                            //System.out.println("Longitude: " + curText);
                        } else if (tagname.equalsIgnoreCase(KEY_LONG)) {
                            currentLocation.setLongitude(Double.parseDouble(curText));
                            //System.out.println("Latitude: " + curText);
                        }else if (tagname.equalsIgnoreCase(KEY_ROUTE)) {
                            currentLocation.setRoute_number(Integer.parseInt(curText));
                            //System.out.println("Latitude: " + curText);
                        }else if (tagname.equalsIgnoreCase(KEY_LASTUPDATE)) {
                            currentLocation.setLast_updated(curText);
                            //System.out.println("Latitude: " + curText);
                        }else if (tagname.equalsIgnoreCase(KEY_ONBOARD)) {
                            currentLocation.setOnBoard(Integer.parseInt(curText));
                            //System.out.println("Latitude: " + curText);
                        }
                        break;

                    default:
                        break;
                }
                //move on to next iteration
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return the populated list
        for (Iterator<Bus> iterator = specials.iterator(); iterator.hasNext();) {
            MyLocation bus = iterator.next();
        }

        //Randomize the order of the list
        //Collections.shuffle(specials);
        //Return the list
        return specials;
    }
}