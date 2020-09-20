package com.example.sofra.ui.fragment.home.restaurant.categories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategories;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class DeleteRestaurantCategoriesViewModel extends ViewModel {

    public MutableLiveData<RestaurantCategories> deleteRestaurantCategoriesMutableLiveData = new MutableLiveData<>();

    public void deleteRestaurantCategories(int itemId, String apiToken) {
        getClient().deleteCategory(itemId, apiToken).enqueue(new Callback<RestaurantCategories>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantCategories> call, @NonNull Response<RestaurantCategories> response) {
                try {
                    deleteRestaurantCategoriesMutableLiveData.setValue(response.body());
                } catch (RuntimeException e) {
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
