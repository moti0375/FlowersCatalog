package com.bartovapps.flowerscatalog.parsers;

import com.bartovapps.flowerscatalog.data.Flower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BartovMoti on 11/22/14.
 */
public class FlowerJSONParser {

    public static ArrayList<Flower> parseFeed(String content) {
        try {

            JSONArray ar = new JSONArray(content);

            ArrayList<Flower> flowerList = new ArrayList<Flower>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Flower flower = new Flower();

                flower.setProductId(obj.getInt("productId"));
                flower.setName(obj.getString("name"));
                flower.setCategory(obj.getString("category"));
                flower.setInstructions(obj.getString("instructions"));
                flower.setImageUrl(obj.getString("photo"));
                flower.setPrice(obj.getDouble("price"));
                flowerList.add(flower);

            }
            return flowerList;
        }catch (JSONException e) {
                e.printStackTrace();
                return null;
        }
    }
}
