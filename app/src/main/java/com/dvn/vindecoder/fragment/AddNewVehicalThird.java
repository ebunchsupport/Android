package com.dvn.vindecoder.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.interface_package.DataComplete;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment3;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment4;
import com.dvn.vindecoder.util.AndroidMultiPartEntity;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.ImageConversionAsynchronus;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Palash on 1/27/2017.
 */

@SuppressLint("ParcelCreator")
public class AddNewVehicalThird extends Fragment implements DataComplete {
    View mainView;
    Button next_btn;
    public String v_id;
    private long totalSize;
    private RadioButton lb_kg,kg;
    private RadioButton tire_missing_yes,tire_missing_no;
    private LinearLayout linear_kg;
    private String lb_or_kg="LB and KG";
    private ProgressDialog progressDialog;
    private EditText edit_txt_gvwr,edit_txt_gvrw_lbs,edit_txt_front_gawr,edit_txt_rear_gawrr,edit_txt_model,edit_txt_f_tire_rim_size,
            edit_txt_f_pressure,edit_txt_r_tire,edit_txt_r_tire_rim_size,edit_txt_r_pressure;
    private ImageView imageViewphoto_of_tire_label;
    private int check_img_status;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE=3;
    private static final int REQUEST_ACESS_CAMERA=2;
    private Uri uri;
    private LinearLayout is_tire_missing_linear;
    private String edit_txt_gvwr1,edit_txt_gvrw_lbs1,edit_txt_front_gawr1,edit_txt_rear_gawrr1,edit_txt_model1,edit_txt_f_tire_rim_size1,
            edit_txt_f_pressure1,edit_txt_r_tire1,edit_txt_r_tire_rim_size1,edit_txt_r_pressure1;
    private String is_tire_missing="Yes";
    private String tirelableimg;
    private boolean checckBackPress=false;
    private ImageConversionAsynchronus imageConversionAsynchronus=null;
    private static final String V_ID = "v_id";
    /* public AddNewVehicalFragmentWithouScan(DataComplete dataComplete)
     {
     this.dataComplete=dataComplete;
     }*/
    public static AddNewVehicalThird newInstance(String vid1) {
        AddNewVehicalThird f = new AddNewVehicalThird();

        // Supply num input as an argument.
        Bundle bundle = new Bundle();
        bundle.putString(V_ID, vid1);
        f.setArguments(bundle);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v_id=(String) getArguments().getString(
                V_ID);

        //Inflate the layout for this fragment

        mainView = (View) inflater.inflate(R.layout.add_vehical_third_part, container, false);
        lb_kg=(RadioButton)mainView.findViewById(R.id.radio_btn_lb_kg);
        kg=(RadioButton)mainView.findViewById(R.id.radio_btn_kg);
        tire_missing_yes=(RadioButton)mainView.findViewById(R.id.radio_is_tire_yes);
        tire_missing_no=(RadioButton)mainView.findViewById(R.id.radio_is_tire_no);
        next_btn=(Button)mainView.findViewById(R.id.third_button_ok);
        linear_kg=(LinearLayout)mainView.findViewById(R.id.linear_kg);
        imageViewphoto_of_tire_label=(ImageView)mainView. findViewById(R.id.imageViewphoto_of_tire_label);
        is_tire_missing_linear=(LinearLayout)mainView.findViewById(R.id.is_tire_missing_linear);
        linear_kg.setVisibility(View.GONE);
        setEditText(mainView);
        setListner();
        return mainView;
    }

