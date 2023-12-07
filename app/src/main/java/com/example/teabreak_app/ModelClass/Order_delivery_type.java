package com.example.teabreak_app.ModelClass;

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
