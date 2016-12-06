package com.bartovapps.flowerscatalog.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bartovapps.flowerscatalog.R;
import com.bartovapps.flowerscatalog.adapters.FlowersRecyclerAdapter;
import com.bartovapps.flowerscatalog.control.HttpConnectionManger;
import com.bartovapps.flowerscatalog.data.Flower;
import com.bartovapps.flowerscatalog.data.FlowersDataSource;
import com.bartovapps.flowerscatalog.parsers.FlowerJSONParser;
import com.bartovapps.flowerscatalog.parsers.FlowerXMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by BartovMoti on 01/07/15.
 */
public class CatalogListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, NavigationDrawerFragment.DrawerItemClickListener {

    private static final String LOG_TAG = CatalogListFragment.class.getSimpleName();
    private ArrayList<Flower> flowers;
    CatalogCallback callback;

    private SharedPreferences prefs;
    NavigationDrawerFragment drawerFragment;
    RecyclerView recyclerView;
    FlowersRecyclerAdapter flowersRecyclerAdapter;
    int httpMethod = HttpConnectionManger.STANDARD_HTTP;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowers = new ArrayList<>();

        //  setRetainInstance(true);
        if (savedInstanceState == null) {

            Log.i(LOG_TAG, "onCreate called FIRST TIME");
            // initialize the items list
            // initialize and set the list adapter
        } else {
            Log.i(LOG_TAG, "onCreate called SUBSEQUENT TIME");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.catalog_list_layout, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.catalogList);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getActivity(), "RecyclerView item " + position + " clicked..", Toast.LENGTH_SHORT).show();
                callback.onSelectedItem(flowers.get(position));
            }

            @Override
            public void onLongClick(View v, int position) {
                Toast.makeText(getActivity(), "RecyclerView item " + position + " long clicked..", Toast.LENGTH_SHORT).show();
                callback.onItemLongClick(flowers.get(position));
            }
        }));

        //  flowersRecyclerAdapter = new FlowersRecyclerAdapter(getActivity(),flowers );
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setCameraDistance(100);

        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // remove the dividers from the ListView of the ListFragment

