package com.dvn.vindecoder.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.SetUserofSellertDto;
import com.dvn.vindecoder.ui.seller.SellerDetail;
import com.dvn.vindecoder.ui.user.UserDetail;
import com.dvn.vindecoder.util.AndroidMultiPartEntity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class SignUpAsSeller extends BaseActivity implements AsyncCompleteListener {
    private TextView page_title;
    Button signUp_btn;
    EditText etUsername,etUserLastname,etEmailname,etcontactnumber,et_seller_number,et_tax_number,etland_line,etAddress,etPassword,etCPassword;
    private AsyncGetTask asyncGetTask;
    String seller_id,android_id,device_type;
    private long totalSize;
    String sellername,seller_last_name,email,contactnumber,seller_number,tax_number,land_line_number,Address,Password,Con_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawChildLayout();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        seller_id= LoginDB.getTitle(SignUpAsSeller.this,"value");
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_type="android";

        signUp_btn=(Button)findViewById(R.id.signUp_btn);
        page_title=(TextView)findViewById(R.id.page_title);
        page_title.setText("SignUP");
        etUsername=(EditText)findViewById(R.id.etUsername);
        etUserLastname=(EditText)findViewById(R.id.etUserLastname);
        etEmailname=(EditText)findViewById(R.id.etEmailname);
        etcontactnumber=(EditText)findViewById(R.id.etcontactnumber);
        et_seller_number=(EditText)findViewById(R.id.et_seller_number);
        et_tax_number=(EditText)findViewById(R.id.et_tax_number);
        etland_line=(EditText)findViewById(R.id.etland_line);
        etAddress=(EditText)findViewById(R.id.etAddress);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etCPassword=(EditText)findViewById(R.id.etCPassword);


        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               sellername = etUsername.getText().toString().trim();
                seller_last_name = etUserLastname.getText().toString().trim();
                 email=  etEmailname.getText().toString().trim();
                 contactnumber = etcontactnumber.getText().toString().trim();
                 seller_number = et_seller_number.getText().toString().trim();
                tax_number = et_tax_number.getText().toString().trim();
                 land_line_number = etland_line.getText().toString().trim();
               Address = etAddress.getText().toString().trim();
               Password = etPassword.getText().toString().trim();
               Con_Password = etCPassword.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (sellername.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter Seller Name", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                } else if (seller_last_name.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter Seller Last Name", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                } else if (email.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter Email", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }else if(!email.matches(emailPattern)){
                    Snackbar snack = Snackbar.make(v, "Please Enter Valid Email", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();

                } else if (contactnumber.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter mobile number", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }
                else if (seller_number.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter Seller Number", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }
                else if (tax_number.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter Tax Number/Id", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }
                else if (land_line_number.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter Land Line Number", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }
                else if (Address.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter Address", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }
                else if (Password.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter Password", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }
                else if (Con_Password.length() <= 0) {
                    Snackbar snack = Snackbar.make(v, "Please Enter Confirm Password", Snackbar.LENGTH_LONG);
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }
                else {


                      new SaveDataToServer().execute();

                   // saveDataOnServer(sellername,seller_last_name,email,contactnumber,seller_number,tax_number,land_line_number,Address,Con_Password);


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
    public void sendFbData() {

    }


    private void saveDataOnServer(String etUsername, String seller_last_name,String email,String contactnumber,String seller_number,String tax_number,String land_line_number,String Address,String Con_Password)
    {
        SetUserofSellertDto setUserofSellertDto = new SetUserofSellertDto();
        setUserofSellertDto.setEmail(email);
        setUserofSellertDto.setAppid(CommonURL.APP_ID);
        setUserofSellertDto.setDeviceid(android_id);
        setUserofSellertDto.setDevicetype(device_type);
        setUserofSellertDto.setAddress(Address);
        setUserofSellertDto.setContact(land_line_number);
        setUserofSellertDto.setMobile(contactnumber);
        setUserofSellertDto.setName(etUsername);
        setUserofSellertDto.setRole("1");
        setUserofSellertDto.setSeller(seller_id);
        setUserofSellertDto.setPassword(Con_Password);
        asyncGetTask = new AsyncGetTask(SignUpAsSeller.this, CallType.POST_USER_SAVE, SignUpAsSeller.this, true, setUserofSellertDto);
        asyncGetTask.execute();
    }


    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.POST_USER_SAVE) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());
            if(aMasterDataDtos.getResponseCode()==1)
            {

                displayPrompt(SignUpAsSeller.this);


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

        final String message = "Successfully Registered,Now You May Login";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {

                                d.dismiss();
                                activity.finish();
                                Intent intent=new Intent(activity,LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(intent);

                            }
                        });

        builder.create().show();
    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
     finish();

    }



    private class SaveDataToServer extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(SignUpAsSeller.this);
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
                entity.addPart("email", new StringBody(email));
                entity.addPart("deviceId", new StringBody(android_id));
                entity.addPart("deviceType", new StringBody(device_type));
                entity.addPart("address", new StringBody(Address));
                entity.addPart("contact", new StringBody(land_line_number));
                entity.addPart("mobile", new StringBody(contactnumber));
                entity.addPart("name", new StringBody(sellername));
                entity.addPart("role", new StringBody("1"));
                entity.addPart("seller", new StringBody(seller_id));
                entity.addPart("password", new StringBody(Con_Password));
                entity.addPart("number", new StringBody(seller_number));
                entity.addPart("tax", new StringBody(tax_number));
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
                        Toast.makeText(SignUpAsSeller.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();

                        displayPrompt(SignUpAsSeller.this);

                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(SignUpAsSeller.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(SignUpAsSeller.this, result, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }



}
