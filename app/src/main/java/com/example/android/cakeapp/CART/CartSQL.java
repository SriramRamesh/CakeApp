package com.example.android.cakeapp.CART;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DELL on 20-08-2015.
 */
public class CartSQL extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDatabase";


    public static String CartAddOnTable = "AddOn";
    public static String AddOnName="AddonNames";
    public static String AddOnsTotalPrice="TotalAddOns";


    public static String Key = "Sno";

    public static String CartCakeTable = "CartCakeTable";
    public static String CakeName = "CakeName";
    public static String CakeTotalPrice="TotalCakes";
    public static String CakeWeight = "Weight";

    public static String CakeServerTable="CakeServer";
    public static String CakePrice= "Price";

    public static String AddOnServerTable="AddOnsServer";
    public static String AddOnsPrice= "Price";
    public static String AddOnsQuantity="Quantity";


    SQLiteDatabase database;



    public CartSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CartCakeTable +
                " ( " + Key + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CakeName + " TEXT, "
                + CakeWeight + " REAL, "
                + CakeTotalPrice + " REAL " + ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CartAddOnTable +
                " (" + Key + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AddOnName + " TEXT, "
                + AddOnsQuantity + " INTEGER, "
                + AddOnsTotalPrice + " INTEGER " + ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CakeServerTable + " (" + CakeName + " TEXT, " + CakePrice + " INTEGER " + ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AddOnServerTable + " (" + AddOnName + " TEXT, " + AddOnsPrice + " INTEGER " + ");");

    }
    public int  getCakePrice(String gCakeName){
        try{
            database=getReadableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        String allColumns[]=new String[]{ CakeName,CakePrice };
        Cursor cursor=database.query(CakeServerTable,allColumns,null, null, null,null, null);
        if(cursor==null){
            Log.d("Debug", "Unable to get Cake Price ");
            return 0 ;
        }
        cursor.moveToFirst();
        int found=0;
        int pos=0;
        for(int i=0;i<cursor.getCount();i++){
            if(cursor.getString(0).equals(gCakeName)){
                found=1;
                pos=i;
                Log.d("Debug","Found"+gCakeName);
                break;
            }
            cursor.moveToPosition(i);
        }
        cursor.moveToPosition(pos);
        if(found==0)
        {
            return 0;
        }
        return cursor.getInt(1);
    }
    public int  getAddOnsPrice(String gAddOnName){
        try{
            database=getReadableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        String allColumns[]=new String[]{ AddOnName,AddOnsPrice };
        Cursor cursor=database.query(AddOnServerTable,allColumns,null, null, null,null, null);
        if(cursor==null){
            Log.d("Debug", "Unable to get Addons Price");
            return 0 ;
        }
        cursor.moveToFirst();
        int found=0;
        int pos=0;
        for(int i=0;i<cursor.getCount();i++){
            if(cursor.getString(0).equals(gAddOnName)){
                found=1;
                pos=i;
                Log.d("Debug","Found"+gAddOnName);
                break;
            }
            cursor.moveToPosition(i);
        }
        cursor.moveToPosition(pos);
        if(found==0)
        {
            return 0;
        }
        return cursor.getInt(1);
    }
    public void AddCaketoCart(String gCakeName,float gCakeWeigjht,float gTotalCakePrice ){
        try{
            database=getWritableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        ContentValues cv=new ContentValues();
        cv.put(CakeName, gCakeName);
        cv.put(CakeWeight,gCakeWeigjht);
        cv.put(CakeTotalPrice,gTotalCakePrice);
        Log.d("debug","CakeName ="+gCakeName+"CakeWeight ="+gCakeWeigjht+"CakePricetot= "+gTotalCakePrice);
        database.update(CartCakeTable, cv, null, null);
        Log.d("Debug", "Added Cake to Cart List");
    }
    public void AddOntoCart(String gAddOnsName,int gQuantity,String gTotalAddOnsPrice ){
        try{
            database=getWritableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        ContentValues cv=new ContentValues();
        cv.put(AddOnName,gAddOnsName);
        cv.put(AddOnsQuantity,gQuantity);
        cv.put(AddOnsTotalPrice,gTotalAddOnsPrice);
        database.update(CartAddOnTable, cv, null, null);
        Log.d("Debug", "Added AddONs");
    }

    public Cursor ReturnCakeCartList() {

        try {
            database = this.getReadableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        String allColumns[]=new String[]{ CakeName,CakeWeight,CakeTotalPrice};
        Cursor cursor=database.query(CartCakeTable,allColumns,null, null, null, null, null);
        if(cursor==null){
            return null;
        }
        return cursor;
    }
    public Cursor ReturnAddOnsCartList() {

        try {
            database = this.getReadableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        String allColumns[]=new String[]{ AddOnName,AddOnsQuantity,AddOnsTotalPrice};
        Cursor cursor=database.query(CartAddOnTable,allColumns,null, null, null, null, null);
        if(cursor==null){
            return null;
        }
        return cursor;
    }

    public void DropCartCake(){
        try {
            database = this.getReadableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        database.execSQL("DROP TABLE IF EXISTS"+CartCakeTable);
    }
    public void DropCartAddOns(){
        try {
            database = this.getReadableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        database.execSQL("DROP TABLE IF EXISTS"+CartAddOnTable);
    }
    // TODO:public void DeleterowCartCake public void DeleterowCartAdons
    @Override
    public void onUpgrade(SQLiteDatabase db,int old,int newNo){
        db.execSQL("DROP TABLE IF EXISTS"+CartCakeTable);
        db.execSQL("DROP TABLE IF EXISTS"+CartAddOnTable);
        onCreate(db);
    }


}