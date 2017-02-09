package com.rajat.gopetting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rajatbeck on 2/9/2017.
 */

public class ProductDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ProductDatabases";
    public static final String TABLE_NAME ="itemTables";
    public static final int DATABASE_VERSION = 3;
    public static final String COLUMN_SN = "_id";
    public static final String COLUMN_NAME= "name";
    public static final String COLUMN_ID =" UID";
    public static final String CREATE_TABLE ="CREATE TABLE " + TABLE_NAME + " (" +COLUMN_SN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ID + " VARCHAR(255) UNIQUE NOT NULL, " + COLUMN_NAME + " VARCHAR(255) DEFAULT NULL);";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " +TABLE_NAME;


    Context context;

    ProductDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try
        {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }
    }
}
