package com.example.sofra.ui.generalViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.general.city.City;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class CityViewModel extends ViewModel {
    private static final String TAG = CityViewModel.class.getName();
    public MutableLiveData<City> cityMutableLiveData = new MutableLiveData<>();

    public void getCity() {
        getClient().getCity().enqueue(new Callback<City>() {
            @Override
            public void onResponse(@NonNull Call<City> call, @NonNull Response<City> response) {
                try {
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        cityMutableLiveData.setValue(response.body());
                    }
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
