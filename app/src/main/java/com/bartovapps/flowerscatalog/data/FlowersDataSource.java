package com.bartovapps.flowerscatalog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class FlowersDataSource {
    public static final String LOG_TAG = FlowersDataSource.class.getSimpleName();

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            FlowersContract.FlowersEntry._ID,
            FlowersDBOpenHelper.COLUMN_PRODUCT_ID,
            FlowersDBOpenHelper.COLUMN_NAME,
            FlowersDBOpenHelper.COLUMN_CATEGORY,
            FlowersDBOpenHelper.COLUMN_INSTRUCTIONS,
            FlowersDBOpenHelper.COLUMN_PRICE,
            FlowersDBOpenHelper.COLUMN_IMAGE_URL
    };

    private static final String[] favoritesAllColumns = {
            FlowersContract.FavoritesEntry._ID,
            FlowersDBOpenHelper.COLUMN_PRODUCT_ID
    };

    public FlowersDataSource(Context context) {
        if (context == null) {
            Log.i(LOG_TAG, "activity is null");
        } else {
            dbhelper = new FlowersDBOpenHelper(context);
        }
    }

    public void open() {
//		Log.i(LOG_TAG, "Database opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
//		Log.i(LOG_TAG, "Database closed");		
        dbhelper.close();
    }

    public Flower addToDatabase(Flower flower) {
        ContentValues values = new ContentValues();
        values.put(FlowersDBOpenHelper.COLUMN_PRODUCT_ID, flower.getProductId());
        values.put(FlowersDBOpenHelper.COLUMN_NAME, flower.getName());
        values.put(FlowersDBOpenHelper.COLUMN_CATEGORY, flower.getCategory());
        values.put(FlowersDBOpenHelper.COLUMN_INSTRUCTIONS, flower.getInstructions());
        values.put(FlowersDBOpenHelper.COLUMN_PRICE, flower.getPrice());
        values.put(FlowersDBOpenHelper.COLUMN_IMAGE_URL, flower.getImageUrl());

        long insertId = database.insert(FlowersDBOpenHelper.TABLE_FLOWERS, null, values);
//        flower.setProductId((int) insertId);
//		Log.i(LOG_TAG, "Trip created, map: " + trip.getKml());

        return flower;
    }

    public ArrayList<Flower> findAll(String where_args) {
        ArrayList<Flower> flowers = new ArrayList<>();
        String where_clouse = null;
        String[] args = null;


        if (where_args != null) {
            where_clouse = FlowersDBOpenHelper.COLUMN_CATEGORY + "=?";
            args = new String[]{where_args};
        } else {
            where_clouse = null;
        }

        Log.i(LOG_TAG, "About to get " + where_clouse + " " + where_args + " from DB");
        Cursor cursor = null;
        try {
            cursor = database.query(FlowersDBOpenHelper.TABLE_FLOWERS, allColumns,
                    where_clouse, args, null, null, FlowersContract.FlowersEntry._ID);
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "There was an SQLite exception: " + e.getMessage());

            if (e.getMessage().contains("no such column")) {
                // recoverDb();
            }
            return flowers;
        }

        Log.i(LOG_TAG, cursor.getCount() + " Results were founded");

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Flower flower = new Flower();
                flower.setProductId((int) cursor.getLong(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_PRODUCT_ID)));
                flower.setName(cursor.getString(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_NAME)));
                flower.setCategory(cursor.getString(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_CATEGORY)));
                flower.setInstructions(cursor.getString(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_INSTRUCTIONS)));
                flower.setPrice(cursor.getDouble(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_PRICE)));
                flower.setImageUrl(cursor.getString(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_IMAGE_URL)));
                flowers.add(flower);

