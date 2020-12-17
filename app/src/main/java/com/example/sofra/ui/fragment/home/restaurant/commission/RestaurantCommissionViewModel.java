package com.example.sofra.ui.fragment.home.restaurant.commission;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.restaurant.commission.Commission;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantCommissionViewModel extends ViewModel {
    private final MutableLiveData<Commission> commissionMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Commission> getCommissionMutableLiveData() {
        return commissionMutableLiveData;
    }

    public void getRestaurantCommission(String apiToken) {
        getClient().getRestaurantCommission(apiToken).enqueue(new Callback<Commission>() {
            @Override
            public void onResponse(@NonNull Call<Commission> call, @NonNull Response<Commission> response) {
                commissionMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Commission> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
