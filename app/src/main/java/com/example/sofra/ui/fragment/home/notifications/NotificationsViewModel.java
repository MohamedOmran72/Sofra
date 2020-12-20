package com.example.sofra.ui.fragment.home.notifications;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.notifications.Notifications;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class NotificationsViewModel extends ViewModel {
    private final MutableLiveData<Notifications> restaurantNotificationsMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Notifications> getRestaurantNotificationsMutableLiveData() {
        return restaurantNotificationsMutableLiveData;
    }

    public void getRestaurantNotification(String apiToken, int page) {
        getClient().getRestaurantNotifications(apiToken, page).enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(@NonNull Call<Notifications> call, @NonNull Response<Notifications> response) {
                restaurantNotificationsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Notifications> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
