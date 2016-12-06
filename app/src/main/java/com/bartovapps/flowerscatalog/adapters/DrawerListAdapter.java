package com.bartovapps.flowerscatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartovapps.flowerscatalog.R;

import java.util.ArrayList;

/**
 * Created by BartovMoti on 05/02/15.
 */
public class DrawerListAdapter extends ArrayAdapter<String> {

    Activity context;
    ArrayList<String> itemsNames;
    Integer[] images;

    public DrawerListAdapter(Activity context, ArrayList<String> itemsNames, Integer[] imagesId) {
        super(context, R.layout.drawer_row_item, itemsNames);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemsNames=itemsNames;
        this.images=imagesId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //super.getView(position, convertView, parent);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View rowView = inflater.inflate(R.layout.drawer_row_item, null, true);

        TextView textView = (TextView)rowView.findViewById(R.id.drawer_list_textView);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.drawer_list_image);

        textView.setText(itemsNames.get(position));
        imageView.setImageResource(images[position]);

        return rowView;
    }
}
