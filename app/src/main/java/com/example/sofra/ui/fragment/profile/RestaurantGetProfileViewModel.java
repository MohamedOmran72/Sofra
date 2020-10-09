package com.example.sofra.ui.fragment.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.client.login.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantGetProfileViewModel extends ViewModel {
    private static final String TAG = RestaurantGetProfileViewModel.class.getName();

    public MutableLiveData<Login> getRestaurantProfileMutableLiveData = new MutableLiveData<>();

    public void getRestaurantProfile(String apiToken) {
        getClient().getRestaurantProfile(apiToken).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                assert response.body() != null;
                if (response.body().getStatus() == 1) {
                    getRestaurantProfileMutableLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "onResponse: != 1" + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
