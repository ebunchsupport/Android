package com.dvn.vindecoder.dto;

/**
 * Created by Palash on 2/15/2017.
 */


        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class UserAssingCarListDto {

    private boolean onetimeIsselected=false;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("vinno")
    @Expose
    private String vinno;

    @SerializedName("model")
    @Expose
    private String model;
    private boolean isSelected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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


    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setOneTimeIsselected(boolean onetimeIsselected1) {
        this.onetimeIsselected = onetimeIsselected1;
    }


    public boolean getOneTimeIsselected() {

        return onetimeIsselected;
    }

    public String getVinno() {
        return vinno;
    }

    public void setVinno(String vinno) {
        this.vinno = vinno;
    }

}