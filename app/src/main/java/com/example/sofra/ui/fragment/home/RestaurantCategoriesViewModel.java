package com.example.sofra.ui.fragment.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategories;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantCategoriesViewModel extends ViewModel {

    private final MutableLiveData<RestaurantCategories> restaurantCategoriesMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<RestaurantCategories> getRestaurantCategoriesMutableLiveData() {
        return restaurantCategoriesMutableLiveData;
    }

    public void setRestaurantCategoriesMutableLiveData(RestaurantCategories restaurantCategories) {
        restaurantCategoriesMutableLiveData.setValue(restaurantCategories);
    }

    public void getRestaurantCategories(String apiToken, int page) {
        getClient().getRestaurantCategories(apiToken, page).enqueue(new Callback<RestaurantCategories>() {
            @Override
            public void onResponse(@NonNull Call<RestaurantCategories> call, @NonNull Response<RestaurantCategories> response) {
                restaurantCategoriesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<RestaurantCategories> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
