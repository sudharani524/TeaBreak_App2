package com.example.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemsorderedModel {
    @SerializedName("order_id")
    @Expose
    public String order_id;
    @SerializedName("sub_total_price")
    @Expose
    public String sub_total_price;
    @SerializedName("quantity")
    @Expose
    public String quantity;
    @SerializedName("line_item_name")
    @Expose
    public String line_item_name;
    @SerializedName("vendor_name")
    @Expose
    public String vendor_name;
    @SerializedName("order_date_time")
    @Expose
    public String order_date_time;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("line_item_id")
    @Expose
    public String line_item_id;
    @SerializedName("pack_of_qty")
    @Expose
    public String pack_of_qty;

    @SerializedName("order_item_id")
    @Expose
    public String order_item_id;


    @SerializedName("available_quantity")
    @Expose
    public String available_quantity;



    @SerializedName("actually_order_qty")
    @Expose
    public String actually_order_qty;

    public String getActually_order_qty() {
        return actually_order_qty;
    }

    public void setActually_order_qty(String actually_order_qty) {
        this.actually_order_qty = actually_order_qty;
    }

    public String getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(String available_quantity) {
        this.available_quantity = available_quantity;
    }

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

    public String getSub_total_price() {
        return sub_total_price;
    }

    public void setSub_total_price(String sub_total_price) {
        this.sub_total_price = sub_total_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLine_item_name() {
        return line_item_name;
    }

    public void setLine_item_name(String line_item_name) {
        this.line_item_name = line_item_name;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(String order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLine_item_id() {
        return line_item_id;
    }

    public void setLine_item_id(String line_item_id) {
        this.line_item_id = line_item_id;
    }

    public String getPack_of_qty() {
        return pack_of_qty;
    }

    public void setPack_of_qty(String pack_of_qty) {
        this.pack_of_qty = pack_of_qty;
    }
}
