package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 2/13/2015.
 */

public class Bus extends MyLocation {

    int route_number;
    String last_updated;
    int onBoard;

    public void Bus() {

    }

    public void setLast_updated(String last) {
        last_updated = last;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setOnBoard(int num) {
        onBoard = num;
    }

    public int getOnBoard() {
        return onBoard;
    }

    public void setRoute_number(int route) {
        route_number = route;
    }

    public int getRoute_number() {
        return route_number;
    }

    public String getRoute_number(int route) {
        String route_name;

        switch (route) {
            case 1:
                route_name = "A - Park Forest";
                break;
            case 4:
                route_name = "B - Boalsburg";
                break;
            case 7:
                route_name = "C - Houserville";
                break;
            case 10:
                route_name = "F - Pine Grove";
                break;
            case 11:
                route_name = "G - Stormstown";
                break;
            case 13:
                route_name = "HP - Toftrees/Scenery Park";
                break;
            case 16:
                route_name = "K - Cato Park";
                break;
            case 19:
                route_name = "M - Nittany Mall";
                break;
            case 22:
                route_name = "N - Martin St/Aaron Dr";
                break;
            case 21:
                route_name = "NE - Martin St/Aaron Dr Express";
                break;
            case 25:
                route_name = "NV - Martin St/Vairo Blvd";
                break;
            case 31:
                route_name = "R - Waupelani Dr";
                break;
            case 33:
                route_name = "RC - Waupelani Dr - Campus";
                break;
            case 34:
                route_name = "RP - Waupelani Dr - Downtown";
                break;
            case 37:
                route_name = "S - Science Park";
                break;
            case 40:
                route_name = "UT - University Terrace";
                break;
            case 43:
                route_name = "V - Vairo Blvd";
                break;
            case 42:
                route_name = "VE - Vairo Blvd Express";
                break;
            case 44:
                route_name = "VN - Toftrees Vairo Martin";
                break;
            case 46:
                route_name = "W - Valley Vista";
                break;
            case 45:
                route_name = "WE - Valley Vista Express";
                break;
            case 49:
                route_name = "XB - Bellefonte";
                break;
            case 50:
                route_name = "XG - Pleasant Gap";
                break;
            case 55:
                route_name = "Blue Loop";
                break;
            case 57:
                route_name = "White Loop";
                break;
            case 51:
                route_name = "Red Link";
                break;
            case 53:
                route_name = "Green Link";
                break;
            default:
                route_name = "Invalid Route Number";
                break;
        }
        return route_name;
    }
}