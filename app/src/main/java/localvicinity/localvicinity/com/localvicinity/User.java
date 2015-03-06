package localvicinity.localvicinity.com.localvicinity;

/**
 * Created by Tim on 3/6/2015.
 */

import android.location.Location;

public class User extends Location {

    String name;

    public User(String provider) {
        super(provider);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
