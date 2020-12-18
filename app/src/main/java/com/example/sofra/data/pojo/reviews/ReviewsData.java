package com.example.sofra.data.pojo.reviews;

import com.example.sofra.data.pojo.client.login.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewsData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("client")
    @Expose
    private User client;
    @SerializedName("restaurant")
    @Expose
    private User restaurant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(User restaurant) {
        this.restaurant = restaurant;
    }

}
