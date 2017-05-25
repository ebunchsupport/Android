package com.dvn.vindecoder.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by alina on 18-05-2017.
 */

public class RecentListResponsModel implements Serializable {

    @SerializedName("pk_id")
    private String pk_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("seller_id")
    private String seller_id;

    @SerializedName("appid")
    private String appid;

    @SerializedName("buyer_id")
    private String buyer_id;

    @SerializedName("price")
    private String price;

    @SerializedName("bill_copy")
    private String bill_copy;

    @SerializedName("image")
    private String image;

    @SerializedName("assign_user")
    private String assign_user;

    @SerializedName("status")
    private String status;


    @SerializedName("date")
    private String date;

    @SerializedName("vehicle_id")
    private String vehicle_id;

    @SerializedName("vinno")
    private String vinno;


    @SerializedName("vinstarts")
    private String vinstarts;


    @SerializedName("make")
    private String make;

    @SerializedName("year")
    private String year;

    @SerializedName("model")
    private String model;

    @SerializedName("vehicle_type")
    private String vehicle_type;


    @SerializedName("manufacture_lable")
    private String manufacture_lable;

    public RecentListResponsModel(String pk_id, String user_id, String seller_id, String appid, String buyer_id, String price, String bill_copy, String image, String assign_user, String status, String date, String vehicle_id, String vinno, String vinstarts, String make, String year, String model, String vehicle_type, String manufacture_lable) {
        this.pk_id = pk_id;
        this.user_id = user_id;
        this.seller_id = seller_id;
        this.appid = appid;
        this.buyer_id = buyer_id;
        this.price = price;
        this.bill_copy = bill_copy;
        this.image = image;
        this.assign_user = assign_user;
        this.status = status;
        this.date = date;
        this.vehicle_id = vehicle_id;
        this.vinno = vinno;
        this.vinstarts = vinstarts;
        this.make = make;
        this.year = year;
        this.model = model;
        this.vehicle_type = vehicle_type;
        this.manufacture_lable = manufacture_lable;
    }


    public String getPk_id() {
        return pk_id;
    }

    public void setPk_id(String pk_id) {
        this.pk_id = pk_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBill_copy() {
        return bill_copy;
    }

    public void setBill_copy(String bill_copy) {
        this.bill_copy = bill_copy;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAssign_user() {
        return assign_user;
    }

    public void setAssign_user(String assign_user) {
        this.assign_user = assign_user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVinno() {
        return vinno;
    }

    public void setVinno(String vinno) {
        this.vinno = vinno;
    }

    public String getVinstarts() {
        return vinstarts;
    }

    public void setVinstarts(String vinstarts) {
        this.vinstarts = vinstarts;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getManufacture_lable() {
        return manufacture_lable;
    }

    public void setManufacture_lable(String manufacture_lable) {
        this.manufacture_lable = manufacture_lable;
    }
}
