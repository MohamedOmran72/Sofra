package com.example.sofra.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class HelperMethod {

    private static final String TAG = Context.class.getName();

    /**
     * Called when user click on view to disappear the keyboard
     *
     * @param activity that contain view
     * @param view     that user click
     */
    public static void disappearKeypad(Activity activity, View view) {
        try {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.i(TAG, "disappearKeypad:error " + e);
        }
    }

    /**
     * Called to replace view with fragment
     *
     * @param getChildFragmentManager Return the FragmentManager for interacting with fragments associated with this activity.
     * @param viewId                  the Id of the view that we replace on
     * @param fragment                the fragment that we add on view
     * @param backStackName           BackStack name that we add the fragment to {can be null}
     * @param arguments               arguments that we need to send to new fragment {can be null}
     */
    public static void replaceFragment(@NonNull FragmentManager getChildFragmentManager, @NonNull Integer viewId
            , @NonNull Fragment fragment, String backStackName, Bundle arguments) {

        fragment.setArguments(arguments);
        FragmentTransaction transaction = getChildFragmentManager.beginTransaction();
        transaction.replace(viewId, fragment);
        transaction.addToBackStack(backStackName);
        transaction.commit();
    }
}
