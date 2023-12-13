package com.example.teabreak_app.ModelClass;

import android.os.Parcel;
import android.os.Parcelable;

public class orderDetails implements Parcelable {
    public String getEnc_val() {
        return enc_val;
    }

    public void setEnc_val(String enc_val) {
        this.enc_val = enc_val;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getCancel_url() {
        return cancel_url;
    }

    public void setCancel_url(String cancel_url) {
        this.cancel_url = cancel_url;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getAccess_code() {
        return access_code;
    }

    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }

    String enc_val,redirect_url,cancel_url,order_id,access_code;
    public orderDetails() {
    }

    public orderDetails(Parcel in) {
        enc_val = in.readString();
        redirect_url = in.readString();
        cancel_url = in.readString();
        order_id = in.readString();
        access_code = in.readString();
    }

    public static final Creator<orderDetails> CREATOR = new Creator<orderDetails>() {
        @Override
        public orderDetails createFromParcel(Parcel in) {
            return new orderDetails(in);
        }

        @Override
        public orderDetails[] newArray(int size) {
            return new orderDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(enc_val);
        parcel.writeString(redirect_url);
        parcel.writeString(cancel_url);
        parcel.writeString(order_id);
        parcel.writeString(access_code);
    }
}
