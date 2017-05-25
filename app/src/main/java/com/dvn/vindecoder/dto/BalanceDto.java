package com.dvn.vindecoder.dto;

/**
 * Created by palash on 20-01-2017.
 */

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class BalanceDto {

    @SerializedName("API Decode")
    @Expose
    private Integer aPIDecode;

    public Integer getAPIDecode() {
        return aPIDecode;
    }

    public void setAPIDecode(Integer aPIDecode) {
        this.aPIDecode = aPIDecode;
    }

}