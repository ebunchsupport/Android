package com.dvn.vindecoder.util;

import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.dto.LoginDto;
import com.dvn.vindecoder.dto.SetUserofSellertDto;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by palash on 23-06-2016.
 */
public interface Retrofit {
    //public static String server = CommonURL.URL;

    /**
     * @param loginDto
     * @return
     */
    @POST(CommonURL.LOGIN)
    PostResponse postLogin(@Body LoginDto loginDto);

    @POST(CommonURL.VEHICAL_LIST)
    PostResponse postVehicalList(@Body GetVehicalDto getVehicalDto);

    @POST(CommonURL.SELLER_USER_LIST)
    PostResponse postUserSellerList(@Body GetVehicalDto getVehicalDto);


    @POST(CommonURL.USER_VEHICAL_LIST)
    PostResponse postUserVehicalList(@Body GetVehicalDto getVehicalDto);

    @POST(CommonURL.SIGNUP_USER)
    PostResponse postSaveUser(@Body SetUserofSellertDto setUserofSellertDto);


    @POST(CommonURL.GET_VIN_NUM)
    PostResponse postVinInfo(@Body GetVehicalDto getVinDto);

    @POST(CommonURL.GET_BUYER_NAME)
    PostResponse postGetBuyerList();

    @POST("/")
    PostResponse postVINList();

    @POST(CommonURL.GET_USER_VEHICAL_SELECTED_NAME)
    PostResponse postUserVehicalAssignedList(@Body GetVehicalDto objectTosend);

    @POST(CommonURL.GET_UNASSIGEND_VEHICAL)
    PostResponse postUnAssignedVehicalList(@Body GetVehicalDto objectTosend);

    @POST(CommonURL.GET_ASSIGEND_VEHICAL_TO_USER)
    PostResponse postAssignedVehicalToUser(@Body GetVehicalDto objectTosend);

    @POST(CommonURL.GET_VEHICAL_STEP_FIRST)
    PostResponse postUserDetailStepFirst(@Body GetVehicalDto objectTosend);

    @POST(CommonURL.GET_VEHICAL_STEP_SECOND)
    PostResponse postUserDetailStepSecond(@Body GetVehicalDto objectTosend);

    @POST(CommonURL.GET_VEHICAL_STEP_THIRD)
    PostResponse postUserDetailStepThird(@Body GetVehicalDto objectTosend);

    @POST(CommonURL.GET_VEHICAL_STEP_FOURTH)
    PostResponse postUserDetailStepFourth(@Body GetVehicalDto objectTosend);

    @POST(CommonURL.GET_VEHICAL_STEP_FIFTH)
    PostResponse postUserDetailStepFifth(@Body GetVehicalDto objectTosend);

    @POST(CommonURL.GET_STOP_CAR)
    PostResponse postStopCar(@Body GetVehicalDto objectTosend);




    //@POST("/childRegistration/{userId}")
    //PostResponse postAddChild(@Path("userId") String providerId, @Body AddChildDto signupDto);

    @GET("/userProfile/{userId}")
    PostResponse postGetProfile(@Path("userId") String providerId, @Query("task") String childProfile);

    @GET("/subCategory/{userId}")
    PostResponse getSubCategory(@Path("userId") String providerId, @Query("category") String subCategory);

    @GET("/tuition/{userId}")
    PostResponse getSubServiceCity(@Path("userId") String providerId, @Query("where") String subCategory, @Query("subject") String subject);

    @GET("/training/{providerId}")
    PostResponse getTrainingServiceCity(@Path("providerId") String providerId, @Query("subject") String subject, @Query("where") String subCategory);

    @GET("/dayCare/{userId}")
    PostResponse getDayCare(@Path("userId") String providerId, @Query("where") String subCategory, @Query("subject") String subject);

    @GET("/school/{userId}")
    PostResponse getSchoolDetail(@Path("userId") String providerId, @Query("type") String subject, @Query("where") String subCategory);

