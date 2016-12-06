package com.bartovapps.flowerscatalog.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BartovMoti on 12/07/14.
 */
public class HttpConnectionManger {

    private static final String LOG_TAG = "HttpConnectionManager";
    public final static int STANDARD_HTTP = 100;
    public final static int OK_HTTP = 200;


    public static String getData(String uri){

        BufferedReader reader = null;
        HttpURLConnection con = null;
        Log.i(LOG_TAG, "Getting data Http request");


        try{
            URL url = new URL(uri);
//            OkHttpClient client = new OkHttpClient();
//            HttpURLConnection con = (HttpURLConnection) url.openConnection(); //This code uses the HttpURLConnection class, a part of Java
//            HttpURLConnection con = client.open(url); //This code uses the OkHttpClient which is a part of the OKHttp library.
            con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }

    public static Bitmap getImage(String uri){
        Bitmap bitmap = null;
        InputStream in = null;
        Log.i(LOG_TAG, "Getting image by Http request");
        try{
            URL url = new URL(uri);
//                HttpURLConnection con = client.open(new uri(image_url)); //We adding this line to use the OKHttp lib.
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            in = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);

        }catch (Exception e){
            e.printStackTrace();
            return null;

        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bitmap;

    }

    public static String getDataByOkHttp(String uri) throws IOException{

        BufferedReader reader = null;
        Log.i(LOG_TAG, "Getting data by OkHttp from: " + uri);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(uri).build();


            Response response = client.newCall(request).execute();

            String resultStr = response.body().string();

            Log.i(LOG_TAG, "OK Http response: " + resultStr);
            return resultStr;


    }

}
