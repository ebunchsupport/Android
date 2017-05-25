package com.dvn.vindecoder.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by palash on 18-01-2017.
 */
public class GetVehicalDto {
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("long")
    @Expose
    private String long1;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("appid")
    @Expose
    private String appid;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("seller_id")
    @Expose
    private String seller_id;

    @SerializedName("vinno")
    @Expose
    private String vinno;//seller_id

    @SerializedName("carlist")
    @Expose
    private String carlist=null;

    @SerializedName("vid")
    @Expose
    private String vid=null;

    @SerializedName("status")
    @Expose
    private String status=null;

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return user_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

//vinno
public String getVinno() {
    return vinno;
}

    public void setVinno(String vinno) {
        this.vinno = vinno;
    }

   /* public List<String> getCarlist() {
        return carlist;
    }

    public void setCarlist(List<String> carlist) {
        this.carlist = carlist;
    }*/

    public String getCarlist() {
        return carlist;
    }

    public void setCarlist(String carlist) {
        this.carlist = carlist;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getLong1() {
        return long1;
    }

    public void setLong1(String long1) {
        this.long1 = long1;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
