package com.dvn.vindecoder.ui.seller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.ui.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AddDriverActivity extends BaseActivity {
    private TextView page_title;
    private EditText etUsername,etEmailname,etnumber,etcontactnumber,etfax,etAddress,etGst,etPassword,etCPassword;
    private String etUsername1,etEmailname1,etnumber1,etcontactnumber1,etfax1,etAddress1,etGst1,etPassword1,etCPassword1;
    private Button signUp_btn;
    private int check_img_status=0;//1 for driving licence , 2 for Insurance Plate , 3 for Passprot
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE=3;
    private static final int REQUEST_ACESS_CAMERA=2;
    private Uri uri;
    private ImageView img_view_drivingLicense,insurance_plate_img_view,passport_imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_add_driver);
        drawChildLayout();
        setTitle("Add Driver");

      etUsername=(EditText)findViewById(R.id.etUsername);
        etEmailname=(EditText)findViewById(R.id.etEmailname);
        //etnumber=(EditText)findViewById(R.id.etnumber);
        etcontactnumber=(EditText)findViewById(R.id.etcontactnumber);
        etfax=(EditText)findViewById(R.id.etfax);
        etAddress=(EditText)findViewById(R.id.etAddress);
        etGst=(EditText)findViewById(R.id.etGst);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etCPassword=(EditText)findViewById(R.id.etCPassword);
        signUp_btn=(Button)findViewById(R.id.signUp_btn);
        img_view_drivingLicense=(ImageView) findViewById(R.id.img_view_drivingLicense);
        insurance_plate_img_view=(ImageView)findViewById(R.id.insurance_plate_img_view);
        passport_imageView=(ImageView)findViewById(R.id.passport_imageView);
        setClicklitener();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_add_driver),
                layoutParams);
    }

    @Override
    public void sendFbData() {

    }

    private void setClicklitener()
    {
        img_view_drivingLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status=1;
                handleCameraForPickingPhoto();
            }
        });
        insurance_plate_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status=2;
                handleCameraForPickingPhoto();
            }
        });
        passport_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_img_status=3;
                handleCameraForPickingPhoto();
            }
        });
    }
        //camera option
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

    public static void requestPermission(Fragment fragment, String[] permission, int requestCode) {
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
            AlertDialog.Builder myAlertDilog = new AlertDialog.Builder(AddDriverActivity.this);
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
                        if(checkPermission(Manifest.permission.CAMERA,AddDriverActivity.this)){
                            openCameraApplication();
                        }else{
                            requestPermission(AddDriverActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_ACESS_CAMERA);
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    try {
                       // BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                        options.inSampleSize =calculateInSampleSize(options, 100, 100);
                        options.inJustDecodeBounds = false;
                        Bitmap image = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                        if(check_img_status==1)
                        {
                            img_view_drivingLicense.setImageBitmap(image);
                        }
                        else if(check_img_status==2)
                        {
                            insurance_plate_img_view.setImageBitmap(image);
                        }
                        else if(check_img_status==3)
                        {
                            passport_imageView.setImageBitmap(image);
                        }

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
                    uri = getImageUri(AddDriverActivity.this, bitmap);
                    File finalFile = new File(getRealPathFromUri(uri));
                    //img_view_drivingLicense.setImageBitmap(bitmap);
                    if(check_img_status==1)
                    {
                        img_view_drivingLicense.setImageBitmap(bitmap);
                    }
                    else if(check_img_status==2)
                    {
                        insurance_plate_img_view.setImageBitmap(bitmap);
                    }
                    else if(check_img_status==3)
                    {
                        passport_imageView.setImageBitmap(bitmap);
                    }
                } else if (data.getExtras() == null) {

                    Toast.makeText(getApplicationContext(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());
                    //img_view_drivingLicense.setImageDrawable(thumbnail);
                    if(check_img_status==1)
                    {
                        img_view_drivingLicense.setImageDrawable(thumbnail);
                    }
                    else if(check_img_status==2)
                    {
                        insurance_plate_img_view.setImageDrawable(thumbnail);
                    }
                    else if(check_img_status==3)
                    {
                        passport_imageView.setImageDrawable(thumbnail);
                    }
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = this.getContentResolver().query(tempUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private Uri getImageUri(AddDriverActivity yourActivity, Bitmap bitmap) {
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

    //ends of camera option

    public static void displayPromptForEnablingGPS(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "User sucessfully inserted";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        });

        builder.create().show();
    }
}
