package com.example.sofra.ui.fragment.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.client.login.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantLoginViewModel extends ViewModel {

    private static final String TAG = RestaurantLoginViewModel.class.getName();

    public MutableLiveData<Login> loginMutableLiveData = new MutableLiveData<>();


    public void getRestaurantLogin(String email, String password) {
        getClient().restaurantLogin(email, password).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                loginMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
