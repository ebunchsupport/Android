package com.dvn.vindecoder.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by palash on 18-01-2017.
 */
public class SetUserofSellertDto {

    //,,fax,tax,number
//name,contact,address,mobile,email,password,appid,role,seller  devicetype,deviceid
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("deviceType")
    @Expose
    private String deviceType;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;


    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("seller")
    @Expose
    private String seller;

    @SerializedName("appid")
    @Expose
    private String appid;

    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("contact")
    @Expose
    private String contact;

    public String getDevicetype() {
        return deviceType;
    }

    public void setDevicetype(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceid() {
        return deviceId;
    }

    public void setDeviceid(String deviceId){
        this.deviceId = deviceId;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
