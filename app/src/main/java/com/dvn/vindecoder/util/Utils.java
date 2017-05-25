package com.dvn.vindecoder.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.util.HashMap;


/**
 * Created by IT-0870 on 20-May-16.
 */
public class Utils {

    public static void setUserLogin(Context context, String userid, String name, String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", userid);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.apply();
    }




public  static HashMap<String,String> getUserPrefrens(Context context){
    SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
    HashMap<String,String> values=new HashMap<String, String>();
    values.put("id", preferences.getString("id",null));
    values.put("name", preferences.getString("name",null));
    values.put("email", preferences.getString("email",null));

    return values;
}






    public static String getImeiNumber(Activity context) {
        String ImeiNumber = null;
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ImeiNumber = System.currentTimeMillis() + "";
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
            ImeiNumber = tm.getDeviceId();
        }

        return ImeiNumber;
    }


    public static int getOSVersionNumber() {
        return android.os.Build.VERSION.SDK_INT;
    }




}
