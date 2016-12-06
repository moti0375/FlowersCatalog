package com.bartovapps.flowerscatalog.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartovapps.flowerscatalog.R;
import com.bartovapps.flowerscatalog.data.DrawerItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by BartovMoti on 04/29/15.
 */
public class DrawerRecyclerAdapterOptionB extends RecyclerView.Adapter <DrawerRecyclerAdapterOptionB.DrawerViewHolder>{


    LayoutInflater inflater;
    List<DrawerItem> data = Collections.emptyList();
    Activity context;

    public DrawerRecyclerAdapterOptionB(Activity context, List<DrawerItem> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
   }
    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.drawer_row_item, parent, false);
        DrawerViewHolder viewHolder = new DrawerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrawerRecyclerAdapterOptionB.DrawerViewHolder viewHolder, int i) {
        DrawerItem item = new DrawerItem();
        item.iconId = data.get(i).iconId;
        item.title = data.get(i).title;

        viewHolder.drawerRowTitle.setText(item.title);
        viewHolder.drawerRowIcon.setImageResource(item.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }


    class DrawerViewHolder extends RecyclerView.ViewHolder {

        ImageView drawerRowIcon;
        TextView drawerRowTitle;


        public DrawerViewHolder(View itemView) {
            super(itemView);
            drawerRowIcon = (ImageView) itemView.findViewById(R.id.drawer_list_image);
            drawerRowTitle = (TextView) itemView.findViewById(R.id.drawer_list_textView);
            //drawerRowIcon.setOnClickListener(this);

        }

    }

}
