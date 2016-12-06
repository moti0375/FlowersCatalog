package com.bartovapps.flowerscatalog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.bartovapps.flowerscatalog.data.FlowersContract;
import com.bartovapps.flowerscatalog.data.FlowersDBOpenHelper;

/**
 * Created by BartovMoti on 08/08/15.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(FlowersDBOpenHelper.DATABASE_NAME); //mContext is defined in the AndroidTestCase superclass
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

//        public void testCreateDb() throws Throwable {
//        // build a HashSet of all of the table names we wish to look for
//        // Note that there will be another table in the DB that stores the
//        // Android metadata (db version information)
//        final HashSet<String> tableNameHashSet = new HashSet<String>();
//        tableNameHashSet.add(FlowersContract.FlowersEntry.TABLE_NAME);
//        tableNameHashSet.add(FlowersContract.FavoritesEntry.TABLE_NAME);
//
//        mContext.deleteDatabase(FlowersDBOpenHelper.DATABASE_NAME);
//        SQLiteDatabase db = new FlowersDBOpenHelper(this.mContext).getWritableDatabase();
//        assertEquals(true, db.isOpen());
//
//        // have we created the tables we want?
//        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//        assertTrue("Error: This means that the database has not been created correctly",
//                c.moveToFirst());
//
//        // verify that the tables have been created
//        do {
//            tableNameHashSet.remove(c.getString(0));
//        } while( c.moveToNext() );
//
//        // if this fails, it means that your database doesn't contain both the flowers entry
//        // and favorites entry tables
//        assertTrue("Error: Your database was created without both the favorites entry and flowers entry tables",
//                tableNameHashSet.isEmpty());
//
//        // now, do our tables contain the correct columns?
//        c = db.rawQuery("PRAGMA table_info(" + FlowersContract.FavoritesEntry.TABLE_NAME + ")",
//                null);
//
//        assertTrue("Error: This means that we were unable to query the database for table information.",
//                c.moveToFirst());
//
//            // Build a HashSet of all of the column names we want to look for
//        final HashSet<String> flowersColumnHashSet = new HashSet<>();
//        flowersColumnHashSet.add(FlowersContract.FlowersEntry._ID); //._ID is defined in the ColumnBase superclass
//        flowersColumnHashSet.add(FlowersContract.FavoritesEntry.COLUMN_PRODUCT_ID);
//
//        int columnNameIndex = c.getColumnIndex("name"); //The result from 'PRAGMA table_info ... ' is a new table with column 'name' 'data type' and more..
//                                                        //Each row represent a column in the table_info( TABLE_NAME) from the query.
//
//        Log.i(LOG_TAG, "number of columns: " + c.getCount());
//
//        do {
//            String columnName = c.getString(columnNameIndex);
//            flowersColumnHashSet.remove(columnName);
//        } while(c.moveToNext());
//
//        // if this fails, it means that your database doesn't contain all of the required location
//        // entry columns
//        assertTrue("Error: The database doesn't contain all of the required favorites entry columns",
//                flowersColumnHashSet.isEmpty());
//        db.close();
//    }


    /*
    Students:  Here is where you will build code to test that we can insert and query the
    database.  We've done a lot of work for you.  You'll want to look in TestUtilities
    where you can use the "createWeatherValues" function.  You can
    also make use of the validateCurrentRecord function from within TestUtilities.
 */
    public void testFlowersTable() {
        Log.i(LOG_TAG, "testWeatherTable function called");

//        long rowId = TestUtilities.insertAzaleaValues(mContext);
//        Log.i(LOG_TAG, "testWeatherTable function: Insert Row ID: " + rowId);
//        assertTrue("insert location content", rowId != -1);

        // the weather. Make sure to cover as many failure cases as you can.

        // Instead of rewriting all of the code we've already written in testLocationTable
        // we can move this code to insertLocation and then call insertLocation from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testLocationTable can only return void because it's a test.

        // First step: Get reference to writable database

        // Create ContentValues of what you want to insert
        // (you can use the createWeatherValues TestUtilities function if you wish)

        ContentValues weatherValues = TestUtilities.createFlowersValues();


        // Insert ContentValues into database and get a row ID back
        SQLiteDatabase database = new FlowersDBOpenHelper(this.mContext).getWritableDatabase();

        long flowerId = database.insert(FlowersContract.FlowersEntry.TABLE_NAME, null, weatherValues);
        assertTrue("insert weather content", flowerId != -1);

        // Query the database and receive a Cursor back
        // Move the cursor to a valid database row
        Cursor cursor = database.query(FlowersContract.FlowersEntry.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        Log.i(LOG_TAG, "cursor count: " + cursor.getCount());

        long id = Long.parseLong(cursor.getString(1));
        double flower_id = Double.parseDouble(cursor.getString(2));
        String name = cursor.getString(3);
        String category = cursor.getString(4);
        String instructions = cursor.getString(5);
        double price = Double.parseDouble(cursor.getString(6));
        String uri = cursor.getString(7);



        assertTrue("move to next should be false", cursor.moveToNext() == false);

        String logStr = "|" + id + "|" + flower_id + "|" +
                name + "|" +
                category + "|" +
                instructions + "|" +
                price + "|" +
                uri + "|" ;

        Log.i(LOG_TAG, "received row: " + logStr);

        cursor.close();
        database.close();

        assertEquals("assert test name", TestUtilities.TEST_FLOWER_NAME, name);
        assertEquals("category test", TestUtilities.TEST_CATEGORY, category);
        assertEquals("instructions test ", TestUtilities.TEST_INSTRUCTIONS, instructions);
        assertEquals("price test for flower ", TestUtilities.TEST_PRICE, price);
        assertEquals("uri test ", TestUtilities.TEST_IMAGE_URI, uri);


//        public static final long TEST_DATE = 1419033600L;  // December 20th, 2014
//        public static final double TEST_DEGREES = 1.1;
//        public static final double TEST_HUMIDITY = 1.2;
//        public static final double TEST_PRESSURE = 1.2;
//        public static final double TEST_MAX_TEMP = 75.0;
//        public static final double TEST_MIN_TEMP = 65;
//        public static final String TEST_SHORT_DESC = "Asteroids";
//        public static final double TEST_WIND_SPEED = 5.5;
//        public static final double TEST_WEATHER_ID = 321;

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)

        // Finally, close the cursor and database
    }


}
