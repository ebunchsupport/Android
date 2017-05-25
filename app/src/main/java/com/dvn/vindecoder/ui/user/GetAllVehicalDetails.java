 package com.dvn.vindecoder.ui.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.interface_package.DataComplete;
import com.dvn.vindecoder.ui.BaseActivity;
import com.dvn.vindecoder.ui.SignUpAsSeller;
import com.dvn.vindecoder.ui.UserBaseActivity;
import com.dvn.vindecoder.ui.seller.SellerDetail;
import com.dvn.vindecoder.util.AndroidMultiPartEntity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("ParcelCreator")
public class GetAllVehicalDetails extends UserBaseActivity implements DataComplete, AsyncCompleteListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    int mStackLevel = 1;
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    public Double latitude = 0.0;
    public Double longitute = 0.0;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE=3;
    private static final int REQUEST_ACESS_CAMERA=2;
    private Uri uri;
  //  CircleImageView car_image;
    private String image_path=null;
    private FloatingActionButton fab;
    public String v_id;
    public String reason;
    public boolean chck_geo_boolean = false;
    public boolean chck_geo_boolean1 = false;
    private boolean chck_imageDone = false;
    protected GoogleApiClient mGoogleApiClient;
    public GetNewVehicalFragment newFragment;
    public  AddVehicalFragment_New_For_User addVehicalFragment_new_for_user;
    protected Location mLastLocation;
    private long totalSize;
  //  private CircleImageView car_image1;
   // private CollapsingToolbarLayout collapsingToolbarLayout;
      //user_type=1 for seller and 0 for user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_get_all_vehical_details);
        // Watch for button clicks.
       /* Button button = (Button)findViewById(R.id.new_fragment);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                addFragmentToStack();
            }
        });*/
        setTitle("Vehical details");
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            CommonURL.V_ID=bundle.getString("v_id");
            v_id=bundle.getString("v_id");
            CommonURL.PK_ID=v_id;
            if (bundle.containsKey("user_type"))
            {
                CommonURL.USER_TYPE=bundle.getInt("user_type");;
            }
        }
        drawChildLayout();
        if (savedInstanceState == null) {
            // Do first time initialization -- add initial fragment.
           /*  newFragment = GetNewVehicalFragment.newInstance(GetAllVehicalDetails.this);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment1, newFragment).commit();*/

            addVehicalFragment_new_for_user = AddVehicalFragment_New_For_User.newInstance(GetAllVehicalDetails.this);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment1, addVehicalFragment_new_for_user).commit();
        }
        else
        {
            mStackLevel = savedInstanceState.getInt("level");
        }



        fab = (FloatingActionButton)findViewById(R.id.fab);
        TextView fab_txt=(TextView)findViewById(R.id.textView12);
        if(CommonURL.USER_TYPE==1)
        {

            fab.setVisibility(View.GONE);
            fab_txt.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handleCameraForPickingPhoto();
                openDialog();
            }
        });

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_get_all_vehical_details),
                layoutParams);
      /*  car_image=(CircleImageView)findViewById(R.id.car_image);
        car_image1=(CircleImageView)findViewById(R.id.car_image1);*/
        buildGoogleApiClient();
      //  checkCondition();
        //checkCondition1();
      /*  car_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCameraForPickingPhoto();
            }
        });
        car_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCameraForPickingPhoto();
            }
        });*/
        setScrollContent();
    }

    @Override
    public void sendFbData() {

    }


    public void openDialog() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(GetAllVehicalDetails.this);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                GetAllVehicalDetails.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Why you want to stop this");
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                //result.setText(userInput.getText());
                                if(!userInput.getText().toString().trim().isEmpty())
                                {
                                    reason=userInput.getText().toString().trim();
                                    getUserData(reason);
                                }
                                else {
                                    Toast.makeText(GetAllVehicalDetails.this,"Please fill your reason",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("level", mStackLevel);
    }

    void addFragmentToStack() {
        mStackLevel++;

        // Instantiate a new fragment.
        Fragment newFragment = GetNewVehicalFragment.newInstance(GetAllVehicalDetails.this);

        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        ft.replace(R.id.fragment1, newFragment);
       // ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onDataComplete(int i, String image_path) {
        if(i==1)
        {
           /* Bitmap bitmap = ((BitmapDrawable)car_image.getDrawable()).getBitmap();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(getCacheDir(),"car.jpg");
            FileOutputStream fo;
            try {
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image_path=destination.getAbsolutePath();
            // car_image.setImageBitmap(bitmap);
            newFragment.setImagePath(image_path);*/
        }
        else if(i==2)
        {
            /*car_image.setOnClickListener(null);
            car_image1.setOnClickListener(null);*/
         //   collapsingToolbarLayout.setVisibility(View.GONE);
            Fragment newFragment = UserAddNewVehicalSecond.newInstance(GetAllVehicalDetails.this);

            // Add the fragment to the activity, pushing this transaction
            // on to the back stack.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_enter,
                    R.animator.fragment_slide_right_exit);
            ft.replace(R.id.fragment1, newFragment);
           //  ft.addToBackStack(null);
            ft.commit();
        }
        else if(i==3)
        {
            Fragment newFragment = UserAddNewVehicalThird.newInstance(GetAllVehicalDetails.this);

            // Add the fragment to the activity, pushing this transaction
            // on to the back stack.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_enter,
                    R.animator.fragment_slide_right_exit);
            ft.replace(R.id.fragment1, newFragment);
            // ft.addToBackStack(null);
            ft.commit();
        }
        else if(i==4)
        {
            Fragment newFragment = UserNewVehicalFourth.newInstance(GetAllVehicalDetails.this);

            // Add the fragment to the activity, pushing this transaction
            // on to the back stack.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_enter,
                    R.animator.fragment_slide_right_exit);
            ft.replace(R.id.fragment1, newFragment);
          //   ft.addToBackStack(null);
            ft.commit();
        }
        else if(i==5)
        {
            Fragment newFragment = UserAddNewVehicalFifth.newInstance(GetAllVehicalDetails.this);

            // Add the fragment to the activity, pushing this transaction
            // on to the back stack.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit,
                    R.animator.fragment_slide_right_enter,
                    R.animator.fragment_slide_right_exit);
            ft.replace(R.id.fragment1, newFragment);
           //  ft.addToBackStack(null);
            ft.commit();
        }

        else if(i==0)
        {

            Glide.with(GetAllVehicalDetails.this).load(image_path)
                    .asBitmap().into(new SimpleTarget<Bitmap>(300,300) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    //setBackgroundImage(resource);
                  //  car_image.setImageBitmap(resource);
                   // car_image1.setImageBitmap(resource);
                }
            });
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(CommonURL.USER_TYPE==1)
        {
            Intent intent=new Intent(this,SellerDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(this,UserDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Activity sss1", "sds");
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    image_path = uri.getPath();
                    //addNewVehicalFragment.setImagePath(image_path);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    try {
                        // BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                        options.inSampleSize = calculateInSampleSize(options, 100, 100);
                        options.inJustDecodeBounds = false;
                        Bitmap image = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

                     //   car_image.setImageBitmap(image);
                        //car_image1.setImageBitmap(image);

                       /* Bitmap bitmap = ((BitmapDrawable) car_image.getDrawable()).getBitmap();
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        File destination = new File(getCacheDir(), "car.jpg");
                        FileOutputStream fo;
                        try {
                            fo = new FileOutputStream(destination);
                            fo.write(bytes.toByteArray());
                            fo.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        image_path = destination.getAbsolutePath();*/
                        // car_image.setImageBitmap(bitmap);
                      //  addNewVehicalFragment.setImagePath(image_path);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                   /* ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File destination = new File(getCacheDir(), "car.jpg");
                    FileOutputStream fo;
                    try {
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image_path = destination.getAbsolutePath();*/
                  //  car_image.setImageBitmap(bitmap);
                  //  car_image1.setImageBitmap(bitmap);
                    // addNewVehicalFragment.setImagePath(image_path);
                } else if (data.getExtras() == null) {

                    Toast.makeText(getApplicationContext(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());
                    //img_view_drivingLicense.setImageDrawable(thumbnail);
                //    car_image.setImageDrawable(thumbnail);
                  //  car_image1.setImageDrawable(thumbnail);

                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(AppCompatActivity activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
            Toast.makeText(activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    public static void requestPermission(android.support.v4.app.Fragment fragment, String[] permission, int requestCode) {
        fragment.requestPermissions(permission, requestCode);
    }

    private void handleCameraForPickingPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
                startDilog();
            } else {
                requestPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ACESS_STORAGE);
            }
        } else {
            startDilog();
        }
    }

    private void startDilog() {
        AlertDialog.Builder myAlertDilog = new AlertDialog.Builder(GetAllVehicalDetails.this);
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
                    if(checkPermission(Manifest.permission.CAMERA,GetAllVehicalDetails.this)){
                        openCameraApplication();
                    }else{
                        requestPermission(GetAllVehicalDetails.this,new String[]{Manifest.permission.CAMERA},REQUEST_ACESS_CAMERA);
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
        if (picIntent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(picIntent, CAMERA_REQUEST);
        }
    }


    private Uri getImageUri(GetAllVehicalDetails yourActivity, Bitmap bitmap) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
    if(type==CallType.GET_STOP_CAR && aMasterDataDtos.getResponseCode()==1)
    {
        Toast.makeText(GetAllVehicalDetails.this,"Car is deleted sucessfully",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,UserDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
        else {
        Toast.makeText(GetAllVehicalDetails.this,"Error in updation try again",Toast.LENGTH_LONG).show();
    }



    }



    private void getUserData(final String reason) {

        new StopCarFromServer().execute();

     /*   Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        String user_id = LoginDB.getTitle(GetAllVehicalDetails.this, "value");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String current_Date = df.format(c.getTime());
        GetVehicalDto getVehicalDto = new GetVehicalDto();
        getVehicalDto.setVid(v_id);
        getVehicalDto.setNotes(reason);
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setLat(""+latitude);
        getVehicalDto.setLong1(""+longitute);
        getVehicalDto.setUserId(""+user_id);
        getVehicalDto.setDate(current_Date);
        //getVehicalDto.setAppid(CommonURL.APP_ID);
        AsyncGetTask asyncGetTask = new AsyncGetTask(GetAllVehicalDetails.this, CallType.GET_STOP_CAR, GetAllVehicalDetails.this, true, getVehicalDto);
        asyncGetTask.execute();*/
    }




    private class StopCarFromServer extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(GetAllVehicalDetails.this);
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

            httppost = new HttpPost(CommonURL.URL + "/api/stop_car");


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                String user_id = LoginDB.getTitle(GetAllVehicalDetails.this, "value");
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String current_Date = df.format(c.getTime());

                entity.addPart("vid", new StringBody(v_id));
                // Adding file data to http body
                entity.addPart("notes", new StringBody(reason));
                entity.addPart("user_id", new StringBody(user_id));
                entity.addPart("lat", new StringBody(""+latitude));
                entity.addPart("long", new StringBody(""+longitute));
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
            // progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
            // showing the server response in an alert dialog
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("response_code")) {
                    if (jsonObject.optString("response_code").equals("1")) {
                        Toast.makeText(GetAllVehicalDetails.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();

                      //  displayPrompt(GetAllVehicalDetails.this);

                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(GetAllVehicalDetails.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(GetAllVehicalDetails.this, result, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }


    public boolean checkCondition() {

        if (chck_geo_boolean) {
            //displayPromptForEnablingGPS(GetAllVehicalDetails.this);
            return true;
        } else {


            try {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int hasReadContactPermission = GetAllVehicalDetails.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
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
                    int hasReadContactPermission = GetAllVehicalDetails.this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
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
        mGoogleApiClient = new GoogleApiClient.Builder(GetAllVehicalDetails.this)
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
        if (ActivityCompat.checkSelfPermission(GetAllVehicalDetails.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GetAllVehicalDetails.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(GetAllVehicalDetails.this, "No location detected please start location setting", Toast.LENGTH_LONG).show();
           // displayPromptForEnablingGPS(GetAllVehicalDetails.this);
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

    private void setScrollContent()
    {

     //   collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
      //  final RelativeLayout img_container1=(RelativeLayout)findViewById(R.id.img_container1);
        // load the animation
        final Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);
        // start the animation
        // set animation listener
        animFadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
           /* if(img_container1.getVisibility() == View.VISIBLE)
            {
                img_container1.setVisibility(View.GONE);
            }*/
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    // Log.e("val","-1  choti imge ko hide kerna h");
                //    img_container1.setVisibility(View.GONE);
                }
                if (scrollRange + verticalOffset == 0) {
                    //  collapsingToolbarLayout.setTitle("Title");
                    // Log.e("val","0 choti imge ko show kerna h");
                  //  img_container1.setVisibility(View.VISIBLE);
                 //   img_container1.startAnimation(animFadein);
                    isShow = true;
                } else if(isShow) {
                    // collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    //Log.e("val","Showing choti imge ko hide kerna h");
                  //  img_container1.setVisibility(View.GONE);

                    isShow = false;
                }
            }
        });
    }
}
