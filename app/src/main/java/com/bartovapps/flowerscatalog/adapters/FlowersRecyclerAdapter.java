package com.bartovapps.flowerscatalog.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartovapps.flowerscatalog.FlowersMain;
import com.bartovapps.flowerscatalog.R;
import com.bartovapps.flowerscatalog.data.Flower;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by BartovMoti on 11/08/15.
 */
public class FlowersRecyclerAdapter extends RecyclerView.Adapter<FlowersRecyclerAdapter.RecyclerViewHolder> {

    private static final String LOG_TAG = FlowersRecyclerAdapter.class.getSimpleName();
    LayoutInflater inflater;
    ArrayList<Flower> data = new ArrayList<>();
    Activity context;
    private SparseBooleanArray selectedItems;

    public FlowersRecyclerAdapter(Activity context, ArrayList<Flower> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listview_item, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Log.i(LOG_TAG, "onBindViewHolder was called");
        Flower flower = new Flower();
        flower.setName(data.get(position).getName());
        flower.setPrice(data.get(position).getPrice());
        flower.setFlowerBitmap(data.get(position).getFlowerBitmap());
        flower.setImageUrl(data.get(position).getImageUrl());

        holder.flowerName.setText(flower.getName());
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        holder.flowerPrice.setText(fmt.format(flower.getPrice()));
        Picasso.with(context).load(FlowersMain.IMAGES_BASE_URL + "/" + flower.getImageUrl()).transform(new CircleTransform()).into(holder.flowerImage);
        holder.itemView.setActivated(selectedItems.get(position, false));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void toggleSelection(int position) {
        Log.i(LOG_TAG, "toggleSelection was called");
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemsCound() {
        return selectedItems.size();
    }

    public ArrayList<Flower> getSelectedItems() {
        ArrayList<Flower> items = new ArrayList<>();
        for(int i=0; i<selectedItems.size(); i++){
            items.add(data.get(selectedItems.keyAt(i)));
        }
        return items;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView flowerImage;
        TextView flowerName;
        TextView flowerPrice;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            flowerImage = (ImageView) itemView.findViewById(R.id.ivFlowerImage);
            flowerName = (TextView) itemView.findViewById(R.id.tvFlowerName);
            flowerPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            //drawerRowIcon.setOnClickListener(this);

        }

    }


    public void updateFlowers(ArrayList<Flower> data){
        this.data.clear();
        this.data.addAll(data);
        Log.i(LOG_TAG, "data size: " + this.data.size());
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }


    private static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }


}
