package com.example.sofra.ui.fragment.order.restaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.order.Order;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantOrderCurrentViewModel extends ViewModel {
    private final MutableLiveData<Order> restaurantCurrentOrderMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Order> getRestaurantCurrentOrderMutableLiveData() {
        return restaurantCurrentOrderMutableLiveData;
    }

    public void getRestaurantCurrentOrderList(String apiToken, final String state, int page) {
        getClient().getRestaurantOrderList(apiToken, state, page).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                try {
                    restaurantCurrentOrderMutableLiveData.setValue(response.body());

                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
