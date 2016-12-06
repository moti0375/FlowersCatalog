package com.bartovapps.flowerscatalog.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FlowersDBOpenHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "FlowersDBOpenHelper";
    public static final String DATABASE_NAME = "flowers.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_FLOWERS = "flowers";
    public static final String TABLE_FAVORITES = "favorites";

    public static final String COLUMN_ITEM_NUMBER = "item_number";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_NAME = "flower_name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_INSTRUCTIONS = "instructions";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGE_URL = "image_uri";



    private static final String TABLE_CREATE =
            "CREATE TABLE " + FlowersContract.FlowersEntry.TABLE_NAME + " (" +
                    FlowersContract.FlowersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FlowersContract.FlowersEntry.COLUMN_FAV_KEY + " INTEGER, " +
                    FlowersContract.FlowersEntry.COLUMN_PRODUCT_ID + " NUMERIC, " +
                    FlowersContract.FlowersEntry.COLUMN_NAME + " TEXT, " +
                    FlowersContract.FlowersEntry.COLUMN_CATEGORY + " TEXT, " +
                    FlowersContract.FlowersEntry.COLUMN_INSTRUCTIONS + " TEXT, " +
                    FlowersContract.FlowersEntry.COLUMN_PRICE + " NUMERIC, " +
                    FlowersContract.FlowersEntry.COLUMN_IMAGE_URI + " TEXT " + ")";



    private static final String TABLE_FAVORITES_CREATE =
            "CREATE TABLE " + FlowersContract.FavoritesEntry.TABLE_NAME + " (" +
                    FlowersContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY, " +
                    FlowersContract.FavoritesEntry.COLUMN_PRODUCT_ID + " NUMERIC " + ")";

    public FlowersDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "onCreate was called");
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FLOWERS;
        db.execSQL(DROP_TABLE);

        db.execSQL(TABLE_CREATE);
        Log.i(LOG_TAG, "Flowers table has been created");
        db.execSQL(TABLE_FAVORITES_CREATE);
        Log.i(LOG_TAG, "Favorites table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FlowersContract.FlowersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FlowersContract.FavoritesEntry.TABLE_NAME);
        onCreate(db);

    }
}
