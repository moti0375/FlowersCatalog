package com.bartovapps.flowerscatalog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bartovapps.flowerscatalog.adapters.PagerAdapter;
import com.bartovapps.flowerscatalog.data.Flower;
import com.bartovapps.flowerscatalog.data.FlowersDataSource;
import com.bartovapps.flowerscatalog.fragments.AddToFavoritesDialog;
import com.bartovapps.flowerscatalog.fragments.CatalogListFragment;
import com.bartovapps.flowerscatalog.fragments.DetailsFragment;
import com.bartovapps.flowerscatalog.fragments.FavoritesFlowersFragment;
import com.bartovapps.flowerscatalog.fragments.NavigationDrawerFragment;
import com.parse.ParseAnalytics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class FlowersMain extends ActionBarActivity implements ActionBar.TabListener, CatalogListFragment.CatalogCallback,
                                                             AddToFavoritesDialog.DialogCallback{

    public static final String FLOWERS_XML_URL = "http://services.hanselandpetal.com/feeds/flowers.xml";
    public static final String FLOWERS_JSON_URL = "http://services.hanselandpetal.com/feeds/flowers.json";
    public static final String IMAGES_BASE_URL = "http://services.hanselandpetal.com/photos";
    public static final String IMAGES_C9_URL = "https://preview.c9.io/moti0375/flowers/images";

    private static final String LOG_TAG = "FlowersMainActivity";
    public static final String FLOWER_BUNDLE = "FLOWER_BUNDLE";

    public static final String FLOWERS_DB = "flowers.db";
    public static final String FLOWERS_DB_JOURNAL = "flowers.db-journal";
    public static final String DB_ROOT = "/data/com.bartovapps.flowerscatalog/databases/";

    ViewPager pager = null;
    android.support.v7.app.ActionBar actionBar;
    PagerAdapter pagerAdapter;
    Flower selecetedFlower;
    FragmentManager fragmentManager;
    int pagerPosition;
    private boolean isTwoPanes = false;

    private  ProgressDialog dialog;
    Toolbar toolBar;

    NavigationDrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ParseAnalytics.trackAppOpened(getIntent());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowers_main);
        toolBar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);


        drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.navigationDrawerLayout), toolBar);

        pager = (ViewPager) findViewById(R.id.pager);

        Log.i(LOG_TAG, "onCreate was called");

        if(savedInstanceState == null){
        }else{

        }

        fragmentManager = getSupportFragmentManager();
        pagerAdapter = new PagerAdapter(fragmentManager);


       actionBar = getSupportActionBar();
     //  actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(pageChangeListener);

//        ActionBar.Tab tab1 = actionBar.newTab();
//        tab1.setText("Catalog");
//        tab1.setTabListener(this);
//
//        ActionBar.Tab tab2 = actionBar.newTab();
//        tab2.setText("Favorites");
//        tab2.setTabListener(this);

//        actionBar.addTab(tab1);
//        actionBar.addTab(tab2);
        if(findViewById(R.id.detailsContainer) != null){
            isTwoPanes = true;
        }else{
            isTwoPanes = false;
        }
