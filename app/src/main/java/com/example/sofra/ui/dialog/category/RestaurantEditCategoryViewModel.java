package com.example.sofra.ui.dialog.category;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategories;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantEditCategoryViewModel extends ViewModel {

    public MutableLiveData<RestaurantCategories> updateCategoryMutableLiveData = new MutableLiveData<>();


    public void updateCategory(RequestBody name, MultipartBody.Part photo, RequestBody apiToken, RequestBody categoryId) {

        getClient().updateCategory(name, photo, apiToken, categoryId).enqueue(new Callback<RestaurantCategories>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantCategories> call, @NonNull Response<RestaurantCategories> response) {
                try {
                    updateCategoryMutableLiveData.setValue(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantCategories> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });


    }
}