    private void setEditText(View mainView)
    {
        edit_txt_gvwr=(EditText)mainView.findViewById(R.id.edit_txt_gvwr);
        edit_txt_gvrw_lbs=(EditText)mainView.findViewById(R.id.edit_txt_gvrw_lbs);
        edit_txt_front_gawr=(EditText)mainView.findViewById(R.id.edit_txt_front_gawr);
        edit_txt_rear_gawrr=(EditText)mainView.findViewById(R.id.edit_txt_rear_gawrr);
        edit_txt_model=(EditText)mainView.findViewById(R.id.edit_txt_model);
        edit_txt_f_tire_rim_size=(EditText)mainView.findViewById(R.id.edit_txt_f_tire_rim_size);
        edit_txt_f_pressure=(EditText)mainView.findViewById(R.id.edit_txt_f_pressure);
        edit_txt_r_tire=(EditText)mainView.findViewById(R.id.edit_txt_r_tire);
        edit_txt_r_tire_rim_size=(EditText)mainView.findViewById(R.id.edit_txt_r_tire_rim_size);
        edit_txt_r_pressure=(EditText)mainView.findViewById(R.id.edit_txt_r_pressure);

    }
    public void setListner()
    {
        lb_kg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb_or_kg="LB and KG";
                linear_kg.setVisibility(View.GONE);
            }
        });
        kg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb_or_kg="KG Only";
                linear_kg.setVisibility(View.VISIBLE);
            }
        });
        tire_missing_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_tire_missing="Yes";
                is_tire_missing_linear.setVisibility(View.VISIBLE);
            }
        });
        tire_missing_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_tire_missing="No";
                is_tire_missing_linear.setVisibility(View.GONE);
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToServer();

            }
        });

        imageViewphoto_of_tire_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status=1;
                handleCameraForPickingPhoto();
            }
        });

    }

    //camera option
    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(DialogInterface.OnClickListener activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((AddVehicalAndPayment3) activity, permission[0])) {
            Toast.makeText((AddVehicalAndPayment3)activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions((AddVehicalAndPayment3)activity, permission, requestCode);
    }

    public static void requestPermission(Fragment fragment, String[] permission, int requestCode) {
        fragment.requestPermissions(permission, requestCode);
    }

    private void handleCameraForPickingPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getActivity())) {
                startDilog();
            } else {
                requestPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ACESS_STORAGE);
            }
        } else {
            startDilog();
        }
    }

    private void startDilog() {
        AlertDialog.Builder myAlertDilog = new AlertDialog.Builder(getActivity());
        myAlertDilog.setTitle("Upload picture option..");
        myAlertDilog.setMessage("Take Photo");
        myAlertDilog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
                picIntent.setType("image/*");
                picIntent.putExtra("return_data",true);
                startActivityForResult(picIntent,GALLERY_REQUEST);
            }
        });
        myAlertDilog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkPermission(Manifest.permission.CAMERA,getActivity())){
                        openCameraApplication();
                    }else{
                        requestPermission(this,new String[]{Manifest.permission.CAMERA},REQUEST_ACESS_CAMERA);
                    }
                }else{
                    openCameraApplication();
                }
            }
        });
        myAlertDilog.show();
    }

    private void openCameraApplication() {
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(getActivity().getPackageManager())!= null){
            startActivityForResult(picIntent, CAMERA_REQUEST);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    try {
                        // BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                        options.inSampleSize =calculateInSampleSize(options, 100, 100);
                        options.inJustDecodeBounds = false;
                        Bitmap image = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
                        if(check_img_status==1)
                        {
                            imageViewphoto_of_tire_label.setImageBitmap(image);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    //uri = getImageUri(getActivity(), bitmap);
                    // File finalFile = new File(getRealPathFromUri(uri));
                    //imageViewphoto_of_tire_label.setImageBitmap(bitmap);
                    if(check_img_status==1)
                    {
                        imageViewphoto_of_tire_label.setImageBitmap(bitmap);
                    }

                } else if (data.getExtras() == null) {

                    Toast.makeText(getActivity(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());
                    //imageViewphoto_of_tire_label.setImageDrawable(thumbnail);
                    if(check_img_status==1)
                    {
                        imageViewphoto_of_tire_label.setImageDrawable(thumbnail);
                    }

                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String setImagePath(ImageView imageView,String doc_name)
    {
        String image_path;
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),(doc_name+".jpg"));
        FileOutputStream fo;
        try {
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image_path=destination.getAbsolutePath();
        return image_path;
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_ACESS_STORAGE && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            startDilog();
        }
        if(requestCode==REQUEST_ACESS_CAMERA && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            openCameraApplication();
        }
    }

    private void sendToServer()
    {
        if(edit_txt_gvwr.getText().toString().isEmpty())
        {
            edit_txt_gvwr1="";

        }else {
            edit_txt_gvwr1=edit_txt_gvwr.getText().toString();
        }
        if(edit_txt_gvrw_lbs.getText().toString().isEmpty())
        {
            edit_txt_gvrw_lbs1="";

        }else {
            edit_txt_gvrw_lbs1=edit_txt_gvrw_lbs.getText().toString();
        }
        if(edit_txt_front_gawr.getText().toString().isEmpty())
        {
            edit_txt_front_gawr1="";

        }else {
            edit_txt_front_gawr1=edit_txt_front_gawr.getText().toString();
        }
        if(edit_txt_rear_gawrr.getText().toString().isEmpty())
        {
            edit_txt_rear_gawrr1="";

        }else {
            edit_txt_rear_gawrr1=edit_txt_rear_gawrr.getText().toString();
        }
        if(edit_txt_model.getText().toString().isEmpty())
        {
            edit_txt_model1="";

        }else {
            edit_txt_model1=edit_txt_model.getText().toString();
        }
        if(edit_txt_f_tire_rim_size.getText().toString().isEmpty())
        {
            edit_txt_f_tire_rim_size1="";

        }else {
            edit_txt_f_tire_rim_size1=edit_txt_f_tire_rim_size.getText().toString();
        }
        if(edit_txt_f_pressure.getText().toString().isEmpty())
        {
            edit_txt_f_pressure1="";

        }else {
            edit_txt_f_pressure1=edit_txt_f_pressure.getText().toString();
        }
        if(edit_txt_r_tire.getText().toString().isEmpty())
        {
            edit_txt_r_tire1="";

        }else {
            edit_txt_r_tire1=edit_txt_r_tire.getText().toString();
        }
        if(edit_txt_r_tire_rim_size.getText().toString().isEmpty())
        {
            edit_txt_r_tire_rim_size1="";

        }else {
            edit_txt_r_tire_rim_size1=edit_txt_r_tire_rim_size.getText().toString();
        }
        if(edit_txt_r_pressure.getText().toString().isEmpty())
        {
            edit_txt_r_pressure1="";

        }else {
            edit_txt_r_pressure1=edit_txt_r_pressure.getText().toString();
        }
      //  tirelableimg=setImagePath(,"Tire_Label");
        //is_tire_missing
        //lb_or_kg
        imageConversionAsynchronus=new ImageConversionAsynchronus(getActivity(),this,1,imageViewphoto_of_tire_label,"Tire_Label");
        imageConversionAsynchronus.execute();

    }
    @Override
    public void onDataComplete(int i, String image_path) {
        if(i==1)
        {
        Log.e("Image_path",image_path);
            tirelableimg=image_path;
            new UploadFileToServer().execute();
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible

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
            if(checckBackPress){
                httppost = new HttpPost(CommonURL.URL+"/api/update_thirdstepdata");
            }
            else {
                httppost =  new HttpPost(CommonURL.URL+"/api/add_carthirdstep");
            }

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(tirelableimg);

                // Adding file data to http body
                entity.addPart("tirelableimg", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("vid",
                        new StringBody(v_id));
                entity.addPart("manufactureweight", new StringBody(lb_or_kg));

                entity.addPart("gvwr7200",
                        new StringBody(edit_txt_gvwr1));

                entity.addPart("gvwrlbs",
                        new StringBody(edit_txt_gvrw_lbs1));


                entity.addPart("gawr3900",
                        new StringBody(edit_txt_front_gawr1));

                entity.addPart("rear3850lb",
                        new StringBody(edit_txt_rear_gawrr1));


                entity.addPart("tirelablemissing",
                        new StringBody(is_tire_missing));


                entity.addPart("fronttiresize",
                        new StringBody(edit_txt_model1));


                entity.addPart("sizeno",
                        new StringBody(edit_txt_f_tire_rim_size1));







                entity.addPart("fronttirepressure",
                        new StringBody(edit_txt_f_pressure1));

                entity.addPart("reartiresize",
                        new StringBody(edit_txt_r_tire1));


                entity.addPart("rearrimsize",
                        new StringBody(edit_txt_r_tire_rim_size1));


                entity.addPart("reartirepressure",
                        new StringBody(edit_txt_r_pressure1));


                entity.addPart("appid", new StringBody(CommonURL.APP_ID));

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
            progressDialog.dismiss();
            // showing the server response in an alert dialog
            try {
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.has("response_code"))
                {
                    if(jsonObject.optString("response_code").equals("1"))
                    {
                        checckBackPress=true;
                        Toast.makeText(getActivity(),"vehicle Information are successfully saved",Toast.LENGTH_LONG).show();
                        String vid=jsonObject.optString("vid");
                        Intent intent=new Intent(getActivity(), AddVehicalAndPayment4.class);
                        intent.putExtra("vid",vid);
                        startActivity(intent);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }
}