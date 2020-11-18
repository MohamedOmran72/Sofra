package com.example.sofra.ui.fragment.order.restaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.sofra.data.pojo.order.Order;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantOrderPendingViewModel extends RestaurantOrderShearedViewModel {
    private final MutableLiveData<Order> acceptOrderMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Order> getAcceptOrderMutableLiveData() {
        return acceptOrderMutableLiveData;
    }

    public void restaurantAcceptOrder(String apiToken, int orderId) {
        getClient().restaurantAcceptOrder(apiToken, orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                acceptOrderMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
