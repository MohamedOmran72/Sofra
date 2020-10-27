package com.example.sofra.data.api;

import com.example.sofra.data.pojo.client.forgetPassword.ResetPassword;
import com.example.sofra.data.pojo.client.login.Login;
import com.example.sofra.data.pojo.general.city.City;
import com.example.sofra.data.pojo.restaurant.foodItems.FoodItems;
import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategories;

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

    // get Restaurant Categories (Home Activity)
    @GET("restaurant/my-categories")
    Call<RestaurantCategories> getRestaurantCategories(@Query("api_token") String apiToken
            , @Query("page") int page);

    // add new category
    @Multipart
    @POST("restaurant/new-category")
    Call<RestaurantCategories> newCategory(@Part("name") RequestBody name,
                                           @Part MultipartBody.Part photo,
                                           @Part("api_token") RequestBody apiToken);

    // edit food category
    @Multipart
    @POST("restaurant/update-category")
    Call<RestaurantCategories> updateCategory(@Part("name") RequestBody name
            , @Part MultipartBody.Part photo
            , @Part("api_token") RequestBody apiToken
            , @Part("category_id") RequestBody categoryId);

    // delete food category
    @POST("restaurant/delete-category")
    @FormUrlEncoded
    Call<RestaurantCategories> deleteCategory(@Field("category_id") int itemId
            , @Field("api_token") String apiToken);

    // get food list (from category)
    @GET("restaurant/my-items")
    Call<FoodItems> getMyFoodItemList(@Query("api_token") String apiToken
            , @Query("category_id") int categoryId
            , @Query("page") int page);

    // add food item to specific category
    @Multipart
    @POST("restaurant/new-item")
    Call<FoodItems> addNewFoodItem(@Part("name") RequestBody name
            , @Part("description") RequestBody description
            , @Part("price") RequestBody price
            , @Part MultipartBody.Part photo
            , @Part("offer_price") RequestBody offerPrice
            , @Part("preparing_time") RequestBody preparingTime
            , @Part("api_token") RequestBody apiToken
            , @Part("category_id") RequestBody categoryId);

    // edit food item to specific category
    @Multipart
    @POST("restaurant/update-item")
    Call<FoodItems> editFoodItem(@Part("name") RequestBody name
            , @Part("description") RequestBody description
            , @Part("price") RequestBody price
            , @Part MultipartBody.Part photo
            , @Part("offer_price") RequestBody offerPrice
            , @Part("preparing_time") RequestBody preparingTime
            , @Part("api_token") RequestBody apiToken
            , @Part("item_id") RequestBody itemId
            , @Part("category_id") RequestBody categoryId);

    // delete food item from specific category
    @POST("restaurant/delete-item")
    @FormUrlEncoded
    Call<FoodItems> deleteFoodItem(@Field("item_id") int itemId
            , @Field("api_token") String apiToken);

    // get restaurant data from server
    @POST("restaurant/profile")
    @FormUrlEncoded
    Call<Login> getRestaurantProfile(@Field("api_token") String apiToken);

    // set restaurant data to server after edit
    @Multipart
    @POST("restaurant/profile")
    Call<Login> editRestaurantProfile(@Part("api_token") RequestBody api_token
            , @Part MultipartBody.Part photo
            , @Part("name") RequestBody name
            , @Part("email") RequestBody email
            , @Part("region_id") RequestBody regionId
            , @Part("delivery_cost") RequestBody deliveryCost
            , @Part("minimum_charger") RequestBody minimumCharger
            , @Part("delivery_time") RequestBody deliveryTime
            , @Part("availability") RequestBody availability
            , @Part("phone") RequestBody phone
            , @Part("whatsapp") RequestBody whatsapp);
}
