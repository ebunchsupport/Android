package com.dvn.vindecoder.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by palash on 16-06-2016.
 */
public class LoginDB {


    public static boolean LOGIN_FLAG;

    public static void setServiceName(Context context, String key_service_name, String value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key_service_name, value);
        edit.commit();
    }

    public static String getServiceName(Context context, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String string = pref.getString(key, "");
        return string;
    }
    public static void setUserType(Context context, String user_type, String value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(user_type, value);
        edit.commit();
    }

    public static String getUserType(Context context, String user_type) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String string = pref.getString(user_type, "");
        return string;
    }

    public static void setLoginFlag(Context context, Boolean flag) {
        SharedPreferences spp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = spp.edit();
        edit.putBoolean("LOGIN_FLAG", flag);
        edit.commit();
    }

    public static Boolean getLoginFlag(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean flag = pref.getBoolean("LOGIN_FLAG", false);
        return flag;
    }

    public static void setTitle(Context context, String keyString, String titleName) {
        Log.e("HelperClass", "Set Title  : " + titleName);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(keyString, titleName);
        edit.commit();
    }

    public static String getTitle(Context context, String keyTitle) {
        Log.e("HelperClass", "Get Title  : " + keyTitle);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String titleName = pref.getString(keyTitle, "");
        return titleName;

    }

    public static void setSubCategoryName(Context context, String key_subcategory_name, String subcategoryValue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key_subcategory_name, subcategoryValue);
        edit.commit();

    }

    public static String getSubCategoryName(Context context, String keySubCategroy) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String titleName = pref.getString(keySubCategroy, "");
        return titleName;

    }

    public static void setServiceProvider(Context context, String subcategoryValue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("serviceProvider", subcategoryValue);
        Log.e("serv", subcategoryValue);
        edit.commit();

    }

    public static String getServiceProvider(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String titleName = pref.getString("serviceProvider", "");
        return titleName;

    }


    public static void setVehicleFlag(Context context,String screen_name ,Boolean flag) {
        SharedPreferences spp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = spp.edit();
        edit.putBoolean(screen_name, flag);
        edit.commit();
    }

    public static Boolean getVehicleFlag(Context context,String screen_name) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean flag = pref.getBoolean(screen_name, false);
        return flag;
    }
}
