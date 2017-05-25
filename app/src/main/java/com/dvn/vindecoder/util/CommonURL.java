package com.dvn.vindecoder.util;

/**
 * Created by palash on 23-06-2016.
 */
public class CommonURL {
    //*************************Testing Server Url******************************//
    //public static final String URL = "http://144.76.237.246:6060/superkids/r1";
    //*************************Production Server Url***************************//
    public static final String URL = "http://ebunchapps.com/quickcross";
    public static final String APP_ID = "9aa1412851a0cc4ac468ea161bfe6a0b";
    public static final String CREATE_PARENT = "/userRegistration";
    public static final String GET_PROVIDER_PROFILE = "/providerProfile/";
    public static final String LOGIN = "/api/login";
    public static final String VEHICAL_LIST = "/api/get_sellervehicle";
    public static final String SIGNUP_USER = "/api/signup";
    public static final String USER_VEHICAL_LIST = "/api/get_vehicledetails";//http://ebunchapps.com/quickcross/api/get_selleruser
    public static final String SELLER_USER_LIST = "/api/get_selleruser";
    public static final String GET_VIN_NUM = "/api/get_vindetails ";
    public static final String GET_BUYER_NAME = "/api/get_buyerlist";
    public static final String VIN_API_LINK="https://api.vindecoder.eu/2.0";
    public static final String GET_USER_VEHICAL_SELECTED_NAME = "/api/get_assigncarlist";
    public static final String VIN_API_KEY="8ba90e6aa631";
    public static final String VIN_API_SECRET_KEY="262ce290f9";
    public static  String VIN_API_SHA1_KEY="";

    public static final String GET_UNASSIGEND_VEHICAL = "/api/get_unassigncarlist";
    public static final String GET_ASSIGEND_VEHICAL_TO_USER = "/api/get_assigncartouser";
    public static final String GET_VEHICAL_STEP_FIRST="/api/get_editfirststep";
    public static final String GET_VEHICAL_STEP_SECOND="/api/get_editsecondstep";
    public static final String GET_VEHICAL_STEP_THIRD="/api/get_editthirdstep";
    public static final String GET_VEHICAL_STEP_FOURTH="/api/get_editfourthstep";
    public static final String GET_VEHICAL_STEP_FIFTH="/api/get_editfifthstep";
    public static final String GET_STOP_CAR="/stopcar.php";
    public static  String V_ID="";
    public static  String PK_ID="";
    public static int USER_TYPE=0;//0 for user and 1 for seller

}
