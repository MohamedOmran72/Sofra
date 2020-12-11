package com.example.sofra.ui.fragment.more.restaurant.offers;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.offer.Offer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantOfferViewModel extends ViewModel {
    private final MutableLiveData<Offer> offerMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Offer> deleteOfferMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Offer> getOfferMutableLiveData() {
        return offerMutableLiveData;
    }

    public MutableLiveData<Offer> getDeleteOfferMutableLiveData() {
        return deleteOfferMutableLiveData;
    }

    public void getRestaurantOfferList(String apiToken, int page) {
        getClient().getRestaurantOffers(apiToken, page).enqueue(new Callback<Offer>() {
            @Override
            public void onResponse(@NonNull Call<Offer> call, @NonNull Response<Offer> response) {
                offerMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Offer> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteRestaurantOffer(int offerId, String apiToken) {
        getClient().deleteOffer(offerId, apiToken).enqueue(new Callback<Offer>() {
            @Override
            public void onResponse(@NonNull Call<Offer> call, @NonNull Response<Offer> response) {
                deleteOfferMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Offer> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
