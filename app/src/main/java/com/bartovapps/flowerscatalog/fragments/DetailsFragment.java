package com.bartovapps.flowerscatalog.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartovapps.flowerscatalog.FlowersMain;
import com.bartovapps.flowerscatalog.R;
import com.bartovapps.flowerscatalog.control.FlowerAndView;
import com.bartovapps.flowerscatalog.data.Flower;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;

/**
 * Created by BartovMoti on 01/07/15.
 */
public class DetailsFragment extends Fragment {

    private static final String LOG_TAG = "DetailsFragment";

    TextView tvFlowerTitle;
    TextView tvCategory;
    TextView tvInstructions;
    TextView tvPrice;
    ImageView ivFlowerImage;
    Flower flower;

    public DetailsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i(LOG_TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.flowers_details_fragment, container, false);

        tvFlowerTitle = (TextView)view.findViewById(R.id.tvFlowerTitle);
        tvCategory = (TextView)view.findViewById(R.id.tvCategory);
        tvInstructions = (TextView)view.findViewById(R.id.tvInstructions);
        tvCategory = (TextView)view.findViewById(R.id.tvCategory);
        tvPrice = (TextView)view.findViewById(R.id.tvPrice);
        ivFlowerImage = (ImageView)view.findViewById(R.id.ivFlowerImage);

        if(tvFlowerTitle == null){
            Log.i(LOG_TAG, "view is null");
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(flower != null){
            setFlower(flower);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setRetainInstance(true);

        Bundle b = getArguments();
        if(b != null){
            flower = new Flower(b);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "onSaveInstanceState");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(LOG_TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(LOG_TAG, "onDetach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    public void setFlower(Flower flower){
        Log.i(LOG_TAG, "setFlower called");

        if(flower != null){
            tvFlowerTitle.setText(flower.getName());
            tvCategory.setText(flower.getCategory());
            NumberFormat fmt = NumberFormat.getCurrencyInstance();
            tvInstructions.setText(flower.getInstructions());
            tvPrice.setText(fmt.format(flower.getPrice()));
            FlowerAndView fov = new FlowerAndView();
            fov.flower = flower;
            fov.view = ivFlowerImage;
            Picasso.with(getActivity()).load(FlowersMain.IMAGES_BASE_URL + "/" + flower.getImageUrl()).into(ivFlowerImage);
//
//            ImageLoader imageLoader = new ImageLoader();
//            imageLoader.execute(fov);
        }

    }

    public class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView> {

        private static final String LOG_TAG = "ImageLoader";

        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {
            FlowerAndView container = params[0];
            com.bartovapps.flowerscatalog.data.Flower flower = container.flower;
            HttpURLConnection con = null;
            try {
                Log.i(LOG_TAG, "About to get image for " + container.flower.getName());
                String image_url = FlowersMain.IMAGES_BASE_URL + "/" + flower.getImageUrl();
                URL url = new URL(image_url);
                con = (HttpURLConnection) url.openConnection();
//                con = client.open(new URL(image_url)); //We adding this line to use the OKHttp lib.
//                InputStream in = (InputStream) new URL(image_url).getContent(); //This line we use when no OKHttp is in use..
                InputStream in = con.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                flower.setFlowerBitmap(bitmap);
                in.close();
                return container;
            } catch (Exception e) {
                Log.i(LOG_TAG, "There was an exception...");
                e.printStackTrace();
                return  null;
            }
        }


        @Override
        protected void onPostExecute(FlowerAndView result) {
            if(result != null){
                ImageView iv = (ImageView)result.view;
                iv.setImageBitmap(result.flower.getFlowerBitmap());
                // imageCache.put(result.flower.getProductId(), result.bitmap);
            }else{
                Log.i(LOG_TAG, "Result = null");
            }
        }
    }

}
