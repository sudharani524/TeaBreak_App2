package com.example.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListItemsModel implements Serializable {

    @SerializedName("line_item_id")
    @Expose
    public String line_item_id;

    @SerializedName("quantity")
    @Expose
    public String quantity;

    @SerializedName("line_item_name")
    @Expose
    public String line_item_name;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("pack_of_qty")
    @Expose
    public String pack_of_qty;

    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("date_created")
    @Expose
    public String date_created;



    @SerializedName("cart_id")
    @Expose
    public String cart_id;

    @SerializedName("user_id")
    @Expose
    public String user_id;


    @SerializedName("update_date_time")
    @Expose
    public String update_date_time;

    @SerializedName("available_quantity")
    @Expose
    public String available_quantity;

    public String getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(String available_quantity) {
        this.available_quantity = available_quantity;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUpdate_date_time() {
        return update_date_time;
    }

    public void setUpdate_date_time(String update_date_time) {
        this.update_date_time = update_date_time;
    }

    public String getLine_item_id() {
        return line_item_id;
    }

    public void setLine_item_id(String line_item_id) {
        this.line_item_id = line_item_id;
    }

    public String getLine_item_name() {
        return line_item_name;
    }

    public void setLine_item_name(String line_item_name) {
        this.line_item_name = line_item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPack_of_qty() {
        return pack_of_qty;
    }

    public void setPack_of_qty(String pack_of_qty) {
        this.pack_of_qty = pack_of_qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
