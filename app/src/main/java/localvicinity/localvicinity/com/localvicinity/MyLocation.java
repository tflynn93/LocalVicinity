package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/10/2015.
 */

public class MyLocation implements Comparable {

    String doc_id;
    String name;
    double longitude;
    double latitude;
    String location_type;
    LocationType lt;
    int distance;

    public MyLocation() {

    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return location_type;
    }

    public void setType(String location_type) {
        this.location_type = location_type;
    }

    public LocationType getLocationType() {
        return lt;
    }

    public void setLocationType(LocationType lt) {
        this.lt = lt;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
