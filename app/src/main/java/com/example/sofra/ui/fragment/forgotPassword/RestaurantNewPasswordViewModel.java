package com.example.sofra.ui.fragment.forgotPassword;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.client.forgetPassword.ResetPassword;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantNewPasswordViewModel extends ViewModel {
    private static final String TAG = RestaurantNewPasswordViewModel.class.getName();
    public MutableLiveData<ResetPassword> restaurantNewPasswordMutableLiveData = new MutableLiveData<>();

    public void restaurantNewPassword(String code, String password, String passwordConfirmation) {
        getClient().restaurantNewPassword(code, password, passwordConfirmation).enqueue(new Callback<ResetPassword>() {
            @Override
            public void onResponse(@NonNull Call<ResetPassword> call, @NonNull Response<ResetPassword> response) {
                restaurantNewPasswordMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ResetPassword> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
