package com.dvn.vindecoder.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by palash on 26-07-2016.
 */
public class UploadImage {

    Activity activity;
    String bimapString;
    public String UPLOAD_URL="http://128.199.196.34:8080/smartgrowth/r1/providerImageUpload/";
    public UploadImage(Activity activity)
    {
    this.activity=activity;
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void uploadImage(String img_bitmap, String prov_id){
        //Showing the progress dialog
        bimapString=img_bitmap;
        final ProgressDialog loading = ProgressDialog.show(activity,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, (UPLOAD_URL+prov_id),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(activity, s , Toast.LENGTH_LONG).show();
                        Log.e("ss1",s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.e("ss2",volleyError.getMessage().toString());
                        //Showing toast
                        Toast.makeText(activity, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = bimapString;

                //Getting Image Name

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("name", image);
                params.put("filename", "DSC.JPG");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
