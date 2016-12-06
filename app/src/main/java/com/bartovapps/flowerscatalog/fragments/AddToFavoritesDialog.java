package com.bartovapps.flowerscatalog.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by BartovMoti on 01/10/15.
 */
public class AddToFavoritesDialog extends android.support.v4.app.DialogFragment {

    private static final String LOG_TAG = "AddToFavoritesDialog";
    DialogCallback activity;

    public AddToFavoritesDialog(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Favorites");
        builder.setMessage("Add this flower to favorites?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(LOG_TAG, "Positive button clicked");
                dialog.dismiss();
                activity.onPositiveButtonClicked(getActivity());
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(LOG_TAG, "Negative button clicked");
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (DialogCallback)activity;
    }

    public interface DialogCallback{
        public void onPositiveButtonClicked(Activity activity);
    }
}
