
package com.example.sofra.data.pojo.restaurant.commission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commission {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private CommissionData data;

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

    public CommissionData getData() {
        return data;
    }

    public void setData(CommissionData data) {
        this.data = data;
    }

}
