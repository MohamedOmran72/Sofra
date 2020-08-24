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

public class ClientResetPasswordViewModel extends ViewModel {
    private static final String TAG = ClientResetPasswordViewModel.class.getName();
    public MutableLiveData<ResetPassword> clientResetPasswordMutableLiveData = new MutableLiveData<>();

    public void clientResetPassword(String email) {
        getClient().clientResetPassword(email).enqueue(new Callback<ResetPassword>() {
            @Override
            public void onResponse(@NonNull Call<ResetPassword> call, @NonNull Response<ResetPassword> response) {
                clientResetPasswordMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ResetPassword> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
