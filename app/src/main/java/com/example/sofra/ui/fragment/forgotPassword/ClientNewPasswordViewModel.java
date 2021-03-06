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

public class ClientNewPasswordViewModel extends ViewModel {
    private static final String TAG = ClientNewPasswordViewModel.class.getName();
    public MutableLiveData<ResetPassword> clientNewPasswordMutableLiveData = new MutableLiveData<>();

    public void clientNewPassword(String code, String password, String passwordConfirmation) {
        getClient().clientNewPassword(code, password, passwordConfirmation).enqueue(new Callback<ResetPassword>() {
            @Override
            public void onResponse(@NonNull Call<ResetPassword> call, @NonNull Response<ResetPassword> response) {
                clientNewPasswordMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ResetPassword> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
