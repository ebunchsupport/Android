package com.dvn.vindecoder.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.LoginDto;
import com.dvn.vindecoder.dto.User;
import com.dvn.vindecoder.gallery.GalleryActivity;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment;
import com.dvn.vindecoder.ui.seller.SellerDetail;
import com.dvn.vindecoder.ui.user.UserDetail;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.DBHelper;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;
import com.dvn.vindecoder.util.UIUtility;
import com.dvn.vindecoder.util.Utils;
import com.karumi.dexter.Dexter;

import android.provider.Settings.Secure;
import android.widget.Toast;
import android.widget.VideoView;

import static com.dvn.vindecoder.util.CommonURL.URL;

public class LoginActivity extends AppCompatActivity implements AsyncCompleteListener {
     private Button loginBtn,ocrBtn;
     private TextView signupBtn;

    private LoginDto loginDto;
    public final int REQUEST_ACESS_STORAGE=1;
    public final int REQUEST_ACESS_CAMERA=2;
    public final int REQUEST_ACESS_CROSS=3;
    public final int REQUEST_ACESS_FINE=4;
    public final int REQUEST_CLEAR_CACHE=4;

    private AsyncGetTask asyncGetTask;


    EditText email_txt,pass_txt;
    String device_type;
VideoView video_view;

    private String android_id;
    private DBHelper mydb ;
    private String value;
    Uri uri = Uri.parse("http://ebunchapps.com/quickcross/animated.mp4");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Dexter.initialize(this);
        android_id = Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);
        device_type="android";
        mydb=new DBHelper(LoginActivity.this);
        email_txt=(EditText)findViewById(R.id.etUsername);
        pass_txt =(EditText)findViewById(R.id.etPassword);
        signupBtn=(TextView)findViewById(R.id.sign_up_btn);
        loginBtn=(Button)findViewById(R.id.login_btn);
        video_view=(VideoView)findViewById(R.id.video_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  ocrBtn=(Button)findViewById(R.id.fb_ocr_btn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ChooseSignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (usernameWrapper.isErrorEnabled())
                    usernameWrapper.setErrorEnabled(true);
                if (passwordWrapper.isErrorEnabled())
                    passwordWrapper.setErrorEnabled(true);*/
                if (!UIUtility.isEmailValid(email_txt.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Please insert Valid Email Id", Toast.LENGTH_LONG).show();
                    return;
                } else if (pass_txt.getText().toString().trim().length() < 4) {
                    Toast.makeText(LoginActivity.this, "Please insert Valid Password", Toast.LENGTH_LONG).show();
                    return;
                }
                loginFun();

            }
        });
        video_view.setMediaController(new MediaController(this));
        video_view.setVideoURI(uri);
        video_view.requestFocus();
        video_view.start();


     /*   final ObjectAnimator animation_onboard = ObjectAnimator.ofFloat(image_car, "rotationX", -0.0f,
                360f);
        animation_onboard.setDuration(3000);
        animation_onboard.setRepeatCount(3);
        animation_onboard.setInterpolator(new AccelerateDecelerateInterpolator());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animation_onboard.start();


            }
        }, 1000);*/

      /*  ocrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//BarCodeActivity
                Intent intent = new Intent(LoginActivity.this,GalleryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
            }
        });*/

        setPermission();
    }


    private void loginFun() {
        loginDto = new LoginDto();
        loginDto.setEmail(email_txt.getText().toString().trim());
        loginDto.setPassword(pass_txt.getText().toString().trim());
        loginDto.setDeviceId(android_id);
        loginDto.setDeviceType(device_type);
        loginDto.setAppid(CommonURL.APP_ID);
        Log.e("Email",email_txt.getText().toString().trim());
        Log.e("Password",pass_txt.getText().toString().trim());
        Log.e("android_id",android_id);
        Log.e("device_type",device_type);
        asyncGetTask = new AsyncGetTask(LoginActivity.this, CallType.POST_LOGIN, LoginActivity.this, true, loginDto);
        asyncGetTask.execute();
    }

    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.POST_LOGIN) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
           // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());
            if(aMasterDataDtos.getResponseCode()==1) {
                LoginDB.setTitle(LoginActivity.this, "value", aMasterDataDtos.getResponse().getUser().getId());

                User user = aMasterDataDtos.getResponse().getUser();
                LoginDB.setUserType(LoginActivity.this, "user_type", "" + user.getRole());
                run(user);
                LoginDB.setLoginFlag(LoginActivity.this, true);
                Intent intent;
                if (user.getRole().equalsIgnoreCase("1"))
                {
                    intent = new Intent(LoginActivity.this,SellerDetail.class);
                }
                else//(user.getRole().equalsIgnoreCase("3"))
                {
                    intent = new Intent(LoginActivity.this,UserDetail.class);
                }

                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"Error In Login", Toast.LENGTH_SHORT).show();
        }
    }

    public void run(final User user) {
        value=LoginDB.getTitle(LoginActivity.this,"value");//String id, String email, String pass, String name,String appid,String profile_pic
            if(value != null || !value.equalsIgnoreCase("")){
                if(mydb.updateContact(value,user.getEmail(),
                        user.getPass(),user.getName(),
                        user.getAppid(), user.getProfilePic())){
                   // Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                } else{
                    //Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else{//String id, String email, String pass, String name,String appid,String profile_pic
                if(mydb.insertContact(user.getId(),user.getEmail(),
                        user.getPass(),user.getName(),
                        user.getAppid(), user.getProfilePic())){
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
            }

        Utils.setUserLogin(LoginActivity.this,user.getId(),user.getName(),user.getEmail());

    }

    private void setPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)==false) {
                requestPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ACESS_STORAGE);
                Log.e("Val","1");
            }
            else if(checkPermission(Manifest.permission.CAMERA,this)==false){
                requestPermission(this,new String[]{Manifest.permission.CAMERA},REQUEST_ACESS_CAMERA);
                Log.e("Val","2");
            }
            else if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,this)==false){
                requestPermission(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_ACESS_FINE);
                Log.e("Val","3");
            }
            else if(checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,this)==false){
                requestPermission(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_ACESS_CROSS);
                Log.e("Val","4");
            }
          /*  else if(checkPermission(Manifest.permission.CLEAR_APP_CACHE,this)==false){
                requestPermission(this,new String[]{Manifest.permission.CLEAR_APP_CACHE},REQUEST_CLEAR_CACHE);
                Log.e("Val","5");
            }*/

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        setPermission();

    }
    public static void requestPermission(AppCompatActivity activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
            Toast.makeText(activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }
}
