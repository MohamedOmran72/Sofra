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
    private final MutableLiveData<Order> restaurantConfirmDeliveryMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Order> cancelOrderMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Order> getRestaurantCurrentOrderMutableLiveData() {
        return restaurantCurrentOrderMutableLiveData;
    }

    public MutableLiveData<Order> getRestaurantConfirmDeliveryMutableLiveData() {
        return restaurantConfirmDeliveryMutableLiveData;
    }

    public MutableLiveData<Order> getCancelOrderMutableLiveData() {
        return cancelOrderMutableLiveData;
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

    public void restaurantConfirmOrderDelivery(String apiToken, int orderId) {
        getClient().restaurantConfirmOrderDelivery(apiToken, orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                try {
                    restaurantConfirmDeliveryMutableLiveData.setValue(response.body());

                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void restaurantCancelOrder(String apiToken, int orderId, String reason) {
        getClient().restaurantCancelOrder(apiToken, orderId, reason).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                cancelOrderMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
