package com.example.android.cakeapp.CART;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.cakeapp.R;

import java.util.ArrayList;

public class CartListActivity extends Activity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        Toast.makeText(getApplicationContext(),"Added to cart",Toast.LENGTH_LONG).show();
        Log.d("debug", "Inside Cart List activity");
        CartSQL cartSQL=new CartSQL(this);
        Cursor cakecursor=cartSQL.ReturnCakeCartList();
        if(cakecursor==null){
            Log.d("debug","cakecursor is null");
            return;
        }
        Log.d("debug","cakecursor count"+cakecursor.getCount());
        cakecursor.moveToFirst();
        ArrayList<CakeObject> cakeObjects=new ArrayList<CakeObject>();
        CakeObject temp;
        for (int i = 0; i < cakecursor.getCount(); i++) {
            temp = new CakeObject();
            temp.Name = cakecursor.getString(0);
            temp.Weight = cakecursor.getInt(1);
            temp.TotalPrice = cakecursor.getInt(2);
            Log.d("debug","i="+i+" CakeName= "+temp.Name+" Cake Weight:"+temp.Weight+" Cake TOtprice"+temp.TotalPrice);
            cakeObjects.add(i, temp);
            cakecursor.moveToNext();
        }

        listView = (ListView) findViewById(R.id.listView2);
        CartListAdapter cartListAdapter = new CartListAdapter(getApplicationContext(), R.layout.cakerow, cakeObjects);
        listView.setAdapter(cartListAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart_list, menu);
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
