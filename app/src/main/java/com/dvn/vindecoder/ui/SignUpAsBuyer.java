package com.dvn.vindecoder.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dvn.vindecoder.R;

public class SignUpAsBuyer extends AppCompatActivity {
    private TextView page_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_by_buyer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        page_title=(TextView)findViewById(R.id.page_title);
        page_title.setText("SignUP");
    }
}
