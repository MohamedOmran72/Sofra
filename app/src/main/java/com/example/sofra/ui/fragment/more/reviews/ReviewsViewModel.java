package com.example.sofra.ui.fragment.more.reviews;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.reviews.Reviews;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class ReviewsViewModel extends ViewModel {
    private final MutableLiveData<Reviews> restaurantReviewsMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Reviews> getRestaurantReviewsMutableLiveData() {
        return restaurantReviewsMutableLiveData;
    }

    public void getRestaurantReviews(String apiToken, int page, int restaurant_id) {
        getClient().getRestaurantsReviews(apiToken, page, restaurant_id).enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(@NonNull Call<Reviews> call, @NonNull Response<Reviews> response) {
                restaurantReviewsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Reviews> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
