
package com.example.sofra.data.pojo.restaurant.foodItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodItems {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private FoodItemsPagination data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FoodItemsPagination getData() {
        return data;
    }

    public void setData(FoodItemsPagination data) {
        this.data = data;
    }

}
