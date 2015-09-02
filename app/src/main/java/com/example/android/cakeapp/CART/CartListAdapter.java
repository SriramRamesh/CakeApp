package com.example.android.cakeapp.CART;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.cakeapp.R;

import java.util.ArrayList;

/**
 * Created by DELL on 20-08-2015.
 */
public class CartListAdapter extends ArrayAdapter<CakeObject> {
    private LayoutInflater mInflater;
    private int mViewResourceId;
    private static CartSQL cartSQL;
    public static ArrayList<CakeObject> cakeObjects=new ArrayList<CakeObject>();


    public CartListAdapter(Context context, int resource,ArrayList<CakeObject> cakeList) {
        super(context, resource);
        mInflater= (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        cakeObjects=cakeList;
        mViewResourceId=resource;
        cartSQL=new CartSQL(context);
    }

    @Override
    public int getCount() {
        return cakeObjects.size();
    }

    @Override
    public CakeObject getItem(int position) {

        return cakeObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.cakerow, null);

        }


        final TextView SNo = (TextView) v.findViewById(R.id.rowS_No);
        final TextView CakeName= (TextView) v.findViewById(R.id.rowCakeName);
        final TextView CakePrice=(TextView)v.findViewById(R.id.rowCakePrice);
        final TextView CakeWeight=(TextView)v.findViewById(R.id.rowCakeWeight);

        if(position==0){
            SNo.setText("S.No");
            CakeName.setText("Cake Name");
            CakePrice.setText("Price");
            CakeWeight.setText("Weight");
            return v;
        }

        final CakeObject temp = cakeObjects.get(position);
        if (temp != null) {

            SNo.setText(position+".");
            CakeName.setText(temp.Name);
            CakePrice.setText("₹"+temp.TotalPrice);
            CakeWeight.setText(temp.Weight+"kg");

        }

        return v;

    }

}
