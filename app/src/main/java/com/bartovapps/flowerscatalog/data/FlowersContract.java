package com.bartovapps.flowerscatalog.data;

import android.provider.BaseColumns;

/**
 * Created by BartovMoti on 08/08/15.
 */
public class FlowersContract {

    public static final class FlowersEntry implements BaseColumns{
        public static final String TABLE_NAME = "flowers";

        public static final String COLUMN_FAV_KEY = "location_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_NAME = "flower_name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_INSTRUCTIONS = "instructions";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE_URI = "image_uri";
    }

    public static final class FavoritesEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_PRODUCT_ID = "product_id";

    }
}
