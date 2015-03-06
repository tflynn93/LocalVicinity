package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/19/2015.
 */

import java.util.Comparator;

public class DistanceComporator implements Comparator<MyLocation> {

    @Override
    public int compare(MyLocation a, MyLocation b) {
        return a.getDistance() - b.getDistance(); // Ascending
    }
}