package com.dvn.vindecoder.dto;

/**
 * Created by palash on 13-01-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("pass")
    @Expose
    private String pass;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("appid")
    @Expose
    private String appid;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("banned")
    @Expose
    private String banned;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("last_login_attempt")
    @Expose
    private String lastLoginAttempt;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("login_attempts")
    @Expose
    private String loginAttempts;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getBanned() {
        return banned;
    }

    public void setBanned(String banned) {
        this.banned = banned;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastLoginAttempt() {
        return lastLoginAttempt;
    }

    public void setLastLoginAttempt(String lastLoginAttempt) {
        this.lastLoginAttempt = lastLoginAttempt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(String loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}