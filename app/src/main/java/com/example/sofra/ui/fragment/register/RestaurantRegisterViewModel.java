package com.example.sofra.ui.fragment.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.client.login.Login;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantRegisterViewModel extends ViewModel {
    private static final String TAG = RestaurantRegisterViewModel.class.getName();

    public MutableLiveData<Login> registerMutableLiveData = new MutableLiveData<>();

    public void getRestaurantRegister(RequestBody name, RequestBody email, RequestBody password
            , RequestBody confirmPassword, RequestBody phone, RequestBody whatsApp, RequestBody regionId
            , RequestBody deliveryCost, RequestBody minimumCharger, MultipartBody.Part storeImage, RequestBody deliveryTime) {

        getClient().restaurantSignUp(name, email, password, confirmPassword, phone, whatsApp, regionId
                , deliveryCost, minimumCharger, storeImage, deliveryTime).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                registerMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
