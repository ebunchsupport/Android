package com.dvn.vindecoder.interface_package;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Palash on 1/27/2017.
 */
public interface DataComplete extends Parcelable {
    public void onDataComplete(int i,String image_path);
}
