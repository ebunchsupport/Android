package com.dvn.vindecoder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dvn.vindecoder.R;

public class SignUpAsRemainingAll extends AppCompatActivity {
    private Button login_btn;
    private TextView page_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_transporter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        page_title=(TextView)findViewById(R.id.page_title);
        login_btn=(Button)findViewById(R.id.login_btn);

        Bundle bundle=getIntent().getExtras();
        String value=bundle.getString("action_name");
        switch (value)
        {
            case "transporter":
                page_title.setText("SignUP as a Transporter");
                break;
            case "vender":
                page_title.setText("SignUP as a Vendor");
                break;

            case "r_i_f":
                page_title.setText("SignUP as a R I Facility");
                break;

            case "seller info":
                //page_title.setText("SignUP as a Seller Input Vehical Info");
                break;

            default:

                break;

        }
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpAsRemainingAll.this,Transporter_detail.class);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
            }
        });
    }
}
