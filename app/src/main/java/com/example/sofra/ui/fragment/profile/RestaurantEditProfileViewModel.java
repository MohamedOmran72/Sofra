package com.example.sofra.ui.fragment.profile;

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

public class RestaurantEditProfileViewModel extends ViewModel {
    private static final String TAG = RestaurantEditProfileViewModel.class.getName();

    public MutableLiveData<Login> editRestaurantProfileMutableLiveData = new MutableLiveData<>();

    public void editRestaurantProfile(RequestBody api_token, MultipartBody.Part storeImage, RequestBody name, RequestBody email
            , final RequestBody regionId, RequestBody deliveryCost, RequestBody minimumCharger, RequestBody availability
            , RequestBody deliveryTime, RequestBody phone, RequestBody whatsApp) {

        getClient().editRestaurantProfile(api_token, storeImage, name, email, regionId, deliveryCost, minimumCharger
                , deliveryTime, availability, phone, whatsApp).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                editRestaurantProfileMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
