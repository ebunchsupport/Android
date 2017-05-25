package com.dvn.vindecoder.ui.user;

/**
 * Created by Palash on 1/27/2017.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.dvn.vindecoder.receiver.SampleReceiver;
import com.dvn.vindecoder.ui.LoginActivity;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment5;
import com.dvn.vindecoder.ui.seller.SellerDetail;
import com.dvn.vindecoder.util.AndroidMultiPartEntity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.ImageConversionAsynchronus;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;
import com.dvn.vindecoder.util.UIUtility;
import com.geoloqi.android.sdk.LQTracker;
import com.geoloqi.android.sdk.receiver.LQBroadcastReceiver;
import com.geoloqi.android.sdk.service.LQService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@SuppressLint("ParcelCreator")
public class UserAddNewVehicalFifth extends Fragment implements DataComplete, AsyncCompleteListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private View mainView;
    private LQService mService;
    public Double latitude = 0.0;
    public Double longitute = 0.0;
    private boolean mBound;
    private String user_id;
    private String provider;
    Button next_btn, fifth_button_final;
    ImageConversionAsynchronus imageConversionAsynchronus;
    public String v_id;
    private int check_img_status = 0;//1 for driving licence , 2 for Insurance Plate , 3 for Passprot
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private ProgressDialog progressDialog;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE = 3;
    private static final int REQUEST_ACESS_CAMERA = 2;
    private Uri uri;
    private long totalSize;
    private ImageView imageViewphoto_of_bill_sale, imageViewphoto_of_registration, imageViewphoto_of_recall, imageViewphoto_of_manufacture_label;
    private RadioButton radio_btn_km, radio_btn_miles, radio_btn_yes_km, radio_btn_no_already_miles, radio_btn_no_need,
            radio_required_yes, radio_required_no;
    private String radio_btn_km_miles = "KM";
    private String radio_btn_km_no = "YES KM TO MILES";
    private String radio_btn_need_required = "Yes";
    private TextView txt_view;
    private String imageViewphoto_of_bill_sale1, imageViewphoto_of_registration1, imageViewphoto_of_recall1, imageViewphoto_of_manufacture_label1;
    private EditText vehicle_km_miles_edit_txt;
    private String vehicle_km;
    private static final String DESCRIBABLE_KEY = "describable_key";
    private DataComplete dataComplete;
    private List<CarDto1> cardata;
    RelativeLayout rel_msg_box;
    ImageView msg_close;
    private String remark = "";
    private EditText edit_txt_remark;
    String current_Date;
    private LocationListener locListener;
    private Location mobileLocation;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 124;
    public boolean chck_geo_boolean = false;
    public boolean chck_geo_boolean1 = false;
    private boolean chck_imageDone = false;
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    protected String mLatitudeLabel;
    protected String mLongitudeLabel;

    public static UserAddNewVehicalFifth newInstance(DataComplete dataComplete) {
        UserAddNewVehicalFifth f = new UserAddNewVehicalFifth();

        // Supply num input as an argument.
        Bundle bundle = new Bundle();
        bundle.putParcelable(DESCRIBABLE_KEY, dataComplete);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        dataComplete = (DataComplete) getArguments().getParcelable(
                DESCRIBABLE_KEY);
        v_id = CommonURL.V_ID;
        mainView = (View) inflater.inflate(R.layout.add_vehical_fifth_part, container, false);
        buildGoogleApiClient();
       // checkCondition();
      //  checkCondition1();
        imageViewphoto_of_bill_sale = (ImageView) mainView.findViewById(R.id.imageViewphoto_passenger_air_bag);
        imageViewphoto_of_registration = (ImageView) mainView.findViewById(R.id.imageViewphoto_of_driver_seat_belt);
        imageViewphoto_of_recall = (ImageView) mainView.findViewById(R.id.imageViewphoto_of_driver_air_bag);
        imageViewphoto_of_manufacture_label = (ImageView) mainView.findViewById(R.id.imageViewphoto_of_speedo);
        vehicle_km_miles_edit_txt = (EditText) mainView.findViewById(R.id.edit_txt_km_on_vehical);
        radio_btn_km = (RadioButton) mainView.findViewById(R.id.radio_btn_km);
        radio_btn_miles = (RadioButton) mainView.findViewById(R.id.radio_btn_miles);
        radio_btn_yes_km = (RadioButton) mainView.findViewById(R.id.radio_btn_yes_km);
        radio_btn_no_already_miles = (RadioButton) mainView.findViewById(R.id.radio_btn_no_already_miles);
        radio_btn_no_need = (RadioButton) mainView.findViewById(R.id.radio_btn_no_need);
        radio_required_yes = (RadioButton) mainView.findViewById(R.id.radio_required_yes);
        radio_required_no = (RadioButton) mainView.findViewById(R.id.radio_required_no);
        txt_view = (TextView) mainView.findViewById(R.id.txt_view_model);
        next_btn = (Button) mainView.findViewById(R.id.fifth_button_ok);
        fifth_button_final = (Button) mainView.findViewById(R.id.fifth_button_final);
        rel_msg_box = (RelativeLayout) mainView.findViewById(R.id.rel_msg_box);
        msg_close = (ImageView) mainView.findViewById(R.id.msg_close);
        edit_txt_remark = (EditText) mainView.findViewById(R.id.edit_txt_remark);
        user_id = LoginDB.getTitle(getActivity(), "value");
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        current_Date = df.format(c.getTime());
        // Start the tracking service
        Intent intent = new Intent(getActivity(), LQService.class);
        getActivity().startService(intent);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonURL.USER_TYPE==1)
                {
                    sendToServerForSeller();
                }
                else {
                    sendToServer();
                }



            }
        });
        fifth_button_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit_txt_remark.getText().toString().isEmpty()) {
                    remark = edit_txt_remark.getText().toString();
                    edit_txt_remark.requestFocus();
                } else {
                    Toast.makeText(getActivity(),"Please fill all the field",Toast.LENGTH_LONG).show();
                    return;
                }

                sendToServer1();
            }
        });
        msg_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_msg_box.setVisibility(View.GONE);
            }
        });
        setClicklitener();
        getUserData();
        return mainView;
    }

    private void setClicklitener() {
        imageViewphoto_of_bill_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status = 1;
                handleCameraForPickingPhoto();
            }
        });
        imageViewphoto_of_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status = 2;
                handleCameraForPickingPhoto();
            }
        });
        imageViewphoto_of_recall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status = 3;
                handleCameraForPickingPhoto();
            }
        });
        imageViewphoto_of_manufacture_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status = 4;
                handleCameraForPickingPhoto();
            }
        });


        radio_btn_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_miles = "KM";
                txt_view.setText("KM ON VEHICLE");
            }
        });
        radio_btn_miles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_miles = "MILES";
                txt_view.setText("MILES ON VEHICLE");
            }
        });
        radio_btn_yes_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_no = "YES KM TO MILES";
            }
        });
        radio_btn_no_already_miles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_no = "NO ALREADY IN MILES";
            }
        });
        radio_btn_no_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_km_no = "NO NEEDS KM STICKER";
            }
        });
        radio_required_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_need_required = "Yes";
            }
        });

        radio_required_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_btn_need_required = "No";
            }
        });

    }
    private void sendToServerForSeller()
    {
        if(vehicle_km_miles_edit_txt.getText().toString().trim().isEmpty())
        {
            vehicle_km="";
        }
        else{
            vehicle_km=vehicle_km_miles_edit_txt.getText().toString();
        }
        remark="";
        imageConversionAsynchronus = new ImageConversionAsynchronus(getActivity(), this, 1, imageViewphoto_of_registration, "passanger_side_air_bag");
        imageConversionAsynchronus.execute();


    }
    //camera option
    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(DialogInterface.OnClickListener activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((AddVehicalAndPayment5) activity, permission[0])) {
            Toast.makeText((AddVehicalAndPayment5) activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions((AddVehicalAndPayment5) activity, permission, requestCode);
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
                Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                picIntent.setType("image/*");
                picIntent.putExtra("return_data", true);
                startActivityForResult(picIntent, GALLERY_REQUEST);
            }
        });
        myAlertDilog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission(Manifest.permission.CAMERA, getActivity())) {
                        openCameraApplication();
                    } else {
                        requestPermission(this, new String[]{Manifest.permission.CAMERA}, REQUEST_ACESS_CAMERA);
                    }
                } else {
                    openCameraApplication();
                }
            }
        });
        myAlertDilog.show();
    }

    private void openCameraApplication() {
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                        options.inSampleSize = calculateInSampleSize(options, 100, 100);
                        options.inJustDecodeBounds = false;
                        Bitmap image = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
                        if (check_img_status == 1) {
                            imageViewphoto_of_bill_sale.setImageBitmap(image);
                        } else if (check_img_status == 2) {
                            imageViewphoto_of_registration.setImageBitmap(image);
                        } else if (check_img_status == 3) {
                            imageViewphoto_of_recall.setImageBitmap(image);
                        } else if (check_img_status == 4) {
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
                    if (check_img_status == 1) {
                        imageViewphoto_of_bill_sale.setImageBitmap(bitmap);
                    } else if (check_img_status == 2) {
                        imageViewphoto_of_registration.setImageBitmap(bitmap);
                    } else if (check_img_status == 3) {
                        imageViewphoto_of_recall.setImageBitmap(bitmap);
                    } else if (check_img_status == 4) {
                        imageViewphoto_of_manufacture_label.setImageBitmap(bitmap);
                    }
                } else if (data.getExtras() == null) {

                    Toast.makeText(getActivity(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());
                    //imageViewphoto_of_bill_sale.setImageDrawable(thumbnail);
                    if (check_img_status == 1) {
                        imageViewphoto_of_bill_sale.setImageDrawable(thumbnail);
                    } else if (check_img_status == 2) {
                        imageViewphoto_of_registration.setImageDrawable(thumbnail);
                    } else if (check_img_status == 3) {
                        imageViewphoto_of_recall.setImageDrawable(thumbnail);
                    } else if (check_img_status == 4) {
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
        if (requestCode == REQUEST_ACESS_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startDilog();
        }
        if (requestCode == REQUEST_ACESS_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCameraApplication();
        }
    }

    private String setImagePath(ImageView imageView, String doc_name) {
        String image_path;
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), (doc_name + ".jpg"));
        FileOutputStream fo;
        try {
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image_path = destination.getAbsolutePath();
        return image_path;
    }


    private void sendToServer() {
        if (vehicle_km_miles_edit_txt.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "please enter Speed", Toast.LENGTH_LONG).show();
            vehicle_km_miles_edit_txt.requestFocus();
            return;
        }
        showDialog(getActivity(),"");
        //rel_msg_box.setVisibility(View.VISIBLE);


    }

    private void sendToServer1() {

        vehicle_km = vehicle_km_miles_edit_txt.getText().toString();
        if (chck_imageDone) {
            sendToServer2();
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
            chck_imageDone = true;
            if (latitude == 0 || longitute == 0) {
                Toast.makeText(getActivity(), "No location detected please start location setting", Toast.LENGTH_LONG).show();
                return;
            }

            new UploadFileToServer().execute();
        }
    }

    private void sendToServer2() {
        if (latitude == 0 || longitute == 0) {
            Toast.makeText(getActivity(), "please check your gps connection either is off or not connected please wait until is connected", Toast.LENGTH_LONG).show();
            return;
        }

        new UploadFileToServer().execute();
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
            HttpPost httppost = new HttpPost(CommonURL.URL + "/api/update_fifthstepdata");

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
                entity.addPart("type",
                        new StringBody(""+CommonURL.USER_TYPE));
                entity.addPart("ri",
                        new StringBody(radio_btn_need_required));

                entity.addPart("appid", new StringBody(CommonURL.APP_ID));


                entity.addPart("user_id",
                        new StringBody(user_id));
                entity.addPart("lat", new StringBody("" + latitude));

                entity.addPart("long",
                        new StringBody("" + longitute));
                entity.addPart("notes",
                        new StringBody(remark));

                entity.addPart("date", new StringBody(current_Date));

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
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("response_code")) {
                    if (jsonObject.optString("response_code").equals("1")) {
                       /* Toast.makeText(getActivity(),"vehicle Information are successfully saved",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getActivity(), SellerDetail.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                       if(CommonURL.USER_TYPE==1)
                       {
                           Intent intent = new Intent(getActivity(), SellerDetail.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                       }
                       else {
                           Intent intent = new Intent(getActivity(), UserDetail.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intent);
                       }

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
        if (type == CallType.GET_VEHICAL_STEP_FIFTH) {
            if (aMasterDataDtos.getResponseCode() == 1) {
                if (aMasterDataDtos.getResponseCode() == 1) {
                    cardata = aMasterDataDtos.getCardata();
                    setData();
                } else {
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
                }


            }
        }
    }

    private void setData() {
        //,,,
        Glide.with(getActivity()).load(cardata.get(0).getImagePassengerairbag())
                .asBitmap().into(new SimpleTarget<Bitmap>(300, 300) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                //setBackgroundImage(resource);
                imageViewphoto_of_bill_sale.setImageBitmap(resource);
            }
        });

        Glide.with(getActivity()).load(cardata.get(0).getImageDriverseatbelt())
                .asBitmap().into(new SimpleTarget<Bitmap>(300, 300) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                //setBackgroundImage(resource);
                imageViewphoto_of_registration.setImageBitmap(resource);
            }
        });

        Glide.with(getActivity()).load(cardata.get(0).getImageDriverairbag())
                .asBitmap().into(new SimpleTarget<Bitmap>(300, 300) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                //setBackgroundImage(resource);
                imageViewphoto_of_recall.setImageBitmap(resource);
            }
        });


        Glide.with(getActivity()).load(cardata.get(0).getSpeedoImage())
                .asBitmap().into(new SimpleTarget<Bitmap>(300, 300) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                //setBackgroundImage(resource);
                imageViewphoto_of_manufacture_label.setImageBitmap(resource);
            }
        });
        if (UIUtility.isStringnotEmpty(cardata.get(0).getSpeedo())) {
            if (cardata.get(0).getSpeedo().equalsIgnoreCase("KM")) {
                radio_btn_km.setChecked(true);
                radio_btn_km_miles = "KM";
                txt_view.setText("KM ON VEHICLE");
            } else {
                radio_btn_miles.setChecked(true);
                radio_btn_km_miles = "MILES";
                txt_view.setText("MILES ON VEHICLE");
            }
        }
        if (UIUtility.isStringnotEmpty(cardata.get(0).getSpeedoConverter())) {
            if (cardata.get(0).getSpeedoConverter().equalsIgnoreCase("YES KM TO MILES")) {
                radio_btn_yes_km.setChecked(true);
                radio_btn_km_no = "YES KM TO MILES";
            } else if (cardata.get(0).getSpeedoConverter().equalsIgnoreCase("YES KM TO MILES")) {
                radio_btn_no_already_miles.setChecked(true);
                radio_btn_km_no = "NO ALREADY IN MILES";
            } else {
                radio_btn_km_no = "NO NEEDS KM STICKER";
                radio_btn_no_need.setChecked(true);
            }
        }
        if (UIUtility.isStringnotEmpty(cardata.get(0).getRi())) {
            if (cardata.get(0).getRi().equalsIgnoreCase("yes")) {
                radio_required_yes.setChecked(true);
                radio_btn_need_required = "Yes";
            } else {
                radio_btn_need_required = "No";
                radio_required_no.setChecked(true);
            }
        }
        if (UIUtility.isStringnotEmpty(cardata.get(0).getKmMiles())) {
            vehicle_km_miles_edit_txt.setText(cardata.get(0).getKmMiles());
        }

    }

    private void getUserData() {
        GetVehicalDto getVehicalDto = new GetVehicalDto();
        getVehicalDto.setVid(CommonURL.PK_ID);
        //getVehicalDto.setAppid(CommonURL.APP_ID);
        AsyncGetTask asyncGetTask = new AsyncGetTask(getActivity(), CallType.GET_VEHICAL_STEP_FIFTH, UserAddNewVehicalFifth.this, true, getVehicalDto);
        asyncGetTask.execute();
    }


    public boolean checkCondition() {

        if (chck_geo_boolean) {
            //displayPromptForEnablingGPS(getActivity());
            return true;
        } else {


            try {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int hasReadContactPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                    if (hasReadContactPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                101);
                        chck_geo_boolean = true;
                    } else {
                        chck_geo_boolean = true;
                    }
                } else {
                    chck_geo_boolean = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checkCondition();
    }


    public boolean checkCondition1() {

        if (chck_geo_boolean) {
            buildGoogleApiClient();
            return true;
        } else {


            try {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int hasReadContactPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (hasReadContactPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                102);
                        chck_geo_boolean1 = true;
                    } else {
                        chck_geo_boolean1 = true;
                    }
                } else {
                    chck_geo_boolean1 = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checkCondition1();
    }

    public static void displayPromptForEnablingGPS(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Do you want open GPS setting?";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
           /* mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel,
                    mLastLocation.getLatitude()));*/
            latitude=mLastLocation.getLatitude();
            /*mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel,
                    mLastLocation.getLongitude()));*/
            longitute =mLastLocation.getLongitude();

            Log.e("LAT LONG VALUE",""+latitude+" , "+longitute);
        } else {
            Toast.makeText(getActivity(), "No location detected please start location setting", Toast.LENGTH_LONG).show();
            //displayPromptForEnablingGPS(getActivity());
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("LOCATion", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("Location", "Connection suspended");
        mGoogleApiClient.connect();
    }

    public void showDialog(Activity activity, String img_url){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialoug_fifth_end);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ImageView clost_img_btn;
        Button final_btn;
        clost_img_btn=(ImageView)dialog.findViewById(R.id.msg_close);
        final_btn = (Button) dialog.findViewById(R.id.fifth_button_final);
        final EditText edit_txt_remark1=(EditText)dialog.findViewById(R.id.edit_txt_remark);
        final ProgressBar progressBar_image_Large=(ProgressBar)dialog.findViewById(R.id.progressBar_image_Large);
                //Button dialogButton = (Button) dialog.findViewById(R.id.clost_img_btn);
        clost_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit_txt_remark1.getText().toString().isEmpty()) {
                    remark = edit_txt_remark1.getText().toString();

                } else {
                    edit_txt_remark1.requestFocus();
                    Toast.makeText(getActivity(),"Please fill all the field",Toast.LENGTH_LONG).show();
                    return;
                }

                sendToServer1();
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}