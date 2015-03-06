package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 3/5/2015.
 */

public class LocationTypeDescriptor {

    public String typeDescription(LocationType lt) {
        switch (lt) {
            case BUS_STOP:
                return "Bus Stops";
            case COMPUTER_LAB:
                return "Computer Labs";
            case BATHROOM:
                return "Bathrooms";
            case HOSPITAL:
                return "Hospitals";
            case BAR:
                return "Bars";
            case BANK_ATM:
                return "Banks/ATMs";
            case PARKING_LOT:
                return "Parking Lots";
            case RESTAURANT:
                return "Restaurants";
            default:
                return ("Error!");
        }
    }

}
