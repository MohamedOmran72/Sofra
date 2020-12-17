
package com.example.sofra.data.pojo.restaurant.commission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommissionData {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("commissions")
    @Expose
    private String commissions;
    @SerializedName("payments")
    @Expose
    private String payments;
    @SerializedName("net_commissions")
    @Expose
    private Double netCommissions;
    @SerializedName("commission")
    @Expose
    private String commission;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCommissions() {
        return commissions;
    }

    public void setCommissions(String commissions) {
        this.commissions = commissions;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public Double getNetCommissions() {
        return netCommissions;
    }

    public void setNetCommissions(Double netCommissions) {
        this.netCommissions = netCommissions;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

}
