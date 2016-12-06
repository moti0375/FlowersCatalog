package com.bartovapps.flowerscatalog.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.List;

/**
 * Created by BartovMoti on 01/07/15.
 */
public class FlowersListAdapter extends ArrayAdapter<Flower> {

    private static final String LOG_TAG = "FlowersListAdapter";
    LruCache<Integer, Bitmap> imageCache;

    private List<Flower> flowers;
    Context context;

    public FlowersListAdapter(Context context, List<Flower> items) {
        super(context, R.layout.listview_item, items);
        this.flowers = items;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        this.context = getContext();
        imageCache = new LruCache<Integer, Bitmap>(cacheSize);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        Log.i(LOG_TAG, "getView called");
        Flower flower = getItem(position);

        if(convertView == null) {
            Log.i(LOG_TAG, "convertView is null");
            // inflate the GridView flower layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivFlowerImage = (ImageView) convertView.findViewById(R.id.ivFlowerImage);
            viewHolder.tvFlowerName = (TextView) convertView.findViewById(R.id.tvFlowerName);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.ivFlowerImage.setImageBitmap(null);
        }


        // update the flower view
        viewHolder.tvFlowerName.setText(flower.getName());
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        viewHolder.tvPrice.setText(fmt.format(flower.getPrice()));

        //  getImage(iv, viewHolder, flower);
            Picasso.with(context).load(FlowersMain.IMAGES_BASE_URL + "/" + flower.getImageUrl()).into(viewHolder.ivFlowerImage);

       return convertView;
    }

    private void getImage(ImageView iv, ViewHolder viewHolder, Flower flower){
        ImageLoader loader = new ImageLoader();
        FlowerAndView fov = new FlowerAndView();
        fov.flower = flower;
        fov.view = viewHolder.ivFlowerImage;
        loader.execute(fov);
    }

    private static class ViewHolder {
        ImageView ivFlowerImage;
        TextView tvFlowerName;
        TextView tvPrice;
    }


    public class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView> {

        private static final String LOG_TAG = "ImageLoader";

        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {
            FlowerAndView container = params[0];
            com.bartovapps.flowerscatalog.data.Flower flower = container.flower;
            HttpURLConnection con = null;
            try {
                Log.i(LOG_TAG, "About to get image for " + container.flower.getName() + " product ID: " + container.flower.getProductId());
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
                imageCache.put(result.flower.getProductId(), result.flower.getFlowerBitmap());
            }else{
                Log.i(LOG_TAG, "Result = null");
            }
        }
    }
}
