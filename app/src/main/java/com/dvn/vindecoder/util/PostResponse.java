package com.dvn.vindecoder.util;

import com.dvn.vindecoder.dto.BalanceDto;
import com.dvn.vindecoder.dto.BuyerListDto;
import com.dvn.vindecoder.dto.CarDto1;
import com.dvn.vindecoder.dto.DecodeDto;
import com.dvn.vindecoder.dto.Response;
import com.dvn.vindecoder.dto.SellerVehicalListDto;
import com.dvn.vindecoder.dto.UserAssingCarListDto;
import com.dvn.vindecoder.dto.UserSellerListDto;
import com.dvn.vindecoder.dto.SellerVehicalDetailDto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by palash on 23-06-2016.
 */

public class PostResponse {


    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("vehicledetails")
    @Expose
    private List<SellerVehicalDetailDto> vehicledetails;
    @SerializedName("sellerdata")
    @Expose
    private List<SellerVehicalListDto> sellerdata = null;

    @SerializedName("selleruserlist")
    @Expose
    private List<UserSellerListDto> sellerUserList;

    @SerializedName("buyerlist")
    @Expose
    private List<BuyerListDto> buyerlist = null;

    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("price_currency")
    @Expose
    private String priceCurrency;
    @SerializedName("balance")
    @Expose
    private BalanceDto balance;
    @SerializedName("decode")
    @Expose
    private List<DecodeDto> decode = null;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("assingcarlist")//unassigncarlist
    @Expose
    private List<UserAssingCarListDto> assingcarlist = null;
    @SerializedName("unassigncarlist")//
    @Expose
    private List<UserAssingCarListDto> assingcarlist1 = null;
    @SerializedName("cardata")
    @Expose
    private List<CarDto1> cardata = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
    public List<SellerVehicalDetailDto> getVehicledetails() {
        return vehicledetails;
    }

    public void setVehicledetails(List<SellerVehicalDetailDto> vehicledetails) {
        this.vehicledetails = vehicledetails;
    }

    public List<SellerVehicalListDto> getSellerdata() {
        return sellerdata;
    }

    public void setSellerdata(List<SellerVehicalListDto> sellerdata) {
        this.sellerdata = sellerdata;
    }

    public List<UserSellerListDto> getSellerUserList() {
        return sellerUserList;
    }

    public void setSellerUserList(List<UserSellerListDto> userList) {
        this.sellerUserList = userList;
    }

    public List<BuyerListDto> getBuyerlist() {
        return buyerlist;
    }

    public void setBuyerlist(List<BuyerListDto> buyerlist) {
        this.buyerlist = buyerlist;
    }


    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public BalanceDto getBalance() {
        return balance;
    }

    public void setBalance(BalanceDto balance) {
        this.balance = balance;
    }

    public List<DecodeDto> getDecode() {
        return decode;
    }

    public void setDecode(List<DecodeDto> decode) {
        this.decode = decode;
    }

    public List<UserAssingCarListDto> getAssingcarlist() {
        return assingcarlist;
    }

    public void setAssingcarlist(List<UserAssingCarListDto> assingcarlist) {
        this.assingcarlist = assingcarlist;
    }

    public List<UserAssingCarListDto> getUnAssingcarlist() {
        return assingcarlist1;
    }

    public void setUnAssingcarlist(List<UserAssingCarListDto> assingcarlist1) {
        this.assingcarlist1 = assingcarlist1;
    }
    public List<CarDto1> getCardata() {
        return cardata;
    }

    public void setCardata(List<CarDto1> cardata) {
        this.cardata = cardata;
    }
}

