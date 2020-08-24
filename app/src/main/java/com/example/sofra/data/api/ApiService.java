package com.example.sofra.data.api;

import com.example.sofra.data.pojo.client.forgetPassword.ResetPassword;
import com.example.sofra.data.pojo.client.login.Login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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

}
