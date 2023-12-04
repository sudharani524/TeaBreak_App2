package com.example.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListItemsModel implements Serializable {

    @SerializedName("LineItemId")
    @Expose
    public String LineItemId;


    @SerializedName("LineItems")
    @Expose
    public String LineItems;

    @SerializedName("Price")
    @Expose
    public String Price;

    @SerializedName("PackQty")
    @Expose
    public String PackQty;

    @SerializedName("Image")
    @Expose
    public String Image;

    @SerializedName("Status")
    @Expose
    public String Status;

    @SerializedName("DateCreated")
    @Expose
    public String DateCreated;


    public String getLineItemId() {
        return LineItemId;
    }

    public void setLineItemId(String lineItemId) {
        LineItemId = lineItemId;
    }

    public String getLineItems() {
        return LineItems;
    }

    public void setLineItems(String lineItems) {
        LineItems = lineItems;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPackQty() {
        return PackQty;
    }

    public void setPackQty(String packQty) {
        PackQty = packQty;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }
}
