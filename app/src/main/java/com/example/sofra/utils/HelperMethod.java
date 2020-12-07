package com.example.sofra.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


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

    /**
     * Called to add fragment to view
     *
     * @param getChildFragmentManager Return the FragmentManager for interacting with fragments associated with this activity.
     * @param viewId                  the Id of the view that we replace on
     * @param fragment                the fragment that we add on view
     * @param arguments               arguments that we need to send to new fragment {can be null}
     */
    public static void addFragment(@NonNull FragmentManager getChildFragmentManager, @NonNull Integer viewId
            , @NonNull Fragment fragment, Bundle arguments) {

        fragment.setArguments(arguments);
        FragmentTransaction transaction = getChildFragmentManager.beginTransaction();
        transaction.add(viewId, fragment);
        transaction.commit();
    }

    /**
     * Convert image bitmap to file .png
     *
     * @param context the context of the current state of the application
     * @param bitmap  image as bitmap
     * @return converted image file .png
     */
    public static File convertBitmapToFile(Context context, Bitmap bitmap) {

        // use system time as file name
        String imageFileName = String.valueOf(System.currentTimeMillis());
        // path to the directory on the filesystem where files created
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, imageFileName + ".png");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(TAG, "Error writing bitmap", e);
        }
        return imageFile;
    }

    /**
     * Convert image file To MultipartBody
     *
     * @param imageFile input image file
     * @param Key       server key
     * @return MultipartBody
     */
    public static MultipartBody.Part convertFileToMultipart(File imageFile, String Key) {
        if (imageFile != null) {
            RequestBody reqFileSelect = RequestBody.create(MediaType.parse("image/*"), imageFile);
            return MultipartBody.Part.createFormData(Key, imageFile.getName(), reqFileSelect);
        } else {
            return null;
        }
    }


    /**
     * Convert string to RequestBody
     *
     * @param part input string from user
     * @return RequestBody
     */
    public static RequestBody convertStringToRequestBody(String part) {
        try {
            if (!part.equals("")) {
                return RequestBody.create(MediaType.parse("multipart/form-data"), part);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Display date picker dialog
     *
     * @param context     app states.
     * @param displayDate view we need to display date baker.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void showDatePickerDialog(Context context, TextView displayDate) {

        DatePickerDialog.OnDateSetListener mDateSetListener;
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                String date = year + "-";
                if (month < 10) {
                    date += "0" + month;
                } else {
                    date += month;
                }
                if (day < 10) {
                    date += "-0" + day;
                } else {
                    date += "-" + day;
                }

                displayDate.setText(date);

                Log.d(TAG, date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(context
                , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                , mDateSetListener, year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

}
