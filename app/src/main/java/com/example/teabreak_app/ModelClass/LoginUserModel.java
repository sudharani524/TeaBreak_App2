package com.example.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginUserModel implements Serializable {
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("user_mobile")
    @Expose
    public String user_mobile;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("user_email")
    @Expose
    public String user_email;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("role_id")
    @Expose
    public String role_id;

    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("login_user_id")
    @Expose
    public String login_user_id;

    @SerializedName("login_username")
    @Expose
    public String login_username;

    @SerializedName("scroll_msg")
    @Expose
    public String scroll_msg;


    @SerializedName("address")
    @Expose
    public String address;

    @SerializedName("role_full_name")
    @Expose
    public String role_full_name;
    @SerializedName("wallet_amount")
    @Expose
    public String wallet_amount;

    @SerializedName("country")
    @Expose
    public String country;


    @SerializedName("state")
    @Expose
    public String state;

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("pincode")
    @Expose
    public String pincode;

    @SerializedName("outlet_name")
    @Expose
    public String outlet_name;

    @SerializedName("outlet_code")
    @Expose
    public String outlet_code;

    public String getOutlet_code() {
        return outlet_code;
    }

    public void setOutlet_code(String outlet_code) {
        this.outlet_code = outlet_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public String getRole_full_name() {
        return role_full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRole_full_name(String role_full_name) {
        this.role_full_name = role_full_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin_user_id() {
        return login_user_id;
    }

    public void setLogin_user_id(String login_user_id) {
        this.login_user_id = login_user_id;
    }

    public String getLogin_username() {
        return login_username;
    }

    public void setLogin_username(String login_username) {
        this.login_username = login_username;
    }

    public String getScroll_msg() {
        return scroll_msg;
    }

    public void setScroll_msg(String scroll_msg) {
        this.scroll_msg = scroll_msg;
    }

    public String getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }
}
