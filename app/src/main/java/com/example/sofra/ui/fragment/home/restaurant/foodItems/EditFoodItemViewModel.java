package com.example.sofra.ui.fragment.home.restaurant.foodItems;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.restaurant.foodItems.FoodItems;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class EditFoodItemViewModel extends ViewModel {
    MutableLiveData<FoodItems> foodItemsMutableLiveData = new MutableLiveData<>();

    public void editFoodItem(RequestBody name, RequestBody description, RequestBody price
            , MultipartBody.Part photo, RequestBody offerPrice, RequestBody preparingTime
            , RequestBody apiToken, RequestBody itemId, RequestBody categoryId) {

        getClient().editFoodItem(name, description, price, photo, offerPrice, preparingTime
                , apiToken, itemId, categoryId).enqueue(new Callback<FoodItems>() {
            @Override
            public void onResponse(@NonNull Call<FoodItems> call, @NonNull Response<FoodItems> response) {
                foodItemsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<FoodItems> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
