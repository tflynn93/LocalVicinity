package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/10/2015.
 */

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class PointsList extends Application {
    private ArrayList<MyLocation> points;
    private LocationType lt;
    private List<BusStop> busStops;

    public ArrayList<MyLocation> getPoints() {
        return points;
    }//End method

    public void setPoints(ArrayList<MyLocation> givenPoints) {
        points = givenPoints;
    }

    public LocationType getLocationType() {
        return lt;
    }//End method

    public void setLocationType(LocationType givenlt) {
        lt = givenlt;
    }

    public List<BusStop> getBusStops()
    {
        return busStops;
    }

    public void setBusStops(List givenBusStops)
    {
        busStops = givenBusStops;
    }

}
