package localvicinity.localvicinity.com.localvicinity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tim on 3/24/2015.
 */

public class LocationListAdapter extends BaseAdapter {

    private Context mContext;
    private int mLayout;
    private ArrayList<MyLocation> mArr;
    private LayoutInflater mInflater;

    //Constructor
    public LocationListAdapter(Context context, int layout, ArrayList<MyLocation> arr) {
        mContext = context;
        mLayout = layout;
        mArr = arr;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mArr.size();
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(mLayout, parent, false);
        }


        if(mArr.get(position).getFlag().equalsIgnoreCase("true")) {
            ImageView imageview = (ImageView) convertView.findViewById(R.id.imageView1);
            imageview.setImageResource(R.drawable.poop);
        }
        else
        {
            ImageView imageview = (ImageView) convertView.findViewById(R.id.imageView1);
            imageview.setImageResource(R.drawable.smiley);
        }

        //Set the text view
        TextView location = (TextView) convertView.findViewById(R.id.textView1);
        TextView distance = (TextView) convertView.findViewById(R.id.textView2);
        location.setText(mArr.get(position).getName() +  " " + mArr.get(position).getType());
        distance.setText(mArr.get(position).getDistance()+"m");
        //desc.setText("UUID: " + mArr.get(position).getString("UUID"));

        //Return the view
        return convertView;
    }

    public void updateList(ArrayList<MyLocation> newItems){
        mArr = newItems;
        notifyDataSetChanged();
    }
}
