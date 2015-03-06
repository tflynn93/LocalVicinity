package localvicinity.localvicinity.com.localvicinity;

import java.util.Comparator;

/**
 * Created by Tim on 2/19/2015.
 */
public class DistanceComporator implements Comparator<MyLocation> {

        @Override public int compare(MyLocation a, MyLocation b) {
            return a.getDistance() - b.getDistance(); // Ascending
        }

}