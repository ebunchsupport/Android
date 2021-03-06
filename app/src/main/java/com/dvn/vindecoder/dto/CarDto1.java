package com.dvn.vindecoder.dto;

/**
 * Created by Palash on 2/16/2017.
 */


import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class CarDto1 {

    @SerializedName("vid")
    @Expose
    private String vid;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("image_frontdriverside")
    @Expose
    private String image_frontdriverside;
    @SerializedName("image_passengerseatbelt")
    @Expose
    private String imagePassengerseatbelt;
    @SerializedName("image_of_tire")
    @Expose
    private String imageOfTire;
    @SerializedName("image_passengerairbag")
    @Expose
    private String imagePassengerairbag;
    @SerializedName("image_driverseatbelt")
    @Expose
    private String imageDriverseatbelt;
    @SerializedName("image_driverairbag")
    @Expose
    private String imageDriverairbag;
    @SerializedName("speedo")
    @Expose
    private String speedo;
    @SerializedName("speedo_converter")
    @Expose
    private String speedoConverter;
    @SerializedName("speedo_image")
    @Expose
    private String speedoImage;
    @SerializedName("km-miles")
    @Expose
    private String kmMiles;
    @SerializedName("ri")
    @Expose
    private String ri;
    @SerializedName("is_epa")
    @Expose
    private String isEpa;
    @SerializedName("vinplate")
    @Expose
    private String vinplate;
    @SerializedName("image_vinplate")
    @Expose
    private String imageVinplate;
    @SerializedName("image_rearandpassengerside")
    @Expose
    private String imageRearandpassengerside;
    @SerializedName("emisssions")
    @Expose
    private String emisssions;
    @SerializedName("vinno")
    @Expose
    private String vinno;
    @SerializedName("vinstarts")
    @Expose
    private String vinstarts;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("buyer")
    @Expose
    private String buyer;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("bill_copy")
    @Expose
    private String billCopy;
    @SerializedName("copy_of_reg")
    @Expose
    private String copyOfReg;
    @SerializedName("recall_free")
    @Expose
    private String recallFree;
    @SerializedName("copy_of_recalldoc")
    @Expose
    private String copyOfRecalldoc;
    @SerializedName("manufacture_lable")
    @Expose
    private String manufactureLable;
    @SerializedName("image_of_manufacture")
    @Expose
    private String image_of_manufacture;
    @SerializedName("weight_on_manufacture")
    @Expose
    private String weightOnManufacture;
    @SerializedName("gvwr")
    @Expose
    private String gvwr;
    @SerializedName("gvwrinlbs")
    @Expose
    private String gvwrinlbs;
    @SerializedName("frontgawr")
    @Expose
    private String frontgawr;
    @SerializedName("reargawr")
    @Expose
    private String reargawr;
    @SerializedName("istire")
    @Expose
    private String istire;
    @SerializedName("fronttire")
    @Expose
    private String fronttire;
    @SerializedName("frontrimsize")
    @Expose
    private String frontrimsize;
    @SerializedName("fronttirepressure")
    @Expose
    private String fronttirepressure;
    @SerializedName("reartiresize")
    @Expose
    private String reartiresize;
    @SerializedName("rearrimsize")
    @Expose
    private String rearrimsize;
    @SerializedName("reartirepressure")
    @Expose
    private String reartirepressure;


    public String getImagePassengerseatbelt() {
        return imagePassengerseatbelt;
    }

    public void setImagePassengerseatbelt(String imagePassengerseatbelt) {
        this.imagePassengerseatbelt = imagePassengerseatbelt;
    }



    public String getImageOfTire() {
        return imageOfTire;
    }

    public void setImageOfTire(String imageOfTire) {
        this.imageOfTire = imageOfTire;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getBillCopy() {
        return billCopy;
    }

    public void setBillCopy(String billCopy) {
        this.billCopy = billCopy;
    }

    public String getCopyOfReg() {
        return copyOfReg;
    }

    public void setCopyOfReg(String copyOfReg) {
        this.copyOfReg = copyOfReg;
    }

    public String getRecallFree() {
        return recallFree;
    }

    public void setRecallFree(String recallFree) {
        this.recallFree = recallFree;
    }

    public String getCopyOfRecalldoc() {
        return copyOfRecalldoc;
    }

    public void setCopyOfRecalldoc(String copyOfRecalldoc) {
        this.copyOfRecalldoc = copyOfRecalldoc;
    }

    public String getManufactureLable() {
        return manufactureLable;
    }

    public void setManufactureLable(String manufactureLable) {
        this.manufactureLable = manufactureLable;
    }

    public String getWeightOnManufacture() {
        return weightOnManufacture;
    }

    public void setWeightOnManufacture(String weightOnManufacture) {
        this.weightOnManufacture = weightOnManufacture;
    }

    public String getGvwr() {
        return gvwr;
    }

    public void setGvwr(String gvwr) {
        this.gvwr = gvwr;
    }

    public String getGvwrinlbs() {
        return gvwrinlbs;
    }

    public void setGvwrinlbs(String gvwrinlbs) {
        this.gvwrinlbs = gvwrinlbs;
    }

    public String getFrontgawr() {
        return frontgawr;
    }

    public void setFrontgawr(String frontgawr) {
        this.frontgawr = frontgawr;
    }

    public String getReargawr() {
        return reargawr;
    }

    public void setReargawr(String reargawr) {
        this.reargawr = reargawr;
    }

    public String getIstire() {
        return istire;
    }

    public void setIstire(String istire) {
        this.istire = istire;
    }
    public String getFronttire() {
        return fronttire;
    }

    public void setFronttire(String fronttire) {
        this.fronttire = fronttire;
    }

    public String getFrontrimsize() {
        return frontrimsize;
    }

    public void setFrontrimsize(String frontrimsize) {
        this.frontrimsize = frontrimsize;
    }

    public String getFronttirepressure() {
        return fronttirepressure;
    }

    public void setFronttirepressure(String fronttirepressure) {
        this.fronttirepressure = fronttirepressure;
    }

    public String getReartiresize() {
        return reartiresize;
    }

    public void setReartiresize(String reartiresize) {
        this.reartiresize = reartiresize;
    }

    public String getRearrimsize() {
        return rearrimsize;
    }

    public void setRearrimsize(String rearrimsize) {
        this.rearrimsize = rearrimsize;
    }

    public String getReartirepressure() {
        return reartirepressure;
    }

    public void setReartirepressure(String reartirepressure) {
        this.reartirepressure = reartirepressure;
    }
    public String getIsEpa() {
        return isEpa;
    }

    public void setIsEpa(String isEpa) {
        this.isEpa = isEpa;
    }

    public String getVinplate() {
        return vinplate;
    }

    public void setVinplate(String vinplate) {
        this.vinplate = vinplate;
    }

    public String getImageVinplate() {
        return imageVinplate;
    }

    public void setImageVinplate(String imageVinplate) {
        this.imageVinplate = imageVinplate;
    }

    public String getImageRearandpassengerside() {
        return imageRearandpassengerside;
    }

    public void setImageRearandpassengerside(String imageRearandpassengerside) {
        this.imageRearandpassengerside = imageRearandpassengerside;
    }

    public String getEmisssions() {
        return emisssions;
    }

    public void setEmisssions(String emisssions) {
        this.emisssions = emisssions;
    }
    public String getImagePassengerairbag() {
        return imagePassengerairbag;
    }

    public void setImagePassengerairbag(String imagePassengerairbag) {
        this.imagePassengerairbag = imagePassengerairbag;
    }

    public String getImageDriverseatbelt() {
        return imageDriverseatbelt;
    }

    public void setImageDriverseatbelt(String imageDriverseatbelt) {
        this.imageDriverseatbelt = imageDriverseatbelt;
    }

    public String getImageDriverairbag() {
        return imageDriverairbag;
    }

    public void setImageDriverairbag(String imageDriverairbag) {
        this.imageDriverairbag = imageDriverairbag;
    }

    public String getSpeedo() {
        return speedo;
    }

    public void setSpeedo(String speedo) {
        this.speedo = speedo;
    }

    public String getSpeedoConverter() {
        return speedoConverter;
    }

    public void setSpeedoConverter(String speedoConverter) {
        this.speedoConverter = speedoConverter;
    }

    public String getSpeedoImage() {
        return speedoImage;
    }

    public void setSpeedoImage(String speedoImage) {
        this.speedoImage = speedoImage;
    }

    public String getKmMiles() {
        return kmMiles;
    }

    public void setKmMiles(String kmMiles) {
        this.kmMiles = kmMiles;
    }

    public String getRi() {
        return ri;
    }

    public void setRi(String ri) {
        this.ri = ri;
    }
    public String getImageOfManufacture() {
        return image_of_manufacture;
    }

    public void setImageOfManufacture(String image_of_manufacture) {
        this.image_of_manufacture = image_of_manufacture;
    }
    public String getImagefrontdriverside() {
        return image_frontdriverside;
    }

    public void setImageFrontdriverside(String image_frontdriverside) {
        this.image_frontdriverside = image_frontdriverside;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
