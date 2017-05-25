package com.dvn.vindecoder.fragment;

/**
 * Created by Palash on 1/27/2017.
 */
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
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
        import android.os.Build;
        import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.interface_package.DataComplete;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment5;
import com.dvn.vindecoder.ui.seller.SellerDetail;
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
@SuppressLint("ParcelCreator")
public class AddNewVehicalFifth extends Fragment implements DataComplete{
    private View mainView;
    Button next_btn;
    ImageConversionAsynchronus imageConversionAsynchronus;
    public String v_id;
    private int check_img_status=0;//1 for driving licence , 2 for Insurance Plate , 3 for Passprot
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private ProgressDialog progressDialog;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE=3;
    private static final int REQUEST_ACESS_CAMERA=2;
    private Uri uri;
    private long totalSize;
    private ImageView imageViewphoto_of_bill_sale,imageViewphoto_of_registration,imageViewphoto_of_recall,imageViewphoto_of_manufacture_label;
    private RadioButton radio_btn_km,radio_btn_miles,radio_btn_yes_km,radio_btn_no_already_miles,radio_btn_no_need,
            radio_required_yes,radio_required_no;
    private String radio_btn_km_miles="KM";
    private String radio_btn_km_no="YES KM TO MILES";
    private String radio_btn_need_required="Yes";
    private TextView txt_view;
    private String imageViewphoto_of_bill_sale1,imageViewphoto_of_registration1,imageViewphoto_of_recall1,imageViewphoto_of_manufacture_label1;
   private EditText vehicle_km_miles_edit_txt;
    private String vehicle_km;

    private static final String V_ID = "v_id";
    /* public AddNewVehicalFragmentWithouScan(DataComplete dataComplete)
     {
     this.dataComplete=dataComplete;
     }*/
    public static AddNewVehicalFifth newInstance(String vid1) {
        AddNewVehicalFifth f = new AddNewVehicalFifth();

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
        mainView = (View) inflater.inflate(R.layout.add_vehical_fifth_part, container, false);
        imageViewphoto_of_bill_sale=(ImageView)mainView. findViewById(R.id.imageViewphoto_passenger_air_bag);
        imageViewphoto_of_registration=(ImageView)mainView.findViewById(R.id.imageViewphoto_of_driver_seat_belt);
        imageViewphoto_of_recall=(ImageView)mainView.findViewById(R.id.imageViewphoto_of_driver_air_bag);
        imageViewphoto_of_manufacture_label=(ImageView)mainView.findViewById(R.id.imageViewphoto_of_speedo);
        vehicle_km_miles_edit_txt=(EditText)mainView.findViewById(R.id.edit_txt_km_on_vehical);
        radio_btn_km=(RadioButton)mainView.findViewById(R.id.radio_btn_km);
        radio_btn_miles=(RadioButton)mainView.findViewById(R.id.radio_btn_miles);
        radio_btn_yes_km=(RadioButton)mainView.findViewById(R.id.radio_btn_yes_km);
        radio_btn_no_already_miles=(RadioButton)mainView.findViewById(R.id.radio_btn_no_already_miles);
        radio_btn_no_need=(RadioButton)mainView.findViewById(R.id.radio_btn_no_need);
        radio_required_yes=(RadioButton)mainView.findViewById(R.id.radio_required_yes);
        radio_required_no=(RadioButton)mainView.findViewById(R.id.radio_required_no);
        txt_view=(TextView)mainView.findViewById(R.id.txt_view_model);
        next_btn=(Button)mainView.findViewById(R.id.fifth_button_ok);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToServer();

            }
        });
        setClicklitener();
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


        radio_btn_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_miles="KM";
                txt_view.setText("KM ON VEHICLE");
            }
        });
        radio_btn_miles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_miles="MILES";
                txt_view.setText("MILES ON VEHICLE");
            }
        });
        radio_btn_yes_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_no="YES KM TO MILES";
            }
        });
        radio_btn_no_already_miles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_no="NO ALREADY IN MILES";
            }
        });
        radio_btn_no_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_no="NO NEEDS KM STICKER";
            }
        });
        radio_required_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_need_required ="Yes";
            }
        });

        radio_required_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_need_required ="No";
            }
        });

    }
    //camera option
    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(DialogInterface.OnClickListener activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((AddVehicalAndPayment5) activity, permission[0])) {
            Toast.makeText((AddVehicalAndPayment5)activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions((AddVehicalAndPayment5)activity, permission, requestCode);
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
                   // File finalFile = new File(getRealPathFromUri(uri));
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
    private Uri getImageUri(AddVehicalAndPayment5 yourActivity, Bitmap bitmap) {
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
        if(vehicle_km_miles_edit_txt.getText().toString().trim().isEmpty())
        {
            vehicle_km="";
        }
        else{
            vehicle_km=vehicle_km_miles_edit_txt.getText().toString();
        }

        imageConversionAsynchronus = new ImageConversionAsynchronus(getActivity(), this, 1, imageViewphoto_of_registration, "passanger_side_air_bag");
        imageConversionAsynchronus.execute();


    }

    @Override
    public void onDataComplete(int i, String image_path) {
        Log.e("Image Path", image_path);
        if (i == 1) {
            imageViewphoto_of_bill_sale1 = image_path;
            imageConversionAsynchronus = new ImageConversionAsynchronus(getActivity(), this, 2, imageViewphoto_of_registration, "driver_seat_belt");
            imageConversionAsynchronus.execute();
        } else if (i == 2) {
            imageViewphoto_of_registration1 = image_path;
            imageConversionAsynchronus = new ImageConversionAsynchronus(getActivity(), this, 3, imageViewphoto_of_recall, "driver_air_bag");
            imageConversionAsynchronus.execute();
        } else if (i == 3) {
            imageViewphoto_of_recall1 = image_path;
            imageConversionAsynchronus = new ImageConversionAsynchronus(getActivity(), this, 4, imageViewphoto_of_manufacture_label, "speedo");
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
            /*passengerairbagdoc
            speedo_converter*/
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(CommonURL.URL+"/api/add_carfivestep");
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
                entity.addPart("passengerairbagdoc", new FileBody(sourceFile));

                File sourceFile1 = new File(imageViewphoto_of_registration1);

                // Adding file data to http body
                entity.addPart("driverseatbeltdoc", new FileBody(sourceFile1));

                File sourceFile2 = new File(imageViewphoto_of_recall1);

                // Adding file data to http body
                entity.addPart("driverairbagdoc", new FileBody(sourceFile2));

                File sourceFile3 = new File(imageViewphoto_of_manufacture_label1);

                // Adding file data to http body
                entity.addPart("speedodoc", new FileBody(sourceFile3));

                // Extra parameters if you want to pass to server
                entity.addPart("vid",
                        new StringBody(v_id));
                entity.addPart("kmonvehicle", new StringBody(vehicle_km));

                entity.addPart("speedo_converter",
                        new StringBody(radio_btn_km_no));
                entity.addPart("ri",
                        new StringBody(radio_btn_need_required));

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
                        Intent intent=new Intent(getActivity(), SellerDetail.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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