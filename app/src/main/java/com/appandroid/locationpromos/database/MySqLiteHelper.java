package com.appandroid.locationpromos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySqLiteHelper extends SQLiteOpenHelper {

    Context context;
    private static final String nomDb = "propos.db";
    private static final int versionDb = 10;
    public static String COLUMN_ID="_id";

    private static final String SQL_CREATE_USER_TABLE =  "CREATE TABLE user (" +
            COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
             "user_name TEXT NOT NULL, "+
             "user_email TEXT NOT NULL, "+
             "user_password TEXT NOT NULL, "+
             "user_phone TEXT NOT NULL, "+
             "user_type TEXT NOT NULL); ";



    private static final String SQL_CREATE_BRAND_TABLE =  "CREATE TABLE brand (" +
            COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "retailerId INTEGER NOT NULL, "+
            "brand_title TEXT NOT NULL, "+
            "brand_image BLOB NOT NULL, "+
            "brand_desc TEXT NOT NULL, "+
            "latitude TEXT NOT NULL, "+
            "longitude TEXT NOT NULL, "+
            "notification_text TEXT NOT NULL, "+
            " FOREIGN KEY(retailerId) REFERENCES user(_id) ); ";

    private static final String SQL_CREATE_USER_NOTIFICATION_TABLE =  "CREATE TABLE user_notification(" +
            COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "userId INTEGER NOT NULL, "+
            "brandId INTEGER NOT NULL, "+
            "FOREIGN KEY(brandId) REFERENCES brand(_id) , "+
            "FOREIGN KEY(userId) REFERENCES user(_id) ); ";


    public MySqLiteHelper(@Nullable Context context) {
        super(context, nomDb, null, versionDb);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_BRAND_TABLE);
        db.execSQL(SQL_CREATE_USER_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS retailer");
        db.execSQL("DROP TABLE IF EXISTS brand");
        db.execSQL("DROP TABLE IF EXISTS retailer_company");
        db.execSQL("DROP TABLE IF EXISTS user_notification");
        onCreate(db);
    }
}
