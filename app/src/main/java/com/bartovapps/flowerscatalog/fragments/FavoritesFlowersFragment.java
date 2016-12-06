package com.bartovapps.flowerscatalog.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bartovapps.flowerscatalog.R;
import com.bartovapps.flowerscatalog.adapters.FlowersRecyclerAdapter;
import com.bartovapps.flowerscatalog.data.Flower;
import com.bartovapps.flowerscatalog.data.FlowersDataSource;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by BartovMoti on 01/07/15.
 */
public class FavoritesFlowersFragment extends Fragment implements android.view.ActionMode.Callback {

    private static final String LOG_TAG = "FavoritesFlowersFragment";
    ArrayList<Flower> flowers = new ArrayList<>();
    Flower selectedFlower;
    Activity activity;
    CatalogListFragment.CatalogCallback callback;
    RecyclerView recyclerView;
    android.view.ActionMode actionMode;
    FlowersRecyclerAdapter flowersRecyclerAdapter;
    Toolbar toolbar;
    ArrayList<Flower> items;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.catalog_list_layout, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.catalogList);
        flowersRecyclerAdapter = new FlowersRecyclerAdapter(getActivity(), flowers);
        toolbar = (Toolbar) getActivity().findViewById(R.id.app_bar);
        items = new ArrayList<>();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, FavoritesFlowersFragment.this, new ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getActivity(), "Drawer item " + position + " clicked..", Toast.LENGTH_SHORT).show();
                if(actionMode != null){
                    myToggleSelection(position);
                }else{
                    callback.onSelectedItem(flowers.get(position));
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                Toast.makeText(getActivity(), "Drawer item " + position + " long clicked..", Toast.LENGTH_SHORT).show();
                //     callback.onItemLongClick(flowers.get(position));
                selectedFlower = flowers.get(position);
//                RemoveFromFavoritesDialog dialog = new RemoveFromFavoritesDialog();
//                dialog.setTargetFragment(FavoritesFlowersFragment.this, 100);
//                dialog.show(getActivity().getSupportFragmentManager(), "RemoveDialog");

            }
        }));

        //  flowersRecyclerAdapter = new FlowersRecyclerAdapter(getActivity(),flowers );
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setCameraDistance(100);
        recyclerView.setAdapter(flowersRecyclerAdapter);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
//        getListView().setDivider(new ColorDrawable(Color.BLUE));
//        getListView().setDividerHeight(2);
//        getListView().setOnItemLongClickListener(onItemLongClickListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setRetainInstance(true);

    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(LOG_TAG, "onAttach is called");
        super.onAttach(activity);
        this.activity = activity;
        this.callback = (CatalogListFragment.CatalogCallback) activity;
        //    refreshDisplay();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
        Log.i(LOG_TAG, "onDetach is called");
    }

    public void refreshDisplay() {
        // if(this.activity != null){
        Log.i(LOG_TAG, "refreshDisplay is called");
        FlowersDataSource dataSource = new FlowersDataSource(activity);
        dataSource.open();
        flowers = dataSource.findAllFavorites();
        dataSource.close();
//            setListAdapter(new FlowersListAdapter(activity, flowers));
        // flowersRecyclerAdapter = new FlowersRecyclerAdapter(getActivity(), flowers);
        flowersRecyclerAdapter.updateFlowers(flowers);
        //    flowersRecyclerAdapter.notifyDataSetChanged();

        if (activity == null) {
            Log.i(LOG_TAG, "activity is null");
        }
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//        Flower flower = flowers.get(position);
//        callback.onSelectedItem(flower);
//
//    }

    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            selectedFlower = flowers.get(position);
            RemoveFromFavoritesDialog dialog = new RemoveFromFavoritesDialog();
            dialog.setTargetFragment(FavoritesFlowersFragment.this, 100);
            dialog.show(getActivity().getSupportFragmentManager(), "RemoveDialog");
            return true;
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(LOG_TAG, "onActivityResult called");
        if (requestCode == 100) {
            FlowersDataSource dataSource = new FlowersDataSource(getActivity());
            dataSource.open();
            for(Flower flower: items){
                dataSource.removeSavedTrip(flower);
            }
            dataSource.close();
            refreshDisplay();
        }
    }

    public void sortByName() {
        Collections.sort(flowers, Flower.Comparators.NAME);
        /// refreshDisplay();
//        setListAdapter(new FlowersListAdapter(activity, flowers));
        flowersRecyclerAdapter.updateFlowers(flowers);
    }

    public void sortByPrice() {
        if (flowers == null) {
            Log.i(LOG_TAG, "flowers is null");
        } else {
            Collections.sort(flowers, Flower.Comparators.PRICE);
            //   refreshDisplay();
//            setListAdapter(new FlowersListAdapter(activity, flowers));
            flowersRecyclerAdapter.updateFlowers(flowers);
        }
    }

    @Override
    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
        Log.i(LOG_TAG, "onCreateActionMode was called");
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.favorites_actionbar, menu);
        Log.i(LOG_TAG, "SDK Ver': " + Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.lightBlueDark));

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
            Log.i(LOG_TAG, "params: " + params.bottomMargin + "\n" + params.topMargin);
            params.setMargins(0, 0, 0, 0);
