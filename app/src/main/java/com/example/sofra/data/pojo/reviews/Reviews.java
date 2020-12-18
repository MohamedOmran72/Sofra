
package com.example.sofra.data.pojo.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reviews {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ReviewsPagination data;

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

    public ReviewsPagination getData() {
        return data;
    }

    public void setData(ReviewsPagination data) {
        this.data = data;
    }

}
