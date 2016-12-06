package com.bartovapps.flowerscatalog.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.bartovapps.flowerscatalog.fragments.CatalogListFragment;
import com.bartovapps.flowerscatalog.fragments.FavoritesFlowersFragment;

/**
 * Created by BartovMoti on 01/07/15.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {


    private static final String LOG_TAG = "PagerAdapter";
    CatalogListFragment catalogListFragment;
    FavoritesFlowersFragment favoritesFlowersFragment;
    public PagerAdapter(FragmentManager fm) {
        super(fm);

        catalogListFragment = new CatalogListFragment();
        favoritesFlowersFragment = new FavoritesFlowersFragment();

    }

    @Override
    public Fragment getItem(int i) {
        Log.i(LOG_TAG, "getItem called: " + i);
        Fragment fragment = null;

        if(i == 0){
            return catalogListFragment;
        }
        if(i == 1){
            return favoritesFlowersFragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
//        Log.i(LOG_TAG, "getCount is called");
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0){
            return "Catalog";
        }

        if(position == 1){
            return "Favorites";
        }

        return null;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Log.i(LOG_TAG, "instantiateItem called for position: " + position);
        return super.instantiateItem(container, position);


    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
