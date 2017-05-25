package com.dvn.vindecoder.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.ui.seller.SellerDetail;
import com.dvn.vindecoder.ui.user.UserDetail;
import com.dvn.vindecoder.util.ConnectionDetector;
import com.dvn.vindecoder.util.LoginDB;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;
    private ConnectionDetector connectionDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //checking internet connection if available go to next page else exit from app
        networkConctFun();

        //goto next page
        gotoNextWindowFun();
    }

    private void networkConctFun() {
        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet() == false) {
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_LONG).show();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        }
    }

    private void gotoNextWindowFun() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (LoginDB.getLoginFlag(SplashScreen.this)) {
                Intent intent;
                    if(LoginDB.getUserType(SplashScreen.this,"user_type").equalsIgnoreCase("1"))
                    {
                        intent= new Intent(SplashScreen.this,SellerDetail.class);
                    }
                    else //if(LoginDB.getUserType(SplashScreen.this,"user_type").equalsIgnoreCase("3"))
                    {
                        intent= new Intent(SplashScreen.this,UserDetail.class);
                    }

                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                } else {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
