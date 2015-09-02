package com.example.android.cakeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cakeapp.CART.CartListActivity;
import com.example.android.cakeapp.CART.CartSQL;

/**
 * Created by DELL on 15-08-2015.
 */
public class FirstFragment extends Fragment {
    // Store instance variables
    private String cakeName,cakeDesc,cakePic;
    static Context context2;

    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance(Context context,String CakeName, String CakePic,String CakeDesc) {
        FirstFragment fragmentFirst = new FirstFragment();
        Bundle args = new Bundle();
        context2=context;
        args.putString("Cake Name", CakeName);
        args.putString("Cake Pic", CakePic);
        args.putString("Cake Desc",CakeDesc);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cakeName= getArguments().getString("Cake Name");
        cakeDesc = getArguments().getString("Cake Desc");
        cakePic=getArguments().getString("Cake Pic");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.firsrtfragment, container, false);
        final TextView vCakeName = (TextView) view.findViewById(R.id.cakeName);
        TextView vCakeDesc = (TextView) view.findViewById(R.id.CakeDesc);
        String uri="@drawable/"+cakePic;
        /*int imageResource = getResources().getIdentifier(uri, null, null);

        ImageView imageview= (ImageView)view.findViewById(R.id.CakePic);
        Drawable res = getResources().getDrawable(imageResource);
        imageview.setImageDrawable(res);
        */
        ImageView vCakePic=(ImageView)view.findViewById(R.id.CakePic);
        vCakePic.setImageResource(R.drawable.chocalaterambocake);
        vCakeName.setText(cakeName);
        vCakeDesc.setText(cakeDesc);

        ImageButton cart=(ImageButton)view.findViewById(R.id.cart);
        Button Add=(Button)view.findViewById(R.id.WeightAdd);
        Button Sub=(Button)view.findViewById(R.id.WeightSub);

        final TextView weight=(TextView)view.findViewById(R.id.Weight);
        //final String No=weight.getText().toString();


        Add.setOnClickListener(new View.OnClickListener() {
            String No=null;
            float temp;
            @Override
            public void onClick(View v) {
                No=weight.getText().toString();
                if(!("".equals(No))){
                    temp=Float.parseFloat(No);
                    Log.d("debug","temp"+temp);
                    if(temp<5){
                        temp+=0.5;
                    }
                    else{
                        Toast.makeText(context2,"Reached Maximum Limit",Toast.LENGTH_LONG).show();
                    }
                }
                weight.setText(""+temp);

            }
        });

        Sub.setOnClickListener(new View.OnClickListener() {
            String No=weight.getText().toString();
            float temp;
            @Override
            public void onClick(View v) {
                No=weight.getText().toString();
                if(!("".equals(No))){
                    temp=Float.parseFloat(No);
                    Log.d("debug","temp"+temp);
                    if(temp>0.5){
                        temp-=0.5;
                    }
                    else{
                        Toast.makeText(context2,"Reached Minimum Limit",Toast.LENGTH_LONG).show();
                    }
                }
                weight.setText(""+temp);

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(context2,CartListActivity.class);
                CartSQL c=new CartSQL(context2);
                String CakeName=vCakeName.getText().toString();
                float weightNo=Float.parseFloat(weight.getText().toString());
                float totalPrice=550;/*=c.getCakePrice(CakeName);
                totalPrice*=weightNo;*/
                c.AddCaketoCart(CakeName, weightNo, totalPrice);
                startActivity(in);
            }
        });

        return view;
    }
    public boolean isNumeric(String Num){
        int no;
        try{
            no=Integer.parseInt(Num);

        }catch(Exception e){
            return false;
        }
        return true;
    }
}