//        Log.i(LOG_TAG, "Position0 ID: " + pagerAdapter.getItemId(0));
//        Log.i(LOG_TAG, "Position1 ID: "+  pagerAdapter.getItemId(1));
//        throw new RuntimeException("Test Exception!");
          dialog = new ProgressDialog(this);
          dialog.setMessage("Exporting database...");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flowers_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(FlowersMain.this, FlowersPrefsActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.sort_by_price){
            Log.i(LOG_TAG, "Sort by price selected..");
            sortByPrice();
            return true;

        }

        if(id == R.id.sort_by_name){
            Log.i(LOG_TAG, "Sort by name selected..");
            sortByName();
            return true;

        }

        if(id == R.id.backup_database){
            Log.i(LOG_TAG, "Backup Flowers selected..");
            ExportDatabaseFileTask backupTask = new ExportDatabaseFileTask();
            backupTask.execute();
        }

        if(id == R.id.import_database){
            Log.i(LOG_TAG, "Import Favorites selected..");
            ImportDatabaseFileTask backupTask = new ImportDatabaseFileTask();
            backupTask.execute();
        }

        if(id == R.id.delete_all){
            Log.i(LOG_TAG, "Import Favorites selected..");
            FlowersDataSource dataSource = new FlowersDataSource(this);
            dataSource.open();
            dataSource.deleteAll();
            dataSource.close();

                FavoritesFlowersFragment favoritesFlowersFragment = (FavoritesFlowersFragment) pagerAdapter.getItem(1);
                favoritesFlowersFragment.refreshDisplay();
            }
        return super.onOptionsItemSelected(item);

    }

    private void sortByPrice() {
        switch(pagerPosition) {
            case 0:
                CatalogListFragment catalogListFragment = (CatalogListFragment) pagerAdapter.getItem(pagerPosition);
                catalogListFragment.sortByPrice();
                break;
            case 1:
                FavoritesFlowersFragment favoritesFlowersFragment = (FavoritesFlowersFragment) pagerAdapter.getItem(pagerPosition);
                favoritesFlowersFragment.sortByPrice();
                break;
        }
    }

    private void sortByName(){
        switch(pagerPosition) {
            case 0:
                CatalogListFragment catalogListFragment = (CatalogListFragment) pagerAdapter.getItem(pagerPosition);
                catalogListFragment.sortByName();
                break;
            case 1:
                FavoritesFlowersFragment favoritesFlowersFragment = (FavoritesFlowersFragment) pagerAdapter.getItem(pagerPosition);
                favoritesFlowersFragment.sortByName();
                break;
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        Log.i(LOG_TAG, "onTabSelected at position " + tab.getPosition() + " name: " + tab.getText());
        pager.setCurrentItem(tab.getPosition());
        pagerPosition = tab.getPosition();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    //    Log.i(LOG_TAG, "onTabUnselected at position " + tab.getPosition() + " name: " + tab.getText());

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

  //      Log.i(LOG_TAG, "onTabReselected at position " + tab.getPosition() + " name: " + tab.getText());

    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float v, int from) {
  //         Log.i(LOG_TAG, "onPageScrolled at position " + position + " from position " + from);

        }

        @Override
        public void onPageSelected(int i) {
            Log.i(LOG_TAG, "onPageSelected at position " + i);
//            actionBar.setSelectedNavigationItem(i);
            pagerPosition = i;

        }

        @Override
        public void onPageScrollStateChanged(int i) {

            if (i == ViewPager.SCROLL_STATE_IDLE) {
                Log.i(LOG_TAG, "onPageScrolledStateChanged to IDLE");
            }
            if (i == ViewPager.SCROLL_STATE_SETTLING) {
                Log.i(LOG_TAG, "onPageScrolledStateChanged to SETTLING");
            }
            if (i == ViewPager.SCROLL_STATE_DRAGGING) {
                Log.i(LOG_TAG, "onPageScrolledStateChanged to DRAGGING");
            }

        }
    };

    @Override
    public void onSelectedItem(Flower flower) {
        Log.i(LOG_TAG, "onSelectedItem called");
      //  DetailsFragment detailsFragment = (DetailsFragment) pagerAdapter.getItem(1);
       Bundle b = flower.toBundle();



        if(isTwoPanes){
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsContainer, fragment).commit();
        }else{
            Intent intent = new Intent(FlowersMain.this, FlowerDetails.class);
            intent.putExtra(FLOWER_BUNDLE, b);
            startActivityForResult(intent, 1001);
        }


      //  detailsFragment.setFlower(flower);
      //actionBar.setSelectedNavigationItem(1);


    }

    @Override
    public void onItemLongClick(Flower flower) {
        selecetedFlower = flower;
        Log.i(LOG_TAG, "onItemLongClick called, flower id:"  + flower.getProductId());
        AddToFavoritesDialog dialog = new AddToFavoritesDialog();
        dialog.show(getSupportFragmentManager(), "AddToFavoritesDialog");

    }



    @Override
    public void loadCompleted() {
        FavoritesFlowersFragment favoritesFlowersFragment = (FavoritesFlowersFragment) pagerAdapter.getItem(1);
        favoritesFlowersFragment.refreshDisplay();
    }




    @Override
    public void onPositiveButtonClicked(Activity activity) {

        FlowersDataSource dataSource = new FlowersDataSource(this);
        dataSource.open();
        if(dataSource.checkIfExist(selecetedFlower)){
            Toast.makeText(FlowersMain.this, "Flower already exists!", Toast.LENGTH_LONG).show();
        }else{
            dataSource.addToFavorites(selecetedFlower);
            FavoritesFlowersFragment favoritesFlowersFragment = (FavoritesFlowersFragment) pagerAdapter.getItem(1);
            favoritesFlowersFragment.refreshDisplay();
        }
        dataSource.close();
   }


    private class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {


        // can use UI thread here
        protected void onPreExecute() {
//            dialog.show();
        }

        // automatically done on worker thread (separate from UI thread)
        protected Boolean doInBackground(final String... args) {
            String dbFilePath = Environment.getDataDirectory() +  DB_ROOT + FLOWERS_DB;

            String dbJournalPath = Environment.getDataDirectory() + DB_ROOT + FLOWERS_DB_JOURNAL;
                    Log.i(LOG_TAG, "DB File: " + dbFilePath);

            if(!(new File(dbFilePath).exists())){
                Log.i(LOG_TAG, "File cannot be found");
            }else{
                Log.i(LOG_TAG, "db file founded!");

            }

            File dbFile = new File(dbFilePath);
            File dbJournalFile = new File(dbJournalPath);

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File dbBackupFile = new File(exportDir, dbFile.getName());
            File dbJournalBackupFile = new File(exportDir, dbJournalFile.getName());

            try {
                dbBackupFile.createNewFile();
                dbJournalBackupFile.createNewFile();
                this.copyFile(dbFile, dbBackupFile);
                this.copyFile(dbJournalFile, dbJournalBackupFile);
                return true;
            } catch (IOException e) {
                Log.e("mypck", e.getMessage(), e);
                return false;
            }
        }

        // can use UI thread here
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (success) {
                Toast.makeText(getApplicationContext(), "Export successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Export failed", Toast.LENGTH_SHORT).show();
            }
        }

        void copyFile(File src, File dst) throws IOException {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            }
        }

    }


    private class ImportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {


        // can use UI thread here
        protected void onPreExecute() {
//            dialog.show();
        }

        // automatically done on worker thread (separate from UI thread)
        protected Boolean doInBackground(final String... args) {
            String dbFilePath = Environment.getDataDirectory() + DB_ROOT + FLOWERS_DB;

            String dbJournalPath = Environment.getDataDirectory() + DB_ROOT + FLOWERS_DB_JOURNAL;
            Log.i(LOG_TAG, "DB File: " + dbFilePath);

            File importDir = new File(Environment.getExternalStorageDirectory(), "");

            File dbBackupFile = new File(importDir, FLOWERS_DB);
            File dbJournalBackupFile = new File(importDir, FLOWERS_DB_JOURNAL);

            File dbFile = new File(dbFilePath);
            File dbJournalFile = new File(dbJournalPath);

            if (dbBackupFile.exists() && dbJournalBackupFile.exists()) {
                Log.i(LOG_TAG, "Backup founded");
                try {
                    dbFile.createNewFile();
                    dbJournalFile.createNewFile();
                    this.copyFile(dbBackupFile, dbFile );
                    this.copyFile(dbJournalBackupFile, dbJournalFile);
                    return true;
                } catch (IOException e) {
                    Log.e("mypck", e.getMessage(), e);
                    return false;
                }
            }else{
                return false;
            }
        }



        // can use UI thread here
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (success) {
                Toast.makeText(getApplicationContext(), "Import successful!", Toast.LENGTH_SHORT).show();
                FavoritesFlowersFragment favoritesFlowersFragment = (FavoritesFlowersFragment) pagerAdapter.getItem(pagerPosition);
                favoritesFlowersFragment.refreshDisplay();
            } else {
                Toast.makeText(getApplicationContext(), "Import failed", Toast.LENGTH_SHORT).show();
            }
        }

        void copyFile(File src, File dst) throws IOException {
            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            }
        }

    }



}
