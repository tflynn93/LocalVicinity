package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/10/2015.
 */

import android.app.Application;

import java.util.ArrayList;

public class PointsList extends Application {
    private ArrayList<MyLocation> points;

    public ArrayList<MyLocation> getPoints() {
        return points;
    }//End method

    public void setPoints(ArrayList<MyLocation> givenPoints) {
        points = givenPoints;
    }
}
