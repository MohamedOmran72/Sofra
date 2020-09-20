package com.example.sofra.ui.fragment.home.restaurant.foodItems;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.restaurant.foodItems.FoodItems;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantGetFoodItemListViewModel extends ViewModel {
    private MutableLiveData<FoodItems> foodItemsMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<FoodItems> getFoodItemsMutableLiveData() {
        return foodItemsMutableLiveData;
    }

    public void setFoodItemsMutableLiveData(MutableLiveData<FoodItems> foodItemsMutableLiveData) {
        this.foodItemsMutableLiveData = foodItemsMutableLiveData;
    }

    public void getFoodItemList(String apiToken, int categoryId, int page) {

        getClient().getMyFoodItemList(apiToken, categoryId, page).enqueue(new Callback<FoodItems>() {
            @Override
            public void onResponse(@NonNull Call<FoodItems> call, @NonNull Response<FoodItems> response) {
                try {
                    foodItemsMutableLiveData.setValue(response.body());
                } catch (Exception e) {
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
