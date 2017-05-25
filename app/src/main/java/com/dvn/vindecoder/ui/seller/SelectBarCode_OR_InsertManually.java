package com.dvn.vindecoder.ui.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.ui.BaseActivity;

public class SelectBarCode_OR_InsertManually extends BaseActivity {
    private Button bar_code_Btn,manually_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_select_bar_code__or__insert_manually);
        drawChildLayout();
        setTitle("Add Vehical");
        bar_code_Btn=(Button)findViewById(R.id.bar_code_btn);
        manually_btn=(Button)findViewById(R.id.insert_manually_btn);

        bar_code_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectBarCode_OR_InsertManually.this,AddVehicalAndPayment.class);
                intent.putExtra("boolean",true);
                startActivity(intent);
            }
        });


        manually_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectBarCode_OR_InsertManually.this,AddVehicalAndPaymentWithoutScan.class);
                intent.putExtra("boolean",false);
                startActivity(intent);
            }
        });

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_select_bar_code__or__insert_manually),
                layoutParams);
    }

    @Override
    public void sendFbData() {

    }
}
