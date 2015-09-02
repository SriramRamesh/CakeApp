package com.example.android.cakeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Start extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
    EditText Name,Phone,Room,Hostel;

    public void RegisterFirst(View view){
        Name=(EditText)findViewById(R.id.lgetName);
        Phone=(EditText)findViewById(R.id.lgetPhone);
        Room=(EditText)findViewById(R.id.lgetRoomNo);
        Hostel=(EditText)findViewById(R.id.lgetHostel);
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        /*if(isNumeric(Room.getText().toString())){
            Toast.makeText(getApplicationContext(),"Invalid Phone No or Room No",Toast.LENGTH_LONG).show();
            return;
        }*/
        editor.putString("Name", Name.getText().toString());
        editor.putString("Phone", Phone.getText().toString());
        editor.putString("Room No", Room.getText().toString());
        editor.putString("Hostel", Hostel.getText().toString());
        editor.putBoolean("LoggedIN",true);
        editor.apply();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }
    public void SignUp(View view){
        setContentView(R.layout.logiin);

    }


    public boolean isPhone(String No){
        float a;
        try {
            a=Float.parseFloat(No);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isNumeric(String No){
        int a;
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
        getMenuInflater().inflate(R.menu.menu_start, menu);
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
