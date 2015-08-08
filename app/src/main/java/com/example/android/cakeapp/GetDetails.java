package com.example.android.cakeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class GetDetails extends ActionBarActivity {
    TextView Name_field,RoomNo_field,Hostel_field,Phone_field;
    String Name,Room,Hostel,Phone;
    long RoomNo;
    String Flavour;
    long PhoneNo;

    float weight=0,price=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);
        Intent intent=getIntent();
        weight=intent.getFloatExtra("Weight",0);
        price=intent.getFloatExtra("Price", 0);
        Flavour=intent.getStringExtra("Flavour");
        Name_field=(TextView)findViewById(R.id.putName);
        Phone_field=(TextView)findViewById(R.id.putPhone);
        RoomNo_field=(TextView)findViewById(R.id.putRoomNo);
        Hostel_field=(TextView)findViewById(R.id.putHostel);
    }
    public void Order(View view){
        Name=Name_field.getText().toString();
        Room=RoomNo_field.getText().toString();
        Phone=Phone_field.getText().toString();
        Hostel=Hostel_field.getText().toString();
        if(Name.length()==0||Room.length()==0||Phone.length()==0||Hostel.length()==0){
            Toast.makeText(getApplicationContext(),"Enter All Text Fields",Toast.LENGTH_LONG).show();
            return ;
        }
        if(!(isNumeric(Room))){
            Toast.makeText(getApplicationContext(),"Enter Nos in room and phone fields",Toast.LENGTH_LONG).show();
            return ;
        }
        RoomNo = Integer.parseInt(Room);
        //PhoneNo=Integer.parseInt(Phone);

        Intent intent=new Intent(GetDetails.this,OrderStatus.class);

        intent.putExtra("Weight",weight);
        intent.putExtra("Price",price);
        intent.putExtra("Flavour",Flavour);

        intent.putExtra("Name",Name);
        intent.putExtra("RoomNo",RoomNo);
        intent.putExtra("PhoneNo",Phone);
        intent.putExtra("Hostel",Hostel);

        startActivity(intent);

    }
    public boolean isNumeric(String No){
        long a;
        try {
            a=Integer.parseInt(No);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}