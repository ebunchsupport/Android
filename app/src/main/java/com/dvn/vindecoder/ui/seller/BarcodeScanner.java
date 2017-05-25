package com.dvn.vindecoder.ui.seller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.DecodeDto;
import com.dvn.vindecoder.dto.VINAPISetter;
import com.dvn.vindecoder.util.AeSimpleSHA1;
import com.dvn.vindecoder.util.CommonURL;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

/**
 * Created by kvprasad on 10/3/2015.
 */
public class BarcodeScanner extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private ImageView scanButton,image_keyboard,image_close,image_help;
    private ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);
        initControls();
    }

    private void initControls() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();
        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(BarcodeScanner.this, mCamera, previewCb,
                autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

       // scanButton = (ImageView) findViewById(R.id.image_help);
        image_keyboard=(ImageView)findViewById(R.id.image_keyboard);
        image_close=(ImageView)findViewById(R.id.image_close);
        image_help=(ImageView)findViewById(R.id.image_help);

        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              finish();

            }
        });


        image_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             setMunualVinNumber();

            }
        });

        image_help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setHelpValue();
               /* if (barcodeScanned) {
                    barcodeScanned = false;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }*/
            }
        });






    }




    public void setMunualVinNumber() {

        final Dialog dialog = new Dialog(BarcodeScanner.this);
        dialog.setTitle("Add Vehical");
        dialog.setContentView(R.layout.dailog_manual_vin_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.dailog_layout);
        final EditText edit_vin_number = (EditText) dialog.findViewById(R.id.edit_vin_number);
        final Button btn_verify_vin = (Button) dialog.findViewById(R.id.btn_verify_vin);
        final ImageView clost_img_btn = (ImageView) dialog.findViewById(R.id.clost_img_btn);

        btn_verify_vin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String vin_number= edit_vin_number.getText().toString();
                if(edit_vin_number.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"please enter vin number",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(BarcodeScanner.this, AddVehicalAndPaymentWithoutScan.class);
                    intent.putExtra("boolean", false);
                    intent.putExtra("vin",vin_number);
                    startActivity(intent);
                    overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                }
              /*  Intent intent = new Intent(SellerDetail.this, AddVehicalAndPayment.class);
                intent.putExtra("boolean", true);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);*/
            }
        });

        clost_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void setHelpValue() {

        final Dialog dialog = new Dialog(BarcodeScanner.this);
        dialog.setTitle("Help");
        dialog.setContentView(R.layout.dailog_help_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        final TextView clost_img_btn = (TextView) dialog.findViewById(R.id.text_btn_ok);
        clost_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }









    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {

                    Log.i("<<<<<<Asset Code>>>>> ",
                            "<<<<Bar Code>>> " + sym.getData());
                    String scanResult = sym.getData().trim();

                    showAlertDialog(scanResult);

                  /*  Toast.makeText(BarcodeScanner.this, scanResult,
                            Toast.LENGTH_SHORT).show();*/

                    barcodeScanned = true;

                    break;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


    private void showAlertDialog(final String  message) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(BarcodeScanner.this, AddVehicalAndPaymentWithoutScan.class);
                        intent.putExtra("boolean", false);
                        intent.putExtra("vin",message);
                        startActivity(intent);
                        overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);


                    }
                })

                .show();
    }

}
