package com.glitss.teabreak_app.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsersRolesModel implements Serializable {

    @SerializedName("role_id")
    @Expose
    public String role_id;

    @SerializedName("role_full_name")
    @Expose
    public String role_full_name;

    @SerializedName("role_name")
    @Expose
    public String role_name;

    @SerializedName("display_order")
    @Expose
    public String display_order;

    @SerializedName("role_status")
    @Expose
    public String role_status;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_full_name() {
        return role_full_name;
    }

    public void setRole_full_name(String role_full_name) {
        this.role_full_name = role_full_name;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(String display_order) {
        this.display_order = display_order;
    }

    public String getRole_status() {
        return role_status;
    }

    public void setRole_status(String role_status) {
        this.role_status = role_status;
    }


    //Farmer Details

    @SerializedName("farmer_id")
    @Expose
    public String farmer_id;

    @SerializedName("farmer_name")
    @Expose
    public String farmer_name;

    @SerializedName("aadhar_no")
    @Expose
    public String aadhar_no;

    @SerializedName("farmer_address")
    @Expose
    public String farmer_address;

    @SerializedName("village_name")
    @Expose
    public String village_name;

    @SerializedName("state")
    @Expose
    public String state;

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("pincode")
    @Expose
    public String pincode;

    @SerializedName("mobile_no")
    @Expose
    public String mobile_no;

    @SerializedName("email_id")
    @Expose
    public String email_id;

    @SerializedName("rbk_id")
    @Expose
    public String rbk_id;

    @SerializedName("rbk_name")
    @Expose
    public String rbk_name;

    @SerializedName("land_area")
    @Expose
    public String land_area;

    @SerializedName("paddy_available")
    @Expose
    public String paddy_available;

    @SerializedName("estimated_quantity")
    @Expose
    public String estimated_quantity;

    @SerializedName("balance_quantity")
    @Expose
    public String balance_quantity;

    @SerializedName("scheduled_status")
    @Expose
    public String scheduled_status;

    @SerializedName("remarks")
    @Expose
    public String remarks;

    @SerializedName("paddy_bags")
    @Expose
    public String paddy_bags;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("dateCreated")
    @Expose
    public String dateCreated;

    @SerializedName("procurement_type")
    @Expose
    public String procurement_type;

    @SerializedName("no_of_bags")
    @Expose
    public String no_of_bags;

    public String getNo_of_bags() {
        return no_of_bags;
    }

    public void setNo_of_bags(String no_of_bags) {
        this.no_of_bags = no_of_bags;
    }

    public String getProcurement_type() {
        return procurement_type;
    }

    public void setProcurement_type(String procurement_type) {
        this.procurement_type = procurement_type;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public String getFarmer_name() {
        return farmer_name;
    }

    public void setFarmer_name(String farmer_name) {
        this.farmer_name = farmer_name;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public String getFarmer_address() {
        return farmer_address;
    }

    public void setFarmer_address(String farmer_address) {
        this.farmer_address = farmer_address;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
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

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getRbk_id() {
        return rbk_id;
    }

    public void setRbk_id(String rbk_id) {
        this.rbk_id = rbk_id;
    }

    public String getRbk_name() {
        return rbk_name;
    }

    public void setRbk_name(String rbk_name) {
        this.rbk_name = rbk_name;
    }

    public String getLand_area() {
        return land_area;
    }

    public void setLand_area(String land_area) {
        this.land_area = land_area;
    }

    public String getPaddy_available() {
        return paddy_available;
    }

    public void setPaddy_available(String paddy_available) {
        this.paddy_available = paddy_available;
    }

    public String getEstimated_quantity() {
        return estimated_quantity;
    }

    public void setEstimated_quantity(String estimated_quantity) {
        this.estimated_quantity = estimated_quantity;
    }

    public String getBalance_quantity() {
        return balance_quantity;
    }

    public void setBalance_quantity(String balance_quantity) {
        this.balance_quantity = balance_quantity;
    }

    public String getScheduled_status() {
        return scheduled_status;
    }

    public void setScheduled_status(String scheduled_status) {
        this.scheduled_status = scheduled_status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPaddy_bags() {
        return paddy_bags;
    }

    public void setPaddy_bags(String paddy_bags) {
        this.paddy_bags = paddy_bags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