//        getListView().setDivider(new ColorDrawable(Color.RED));
//        getListView().setDividerHeight(2);
//        getListView().setOnItemLongClickListener(itemLongClickListener);
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        // retrieve theListView item
//        Log.i(LOG_TAG, "onListItemClick: "  + flowers.size() + " Flowers");
//        Flower flower = flowers.get(position);
//
//        callback.onSelectedItem(flower);
//        // do something
//      //  Toast.makeText(getActivity(), flower.getName(), Toast.LENGTH_SHORT).show();
//    }

    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            Flower flower = flowers.get(position);
            callback.onItemLongClick(flower);
            return true;
        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "onSaveInstanceState");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.callback = (CatalogCallback) activity;
        drawerFragment = (NavigationDrawerFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setOnDrawerItemClickListere(this);

        Log.i(LOG_TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(LOG_TAG, "onDetach");
        prefs.unregisterOnSharedPreferenceChangeListener(this);
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
//        DataLoader loader = new DataLoader();
//        loader.execute(FlowersMain.FLOWERS_XML_URL);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);

        String flowersUri = prefs.getString(getString(R.string.data_source_key), getResources().getStringArray(R.array.data_source_values)[0]);
        loadFlowers(flowersUri);

    }

    public void refreshDisplay() {
//        setListAdapter(new FlowersListAdapter(getActivity(), flowers));
        recyclerView.setAdapter(new FlowersRecyclerAdapter(getActivity(), flowers));

    }

    @Override
    public void drawerItemClicked(int position) {

        Toast.makeText(getActivity(), "Drawer " + position + " item clicked", Toast.LENGTH_SHORT).show();
        FlowersDataSource dataSource = new FlowersDataSource(getActivity());
        dataSource.open();
        flowers.clear();

        switch (position) {
            case NavigationDrawerFragment.STRUBS:
                flowers = dataSource.findAll("Shrubs");
                break;
            case NavigationDrawerFragment.CACAUTS:
                flowers = dataSource.findAll("Cacti & Succulents");
                break;
            case NavigationDrawerFragment.HERBAS:
                flowers = dataSource.findAll("Herbaceous Perennials");
                break;
            case NavigationDrawerFragment.BONSAI:
                flowers = dataSource.findAll("Container Plants");
                break;
            case NavigationDrawerFragment.POT:
                flowers = dataSource.findAll("Container Plants");
                break;
            case NavigationDrawerFragment.ALL_FLOWERS:
                flowers = dataSource.findAll(null);
                break;
        }
        dataSource.close();
        refreshDisplay();

    }


    class DataLoader extends AsyncTask<String, Void, List<Flower>> {

        @Override
        protected List<Flower> doInBackground(String... params) {

            Log.i(LOG_TAG, "URL: " + params[0]);
//            String response = HttpConnectionManger.getData(params[0]);
            String response = null;

            switch (httpMethod){
                case HttpConnectionManger.STANDARD_HTTP:
                 //   Toast.makeText(getActivity(), "Getting flowers by HttpUrlConnection", Toast.LENGTH_SHORT).show();
                    response = HttpConnectionManger.getData(params[0]);
                    break;
                case HttpConnectionManger.OK_HTTP:
             //       Toast.makeText(getActivity(), "Getting flowers by OkHttpConnection", Toast.LENGTH_SHORT).show();
                    try {
                        response = HttpConnectionManger.getDataByOkHttp(params[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }


            Log.i(LOG_TAG, "Http Response:  " + response);

            if (params[0].contains(".xml")) {
                flowers = FlowerXMLParser.parseFeed(response);
            } else if (params[0].contains(".json")) {
                flowers = FlowerJSONParser.parseFeed(response);
            }

            FlowersDataSource dataSource = new FlowersDataSource(getActivity());
            dataSource.open();
            dataSource.deleteAll();
            dataSource.addAll(flowers);
            dataSource.close();
            return flowers;
        }

        @Override
        protected void onPostExecute(List<Flower> flowers) {
            if (flowers != null) {
                refreshDisplay();
                callback.loadCompleted();
            } else {
                Toast.makeText(getActivity(), "Unable to get data from server", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void sortByName() {
        Collections.sort(flowers, Flower.Comparators.NAME);
        refreshDisplay();
    }

    public void sortByPrice() {
        if (flowers == null) {
            Log.i(LOG_TAG, "flowers is null");
        } else {
            Collections.sort(flowers, Flower.Comparators.PRICE);
            refreshDisplay();
        }
    }


    public interface CatalogCallback {
        public void onSelectedItem(Flower flower);

        public void onItemLongClick(Flower flower);

        public void loadCompleted();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(LOG_TAG, "onSharedPrefsChanged called, key: " + key);

        if (key != null) {

            if (key.equals(getString(R.string.data_source_key))) {
                Toast.makeText(getActivity(), "flowers data source changed", Toast.LENGTH_LONG).show();
                String flowersUri = prefs.getString(getString(R.string.data_source_key), getResources().getStringArray(R.array.data_source_values)[0]);
                Toast.makeText(getActivity(), flowersUri, Toast.LENGTH_LONG).show();
                loadFlowers(flowersUri);
            }

            if(key.equals(getString(R.string.http_key))){
                httpMethod = Integer.parseInt(prefs.getString(getString(R.string.http_key), "" + HttpConnectionManger.STANDARD_HTTP));
                Toast.makeText(getActivity(), "http method changed" , Toast.LENGTH_LONG).show();
                String flowersUri = prefs.getString(getString(R.string.data_source_key), getResources().getStringArray(R.array.data_source_values)[0]);
                loadFlowers(flowersUri);
            }
//            sharedPreferences.edit().
//                    putBoolean( getString( R.string.data_source_key ), true ).commit();
        }
    }

    public void loadFlowers(String feedURl) {
        DataLoader loader = new DataLoader();
        loader.execute(feedURl);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private final String LOG_TAG = RecyclerTouchListener.class.getSimpleName();
        GestureDetector gestureDetector;
        ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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

    public static interface ClickListener {
        public void onClick(View v, int position);

        public void onLongClick(View v, int position);
    }


}
