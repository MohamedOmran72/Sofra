package com.example.sofra.ui.fragment.order.restaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.order.Order;
import com.example.sofra.data.pojo.order.OrderData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantGetOrderViewModel extends ViewModel {

    private final MutableLiveData<Order> restaurantOrderMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Order> getRestaurantOrderMutableLiveData() {
        return restaurantOrderMutableLiveData;
    }

    public void getRestaurantOrderList(String apiToken, final String state, int page) {
        getClient().getRestaurantOrderList(apiToken, state, page).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                try {
                    restaurantOrderMutableLiveData.setValue(response.body());

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

    public void acceptTheOrder(OrderData orderData) {
        //here make your api request

    }

    /*final String ORDER_TYPE = "pending";
                String apiToken = "";
                if (LoadData(activity, "apiToken") != null) {
                    // apiToken = LoadData(activity, "apiToken");
                    apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
                }

                final RestaurantGetOrderViewModel restaurantGetOrderViewModel =
                        new ViewModelProvider(((HomeActivity) activity)).get(RestaurantGetOrderViewModel.class);

                RestaurantAcceptOrderViewModel restaurantAcceptOrderViewModel =
                        new ViewModelProvider(((HomeActivity) activity)).get(RestaurantAcceptOrderViewModel.class);

                restaurantAcceptOrderViewModel.restaurantAcceptOrder(apiToken
                        , restaurantOrderDataList.get(position).getId());

                String finalApiToken = apiToken;
                restaurantAcceptOrderViewModel.orderMutableLiveData.observe(((LifecycleOwner) activity)
                        , new Observer<Order>() {
                            @Override
                            public void onChanged(Order order) {
                                restaurantGetOrderViewModel.getRestaurantOrderList(finalApiToken
                                        , ORDER_TYPE, 1);
                                Toast.makeText(activity, order.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        });
                notifyDataSetChanged();*/
}
