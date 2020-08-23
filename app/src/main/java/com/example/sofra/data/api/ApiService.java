package com.example.sofra.data.api;

import com.example.sofra.data.pojo.client.login.Login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @POST("client/login")
    @FormUrlEncoded
    Call<Login> clientLogin(@Field("email") String email
            , @Field("password") String password);

    @POST("restaurant/login")
    @FormUrlEncoded
    Call<Login> restaurantLogin(@Field("email") String email
            , @Field("password") String password);
}
