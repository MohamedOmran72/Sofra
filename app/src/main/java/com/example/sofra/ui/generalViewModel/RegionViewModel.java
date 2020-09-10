package com.example.sofra.ui.generalViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.general.city.City;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;


public class RegionViewModel extends ViewModel {

    private static final String TAG = RegionViewModel.class.getName();
    public MutableLiveData<City> regionMutableLiveData = new MutableLiveData<>();

    public void getRegion(int cityId) {
        getClient().getRegion(cityId).enqueue(new Callback<City>() {
            @Override
            public void onResponse(@NonNull Call<City> call, @NonNull Response<City> response) {
                try {
                    regionMutableLiveData.setValue(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<City> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });


    }
}
