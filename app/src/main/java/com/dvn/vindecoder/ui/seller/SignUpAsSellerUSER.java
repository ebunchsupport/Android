package com.dvn.vindecoder.ui.seller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.SetUserofSellertDto;
import com.dvn.vindecoder.ui.BaseActivity;
import com.dvn.vindecoder.ui.SignUpAsSeller;
import com.dvn.vindecoder.util.AndroidMultiPartEntity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;
import com.dvn.vindecoder.util.UIUtility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by palash on 18-01-2017.
 */


public class SignUpAsSellerUSER extends BaseActivity implements AsyncCompleteListener {
    private EditText etUsername,etEmailname,etcontactnumber,landLine_number,etAddress,etPassword,etCPassword;
    private String etUsername1,etEmailname1,etnumber1,etcontactnumber1,landLine_number1,etAddress1,etGst1,etPassword1,etCPassword1;
    private Button signUp_btn;
    private String role ="3";
    private String seller_id,android_id,device_type;
    private AsyncGetTask asyncGetTask;
    private long totalSize;
    //name,contact,address,mobile,email,password,appid,role,seller
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_up_by_buyer);
        drawChildLayout();
        seller_id=LoginDB.getTitle(SignUpAsSellerUSER.this,"value");
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_type="android";
        etUsername=(EditText)findViewById(R.id.etUsername);
        etEmailname=(EditText)findViewById(R.id.etEmailname);
        etcontactnumber=(EditText)findViewById(R.id.etcontactnumber);
        landLine_number=(EditText)findViewById(R.id.etland_line);
        etAddress=(EditText)findViewById(R.id.etAddress);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etCPassword=(EditText)findViewById(R.id.etCPassword);
        signUp_btn=(Button)findViewById(R.id.signUp_btn);

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckEdit()==false)
                {
                    return;
                }
                else {
                    etUsername1=etUsername.getText().toString();
                    etEmailname1=etEmailname.getText().toString();
                    etcontactnumber1=etcontactnumber.getText().toString();
                    if(landLine_number.getText().toString().equals("") || landLine_number.getText().toString().trim().length()<1)
                    {
                        landLine_number1="";
                    }
                     else {
                        landLine_number1=landLine_number.getText().toString();
                    }
                    etAddress1=etAddress.getText().toString();
                    //etGst1=etGst.getText().toString();
                    etPassword1=etPassword.getText().toString();
                    saveDataOnServer();
                }
            }
        });
    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_sign_up_by_buyer),
                layoutParams);
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=new Intent(this,SellerDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    @Override
    public void sendFbData() {

    }

    private boolean chckEdit()
    {
        if(etUsername.getText().length()<1 || etUsername.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please insert name",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!UIUtility.isEmailValid(etEmailname.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Please insert valid email id",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(etcontactnumber.getText().toString().trim().length()<10 || etcontactnumber.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please insert valid contact number",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(etAddress.getText().toString().trim().length()<4 || etAddress.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please insert valid Address",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(etPassword.getText().toString().trim().length()<4 || etPassword.getText().toString().trim().equals("") || !etPassword.getText().toString().equals(etCPassword.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Please insert valid Password or your password and confirm password is not same",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void saveDataOnServer()
    {



       new SignUpToServer().execute();
      /*  SetUserofSellertDto setUserofSellertDto = new SetUserofSellertDto();
        setUserofSellertDto.setEmail(etEmailname1);
        setUserofSellertDto.setAppid(CommonURL.APP_ID);
        setUserofSellertDto.setDeviceid(android_id);
        setUserofSellertDto.setDevicetype(device_type);
        setUserofSellertDto.setAddress(etAddress1);
        setUserofSellertDto.setContact(landLine_number1);
        setUserofSellertDto.setMobile(etcontactnumber1);
        setUserofSellertDto.setName(etUsername1);
        setUserofSellertDto.setRole(role);
        setUserofSellertDto.setSeller(seller_id);
        setUserofSellertDto.setPassword(etPassword1);
        asyncGetTask = new AsyncGetTask(SignUpAsSellerUSER.this, CallType.POST_USER_SAVE, SignUpAsSellerUSER.this, true, setUserofSellertDto);
        asyncGetTask.execute();*/
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.POST_USER_SAVE) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());
            if(aMasterDataDtos.getResponseCode()==1)
            {

                displayPrompt(SignUpAsSellerUSER.this);


            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"Error In Login", Toast.LENGTH_SHORT).show();
        }
    }


    public static void displayPrompt(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
     //   final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "User sucessfully inserted";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                               // activity.startActivity(new Intent(action));
                                d.dismiss();
                                activity.finish();
                                activity.overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                            }
                        });

        builder.create().show();
    }



    private class SignUpToServer extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(SignUpAsSellerUSER.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //  progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressDialog.setProgress(progress[0]);

            // updating percentage value
            // txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost;
            //  httppost = new HttpPost(CommonURL.URL+"/api/update_firststepdata");

            httppost = new HttpPost(CommonURL.URL + "/api/signup");


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("appid", new StringBody(CommonURL.APP_ID));
                // Adding file data to http body
                entity.addPart("email", new StringBody(etEmailname1));
                entity.addPart("deviceId", new StringBody(android_id));
                entity.addPart("deviceType", new StringBody(device_type));
                entity.addPart("address", new StringBody(etAddress1));
                entity.addPart("contact", new StringBody(landLine_number1));
                entity.addPart("mobile", new StringBody(etcontactnumber1));
                entity.addPart("name", new StringBody(etUsername1));
                entity.addPart("role", new StringBody(role));
                entity.addPart("seller", new StringBody(seller_id));
                entity.addPart("password", new StringBody(etPassword1));


                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
                        (int) entity.getContentLength());
                entity.writeTo(out);

                String entityContentAsString = new String(out.toByteArray());
                Log.e("Entity:", "" + entity);
                Log.e("multipartEntitty:", "" + entityContentAsString);


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response", "Response from server: " + result);
            // progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
            // showing the server response in an alert dialog
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("response_code")) {
                    if (jsonObject.optString("response_code").equals("1")) {
                        Toast.makeText(SignUpAsSellerUSER.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();

                        displayPrompt(SignUpAsSellerUSER.this);

                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(SignUpAsSellerUSER.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(SignUpAsSellerUSER.this, result, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }


}