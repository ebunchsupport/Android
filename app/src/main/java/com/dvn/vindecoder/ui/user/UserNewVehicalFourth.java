package com.dvn.vindecoder.ui.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.CarDto1;
import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.interface_package.DataComplete;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment4;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment5;
import com.dvn.vindecoder.util.AndroidMultiPartEntity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.ImageConversionAsynchronus;
import com.dvn.vindecoder.util.PostResponse;
import com.dvn.vindecoder.util.UIUtility;

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
import java.util.List;

/**
 * Created by Palash on 1/27/2017.
 */

@SuppressLint("ParcelCreator")
public class UserNewVehicalFourth extends Fragment implements DataComplete,AsyncCompleteListener{
    private View mainView;
            Button next_btn;
    String vid;
    private List<CarDto1> cardata;
    private int check_img_status=0;//1 for driving licence , 2 for Insurance Plate , 3 for Passprot
    public final String APP_TAG = "MyCustomApp";
    private long totalSize;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private ProgressDialog progressDialog;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE=3;
    private static final int REQUEST_ACESS_CAMERA=2;
    private Uri uri;
    private ImageView imageViewphoto_of_bill_sale,imageViewphoto_of_registration,imageViewphoto_of_recall,imageViewphoto_of_manufacture_label;
   private String imageViewphoto_of_bill_sale1,imageViewphoto_of_registration1,imageViewphoto_of_recall1,imageViewphoto_of_manufacture_label1;
   private RadioButton radio_btn_is_epa_yes,radio_btn_is_epa_no,radio_vin_plate_yes,radio_vin_plate_no,
           radio_emission_yes,radio_emission_no;
    private LinearLayout linear1,linear2;
    private String radio_btn_is_epa="Yes";
    private String radio_vin_plate="Yes";
   private String radio_emission="Yes";
    private ImageConversionAsynchronus imageConversionAsynchronus=null;
    private DataComplete dataComplete;
    private static final String DESCRIBABLE_KEY = "describable_key";

