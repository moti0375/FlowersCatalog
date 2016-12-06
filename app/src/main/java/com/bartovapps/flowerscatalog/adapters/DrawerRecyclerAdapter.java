package com.bartovapps.flowerscatalog.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bartovapps.flowerscatalog.R;
import com.bartovapps.flowerscatalog.data.DrawerItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by BartovMoti on 04/29/15.
 */
public class DrawerRecyclerAdapter extends RecyclerView.Adapter <DrawerRecyclerAdapter.DrawerViewHolder>{


    LayoutInflater inflater;
    List<DrawerItem> data = Collections.emptyList();
    Activity context;
    ClickListener clickListener;

    public DrawerRecyclerAdapter(Activity context, List<DrawerItem> data){
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
    public void onBindViewHolder(DrawerRecyclerAdapter.DrawerViewHolder viewHolder, int i) {
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

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView drawerRowIcon;
        TextView drawerRowTitle;


        public DrawerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            drawerRowIcon = (ImageView) itemView.findViewById(R.id.drawer_list_image);
            drawerRowTitle = (TextView) itemView.findViewById(R.id.drawer_list_textView);
            //drawerRowIcon.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Item " + getPosition() + " was clicked...", Toast.LENGTH_SHORT).show();
          //  deleteItem(getPosition());
            if(clickListener != null){
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
}
