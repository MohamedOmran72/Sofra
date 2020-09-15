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

public class RestaurantNewCategoryViewModel extends ViewModel {
    public MutableLiveData<RestaurantCategories> newCategoryMutableLiveData = new MutableLiveData<>();

    public void addNewCategory(RequestBody name, MultipartBody.Part photo, RequestBody apiToken) {

        getClient().newCategory(name, photo, apiToken).enqueue(new Callback<RestaurantCategories>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantCategories> call, @NonNull Response<RestaurantCategories> response) {
                try {
                    newCategoryMutableLiveData.setValue(response.body());
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