    public static UserNewVehicalFourth newInstance(DataComplete dataComplete) {
        UserNewVehicalFourth f = new UserNewVehicalFourth();

        // Supply num input as an argument.
        Bundle bundle = new Bundle();
        bundle.putParcelable(DESCRIBABLE_KEY, dataComplete);
        f.setArguments(bundle);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dataComplete = (DataComplete) getArguments().getParcelable(
                DESCRIBABLE_KEY);
        //Inflate the layout for this fragment
        mainView = (View) inflater.inflate(R.layout.add_vehical_fourth_part, container, false);
        next_btn=(Button)mainView.findViewById(R.id.fourth_button_ok);
        imageViewphoto_of_bill_sale=(ImageView)mainView. findViewById(R.id.imageViewphoto_of_frnt_driver);
        imageViewphoto_of_registration=(ImageView)mainView.findViewById(R.id.imageViewphoto_vin_plate);
        imageViewphoto_of_recall=(ImageView)mainView.findViewById(R.id.imageViewphoto_of_rear_passenger_side);
        imageViewphoto_of_manufacture_label=(ImageView)mainView.findViewById(R.id.imageViewphoto_passenger_seat_belt);
        radio_btn_is_epa_yes=(RadioButton)mainView.findViewById(R.id.radio_btn_is_epa_yes);
        radio_btn_is_epa_no=(RadioButton)mainView.findViewById(R.id.radio_btn_is_epa_no);
        radio_vin_plate_yes=(RadioButton)mainView.findViewById(R.id.radio_vin_plate_yes);
        radio_vin_plate_no=(RadioButton)mainView.findViewById(R.id.radio_vin_plate_no);
        radio_emission_yes=(RadioButton)mainView.findViewById(R.id.radio_emission_yes);
        radio_emission_no=(RadioButton)mainView.findViewById(R.id.radio_emission_no);
        linear1=(LinearLayout)mainView.findViewById(R.id.photo_linear);
        linear2=(LinearLayout)mainView.findViewById(R.id.txt_linear);
        vid=CommonURL.V_ID;
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToServer();
            }
        });
        setClicklitener();
        getUserData();
        return mainView;
    }

    private void setClicklitener()
    {
        imageViewphoto_of_bill_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status=1;
                handleCameraForPickingPhoto();
            }
        });
        imageViewphoto_of_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status=2;
                handleCameraForPickingPhoto();
            }
        });
        imageViewphoto_of_recall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status=3;
                handleCameraForPickingPhoto();
            }
        });
        imageViewphoto_of_manufacture_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status=4;
                handleCameraForPickingPhoto();
            }
        });

        radio_btn_is_epa_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear2.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.GONE);
                radio_btn_is_epa="Yes";
            }
        });
        radio_btn_is_epa_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_is_epa="No";
                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);
            }
        });
        radio_vin_plate_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_vin_plate="Yes";
            }
        });
        radio_vin_plate_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_vin_plate ="No";
            }
        });
        radio_emission_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_emission="Yes";
            }
        });
        radio_emission_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_emission ="No";
            }
        });


    }
    //camera option
    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(DialogInterface.OnClickListener activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((AddVehicalAndPayment4) activity, permission[0])) {
            Toast.makeText((AddVehicalAndPayment4)activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions((AddVehicalAndPayment4)activity, permission, requestCode);
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
                            imageViewphoto_of_bill_sale.setImageBitmap(image);
                        }
                        else if(check_img_status==2)
                        {
                            imageViewphoto_of_registration.setImageBitmap(image);
                        }
                        else if(check_img_status==3)
                        {
                            imageViewphoto_of_recall.setImageBitmap(image);
                        }
                        else if(check_img_status==4)
                        {
                            imageViewphoto_of_manufacture_label.setImageBitmap(image);
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
                    //File finalFile = new File(getRealPathFromUri(uri));
                    //imageViewphoto_of_bill_sale.setImageBitmap(bitmap);
                    if(check_img_status==1)
                    {
                        imageViewphoto_of_bill_sale.setImageBitmap(bitmap);
                    }
                    else if(check_img_status==2)
                    {
                        imageViewphoto_of_registration.setImageBitmap(bitmap);
                    }
                    else if(check_img_status==3)
                    {
                        imageViewphoto_of_recall.setImageBitmap(bitmap);
                    }
                    else if(check_img_status==4)
                    {
                        imageViewphoto_of_manufacture_label.setImageBitmap(bitmap);
                    }
                } else if (data.getExtras() == null) {

                    Toast.makeText(getActivity(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());
                    //imageViewphoto_of_bill_sale.setImageDrawable(thumbnail);
                    if(check_img_status==1)
                    {
                        imageViewphoto_of_bill_sale.setImageDrawable(thumbnail);
                    }
                    else if(check_img_status==2)
                    {
                        imageViewphoto_of_registration.setImageDrawable(thumbnail);
                    }
                    else if(check_img_status==3)
                    {
                        imageViewphoto_of_recall.setImageDrawable(thumbnail);
                    }
                    else if(check_img_status==4)
                    {
                        imageViewphoto_of_manufacture_label.setImageDrawable(thumbnail);
                    }
                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(tempUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private Uri getImageUri(AddVehicalAndPayment4 yourActivity, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(yourActivity.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
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


    private void sendToServer()
    {

        imageConversionAsynchronus = new ImageConversionAsynchronus(getActivity(), this, 1, imageViewphoto_of_registration, "front_side");
        imageConversionAsynchronus.execute();

    }
    @Override
    public void onDataComplete(int i, String image_path) {
        Log.e("Image Path", image_path);
        if (i == 1) {
            imageViewphoto_of_bill_sale1 = image_path;
            imageConversionAsynchronus = new ImageConversionAsynchronus(getActivity(), this, 2, imageViewphoto_of_registration, "vin_pic");
            imageConversionAsynchronus.execute();
        } else if (i == 2) {
            imageViewphoto_of_registration1 = image_path;
            imageConversionAsynchronus = new ImageConversionAsynchronus(getActivity(), this, 3, imageViewphoto_of_recall, "rear_passenger");
            imageConversionAsynchronus.execute();
        } else if (i == 3) {
            imageViewphoto_of_recall1 = image_path;
            imageConversionAsynchronus = new ImageConversionAsynchronus(getActivity(), this, 4, imageViewphoto_of_manufacture_label, "passenger_seat");
            imageConversionAsynchronus.execute();
        } else if (i == 4) {
            imageViewphoto_of_manufacture_label1 = image_path;
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
            HttpPost httppost = new HttpPost(CommonURL.URL+"/api/update_fourthstepdata");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(imageViewphoto_of_bill_sale1);

                // Adding file data to http body
                entity.addPart("frontdriverimg", new FileBody(sourceFile));

                File sourceFile1 = new File(imageViewphoto_of_registration1);

                // Adding file data to http body
                entity.addPart("vinplatedoc", new FileBody(sourceFile1));

                File sourceFile2 = new File(imageViewphoto_of_recall1);

                // Adding file data to http body
                entity.addPart("rearpassangerdoc", new FileBody(sourceFile2));

                File sourceFile3 = new File(imageViewphoto_of_manufacture_label1);

                // Adding file data to http body
                entity.addPart("passengerseatbeltdoc", new FileBody(sourceFile3));

                // Extra parameters if you want to pass to server
                entity.addPart("vid",
                        new StringBody(vid));
                entity.addPart("epaarea", new StringBody(radio_btn_is_epa));

                entity.addPart("vinplatemissing",
                        new StringBody(radio_vin_plate));
                entity.addPart("stock",
                        new StringBody(radio_emission));

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
                        Toast.makeText(getActivity(),"vehicle Information are successfully saved",Toast.LENGTH_LONG).show();
                        dataComplete.onDataComplete(5,vid);
                        /*String vid=jsonObject.optString("vid");
                        Intent intent=new Intent(getActivity(), AddVehicalAndPayment5.class);
                        intent.putExtra("vid",vid);
                        startActivity(intent);*/
                        /*vid=jsonObject.optString("vid");

                        Intent intent=new Intent(getActivity(), AddVehicalAndPayment2.class);
                        intent.putExtra("vid",vid);
                        getActivity().startActivity(intent);*/
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }

    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.GET_VEHICAL_STEP_FOURTH) {
            if (aMasterDataDtos.getResponseCode() == 1) {
                cardata=aMasterDataDtos.getCardata();
                setData();
            } else {
                Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
            }


        }
    }

    private void setData()
    {
        //,,,
        Glide.with(getActivity()).load(cardata.get(0).getImagefrontdriverside())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        // progressBarManuFa.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //progressBarManuFa.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewphoto_of_bill_sale);

        Glide.with(getActivity()).load(cardata.get(0).getImageVinplate())
                .asBitmap().into(new SimpleTarget<Bitmap>(300,300) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                //setBackgroundImage(resource);
                imageViewphoto_of_registration.setImageBitmap(resource);
            }
        });


        Glide.with(getActivity()).load(cardata.get(0).getImageRearandpassengerside())
                .asBitmap().into(new SimpleTarget<Bitmap>(300,300) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                //setBackgroundImage(resource);
                imageViewphoto_of_recall.setImageBitmap(resource);
            }
        });

        Glide.with(getActivity()).load(cardata.get(0).getImagePassengerseatbelt())
                .asBitmap().into(new SimpleTarget<Bitmap>(300,300) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                //setBackgroundImage(resource);
                imageViewphoto_of_manufacture_label.setImageBitmap(resource);
            }
        });

        if (UIUtility.isStringnotEmpty(cardata.get(0).getIsEpa())) {
            if (cardata.get(0).getIsEpa().equalsIgnoreCase("Yes")) {
                radio_btn_is_epa_yes.setChecked(true);
                linear2.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.GONE);
                radio_btn_is_epa = "Yes";
            } else {
                radio_btn_is_epa_no.setChecked(true);
                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);
                radio_btn_is_epa = "No";
            }
        }
        if (UIUtility.isStringnotEmpty(cardata.get(0).getVinplate())) {
            if (cardata.get(0).getVinplate().equalsIgnoreCase("yes")) {
                radio_vin_plate_yes.setChecked(true);
            } else {
                radio_vin_plate = "No";
                radio_vin_plate_no.setChecked(true);
            }
        }
        if (UIUtility.isStringnotEmpty(cardata.get(0).getEmisssions())) {
            if (cardata.get(0).getEmisssions().equalsIgnoreCase("yes")) {
                radio_emission_yes.setChecked(true);
                radio_emission = "Yes";
            } else {
                radio_emission = "No";
                radio_emission_no.setChecked(true);
            }
        }

    }
    private void getUserData() {
        GetVehicalDto getVehicalDto = new GetVehicalDto();
        getVehicalDto.setVid(CommonURL.PK_ID);
        //getVehicalDto.setAppid(CommonURL.APP_ID);
        AsyncGetTask asyncGetTask = new AsyncGetTask(getActivity(), CallType.GET_VEHICAL_STEP_FOURTH, UserNewVehicalFourth.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
}