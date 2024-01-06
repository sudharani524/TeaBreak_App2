package com.example.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderHistoryModel implements Serializable {
    @SerializedName("order_id")
    @Expose
    public String order_id;
    @SerializedName("order_no")
    @Expose
    public String order_no;
    @SerializedName("outlet_id")
    @Expose
    public String outlet_id;
    @SerializedName("user_id")
    @Expose
    public String user_id;
    @SerializedName("vendor_id")
    @Expose
    public String vendor_id;
    @SerializedName("total_quantity")
    @Expose
    public String total_quantity;
    @SerializedName("total_line_items_price")
    @Expose
    public String total_line_items_price;
    @SerializedName("discount")
    @Expose
    public String discount;
    @SerializedName("total_amount")
    @Expose
    public String total_amount;
    @SerializedName("paid_amount")
    @Expose
    public String paid_amount;
    @SerializedName("order_date_time")
    @Expose
    public String order_date_time;
    @SerializedName("order_status")
    @Expose
    public String order_status;
    @SerializedName("payment_trans_id")
    @Expose
    public String payment_trans_id;
    @SerializedName("delivery_type_name")
    @Expose
    public String delivery_type_name;


    @SerializedName("used_wallet_amount")
    @Expose
    public String used_wallet_amount;

    public String getUsed_wallet_amount() {
        return used_wallet_amount;
    }

    public void setUsed_wallet_amount(String used_wallet_amount) {
        this.used_wallet_amount = used_wallet_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
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

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getTotal_line_items_price() {
        return total_line_items_price;
    }

    public void setTotal_line_items_price(String total_line_items_price) {
        this.total_line_items_price = total_line_items_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPayment_trans_id() {
        return payment_trans_id;
    }

    public void setPayment_trans_id(String payment_trans_id) {
        this.payment_trans_id = payment_trans_id;
    }

    public String getDelivery_type_name() {
        return delivery_type_name;
    }

    public void setDelivery_type_name(String delivery_type_name) {
        this.delivery_type_name = delivery_type_name;
    }
}
