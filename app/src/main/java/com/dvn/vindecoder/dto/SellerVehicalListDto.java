package com.dvn.vindecoder.dto;

/**
 * Created by palash on 18-01-2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SellerVehicalListDto {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("assign_user")
    @Expose
    private String assign_user;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("vinno")
    @Expose
    private String vinno;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("username")
    @Expose
    private String username;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssign_user() {
        return assign_user;
    }

    public void setAssign_user(String assign_user) {
        this.assign_user = assign_user;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVinno() {
        return vinno;
    }

    public void setVinno(String vinno) {
        this.vinno = vinno;
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

    public String getImage() {
        return image;
    }

    public void setImage(String model) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}

