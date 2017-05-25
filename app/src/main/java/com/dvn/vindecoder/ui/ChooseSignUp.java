package com.dvn.vindecoder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dvn.vindecoder.R;

public class ChooseSignUp extends AppCompatActivity {
    private LinearLayout buyer_btn,transporter_btn,vender_btn,ri_facility_btn,seller_info_btn;
    private TextView page_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        page_title=(TextView)findViewById(R.id.page_title);
        page_title.setText("SignUP");
        buyer_btn=(LinearLayout)findViewById(R.id.buyer_btn);
        transporter_btn=(LinearLayout)findViewById(R.id.transporter_btn);
        vender_btn=(LinearLayout)findViewById(R.id.vender_btn);
        ri_facility_btn=(LinearLayout)findViewById(R.id.ri_facility_btn);
        seller_info_btn=(LinearLayout)findViewById(R.id.seller_info_btn);
        setClickListener();

    }

    private void setClickListener()
    {
        buyer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseSignUp.this,SignUpAsBuyer.class);
                startActivity(intent);
            }
        });

        transporter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent=new Intent(ChooseSignUp.this,SignUpAsRemainingAll.class);
                intent.putExtra("action_name","transporter");
                startActivity(intent);*/
            }
        });

        vender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent=new Intent(ChooseSignUp.this,SignUpAsRemainingAll.class);
                intent.putExtra("action_name","vender");
                startActivity(intent);*/
            }
        });

        ri_facility_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent=new Intent(ChooseSignUp.this,SignUpAsRemainingAll.class);
                intent.putExtra("action_name","r_i_f");
                startActivity(intent);*/
            }
        });

        seller_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseSignUp.this,SignUpAsSeller.class);
              //  intent.putExtra("action_name","seller info");
                startActivity(intent);
            }
        });
    }
}
