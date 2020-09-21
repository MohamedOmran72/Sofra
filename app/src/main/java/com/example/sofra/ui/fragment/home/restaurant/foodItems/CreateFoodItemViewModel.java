package com.example.sofra.ui.fragment.home.restaurant.foodItems;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.restaurant.foodItems.FoodItems;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.sofra.data.api.RetrofitClient.getClient;

public class CreateFoodItemViewModel extends ViewModel {
    private MutableLiveData<FoodItems> foodItemsMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<FoodItems> getFoodItemsMutableLiveData() {
        return foodItemsMutableLiveData;
    }

    public void setFoodItemsMutableLiveData(MutableLiveData<FoodItems> foodItemsMutableLiveData) {
        this.foodItemsMutableLiveData = foodItemsMutableLiveData;
    }

    public void addNewFoodItem(RequestBody name, RequestBody description, RequestBody price
            , MultipartBody.Part photo, RequestBody offerPrice, RequestBody preparingTime
            , RequestBody apiToken, RequestBody categoryId) {

        getClient().addNewFoodItem(name, description, price, photo, offerPrice, preparingTime
                , apiToken, categoryId).enqueue(new Callback<FoodItems>() {
            @Override
            public void onResponse(@NonNull Call<FoodItems> call, @NonNull Response<FoodItems> response) {
                try {
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {
                        foodItemsMutableLiveData.setValue(response.body());
                    } else {
                        Log.i(TAG, "onResponse: status != 1" + response.message());
                    }
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
