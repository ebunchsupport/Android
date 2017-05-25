package com.dvn.vindecoder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.ui.seller.AddDriverActivity;

public class Transporter_detail extends AppCompatActivity {
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
               // Intent intent=new Intent(Transporter_detail.class,)

                Intent intent = new Intent(Transporter_detail.this,AddDriverActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);

            }
        });
    }

}
