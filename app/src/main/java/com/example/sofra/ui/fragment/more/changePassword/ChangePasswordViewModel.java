package com.example.sofra.ui.fragment.more.changePassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.client.login.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class ChangePasswordViewModel extends ViewModel {
    private final MutableLiveData<Login> restaurantChangePasswordMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Login> getRestaurantChangePasswordMutableLiveData() {
        return restaurantChangePasswordMutableLiveData;
    }

    public void restaurantChangePassword(String apiToken, String oldPassword
            , String newPassword, String passwordConfirmation) {

        getClient().changeRestaurantPassword(apiToken, oldPassword, newPassword, passwordConfirmation)
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                        restaurantChangePasswordMutableLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
