package com.example.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WallethistoryModel implements Serializable {
    @SerializedName("wallet_id")
    @Expose
    public String wallet_id;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("user_id")
    @Expose
    public String user_id;
    @SerializedName("vendor_id")
    @Expose
    public String vendor_id;
    @SerializedName("order_id")
    @Expose
    public String order_id;
    @SerializedName("order_date_time")
    @Expose
    public String order_date_time;
    @SerializedName("used_amount")
    @Expose
    public String used_amount;
    @SerializedName("balanced_amount")
    @Expose
    public String balanced_amount;
    @SerializedName("amount_refunded")
    @Expose
    public String amount_refunded;
    @SerializedName("date_created")
    @Expose
    public String date_created;

    public String getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getUsed_amount() {
        return used_amount;
    }

    public void setUsed_amount(String used_amount) {
        this.used_amount = used_amount;
    }

    public String getBalanced_amount() {
        return balanced_amount;
    }

    public void setBalanced_amount(String balanced_amount) {
        this.balanced_amount = balanced_amount;
    }

    public String getAmount_refunded() {
        return amount_refunded;
    }

    public void setAmount_refunded(String amount_refunded) {
        this.amount_refunded = amount_refunded;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
