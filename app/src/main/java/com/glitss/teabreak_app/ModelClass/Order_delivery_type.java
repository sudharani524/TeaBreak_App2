package com.glitss.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order_delivery_type implements Serializable {

    @SerializedName("sub_cat_id")
    @Expose
    public String sub_cat_id;

    @SerializedName("cat_id")
    @Expose
    public String cat_id;

    @SerializedName("sub_cat_name")
    @Expose
    public String sub_cat_name;

    @SerializedName("sub_cat_status")
    @Expose
    public String sub_cat_status;


    @SerializedName("sub_cat_show")
    @Expose
    public String sub_cat_show;


    @SerializedName("sub_cat_date_created")
    @Expose
    public String sub_cat_date_created;


    @SerializedName("transport_id")
    @Expose
    public String transport_id;

    @SerializedName("transport_name")
    @Expose
    public String transport_name;

    @SerializedName("transport_address")
    @Expose
    public String transport_address;

    @SerializedName("status")
    @Expose
    public String status;

    public String getTransport_id() {
        return transport_id;
    }

    public void setTransport_id(String transport_id) {
        this.transport_id = transport_id;
    }

    public String getTransport_name() {
        return transport_name;
    }

    public void setTransport_name(String transport_name) {
        this.transport_name = transport_name;
    }

    public String getTransport_address() {
        return transport_address;
    }

    public void setTransport_address(String transport_address) {
        this.transport_address = transport_address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSub_cat_name() {
        return sub_cat_name;
    }

    public void setSub_cat_name(String sub_cat_name) {
        this.sub_cat_name = sub_cat_name;
    }

    public String getSub_cat_status() {
        return sub_cat_status;
    }

    public void setSub_cat_status(String sub_cat_status) {
        this.sub_cat_status = sub_cat_status;
    }

    public String getSub_cat_show() {
        return sub_cat_show;
    }

    public void setSub_cat_show(String sub_cat_show) {
        this.sub_cat_show = sub_cat_show;
    }

    public String getSub_cat_date_created() {
        return sub_cat_date_created;
    }

    public void setSub_cat_date_created(String sub_cat_date_created) {
        this.sub_cat_date_created = sub_cat_date_created;
    }
}