//            toolbar.setLayoutParams(params);
            Log.i(LOG_TAG, "params: " + params.bottomMargin + "\n" + params.topMargin);
        }

        return true;
    }


    @Override
    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
        if(item.getItemId() ==  R.id.action_remove_items){
            items = flowersRecyclerAdapter.getSelectedItems();
            RemoveFromFavoritesDialog dialog = new RemoveFromFavoritesDialog();
            dialog.setTargetFragment(FavoritesFlowersFragment.this, 100);
            dialog.show(getActivity().getSupportFragmentManager(), "RemoveDialog");
        }
        actionMode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode mode) {
        this.actionMode = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
            params.setMargins(0, activity.getResources().getDimensionPixelOffset(R.dimen.appBarTopMargin), 0, 0);

            toolbar.setLayoutParams(params);
            Log.i(LOG_TAG, "params: " + params.bottomMargin + "\n" + params.topMargin);
        }
        flowersRecyclerAdapter.clearSelection();
   }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private final String LOG_TAG = RecyclerTouchListener.class.getSimpleName();
        GestureDetector gestureDetector;
        ClickListener clickListener;

        public RecyclerTouchListener(final Activity context, final RecyclerView recyclerView, final android.view.ActionMode.Callback actionModeCallback, final ClickListener clickListener) {
            Log.i(LOG_TAG, "constructor was invoked");
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.i(LOG_TAG, "onSingleTapUp was invoked..: " + e);
//                    return super.onSingleTapUp(e);
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    Log.i(LOG_TAG, "onLongPress was invoked..: " + e);
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (actionMode != null) {
                        return;
                    } else {
                        actionMode = context.startActionMode(actionModeCallback);
                        Log.v(LOG_TAG, "actionMode = " + actionMode);
                        int idx = recyclerView.getChildPosition(childView);
                        myToggleSelection(idx);

                    }
                    if (childView != null && clickListener != null) {
                        clickListener.onLongClick(childView, recyclerView.getChildPosition(childView));
                    }
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//            Log.i(LOG_TAG, "onInterceptTouchEvent was called: " + gestureDetector.onTouchEvent(e));
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e) == true) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.i(LOG_TAG, "onTouchEvent was called: " + e);

        }

    }

    private void myToggleSelection(int idx) {
        flowersRecyclerAdapter.toggleSelection(idx);
        if(actionMode != null){
            int itemsCount = flowersRecyclerAdapter.getSelectedItemsCound();
            if(itemsCount == 0){
                actionMode.finish();
            }else{
                String title = "Selected " + itemsCount;
                actionMode.setTitle(title);
            }
            Log.v(LOG_TAG, "actionMode is not null");
        }else{
            Log.v(LOG_TAG, "actionMode is null");
        }
    }

    public static interface ClickListener {
        public void onClick(View v, int position);

        public void onLongClick(View v, int position);
    }
}
