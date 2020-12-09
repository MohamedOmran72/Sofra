package com.example.sofra.ui.fragment.more.restaurant.offers;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sofra.data.pojo.offer.Offer;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sofra.data.api.RetrofitClient.getClient;

public class RestaurantOfferDetailsFragmentViewModel extends ViewModel {

    private final MutableLiveData<Offer> addOfferMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Offer> editOfferMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Offer> getAddOfferMutableLiveData() {
        return addOfferMutableLiveData;
    }

    public void addRestaurantOffer(RequestBody apiToken, MultipartBody.Part photo, RequestBody name
            , RequestBody price, RequestBody description, RequestBody startingAt, RequestBody endingAt) {

        getClient().addRestaurantOffer(apiToken, photo, name, price, description
                , startingAt, endingAt).enqueue(new Callback<Offer>() {
            @Override
            public void onResponse(@NonNull Call<Offer> call, @NonNull Response<Offer> response) {
                addOfferMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Offer> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public MutableLiveData<Offer> getEditOfferMutableLiveData() {
        return editOfferMutableLiveData;
    }

    public void editRestaurantOffer(RequestBody apiToken, RequestBody offerId, MultipartBody.Part photo
            , RequestBody name, RequestBody price, RequestBody description, RequestBody startingAt
            , RequestBody endingAt) {

        getClient().editRestaurantOffer(apiToken, offerId, photo, name, price, description
                , startingAt, endingAt).enqueue(new Callback<Offer>() {
            @Override
            public void onResponse(@NonNull Call<Offer> call, @NonNull Response<Offer> response) {
                editOfferMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Offer> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
