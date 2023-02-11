package com.appandroid.locationpromos.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.appandroid.locationpromos.models.BrandModel;
import com.appandroid.locationpromos.models.UserModel;
import com.appandroid.locationpromos.models.notification_user;

import java.util.ArrayList;

public class dbhelper {
    private SQLiteDatabase db;
    private MySqLiteHelper dbHelper;
    Context context;

    public dbhelper(Context context) {
        dbHelper = new MySqLiteHelper(context);
        //   db=dbHelper.getWritableDatabase();
        open();
        this.context=context;
    }
    public void open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }

    //insert
    public long Insert (ContentValues values,String nameTable) {
        long ID= db.insert(nameTable,null,values);
        return ID;
    }

    public Cursor query  (String[] projection, String selection, String[] selectionArgs,String nameTable) {

        SQLiteQueryBuilder qb= new SQLiteQueryBuilder();
        qb.setTables(nameTable);

        Cursor cursor=qb.query(db,projection,selection,selectionArgs,null,null,null);

        return cursor;

    }



    @SuppressLint("Range")
    public boolean emailExist(String email) {

        String[] columns = { "user_email","user_type" };
        String selection = "user_email" + " =?" ;
        String[] selectionArgs = { email };
        String limit = "1";
        Cursor cursor = db.query("user", columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    @SuppressLint("Range")
    public ArrayList<BrandModel> getallbrand() {

        ArrayList<BrandModel> mDataList= new ArrayList<>();
        Cursor cursor=null;
        cursor=query(null,null,   null,"brand");

        if(cursor.moveToFirst()){

            do {

                BrandModel brands= new BrandModel();
                brands.setBrand_id(cursor.getInt(cursor.getColumnIndex("_id")));
                brands.setRetailerId(cursor.getInt(cursor.getColumnIndex("retailerId")));
                brands.setBrand_title(cursor.getString(cursor.getColumnIndex("brand_title")));
                brands.setBrand_desc(cursor.getString(cursor.getColumnIndex("brand_desc")));
                brands.setNotification_text(cursor.getString(cursor.getColumnIndex("notification_text")));
                brands.setLogitude(cursor.getString(cursor.getColumnIndex("longitude")));
                brands.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                brands.setBrand_image(cursor.getBlob(cursor.getColumnIndex("brand_image")));

                mDataList.add(brands);

            }

            while(cursor.moveToNext()); }
        return mDataList;

    }

    @SuppressLint("Range")
    public ArrayList<notification_user> getAllNotification() {

        ArrayList<notification_user> mDataList= new ArrayList<>();
        Cursor cursor=null;
        cursor=query(null,null,   null,"user_notification");

        if(cursor.moveToFirst()){

            do {

                notification_user nu= new notification_user();
                nu.setBrand_id(cursor.getInt(cursor.getColumnIndex("brandId")));
                nu.setUser_id(cursor.getInt(cursor.getColumnIndex("userId")));


                mDataList.add(nu);

            }

            while(cursor.moveToNext()); }
        return mDataList;

    }



    @SuppressLint("Range")
    public BrandModel getBrandById(int brand_id) {

        BrandModel brands= new BrandModel();
        Cursor cursor=null;
        cursor=query(null,"_id"+" = "+"?",   new String[] { String.valueOf(brand_id) },"brand");


        if(cursor.moveToFirst()){

            do {



                brands.setRetailerId(cursor.getInt(cursor.getColumnIndex("retailerId")));
                brands.setBrand_title(cursor.getString(cursor.getColumnIndex("brand_title")));
                brands.setBrand_desc(cursor.getString(cursor.getColumnIndex("brand_desc")));
                brands.setNotification_text(cursor.getString(cursor.getColumnIndex("notification_text")));
                brands.setLogitude(cursor.getString(cursor.getColumnIndex("longitude")));
                brands.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                brands.setBrand_image(cursor.getBlob(cursor.getColumnIndex("brand_image")));



            }

            while(cursor.moveToNext()); }
        return brands;

    }
    @SuppressLint("Range")
    public ArrayList<BrandModel> getbrandsByuser(int retailer_id) {

        ArrayList<BrandModel> mDataList= new ArrayList<>();
        Cursor cursor=null;
        cursor=query(null,"retailerId"+" = "+"?",   new String[] { String.valueOf(retailer_id) },"brand");


        if(cursor.moveToFirst()){

            do {

                BrandModel brands= new BrandModel();

                brands.setRetailerId(cursor.getInt(cursor.getColumnIndex("retailerId")));
                brands.setBrand_title(cursor.getString(cursor.getColumnIndex("brand_title")));
                brands.setBrand_desc(cursor.getString(cursor.getColumnIndex("brand_desc")));
                brands.setNotification_text(cursor.getString(cursor.getColumnIndex("notification_text")));
                brands.setLogitude(cursor.getString(cursor.getColumnIndex("longitude")));
                brands.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
                brands.setBrand_image(cursor.getBlob(cursor.getColumnIndex("brand_image")));

                mDataList.add(brands);

            }

            while(cursor.moveToNext()); }
        return mDataList;

    }


    @SuppressLint("Range")
  public UserModel UserExists(String email, String password) {

      String[] columns = { "_id","user_email","user_password","user_phone","user_type" };
      String selection = "user_email" + " =?" + " AND "+ "user_password"  + " =?" ;
      String[] selectionArgs = { email,password };
      String limit = "1";
      Cursor cursor = db.query("user", columns, selection, selectionArgs, null, null, null, limit);
      boolean exists = (cursor.getCount() > 0);
     // cursor.close();
      if(exists==true){
            UserModel model =  new UserModel();
          if(cursor.moveToFirst()){

              do {
                  model.setUser_id(cursor.getInt(cursor.getColumnIndex("_id")));
                  model.setUser_email(cursor.getString(cursor.getColumnIndex("user_email")));
                 model.setUser_password(cursor.getString(cursor.getColumnIndex("user_password")));
                  model.setUser_phone(cursor.getString(cursor.getColumnIndex("user_phone")));
                  model.setUser_type(cursor.getString(cursor.getColumnIndex("user_type")));

              }

              while(cursor.moveToNext()); }
          return  model;

      }else{
            return null;
      }
  }



    public void deletenotification(int brandId, int userId) {
        db.delete("user_notification",
                "brandId" + " = ? AND " + "userId" + " = ?",
                new String[]{String.valueOf(brandId), String.valueOf(userId)});
    }

}

