package com.example.sofra.data.api;

import com.example.sofra.data.pojo.client.forgetPassword.ResetPassword;
import com.example.sofra.data.pojo.client.login.Login;
import com.example.sofra.data.pojo.general.city.City;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    // Login calls
    @POST("client/login")
    @FormUrlEncoded
    Call<Login> clientLogin(@Field("email") String email
            , @Field("password") String password);

    @POST("restaurant/login")
    @FormUrlEncoded
    Call<Login> restaurantLogin(@Field("email") String email
            , @Field("password") String password);

    // Forget password calls
    @POST("client/reset-password")
    @FormUrlEncoded
    Call<ResetPassword> clientResetPassword(@Field("email") String email);

    @POST("restaurant/reset-password")
    @FormUrlEncoded
    Call<ResetPassword> restaurantResetPassword(@Field("email") String email);

    // New password calls
    @POST("client/new-password")
    @FormUrlEncoded
    Call<ResetPassword> clientNewPassword(@Field("code") String code
            , @Field("password") String password
            , @Field("password_confirmation") String passwordConfirmation);

    @POST("restaurant/new-password")
    @FormUrlEncoded
    Call<ResetPassword> restaurantNewPassword(@Field("code") String code
            , @Field("password") String password
            , @Field("password_confirmation") String passwordConfirmation);

    // Register calls
    @Multipart
    @POST("client/sign-up")
    Call<Login> clientSignUp(@Part("name") RequestBody name
            , @Part("email") RequestBody email
            , @Part("password") RequestBody password
            , @Part("password_confirmation") RequestBody passwordConfirmation
            , @Part("phone") RequestBody phone
            , @Part("region_id") RequestBody regionId
            , @Part MultipartBody.Part profile_image);

    @Multipart
    @POST("restaurant/sign-up")
    Call<Login> restaurantSignUp(@Part("name") RequestBody name
            , @Part("email") RequestBody email
            , @Part("password") RequestBody password
            , @Part("password_confirmation") RequestBody passwordConfirmation
            , @Part("phone") RequestBody phone
            , @Part("whatsapp") RequestBody whatsapp
            , @Part("region_id") RequestBody regionId
            , @Part("delivery_cost") RequestBody deliveryCost
            , @Part("minimum_charger") RequestBody minimumCharger
            , @Part MultipartBody.Part photo
            , @Part("delivery_time") RequestBody deliveryTime);

    // City and Region calls
    @GET("cities-not-paginated")
    Call<City> getCity();

    @GET("regions-not-paginated")
    Call<City> getRegion(@Query("city_id") int cityId);

}
