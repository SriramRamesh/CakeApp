package com.example.android.cakeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class OrderStatus extends Activity {

    static final String APP_SERVER_URL = "http://a0e28e79.ngrok.io/CakeAppServer/Register.php";

    Map<String, String> params = new HashMap<String, String>();
    GoogleCloudMessaging gcmObj;
    Context applicationContext;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    AsyncTask<Void, Void, String> createRegIdTask;

    public static final String REG_ID = "regId";


    String Name,Hostel;
    long RoomNo;
    String PhoneNo;
    float price,weight;
    String Flavour;
    TextView eName,eHostel,ePhone,eRoom,eWeight,ePrice,eFlavour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        applicationContext=getApplicationContext();
        eName=(TextView)findViewById(R.id.putName);
        eFlavour=(TextView)findViewById(R.id.putFlavour);
        eHostel=(TextView)findViewById(R.id.putHostel);
        ePhone=(TextView)findViewById(R.id.putPhone);
        ePrice=(TextView)findViewById(R.id.putPrice);
        eWeight=(TextView)findViewById(R.id.putWeight);
        eRoom=(TextView)findViewById(R.id.putRoomNo);

        Intent intent=getIntent();
        weight=intent.getFloatExtra("Weight",0);
        Flavour=intent.getStringExtra("Flavour");
        price=intent.getFloatExtra("Price", 0);

        Name=intent.getStringExtra("Name");
        RoomNo=intent.getLongExtra("RoomNo", 0);
        PhoneNo=intent.getStringExtra("PhoneNo");
        Hostel=intent.getStringExtra("Hostel");

        eName.setText("Name :"+Name);
        ePrice.setText("Price"+price);
        ePhone.setText("Phone No:"+PhoneNo);
        eRoom.setText("Room No"+RoomNo);
        eHostel.setText("Hostel :"+Hostel);
        eWeight.setText("Weight :"+weight);
        eFlavour.setText(Flavour);

    }
    public void OrderCake(View view){
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if("".equals(Name)||"".equals(registrationId)){
            Toast.makeText(getApplicationContext(),"No data",Toast.LENGTH_LONG).show();
        }

        params.put("regId", registrationId);
        params.put("Name", Name);
        params.put("Phone", PhoneNo);
        params.put("Room No", RoomNo+"");
        params.put("Hostel", Hostel);
        params.put("Flavour",Flavour);
        params.put("Weight",weight+"");
        params.put("Price", price + "");
        try {
            post(APP_SERVER_URL, params);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_status, menu);
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
    private static void post(String endpoint, Map<String, String> params)
            throws IOException {

        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v("debug", "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
