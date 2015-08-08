package com.example.android.cakeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.IOException;
import java.util.ArrayList;



public class MainActivity extends Activity {
    Spinner Bakery,Flavour,Weight;
    TextView WeightperKg;

    RequestParams params = new RequestParams();
    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    AsyncTask<Void, Void, String> createRegIdTask;

    public static final String REG_ID = "regId";
    public static final String EMAIL_ID = "eMailId";
    EditText emailET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        applicationContext = getApplicationContext();
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if("".equals(registrationId)) {
            if(checkPlayServices()){
                registerInBackground();
            }
        }
        WeightperKg=(TextView)findViewById(R.id.SetWeight);

        Bakery=(Spinner)findViewById(R.id.BakeryName);
        ArrayList<String> BakeryNames = new ArrayList<String>();
        BakeryNames.add("Choose your Bakery");
        BakeryNames.add("Classic Bakery");
        BakeryNames.add("SC Bakery");
        BakeryNames.add("CCD");
        ArrayAdapter<String> BakeryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, BakeryNames);
        Bakery.setAdapter(BakeryAdapter);

        Flavour=(Spinner)findViewById(R.id.CakeFlavour);
        ArrayList<String> FlavourNames = new ArrayList<String>();
        FlavourNames.add("Choose your Flavour");
        FlavourNames.add("Chocolate");
        FlavourNames.add("Vanilla");
        FlavourNames.add("Butterscotch");
        ArrayAdapter<String> FlavourAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, FlavourNames);
        Flavour.setAdapter(FlavourAdapter);

        Flavour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getSelectedItem().toString();
                switch (item) {
                    case "Chocolate": {
                        WeightperKg.setText("600");
                        break;
                    }
                    case"Vanilla": {
                        WeightperKg.setText("400");
                        break;
                    }
                    case "Butterscotch": {
                        WeightperKg.setText("500");
                        break;
                    }
                    default: {
                        WeightperKg.setText("100");
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Weight = (Spinner) findViewById(R.id.WeightChoice);
        ArrayList<String> WeightValues = new ArrayList<String>();
        WeightValues.add("Choose weight");
        WeightValues.add("0.5");
        WeightValues.add("1.0");
        WeightValues.add("1.5");
        WeightValues.add("2.0");
        WeightValues.add("2.5");
        WeightValues.add("3.0");
        ArrayAdapter<String> WeightAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, WeightValues);
        Weight.setAdapter(WeightAdapter);
    }
    public void ContinueToOrder(View view){
        Intent intent=new Intent(MainActivity.this,GetDetails.class);
        float weight=Float.parseFloat(Weight.getSelectedItem().toString());
        float  price=weight*Float.parseFloat(WeightperKg.getText().toString());
        String FlavourName=Flavour.getSelectedItem().toString();
        intent.putExtra("Price", price);
        intent.putExtra("Weight",weight);
        intent.putExtra("Flavour",FlavourName);
        startActivity(intent);
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    regId = gcmObj
                            .register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    storeRegIdinSharedPref(applicationContext, regId);
                    Toast.makeText(
                            applicationContext,
                            "Registered with GCM Server successfully.\n\n"
                                    + msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            applicationContext,
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    // Store RegId and Email entered by User in SharedPref
    private void storeRegIdinSharedPref(Context context, String regId) {
        SharedPreferences prefs = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
         editor.commit();
        storeRegIdinServer(regId);

    }

    // Share RegID and Email ID with GCM Server Application (Php)
    private void storeRegIdinServer(String regId2) {

        params.put("regId", regId);
        System.out.println(" Reg Id = " + regId);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ApplicationConstants.APP_SERVER_URL, params,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        Toast.makeText(applicationContext,
                                "Reg Id shared successfully with Web App ",
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

    // Check if Google Playservices is installed in Device or not
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        applicationContext,
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();

            }
            return false;
        } else {
            Toast.makeText(
                    applicationContext,
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
