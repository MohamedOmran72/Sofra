package com.example.sofra.ui.fragment.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.client.login.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class ClientLoginViewModel extends ViewModel {

    private static final String TAG = ClientLoginViewModel.class.getName();

    public MutableLiveData<Login> loginMutableLiveData = new MutableLiveData<>();


    public void getClientLogin(String email, String password) {
        getClient().clientLogin(email, password).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                loginMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {

            }
        });
    }
}
