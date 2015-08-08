package com.example.android.cakeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;


public class OrderStatus extends Activity {

    RequestParams params = new RequestParams();
    GoogleCloudMessaging gcmObj;
    Context applicationContext;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    AsyncTask<Void, Void, String> createRegIdTask;

    public static final String REG_ID = "regId";
    public static final String EMAIL_ID = "eMailId";
    EditText emailET;

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
        params.put("regId", registrationId);
        params.put("Name", Name);
        params.put("Phone", PhoneNo);
        params.put("Room No", RoomNo);
        params.put("Hostel", Hostel);
        params.put("Flavour",Flavour);
        params.put("Weight",weight);
        params.put("Price",price);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ApplicationConstants.APP_SERVER_URL, params,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        Toast.makeText(applicationContext,
                                "Sent Info ",
                                Toast.LENGTH_LONG).show();


                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(applicationContext,
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(applicationContext,
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    applicationContext,
                                    "Unexpected Error occcured! [Most common Error: Device might "
                                            + "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
}
