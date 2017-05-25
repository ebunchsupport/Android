package com.dvn.vindecoder.dto;

/**
 * Created by palash on 21-01-2017.
 */
public class VINAPISetter {

   private String apikey;
    private String sha1;
    private String decode;
    private String vin_code;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1){
        this.sha1 = sha1;
    }



    public String getDecode() {
        return decode;
    }

    public void setDecode(String decode) {
        this.decode = decode;
    }

    public String getVin_code() {
        return vin_code;
    }

    public void setVin_code(String vin_code) {
        this.vin_code = vin_code;
    }


}
