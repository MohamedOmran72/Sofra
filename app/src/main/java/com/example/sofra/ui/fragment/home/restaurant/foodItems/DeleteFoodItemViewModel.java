package com.example.sofra.ui.fragment.home.restaurant.foodItems;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.restaurant.foodItems.FoodItems;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class DeleteFoodItemViewModel extends ViewModel {
    public MutableLiveData<FoodItems> foodItemsMutableLiveData = new MutableLiveData<>();

    public void deleteFoodItem(int itemId, String apiToken) {
        getClient().deleteFoodItem(itemId, apiToken).enqueue(new Callback<FoodItems>() {
            @Override
            public void onResponse(@NonNull Call<FoodItems> call, @NonNull Response<FoodItems> response) {
                try {
                    foodItemsMutableLiveData.setValue(response.body());
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FoodItems> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
