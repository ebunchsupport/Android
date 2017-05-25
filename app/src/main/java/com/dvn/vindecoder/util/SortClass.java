package com.dvn.vindecoder.util;

/**
 * Created by palash on 24-06-2016.
 */
public class SortClass {

    public static int sortFun(String[] val, String temp) {
        if(val!=null) {
            for (int i = 0; i < val.length; i++) {
                //Log.e("val:",val[i]);
                if (val[i].trim().compareTo(temp.trim()) == 0) {
                    return 1;
                }
            }
        }
        return 0;

    }
}
