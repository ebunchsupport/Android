package com.dvn.vindecoder.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.dto.LoginDto;
import com.dvn.vindecoder.dto.SetUserofSellertDto;
import com.dvn.vindecoder.dto.VINAPISetter;

import retrofit.RestAdapter;


/**
 *
 *
 */
public class AsyncGetTask extends AsyncTask<String, Void, PostResponse> {

    String reference;
    private Context mContext;
    private AsyncCompleteListener autocompleteListener;
    private CallType mType;
    private ProgressDialog pd;
    private boolean mShallShowProgress;
    private Object objectTosend;
    private PostResponse postResponse;
    /**
     * @param mActivity
     * @param url
     * @param type
     * @param auListener
     */
    public AsyncGetTask(Context mActivity, CallType type,
                        AsyncCompleteListener auListener, boolean shallShowProgress,
                        Object object) {
        this.mContext = mActivity;
        this.autocompleteListener = auListener;
        this.mType = type;
        this.mShallShowProgress = shallShowProgress;
        this.objectTosend = object;
    }

    @Override
    protected void onPreExecute() {
        if (mShallShowProgress) {
            pd = new ProgressDialog(mContext);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }
        super.onPreExecute();
    }

    @Override
    protected PostResponse doInBackground(String... args) {
        // create Gson object for parser
        /*
		 * Gson gson = new GsonBuilder() .setFieldNamingPolicy(
		 * FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		 * .registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
		 */


         postResponse = null;
        RestAdapter restAdapter;
        if(CallType.GET_VIN_DETAILS_API_SERVER.equals(mType))
        {  VINAPISetter temp= (VINAPISetter)(objectTosend);
            restAdapter = new RestAdapter.Builder().setEndpoint(
                    (CommonURL.VIN_API_LINK+"/"+temp.getApikey()+"/"+temp.getSha1()+"/"+temp.getDecode()+"/"+temp.getVin_code())).build();
        }
        else {
            restAdapter = new RestAdapter.Builder().setEndpoint(
                    CommonURL.URL).build();
        }

        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        Retrofit fitManager = restAdapter.create(Retrofit.class);
        try {
            if (CallType.POST_LOGIN.equals(mType)) {
               // Log.e("ss", "::ss");
                postResponse = fitManager.postLogin((LoginDto) objectTosend);
            }
            else if (CallType.GET_VEHICAL_LIST.equals(mType)) {
               // Log.e("ss", "::ss");
                postResponse = fitManager.postVehicalList((GetVehicalDto) objectTosend);
            }
            else if (CallType.GET_USER_VEHICAL_LIST.equals(mType)) {
                //Log.e("ss", "::ss");
                postResponse = fitManager.postUserVehicalList((GetVehicalDto) objectTosend);
            }
            else if (CallType.POST_USER_SAVE.equals(mType)) {
                //Log.e("ss", "::ss");
                postResponse = fitManager.postSaveUser((SetUserofSellertDto) objectTosend);
            }

            else if (CallType.GET_SELLER_USER_LIST.equals(mType)) {
                //Log.e("ss", "::ss");
                postResponse = fitManager.postUserSellerList((GetVehicalDto) objectTosend);
            }
            else if (CallType.GET_VIN_DETAILS.equals(mType)) {
                postResponse = fitManager.postVinInfo((GetVehicalDto) objectTosend);
            }
            else if (CallType.GET_BUYER_LIST.equals(mType)) {
                postResponse = fitManager.postGetBuyerList();
            }
            else if (CallType.GET_VIN_DETAILS_API_SERVER.equals(mType)) {
                postResponse = fitManager.postVINList();
            }
            else if (CallType.GET_USER_VEHICAL_ASSIGN_LIST.equals(mType)) {
                postResponse = fitManager.postUserVehicalAssignedList((GetVehicalDto) objectTosend);
            }
            else if (CallType.UNASSIGNED_CAR_TO_USER.equals(mType)) {
                postResponse = fitManager.postUnAssignedVehicalList((GetVehicalDto) objectTosend);
            }
            else if (CallType.ASSIGNED_CAR_TO_USER.equals(mType)) {
                postResponse = fitManager.postAssignedVehicalToUser((GetVehicalDto) objectTosend);
            }
            else if (CallType.GET_VEHICAL_STEP_FIRST.equals(mType)) {
                postResponse = fitManager.postUserDetailStepFirst((GetVehicalDto) objectTosend);
            }
            else if (CallType.GET_VEHICAL_STEP_SECOND.equals(mType)) {
                postResponse = fitManager.postUserDetailStepSecond((GetVehicalDto) objectTosend);
            }
            else if (CallType.GET_VEHICAL_STEP_THIRD.equals(mType)) {
                postResponse = fitManager.postUserDetailStepThird((GetVehicalDto) objectTosend);
            }
            else if (CallType.GET_VEHICAL_STEP_FOURTH.equals(mType)) {
                postResponse = fitManager.postUserDetailStepFourth((GetVehicalDto) objectTosend);
            }
            else if (CallType.GET_VEHICAL_STEP_FIFTH.equals(mType)) {
                postResponse = fitManager.postUserDetailStepFifth((GetVehicalDto) objectTosend);
            }
            else if (CallType.GET_STOP_CAR.equals(mType)) {
                postResponse = fitManager.postStopCar((GetVehicalDto) objectTosend);
            }
            return postResponse;

        } catch (Exception e) {

                return null;

        }

    }

    @Override
    protected void onPostExecute(PostResponse result) {

        if (pd != null) {
            pd.dismiss();
        }
        if (result != null) {
            Log.e("response",result.toString());
            autocompleteListener.onAsyncCompleteListener(result, mType);
        }
        if (result == null) {
            Toast.makeText(mContext, "Problem in network please try again", Toast.LENGTH_LONG).show();
        }

    }
}