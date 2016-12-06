package com.bartovapps.flowerscatalog;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.bartovapps.flowerscatalog.data.FlowersContract;
import com.bartovapps.flowerscatalog.data.FlowersDBOpenHelper;

/**
 * Created by BartovMoti on 08/08/15.
 */
public class TestUtilities extends AndroidTestCase{

    public static final int TEST_FAV_ID = 1;
    public static final int TEST_FLOWER_ID = 2;
    public static final String TEST_FLOWER_NAME = "Azalea";
    public static final String TEST_CATEGORY = "Strubs";
    public static final String TEST_INSTRUCTIONS = "Large double, Good grower...";
    public static final double TEST_PRICE = 17.95;
    public static final String TEST_IMAGE_URI = "http://";

    public static final int insertAzaleaValues(Context context) {

        int columnNum = 0;

        FlowersDBOpenHelper dbHelper = new FlowersDBOpenHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createFlowersValues();

        long locationRowId;
        locationRowId = db.insert(FlowersContract.FlowersEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);

        return columnNum;
    }


    public static ContentValues createFlowersValues(){

        ContentValues flowersValues = new ContentValues();
        flowersValues.put(FlowersContract.FlowersEntry.COLUMN_FAV_KEY, TEST_FAV_ID);
        flowersValues.put(FlowersContract.FlowersEntry.COLUMN_PRODUCT_ID, TEST_FLOWER_ID);
        flowersValues.put(FlowersContract.FlowersEntry.COLUMN_NAME, TEST_FLOWER_NAME);
        flowersValues.put(FlowersContract.FlowersEntry.COLUMN_CATEGORY, TEST_CATEGORY);
        flowersValues.put(FlowersContract.FlowersEntry.COLUMN_INSTRUCTIONS, TEST_INSTRUCTIONS);
        flowersValues.put(FlowersContract.FlowersEntry.COLUMN_PRICE, TEST_PRICE);
        flowersValues.put(FlowersContract.FlowersEntry.COLUMN_IMAGE_URI, TEST_IMAGE_URI);

        return flowersValues;

    }
}
