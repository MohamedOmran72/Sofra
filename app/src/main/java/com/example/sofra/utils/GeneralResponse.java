package com.example.sofra.utils;

import android.app.Activity;
import android.widget.Spinner;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.sofra.adapter.LocationSpinnerAdapter;
import com.example.sofra.data.pojo.general.city.City;
import com.example.sofra.data.pojo.general.city.CityData;
import com.example.sofra.ui.generalViewModel.CityViewModel;
import com.example.sofra.ui.generalViewModel.RegionViewModel;

import java.util.ArrayList;

public class GeneralResponse {

    private static final String TAG = GeneralResponse.class.getName();


    /**
     * get cities name from server and adapt it to spinner
     *
     * @param activity          View Model Owner
     * @param cityDataArrayList container of the cities name
     * @param citySpinner       view that adapter attached to
     * @param hint              spinner hint
     */
    public static void getCityList(Activity activity, final ArrayList<CityData> cityDataArrayList, final Spinner citySpinner, final String hint) {
        CityViewModel cityViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(CityViewModel.class);
        final LocationSpinnerAdapter citySpinnerAdapter = new LocationSpinnerAdapter(activity);
        if (cityDataArrayList.size() == 0) {
            cityViewModel.getCity();
        }

        cityViewModel.cityMutableLiveData.observe((LifecycleOwner) activity, new Observer<City>() {

            /**
             * Called when the data is changed.
             *
             * @param cityData The new data
             */
            @Override
            public void onChanged(City cityData) {
                cityDataArrayList.clear();
                cityDataArrayList.addAll(cityData.getData());

                        // set data to adapter and attach to view
                        citySpinnerAdapter.setData(cityDataArrayList, hint);
                        citySpinner.setAdapter(citySpinnerAdapter);
                        citySpinner.setEnabled(true);
                    }
                }
        );
    }

    /**
     * Call to get Region List from server and adapt it to spinner
     *
     * @param regionDataArrayList container of the regions name
     * @param activity            View Model Owner
     * @param cityId              the ID of the City that we need to get Region
     * @param regionSpinner       view that adapter attached to
     * @param hint                spinner hint
     */
    public static void getRegionList(Activity activity, final ArrayList<CityData> regionDataArrayList, final int cityId
            , final Spinner regionSpinner, final String hint) {
        RegionViewModel regionViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(RegionViewModel.class);
        final LocationSpinnerAdapter regionSpinnerAdapter = new LocationSpinnerAdapter(activity);
        if (regionDataArrayList.size() == 0) {
            regionViewModel.getRegion(cityId);
        }

        regionViewModel.regionMutableLiveData.observe((LifecycleOwner) activity, new Observer<City>() {
            /**
             * Called when the data is changed.
             *
             * @param regionData The new data
             */
            @Override
            public void onChanged(City regionData) {
                regionDataArrayList.clear();
                regionDataArrayList.addAll(regionData.getData());

                        // set data to adapter and attach to view
                        regionSpinnerAdapter.setData(regionDataArrayList, hint);
                        regionSpinner.setAdapter(regionSpinnerAdapter);
                        regionSpinner.setEnabled(true);
                    }
                }
        );

    }
}
