package com.example.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderdetailsModel {
    @SerializedName("order_item_id")
    @Expose
    public String order_item_id;
    @SerializedName("order_id")
    @Expose
    public String order_id;
    @SerializedName("line_item_id")
    @Expose
    public String line_item_id;
    @SerializedName("quantity")
    @Expose
    public String quantity;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("sub_total_price")
    @Expose
    public String sub_total_price;
    @SerializedName("outlet_id")
    @Expose
    public String outlet_id;
    @SerializedName("user_id")
    @Expose
    public String user_id;
    @SerializedName("vendor_id")
    @Expose
    public String vendor_id;
    @SerializedName("order_date_time")
    @Expose
    public String order_date_time;
    @SerializedName("line_item_name")
    @Expose
    public String line_item_name;
    @SerializedName("image")
    @Expose
    public String image;

    public String getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(String order_item_id) {
        this.order_item_id = order_item_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getLine_item_id() {
        return line_item_id;
    }

    public void setLine_item_id(String line_item_id) {
        this.line_item_id = line_item_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSub_total_price() {
        return sub_total_price;
    }

    public void setSub_total_price(String sub_total_price) {
        this.sub_total_price = sub_total_price;
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

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getLine_item_name() {
        return line_item_name;
    }

    public void setLine_item_name(String line_item_name) {
        this.line_item_name = line_item_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
