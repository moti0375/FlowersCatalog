package com.bartovapps.flowerscatalog.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bartovapps.flowerscatalog.R;
import com.bartovapps.flowerscatalog.adapters.DrawerListAdapter;
import com.bartovapps.flowerscatalog.adapters.DrawerRecyclerAdapter;
import com.bartovapps.flowerscatalog.adapters.DrawerRecyclerAdapterOptionB;
import com.bartovapps.flowerscatalog.data.DrawerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends android.support.v4.app.Fragment implements DrawerRecyclerAdapter.ClickListener{

    public static final int STRUBS = 0;
    public static final int CACAUTS = 1;
    public static final int HERBAS = 2;
    public static final int BONSAI = 3;
    public static final int POT = 4;
    public static final int ALL_FLOWERS = 5;


    private static final String USER_LEARNED_DRAWER_KEY = "user_learned_drawer";
    private static final String LOG_TAG = NavigationDrawerFragment.class.getSimpleName();
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    boolean userLearnedDrawer;
    boolean fromSavedInstanceState;
    View containerView;
    RecyclerView recyclerView;
    DrawerRecyclerAdapterOptionB drawerRecyclerAdapter;
    //ListView drawerList;
    DrawerItemClickListener drawerItemClickListener;

    String[] listItems ={
            "Strubs",
            "Cactus",
            "Herbaseous Perennials",
            "Bonsai",
            "Pot Plant"
    };

    Integer[] imgid={
            R.drawable.ic_action_strub,
            R.drawable.ic_action_cactus_icon,
            R.drawable.ic_action_ic_launcher,
            R.drawable.ic_action_bonsay,
            R.drawable.ic_action_pot_plant,
            R.drawable.ic_action_ic_launcher
    };


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), USER_LEARNED_DRAWER_KEY, "false"));
        if(savedInstanceState != null){
            fromSavedInstanceState = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getActivity(), "Drawer item " + position + " clicked..", Toast.LENGTH_SHORT).show();
                drawerItemClickListener.drawerItemClicked(position);
            }

            @Override
            public void onLongClick(View v, int position) {
                Toast.makeText(getActivity(), "Drawer item " + position + " long clicked..", Toast.LENGTH_SHORT).show();

            }
        }));

        drawerRecyclerAdapter = new DrawerRecyclerAdapterOptionB(getActivity(), NavigationDrawerFragment.getDrawerData(getActivity()));
//        drawerRecyclerAdapter.setClickListener(this); //this is for optionA adapter

        // Inflate the layout for this fragment
//        drawerList = (ListView) layout.findViewById(R.id.drawerListView);
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(getResources().getStringArray(R.array.drawer_list_names)));
//        ArrayList<String> list = new ArrayList<>();
//        for(String item: listItems){
//            //list.add(item);
//        }

       // ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.drawer_row_item, R.id.drawer_list_textView, list);
        DrawerListAdapter drawerListAdapter = new DrawerListAdapter(getActivity(),  list, imgid);
        recyclerView.setAdapter(drawerRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        drawerList.setAdapter(drawerListAdapter);
//        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                drawerItemClickListener.drawerItemClicked(position);
//              //  Toast.makeText(getActivity(), "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
//                drawerLayout.closeDrawer(containerView);
//            }
//        });
        return layout;
    }


    public void setUp(int drawer_fragment_id, DrawerLayout drawerLayout, final Toolbar toolbar){

        this.drawerLayout=drawerLayout;
        containerView = getActivity().findViewById(drawer_fragment_id);
        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(userLearnedDrawer == false){
                    userLearnedDrawer = true;
                    saveToPreferences(getActivity(), USER_LEARNED_DRAWER_KEY, "" + userLearnedDrawer);
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset < 0.6){
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };

        if(userLearnedDrawer == false && !fromSavedInstanceState){
            drawerLayout.openDrawer(containerView);
        }

        drawerLayout.setDrawerListener(drawerToggle );
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

    }

    public static void saveToPreferences(Context context, String preferenceKey, String prefValue)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceKey, prefValue);
        editor.commit();
    }

    public static String readFromPreferences(Context context, String prefKey, String defaultValue){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(prefKey, defaultValue);
    }

    public void setOnDrawerItemClickListere(DrawerItemClickListener drawerItemClickListener){
        this.drawerItemClickListener = drawerItemClickListener;
    }



    public interface DrawerItemClickListener{
        public void drawerItemClicked(int position);
    }

    @Override
    public void itemClicked(View view, int position) {
        drawerItemClickListener.drawerItemClicked(position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private final String LOG_TAG = RecyclerTouchListener.class.getSimpleName();
        GestureDetector gestureDetector;
        ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            Log.i(LOG_TAG, "constructor was invoked");
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
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
                    if(childView != null && clickListener != null){
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
            if(child != null && clickListener != null && gestureDetector.onTouchEvent(e) == true){
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.i(LOG_TAG, "onTouchEvent was called: " + e);

        }

    }

    public static interface ClickListener{
        public void onClick(View v, int position);
        public void onLongClick(View v, int position);
    }

    public static List<DrawerItem> getDrawerData(Context context){
        List<DrawerItem> data = new ArrayList<>();
        String[] drawerTitles = context.getResources().getStringArray(R.array.drawer_list_names);
        Integer[] imgid={
                R.drawable.ic_action_strub,
                R.drawable.ic_action_cactus_icon,
                R.drawable.ic_action_ic_launcher,
                R.drawable.ic_action_bonsay,
                R.drawable.ic_action_pot_plant,
                R.drawable.ic_action_ic_launcher
        };

        for(int i = 0; i < imgid.length && i < drawerTitles.length; i++){
            Log.i(LOG_TAG, "Adding: " + drawerTitles[i] + " With icon ID: " + imgid[i]);
            DrawerItem item = new DrawerItem();
            item.title = drawerTitles[i];
            item.iconId = imgid[i];
            data.add(item);
        }

        return data;
    }

}
