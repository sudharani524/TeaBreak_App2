package com.example.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderedlistModel  implements Serializable {
    @SerializedName("order_no")
    @Expose
    public String order_no;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("outlet_name")
    @Expose
    public String outlet_name;
    @SerializedName("vendor_name")
    @Expose
    public String vendor_name;
    @SerializedName("vendor_mobile")
    @Expose
    public String vendor_mobile;
    @SerializedName("outlet_address")
    @Expose
    public String outlet_address;
    @SerializedName("total_amount")
    @Expose
    public String total_amount;
    @SerializedName("order_date_time")
    @Expose
    public String order_date_time;
    @SerializedName("order_id")
    @Expose
    public String order_id;
    @SerializedName("total_quantity")
    @Expose
    public String total_quantity;


    @SerializedName("online_paid_amount")
    @Expose
    public String online_paid_amount;

    @SerializedName("used_wallet_amount")
    @Expose
    public String used_wallet_amount;

    @SerializedName("outlet_code")
    @Expose
    public String outlet_code;

    public String getOutlet_code() {
        return outlet_code;
    }

    public void setOutlet_code(String outlet_code) {
        this.outlet_code = outlet_code;
    }

    @SerializedName("accounts_updated_date_time")
    @Expose
    public String accounts_updated_date_time;

    public String getUsed_wallet_amount() {
        return used_wallet_amount;
    }

    public void setUsed_wallet_amount(String used_wallet_amount) {
        this.used_wallet_amount = used_wallet_amount;
    }

    public String getAccounts_updated_date_time() {
        return accounts_updated_date_time;
    }

    public void setAccounts_updated_date_time(String accounts_updated_date_time) {
        this.accounts_updated_date_time = accounts_updated_date_time;
    }

    public String getOnline_paid_amount() {
        return online_paid_amount;
    }

    public void setOnline_paid_amount(String online_paid_amount) {
        this.online_paid_amount = online_paid_amount;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getVendor_mobile() {
        return vendor_mobile;
    }

    public void setVendor_mobile(String vendor_mobile) {
        this.vendor_mobile = vendor_mobile;
    }

    public String getOutlet_address() {
        return outlet_address;
    }

    public void setOutlet_address(String outlet_address) {
        this.outlet_address = outlet_address;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }
}