//				Log.i(LOG_TAG, "Trip add: " + trip.toString());
            }
        }
        return flowers;
    }

    public boolean removeSavedTrip(Flower flower) {
        String where = FlowersDBOpenHelper.COLUMN_PRODUCT_ID + "=" + flower.getProductId();
        int result = database.delete(FlowersDBOpenHelper.TABLE_FAVORITES, where, null);
//		Log.i(LOG_TAG, "Trip removed, map file deleted..");

        return (result == 1);
    }

    public boolean checkIfExist(Flower flower) {

        String where = FlowersDBOpenHelper.COLUMN_PRODUCT_ID + "=" + flower.getProductId();

        Cursor cursor = null;

        Log.i(LOG_TAG, "About to check if " + flower.getName() + " exists");
        try {
//            Cursor cursor = db.query(TABLE_WIKIRES, new String[] { KEY_ID }, KEY_SNAME + "=?", new String[] { name }, null, null, null, null);            query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
//                               query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)

            //     cursor = database.query(FlowersDBOpenHelper.TABLE_FLOWERS, allColumns, whereClause,  whereArgs, null, null, null, null);

            cursor = database.query(FlowersDBOpenHelper.TABLE_FAVORITES, // a. table
                    favoritesAllColumns, // b. column names (projection)
                    " product_id = ?", // c. selections
                    new String[]{"" + flower.getProductId()}, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit

        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "There was an SQLite exception: " + e.getMessage());

            if (e.getMessage().contains("no such column")) {
                // recoverDb();
            }
        }

        if (cursor == null) {
            return false;
        }
        Log.i(LOG_TAG, "Cursor size: " + cursor.getCount());

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.i(LOG_TAG, "Flower selected: " + cursor.getString(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_PRODUCT_ID)));
            return true;
        } else {
            return false;
        }
    }

    private void recoverDb() {
        dbhelper.onCreate(database);
    }

    public int deleteAll() {

        int result = database.delete(FlowersDBOpenHelper.TABLE_FLOWERS, null, null);
        Log.i(LOG_TAG, "delete All result: " + result);

        return result;
    }

    public ArrayList<Flower> addAll(ArrayList<Flower> flowers) {
        ContentValues values = new ContentValues();
        for (Flower flower : flowers) {
            values.clear();
            values.put(FlowersDBOpenHelper.COLUMN_PRODUCT_ID, flower.getProductId());
            values.put(FlowersDBOpenHelper.COLUMN_NAME, flower.getName());
            values.put(FlowersDBOpenHelper.COLUMN_CATEGORY, flower.getCategory());
            values.put(FlowersDBOpenHelper.COLUMN_INSTRUCTIONS, flower.getInstructions());
            values.put(FlowersDBOpenHelper.COLUMN_PRICE, flower.getPrice());
            values.put(FlowersDBOpenHelper.COLUMN_IMAGE_URL, flower.getImageUrl());
            long insertId = database.insert(FlowersDBOpenHelper.TABLE_FLOWERS, null, values);
        }

//        flower.setProductId((int) insertId);
//		Log.i(LOG_TAG, "Trip created, map: " + trip.getKml());

        return flowers;
    }

    public Flower addToFavorites(Flower flower) {
        ContentValues values = new ContentValues();

        values.put(FlowersDBOpenHelper.COLUMN_PRODUCT_ID, flower.getProductId());
        long insertId = database.insert(FlowersDBOpenHelper.TABLE_FAVORITES, null, values);

        return flower;
    }


    public ArrayList<Flower> findAllFavorites() {
        ArrayList<Flower> flowers = new ArrayList<>();
        String where_clouse = null;
        String[] args = null;

        String MY_QUERY = "SELECT * FROM " + FlowersDBOpenHelper.TABLE_FLOWERS +" INNER JOIN " +
                                                           FlowersDBOpenHelper.TABLE_FAVORITES + " ON " + FlowersDBOpenHelper.TABLE_FLOWERS + "." + FlowersDBOpenHelper.COLUMN_PRODUCT_ID + "=" + FlowersDBOpenHelper.TABLE_FAVORITES + "." + FlowersDBOpenHelper.COLUMN_PRODUCT_ID ;

        Log.i(LOG_TAG, "Query String: " + MY_QUERY);
        Cursor cursor = null;


        try {
            cursor = database.rawQuery(MY_QUERY, null);
            Log.i(LOG_TAG, "Got " + cursor.getCount() + " favorites results");
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "There was an SQLite exception: " + e.getMessage());

            if (e.getMessage().contains("no such column")) {
                // recoverDb();
            }
            return flowers;
        }

        Log.i(LOG_TAG, cursor.getCount() + " Results were founded");

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Flower flower = new Flower();
                flower.setProductId((int) cursor.getLong(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_PRODUCT_ID)));
                flower.setName(cursor.getString(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_NAME)));
                flower.setCategory(cursor.getString(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_CATEGORY)));
                flower.setInstructions(cursor.getString(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_INSTRUCTIONS)));
                flower.setPrice(cursor.getDouble(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_PRICE)));
                flower.setImageUrl(cursor.getString(cursor.getColumnIndex(FlowersDBOpenHelper.COLUMN_IMAGE_URL)));
                flowers.add(flower);

//				Log.i(LOG_TAG, "Trip add: " + trip.toString());
            }
        }
        return flowers;
    }
}