    @GET("/health/{userId}")
    PostResponse getHealthDetail(@Path("userId") String providerId, @Query("serviceName") String subject, @Query("where") String subCategory);

    @GET("/sport/{userId}")
    PostResponse getSportsDetail(@Path("userId") String providerId, @Query("where") String subCategory, @Query("type") String subject);

    @GET("/celebration/{userId}")
    PostResponse getCelebrationDetail(@Path("userId") String providerId, @Query("where") String subCategory, @Query("type") String subject);

    @GET("/event/{userId}")
    PostResponse getEventDetail(@Path("userId") String providerId, @Query("where") String subCategory, @Query("type") String subject);

    //@POST("/connectRequest/{userId}")
  //  PostResponse postRequetToProvider(@Path("userId") String providerId, @Body RequestToProviderDto request);

    @GET("/recipe/{userId}")
    PostResponse getRecipieDetail(@Path("userId") String providerId);

    @GET("/blog/{userId}")
    PostResponse getArticalDetail(@Path("userId") String providerId);

    @GET("/story/{userId}")
    PostResponse getStoryDetail(@Path("userId") String providerId);

    @GET("/userLogin")
    PostResponse getForgetPassword(@Query("email") String providerId, @Query("forgotPassword") String forgetPass);
    @GET("/bangalore")
    PostResponse getEventHigh(@Query("key") String key);

    @GET("/connectRequest/{userId}")
    PostResponse getContactedProvider(@Path("userId") String providerId, @Query("sender") String user);

    @GET("/story/{userId}")
    PostResponse getStoryLike(@Path("userId") String providerId, @Query("like") String like, @Query("storyId") String story_id);

    @GET("/blog/{userId}")
    PostResponse getArticalLike(@Path("userId") String providerId, @Query("like") String like, @Query("blogId") String blog_id);

    @GET("/recipe/{userId}")
    PostResponse getRecipieLike(@Path("userId") String providerId, @Query("like") String like, @Query("recipeId") String recipie_like);
    /* // @POST("/sport/{providerId}")
     PostResponse postServiceData(@Path("providerId") String providerId, @Body SportDto signupDto);

     @POST("/tuition/{providerId}")
     PostResponse postServiceDataTution(@Path("providerId") String providerId,@Body TuitionDto signupDto);

     @POST("/training/{providerId}")
     PostResponse postServiceDataTraining(@Path("providerId") String providerId,@Body TrainingDto signupDto);

     @POST("/dayCare/{providerId}")
     PostResponse postServiceDataDayCare(@Path("providerId") String providerId,@Body DayCareDto signupDto);

     @POST("/school/{providerId}")
     PostResponse postServiceDataSchool(@Path("providerId") String providerId,@Body SchoolDto signupDto);

     @POST("/health/{providerId}")
     PostResponse postServiceDataHealth(@Path("providerId") String providerId,@Body HealthDto signupDto);

     @POST("/event/{providerId}")
     PostResponse postServiceDataEvent(@Path("providerId") String providerId,@Body EventDto signupDto);

     @POST("/celebration/{providerId}")
     PostResponse postServiceDataCelebration(@Path("providerId") String providerId,@Body CelebrationDto signupDto);
*/
    /*@POST("/userLogin")
    PostResponse postLogin(@Body LoginDto signupDto);*/

    /*
         @POST("/providerProfile/{providerId}")
         PostResponse postPasswordReset(@Path("providerId") String providerId,@Body PasswordResetDto signupDto);


         @GET("/providerProfile/{providerId}")
         PostResponse getProviderInfo(@Path("providerId") String providerId);

         @GET("/connectRequest/{providerId}?sender=provider")//af6d5d84-278f-42fb-8b2f-b06b2e12b8f8
         PostResponse getUserRequestsInfo(@Path("providerId") String providerId);


     */
    @GET("/address/")
    PostResponse getPinCode(@Query("city") String city);
/*
    @GET("/connectRequest/{providerId}")
    PostResponse getSeenByProvider(@Path("providerId") String providerId, @Query("sender") String sender,@Query("connectRequestId") String contReq);
*/
}
