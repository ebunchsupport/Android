package com.dvn.vindecoder.dto;

/**
 * Created by palash on 13-01-2017.
 */
import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class LoginDto {

    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("deviceType")
    @Expose
    private String deviceType;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("appid")
    @Expose
    private String appid;

    @SerializedName("response")
    @Expose
    private Response response;

    @SerializedName("role")
    @Expose
    private String role;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
}


