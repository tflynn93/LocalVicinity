package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/13/2015.
 */

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GetBusStops {

    static final String KEY_LIST = "ArrayOfStop";
    static final String KEY_STOP = "Stop";
    static final String KEY_NAME = "Name";
    static final String KEY_LONG = "Longitude";
    static final String KEY_LAT = "Latitude";

    public static List<BusStop> getStackSitesFromFile(Context ctx) {

        // List of bars that we will return
        List<BusStop> specials;
        specials = new ArrayList<BusStop>();

        //current StackSite while parsing
        BusStop currentLocation = null;
        //current text value while parsing
        String curText = "";

        try {
            // Get our factory and parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            //System.out.println("Starting XML Parser");

            //Create file stream and buffered reader
            FileInputStream fis = ctx.openFileInput("stops.xml");

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
                        if (tagname.equalsIgnoreCase(KEY_STOP)) {
                            //If we are starting a new <bar> block we need a new Bar object to represent it
                            currentLocation = new BusStop();
                            currentLocation.setType("Bus Stop");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        //Parse the current text so we can use it in END_TAG event
                        curText = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase(KEY_STOP)) {
                            //if </bar> then we are done with current bar add it to the list.
                            specials.add(currentLocation);
                        } else if (tagname.equalsIgnoreCase(KEY_NAME)) {
                            currentLocation.setName(curText);
                            //System.out.println("Destination: " + curText);
                        } else if (tagname.equalsIgnoreCase(KEY_LAT)) {
                            currentLocation.setLatitude(Double.parseDouble(curText));
                            //System.out.println("Longitude: " + curText);
                        } else if (tagname.equalsIgnoreCase(KEY_LONG)) {
                            currentLocation.setLongitude(Double.parseDouble(curText));
                            //System.out.println("Longitude: " + curText);
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
        for (Iterator<BusStop> iterator = specials.iterator(); iterator.hasNext(); ) {
            BusStop bus = iterator.next();
        }

        //Randomize the order of the list
        //Collections.shuffle(specials);
        //Return the list
        return specials;
    }
}