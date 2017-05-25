package com.dvn.vindecoder.fragment;

/**
 * Created by Palash on 1/27/2017.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.BuyerListDto;
import com.dvn.vindecoder.dto.DecodeDto;
import com.dvn.vindecoder.dto.VINAPISetter;
import com.dvn.vindecoder.gallery.Image;
import com.dvn.vindecoder.interface_package.DataComplete;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment2;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment5;
import com.dvn.vindecoder.ui.seller.SellerDetail;
import com.dvn.vindecoder.util.AeSimpleSHA1;
import com.dvn.vindecoder.util.AndroidMultiPartEntity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.ImageConversionAsynchronus;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.dvn.vindecoder.fragment.AddNewVehicalFragment.scanFlag;


public class AddVehicalFragment_New extends Fragment implements AsyncCompleteListener {
    private List<BuyerListDto> buyerListDtos;
    public String vin_number, make, model, year, v_type;
    private static final String DESCRIBABLE_KEY = "describable_key";
    private static final String BOOL_KEY = "bool_key";
    private  static String new_Vin_number;
    ScrollView main_scroll;
    private String filePath = null;
    private String vid = "";
    private String vin_no = null;
    private File output = null;
    private File output1 = null;
    private File output2 = null;
    private File output3 = null;
    private File output4 = null;
    private File output5 = null;
    private File output6 = null;
    private File output7 = null;
    private File output8 = null;
    private File output9 = null;
    private File output10 = null;
    private File output11 = null;
    private File output12 = null;

    String isvechicle_recall_free, vechicle_weight_lbs_gvwr, vechicle_weight_front_gawr_lbs, vechicle_weightrear_gwar_kgs, vechicle_weightfront_gawr,
            str_tire_lable_missing_or_damege_yes_no, str_edit_front_tire_size, str_tire_edit_rim_size, str_edit_front_tire_presser, str_edit_rear_tire_size, str_edit_rear_tire_pressure, str_edit_rear_rim_size,
            str_radio_epa_yes, str_radio_inside_vin_plate_missing_yes_no, str_radio_inside_emmission_yes_no, str_radio_speedo_km, str_radio_speedo_miles, str_radio_km_to_miles_yes, str_radio_km_to_miles_no, str_radio_no_need_km_sticker, str_edit_km_on_vehicle_speedo;

    private int check_img_status = 0;
    private Spinner spinner_vin_satrt, spinner_buyer;
    LinearLayout layout_recall_doc, layout_vehicle_weight, layout_tires, layout_epa, layout_inside, layout_speedo, layout_epa_nested, layout_manufacture_lable, layout_open_hood_release, layout_vichel_speed_info, linear_nest_first_layout, linear_neste_second_layout, linear_neste_third,
            layout_nested_inside, layout_speedo_six, layout_inside_photo_and_outher;
    NestedScrollView neste_first, neste_second, neste_third, neste_fourth, inside_fifth, speedo_six;
    ImageView img_vehicle_info_left, img_vehicle_info_right, image_copy_of_recall_doc, image_upload_carimage, image_upload_copy_of_registration, image_copy_of_bill_salle, image_tire_lable, image_epa_front_driver_side, image_photo_of_manufacture_label,
            image_photo_of_vin_plate, image_photo_of_rear_passanger_cehicle, image_photo_of_seat_belt, image_photo_of_Passanger_side_air_bag, image_photo_of_driver_seat_belt, image_photo_of_driver_air_bag;

    RadioButton radio_btn_yes, radio_btn_no, radio_lbs, radio_Kgs, tire_radio_yes, tire_radio_no, radio_epa_yes, radio_epa_no, radio_inside_vin_plate_missing_yes, radio_inside_vin_plate_missing_no, radio_inside_emmission_yes,
            radio_inside_emmission_no, radio_speedo_km, radio_speedo_miles, radio_km_to_miles_yes, radio_km_to_miles_no, radio_no_need_km_sticker;
    TextView btn_save_recall_doc, text_vehicle_info, text_vehicle_weight, text_tires, text_epa, btn_save_vehcle_weight, btn_tire_save, btn_epa_save, btn_inside_save, btn_speedo_save;
    TableRow table_copy_of_recall_doc, table_epa_photo_driver_side_vehicle;
    private boolean checckBackPress = false;
    private Uri uri;

    private String edit_txt_vin_num1, edit_txt_make1, edit_txt_year1, edit_txt_model1, edit_txt_v_type1, edit_txt_selling_price1, spinner_vin_satrt1, spinner_buyer1;

    private ArrayList<DecodeDto> decodeDtoList = new ArrayList<DecodeDto>();

    String bill_copy_image_path;
    private EditText edit_scan_vin, edit_txt_make, edit_txt_year, edit_txt_model, edit_txt_v_type, edit_txt_selling_price, edit_lbs_gvwr, edit_front_gawr_lbs, edit_rear_gwar_kgs, edit_front_gawr,
            edit_front_tire_size, edit_rim_size, edit_front_tire_presser, edit_rear_tire_size, edit_rear_rim_size, edit_rear_tire_pressure, edit_km_on_vehicle_speedo;
    private DataComplete dataComplete;
    private long totalSize;
    private String seller_id;

    private ProgressDialog progressDialog;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE = 3;
    private static final int REQUEST_ACESS_CAMERA = 2;
    final int CAMERA_CAPTURE = 100;
    final int GALLRY_IMAGE = 200;
    boolean status_vechile_information = false;
    boolean status_vechile_weaight = false;
    boolean status_tires = false;
    boolean status_epa = false;
    boolean status_inside_photo = false;
    boolean status_speedo = false;


    private ImageConversionAsynchronus imageConversionAsynchronus = null;

    public static AddVehicalFragment_New newInstance(DataComplete dataComplete, boolean scan_var,String vin_number_new) {
        AddVehicalFragment_New f = new AddVehicalFragment_New();

        // Supply num input as an argument.
        Bundle bundle = new Bundle();
        bundle.putParcelable(DESCRIBABLE_KEY, dataComplete);
        bundle.putBoolean(BOOL_KEY, scan_var);
        new_Vin_number=vin_number_new;
        f.setArguments(bundle);
        return f;
    }

    public void setImagePath(String img_path) {
        filePath = img_path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dataComplete = (DataComplete) getArguments().getParcelable(
                DESCRIBABLE_KEY);
        scanFlag = (boolean) getArguments().getBoolean(
                BOOL_KEY);


        View mainView = (View) inflater.inflate(R.layout.add_vechical_layout, container, false);
        seller_id = LoginDB.getTitle(getActivity(), "value");
        main_scroll = (ScrollView) mainView.findViewById(R.id.main_scroll);
        layout_recall_doc = (LinearLayout) mainView.findViewById(R.id.layout_recall_doc);
        layout_vehicle_weight = (LinearLayout) mainView.findViewById(R.id.layout_vehicle_weight);
        layout_tires = (LinearLayout) mainView.findViewById(R.id.layout_tires);
        layout_epa = (LinearLayout) mainView.findViewById(R.id.layout_epa);
        layout_inside = (LinearLayout) mainView.findViewById(R.id.layout_inside);
        layout_speedo = (LinearLayout) mainView.findViewById(R.id.layout_speedo);


        layout_manufacture_lable = (LinearLayout) mainView.findViewById(R.id.layout_manufacture_lable);
        layout_open_hood_release = (LinearLayout) mainView.findViewById(R.id.layout_open_hood_release);
        layout_vichel_speed_info = (LinearLayout) mainView.findViewById(R.id.layout_vichel_speed_info);
        layout_inside_photo_and_outher = (LinearLayout) mainView.findViewById(R.id.layout_inside_photo_and_outher);
        linear_nest_first_layout = (LinearLayout) mainView.findViewById(R.id.linear_nest_first_layout);
        linear_neste_second_layout = (LinearLayout) mainView.findViewById(R.id.linear_neste_second_layout);
        layout_epa_nested = (LinearLayout) mainView.findViewById(R.id.layout_epa_nested);
        linear_neste_third = (LinearLayout) mainView.findViewById(R.id.linear_neste_third);
        layout_nested_inside = (LinearLayout) mainView.findViewById(R.id.layout_nested_inside);
        layout_speedo_six = (LinearLayout) mainView.findViewById(R.id.layout_speedo_six);
        neste_first = (NestedScrollView) mainView.findViewById(R.id.neste_first);
        neste_second = (NestedScrollView) mainView.findViewById(R.id.neste_second);
        neste_third = (NestedScrollView) mainView.findViewById(R.id.neste_third);
        neste_fourth = (NestedScrollView) mainView.findViewById(R.id.neste_fourth);
        inside_fifth = (NestedScrollView) mainView.findViewById(R.id.inside_fifth);
        speedo_six = (NestedScrollView) mainView.findViewById(R.id.speedo_six);
        edit_txt_make = (EditText) mainView.findViewById(R.id.edit_txt_make);
        edit_txt_year = (EditText) mainView.findViewById(R.id.edit_txt_year);
        edit_txt_model = (EditText) mainView.findViewById(R.id.edit_txt_model);
        edit_txt_v_type = (EditText) mainView.findViewById(R.id.edit_txt_v_type);
        edit_txt_selling_price = (EditText) mainView.findViewById(R.id.edit_txt_selling_price);


        btn_save_recall_doc = (TextView) mainView.findViewById(R.id.btn_save_recall_doc);
        spinner_vin_satrt = (Spinner) mainView.findViewById(R.id.spinner_vin_satrt);
        spinner_buyer = (Spinner) mainView.findViewById(R.id.spinner_buyer);
        text_vehicle_info = (TextView) mainView.findViewById(R.id.text_vehicle_info);
        img_vehicle_info_left = (ImageView) mainView.findViewById(R.id.img_vehicle_info_left);
        img_vehicle_info_right = (ImageView) mainView.findViewById(R.id.img_vehicle_info_right);

        edit_scan_vin = (EditText) mainView.findViewById(R.id.edit_scan_vin);
      //  button_scan_vin = (Button) mainView.findViewById(R.id.button_scan_vin);
        radio_btn_yes = (RadioButton) mainView.findViewById(R.id.radio_btn_yes);
        radio_btn_no = (RadioButton) mainView.findViewById(R.id.radio_btn_no);


        //For vehicl weight

        radio_lbs = (RadioButton) mainView.findViewById(R.id.radio_vichel_lbs);
        radio_Kgs = (RadioButton) mainView.findViewById(R.id.radio_vichel_kgs);
        edit_lbs_gvwr = (EditText) mainView.findViewById(R.id.edit_lbs_gvwr);
        edit_front_gawr_lbs = (EditText) mainView.findViewById(R.id.edit_front_gawr_lbs);
        edit_rear_gwar_kgs = (EditText) mainView.findViewById(R.id.edit_rear_gwar_kgs);
        edit_front_gawr = (EditText) mainView.findViewById(R.id.edit_front_gawr);
        btn_save_vehcle_weight = (TextView) mainView.findViewById(R.id.btn_save_vehcle_weight);


        //For Tires
        tire_radio_yes = (RadioButton) mainView.findViewById(R.id.tire_radio_yes);
        tire_radio_no = (RadioButton) mainView.findViewById(R.id.tire_radio_no);
        image_tire_lable = (ImageView) mainView.findViewById(R.id.image_tire_lable);
        edit_front_tire_size = (EditText) mainView.findViewById(R.id.edit_front_tire_size);
        edit_rim_size = (EditText) mainView.findViewById(R.id.edit_rim_size);
        edit_front_tire_presser = (EditText) mainView.findViewById(R.id.edit_front_tire_presser);
        edit_rear_tire_size = (EditText) mainView.findViewById(R.id.edit_rear_tire_size);
        edit_rear_rim_size = (EditText) mainView.findViewById(R.id.edit_rear_rim_size);
        edit_rear_tire_pressure = (EditText) mainView.findViewById(R.id.edit_rear_tire_pressure);
        btn_tire_save = (TextView) mainView.findViewById(R.id.btn_tire_save);

        //EPA
        radio_epa_yes = (RadioButton) mainView.findViewById(R.id.radio_epa_yes);
        radio_epa_no = (RadioButton) mainView.findViewById(R.id.radio_epa_no);
        image_epa_front_driver_side = (ImageView) mainView.findViewById(R.id.image_epa_front_driver_side);
        btn_epa_save = (TextView) mainView.findViewById(R.id.btn_epa_save);
        table_epa_photo_driver_side_vehicle = (TableRow) mainView.findViewById(R.id.table_epa_photo_driver_side_vehicle);


        //Inside
        radio_inside_emmission_yes = (RadioButton) mainView.findViewById(R.id.radio_inside_emmission_yes);
        radio_inside_emmission_no = (RadioButton) mainView.findViewById(R.id.radio_inside_emmission_no);
        radio_inside_vin_plate_missing_yes = (RadioButton) mainView.findViewById(R.id.radio_inside_vin_plate_missing_yes);
        radio_inside_vin_plate_missing_no = (RadioButton) mainView.findViewById(R.id.radio_inside_vin_plate_missing_no);
        image_photo_of_manufacture_label = (ImageView) mainView.findViewById(R.id.image_photo_of_manufacture_label);
        image_photo_of_vin_plate = (ImageView) mainView.findViewById(R.id.image_photo_of_vin_plate);
        image_photo_of_rear_passanger_cehicle = (ImageView) mainView.findViewById(R.id.image_photo_of_rear_passanger_cehicle);
        image_photo_of_seat_belt = (ImageView) mainView.findViewById(R.id.image_photo_of_seat_belt);
        image_photo_of_Passanger_side_air_bag = (ImageView) mainView.findViewById(R.id.image_photo_of_Passanger_side_air_bag);
        image_photo_of_driver_seat_belt = (ImageView) mainView.findViewById(R.id.image_photo_of_driver_seat_belt);
        image_photo_of_driver_air_bag = (ImageView) mainView.findViewById(R.id.image_photo_of_driver_air_bag);
        btn_inside_save = (TextView) mainView.findViewById(R.id.btn_inside_save);


        //for  speedo

        radio_speedo_km = (RadioButton) mainView.findViewById(R.id.radio_speedo_km);
        radio_speedo_miles = (RadioButton) mainView.findViewById(R.id.radio_speedo_miles);
        radio_km_to_miles_yes = (RadioButton) mainView.findViewById(R.id.radio_km_to_miles_yes);
        radio_km_to_miles_no = (RadioButton) mainView.findViewById(R.id.radio_km_to_miles_no);
        radio_no_need_km_sticker = (RadioButton) mainView.findViewById(R.id.radio_no_need_km_sticker);
        edit_km_on_vehicle_speedo = (EditText) mainView.findViewById(R.id.edit_km_on_vehicle_speedo);
        btn_speedo_save = (TextView) mainView.findViewById(R.id.btn_speedo_save);


        table_copy_of_recall_doc = (TableRow) mainView.findViewById(R.id.table_copy_of_recall_doc);
        text_vehicle_weight = (TextView) mainView.findViewById(R.id.text_vehicle_weight);
        text_tires = (TextView) mainView.findViewById(R.id.text_tires);
        text_epa = (TextView) mainView.findViewById(R.id.text_epa);
        image_copy_of_recall_doc = (ImageView) mainView.findViewById(R.id.image_copy_of_recall_doc);
        image_upload_copy_of_registration = (ImageView) mainView.findViewById(R.id.image_upload_copy_of_registration);
        image_upload_carimage = (ImageView) mainView.findViewById(R.id.image_upload_carimage);
        image_copy_of_bill_salle = (ImageView) mainView.findViewById(R.id.image_copy_of_bill_salle);


        image_copy_of_recall_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 1;
                // openPictureDialog();
                handleCameraForPickingPhoto();
            }
        });
        image_upload_carimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 2;
                handleCameraForPickingPhoto();
            }
        });
        image_upload_copy_of_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 3;
                handleCameraForPickingPhoto();
            }
        });

        image_copy_of_bill_salle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 4;
                handleCameraForPickingPhoto();
            }
        });


        image_tire_lable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 5;
                handleCameraForPickingPhoto();
            }
        });

        image_epa_front_driver_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 6;
                handleCameraForPickingPhoto();
            }
        });

        image_photo_of_manufacture_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 7;
                handleCameraForPickingPhoto();
            }
        });

        image_photo_of_vin_plate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 8;
                handleCameraForPickingPhoto();
            }
        });


        image_photo_of_seat_belt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 9;
                handleCameraForPickingPhoto();
            }
        });


        image_photo_of_Passanger_side_air_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 10;
                handleCameraForPickingPhoto();
            }
        });

        image_photo_of_driver_seat_belt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 11;
                handleCameraForPickingPhoto();
            }
        });


        image_photo_of_driver_air_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_img_status = 12;
                handleCameraForPickingPhoto();
            }
        });

        radio_btn_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    table_copy_of_recall_doc.setVisibility(View.VISIBLE);
                } else {
                    table_copy_of_recall_doc.setVisibility(View.GONE);
                }
            }
        });
        radio_btn_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    table_copy_of_recall_doc.setVisibility(View.GONE);

                } else {
                    table_copy_of_recall_doc.setVisibility(View.VISIBLE);
                }
            }
        });


        radio_Kgs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    edit_lbs_gvwr.setVisibility(View.VISIBLE);
                } else {
                    edit_lbs_gvwr.setVisibility(View.GONE);
                }
            }
        });

        radio_epa_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    table_epa_photo_driver_side_vehicle.setVisibility(View.VISIBLE);
                } else {
                    table_epa_photo_driver_side_vehicle.setVisibility(View.GONE);
                }
            }
        });
        radio_epa_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    table_epa_photo_driver_side_vehicle.setVisibility(View.GONE);
                } else {
                    table_epa_photo_driver_side_vehicle.setVisibility(View.VISIBLE);
                }
            }
        });

//inside radiobutton  click


        radio_inside_emmission_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    str_radio_inside_emmission_yes_no = "yes";
                } else {
                    str_radio_inside_emmission_yes_no = "no";
                }
            }
        });

        radio_inside_emmission_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    str_radio_inside_emmission_yes_no = "no";
                } else {
                    str_radio_inside_emmission_yes_no = "yes";
                }
            }
        });

        radio_inside_vin_plate_missing_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    str_radio_inside_vin_plate_missing_yes_no = "yes";
                } else {
                    str_radio_inside_vin_plate_missing_yes_no = "no";
                }
            }
        });

        radio_inside_vin_plate_missing_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    str_radio_inside_vin_plate_missing_yes_no = "no";
                } else {
                    str_radio_inside_vin_plate_missing_yes_no = "yes";
                }
            }
        });


      /*  button_scan_vin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_txt_vin_num.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Fill Vin Number Firstly", Toast.LENGTH_LONG).show();
                    return;
                }
                String sha1_var = edit_txt_vin_num.getText().toString().trim();
                vin_no = sha1_var;
            *//*    AddVehicalFragment_New.GETApiCall getApiCall = new AddVehicalFragment_New.GETApiCall();
                getApiCall.execute();*//*
                // getVinNumber(sha1_var);
               // new GetVinNumberFromServer().execute();
            }
        });*/

        layout_recall_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status_vechile_information) {
                    if (linear_neste_second_layout.getVisibility() == View.VISIBLE) {
                        linear_neste_second_layout.setVisibility(View.GONE);
                        neste_second.setVisibility(View.GONE);
                        neste_second.post(new Runnable() {
                            public void run() {
                                neste_second.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (linear_neste_third.getVisibility() == View.VISIBLE) {
                        linear_neste_third.setVisibility(View.GONE);
                        neste_third.setVisibility(View.GONE);
                        neste_third.post(new Runnable() {
                            public void run() {
                                neste_third.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });

                    }
                    if (layout_epa_nested.getVisibility() == View.VISIBLE) {
                        layout_epa_nested.setVisibility(View.GONE);
                        neste_fourth.setVisibility(View.GONE);
                        neste_fourth.post(new Runnable() {
                            public void run() {
                                neste_fourth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_nested_inside.getVisibility() == View.VISIBLE) {
                        layout_nested_inside.setVisibility(View.GONE);
                        inside_fifth.setVisibility(View.GONE);
                        inside_fifth.post(new Runnable() {
                            public void run() {
                                inside_fifth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_speedo_six.getVisibility() == View.VISIBLE) {
                        layout_speedo_six.setVisibility(View.GONE);
                        speedo_six.setVisibility(View.GONE);
                        speedo_six.post(new Runnable() {
                            public void run() {
                                speedo_six.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }

                    linear_nest_first_layout.setVisibility(View.VISIBLE);
                    layout_recall_doc.setBackgroundResource(R.color.image_back_color);
                    text_vehicle_info.setTextColor(Color.WHITE);
                    img_vehicle_info_right.setBackgroundResource(R.mipmap.icon_down);
                    img_vehicle_info_right.setMaxWidth(50);
                    img_vehicle_info_right.setMaxHeight(50);
                    main_scroll.fullScroll(View.FOCUS_DOWN);
                    neste_first.setVisibility(View.VISIBLE);
                    main_scroll.post(new Runnable() {
                        public void run() {
                            main_scroll.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }

            }
        });

        layout_manufacture_lable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status_vechile_weaight) {
                    if (linear_nest_first_layout.getVisibility() == View.VISIBLE) {
                        linear_nest_first_layout.setVisibility(View.GONE);
                        neste_first.setVisibility(View.GONE);
                        neste_first.post(new Runnable() {
                            public void run() {
                                neste_first.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (linear_neste_third.getVisibility() == View.VISIBLE) {
                        linear_neste_third.setVisibility(View.GONE);
                        neste_third.setVisibility(View.GONE);
                        neste_third.post(new Runnable() {
                            public void run() {
                                neste_third.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });

                    }
                    if (layout_epa_nested.getVisibility() == View.VISIBLE) {
                        layout_epa_nested.setVisibility(View.GONE);
                        neste_fourth.setVisibility(View.GONE);
                        neste_fourth.post(new Runnable() {
                            public void run() {
                                neste_fourth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_nested_inside.getVisibility() == View.VISIBLE) {
                        layout_nested_inside.setVisibility(View.GONE);
                        inside_fifth.setVisibility(View.GONE);
                        inside_fifth.post(new Runnable() {
                            public void run() {
                                inside_fifth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_speedo_six.getVisibility() == View.VISIBLE) {
                        layout_speedo_six.setVisibility(View.GONE);
                        speedo_six.setVisibility(View.GONE);
                        speedo_six.post(new Runnable() {
                            public void run() {
                                speedo_six.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    linear_neste_second_layout.setVisibility(View.VISIBLE);
                    layout_vehicle_weight.setBackgroundResource(R.color.image_back_color);
                    text_vehicle_weight.setTextColor(Color.WHITE);
                    main_scroll.fullScroll(View.FOCUS_DOWN);
                    neste_second.setVisibility(View.VISIBLE);
                    main_scroll.post(new Runnable() {
                        public void run() {
                            main_scroll.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            }
        });

        layout_open_hood_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status_tires) {

                    if (linear_nest_first_layout.getVisibility() == View.VISIBLE) {
                        linear_nest_first_layout.setVisibility(View.GONE);
                        neste_first.setVisibility(View.GONE);
                        neste_first.post(new Runnable() {
                            public void run() {
                                neste_first.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (linear_neste_second_layout.getVisibility() == View.VISIBLE) {
                        linear_neste_second_layout.setVisibility(View.GONE);
                        neste_second.setVisibility(View.GONE);
                        neste_second.post(new Runnable() {
                            public void run() {
                                neste_second.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });

                    }
                    if (layout_epa_nested.getVisibility() == View.VISIBLE) {
                        layout_epa_nested.setVisibility(View.GONE);
                        neste_fourth.setVisibility(View.GONE);
                        neste_fourth.post(new Runnable() {
                            public void run() {
                                neste_fourth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_nested_inside.getVisibility() == View.VISIBLE) {
                        layout_nested_inside.setVisibility(View.GONE);
                        inside_fifth.setVisibility(View.GONE);
                        inside_fifth.post(new Runnable() {
                            public void run() {
                                inside_fifth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_speedo_six.getVisibility() == View.VISIBLE) {
                        layout_speedo_six.setVisibility(View.GONE);
                        speedo_six.setVisibility(View.GONE);
                        speedo_six.post(new Runnable() {
                            public void run() {
                                speedo_six.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    linear_neste_second_layout.setVisibility(View.VISIBLE);
                    layout_vehicle_weight.setBackgroundResource(R.color.image_back_color);
                    text_vehicle_weight.setTextColor(Color.WHITE);
                    main_scroll.fullScroll(View.FOCUS_DOWN);
                    neste_second.setVisibility(View.VISIBLE);
                    main_scroll.post(new Runnable() {
                        public void run() {
                            main_scroll.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });

                    linear_neste_third.setVisibility(View.VISIBLE);
                    layout_tires.setBackgroundResource(R.color.image_back_color);
                    text_tires.setTextColor(Color.WHITE);
                    main_scroll.fullScroll(View.FOCUS_DOWN);
                    neste_third.setVisibility(View.VISIBLE);
                    main_scroll.post(new Runnable() {
                        public void run() {
                            main_scroll.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            }
        });


        layout_vichel_speed_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status_epa) {
                    if (linear_nest_first_layout.getVisibility() == View.VISIBLE) {
                        linear_nest_first_layout.setVisibility(View.GONE);
                        neste_first.setVisibility(View.GONE);
                        neste_first.post(new Runnable() {
                            public void run() {
                                neste_first.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (linear_neste_second_layout.getVisibility() == View.VISIBLE) {
                        linear_neste_second_layout.setVisibility(View.GONE);
                        neste_second.setVisibility(View.GONE);
                        neste_second.post(new Runnable() {
                            public void run() {
                                neste_second.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });

                    }
                    if (linear_neste_third.getVisibility() == View.VISIBLE) {
                        linear_neste_third.setVisibility(View.GONE);
                        neste_third.setVisibility(View.GONE);
                        neste_third.post(new Runnable() {
                            public void run() {
                                neste_third.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_nested_inside.getVisibility() == View.VISIBLE) {
                        layout_nested_inside.setVisibility(View.GONE);
                        inside_fifth.setVisibility(View.GONE);
                        inside_fifth.post(new Runnable() {
                            public void run() {
                                inside_fifth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_speedo_six.getVisibility() == View.VISIBLE) {
                        layout_speedo_six.setVisibility(View.GONE);
                        speedo_six.setVisibility(View.GONE);
                        speedo_six.post(new Runnable() {
                            public void run() {
                                speedo_six.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }


                    layout_epa_nested.setVisibility(View.INVISIBLE);
                    layout_epa.setBackgroundResource(R.color.image_back_color);
                    text_epa.setTextColor(Color.WHITE);

                    main_scroll.fullScroll(View.FOCUS_DOWN);
                    neste_fourth.setVisibility(View.VISIBLE);
                    main_scroll.post(new Runnable() {
                        public void run() {
                            main_scroll.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                }
            }
        });


        layout_inside_photo_and_outher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status_inside_photo) {
                    if (linear_nest_first_layout.getVisibility() == View.VISIBLE) {
                        linear_nest_first_layout.setVisibility(View.GONE);
                        neste_first.setVisibility(View.GONE);
                        neste_first.post(new Runnable() {
                            public void run() {
                                neste_first.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (linear_neste_second_layout.getVisibility() == View.VISIBLE) {
                        linear_neste_second_layout.setVisibility(View.GONE);
                        neste_second.setVisibility(View.GONE);
                        neste_second.post(new Runnable() {
                            public void run() {
                                neste_second.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });

                    }
                    if (linear_neste_third.getVisibility() == View.VISIBLE) {
                        linear_neste_third.setVisibility(View.GONE);
                        neste_third.setVisibility(View.GONE);
                        neste_third.post(new Runnable() {
                            public void run() {
                                neste_third.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_epa_nested.getVisibility() == View.VISIBLE) {
                        layout_epa_nested.setVisibility(View.GONE);
                        neste_fourth.setVisibility(View.GONE);
                        neste_fourth.post(new Runnable() {
                            public void run() {
                                neste_fourth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_speedo_six.getVisibility() == View.VISIBLE) {
                        layout_speedo_six.setVisibility(View.GONE);
                        speedo_six.setVisibility(View.GONE);
                        speedo_six.post(new Runnable() {
                            public void run() {
                                speedo_six.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }

                    layout_nested_inside.setVisibility(View.VISIBLE);
                    main_scroll.fullScroll(View.FOCUS_DOWN);
                    inside_fifth.setVisibility(View.VISIBLE);
                    inside_fifth.post(new Runnable() {
                        public void run() {
                            inside_fifth.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        });


        layout_speedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status_speedo) {
                    if (linear_nest_first_layout.getVisibility() == View.VISIBLE) {
                        linear_nest_first_layout.setVisibility(View.GONE);
                        neste_first.setVisibility(View.GONE);
                        neste_first.post(new Runnable() {
                            public void run() {
                                neste_first.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (linear_neste_second_layout.getVisibility() == View.VISIBLE) {
                        linear_neste_second_layout.setVisibility(View.GONE);
                        neste_second.setVisibility(View.GONE);
                        neste_second.post(new Runnable() {
                            public void run() {
                                neste_second.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });

                    }
                    if (linear_neste_third.getVisibility() == View.VISIBLE) {
                        linear_neste_third.setVisibility(View.GONE);
                        neste_third.setVisibility(View.GONE);
                        neste_third.post(new Runnable() {
                            public void run() {
                                neste_third.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_epa_nested.getVisibility() == View.VISIBLE) {
                        layout_epa_nested.setVisibility(View.GONE);
                        neste_fourth.setVisibility(View.GONE);
                        neste_fourth.post(new Runnable() {
                            public void run() {
                                neste_fourth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }
                    if (layout_nested_inside.getVisibility() == View.VISIBLE) {
                        layout_nested_inside.setVisibility(View.GONE);
                        inside_fifth.setVisibility(View.GONE);
                        inside_fifth.post(new Runnable() {
                            public void run() {
                                inside_fifth.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    }

                    layout_speedo_six.setVisibility(View.VISIBLE);
                    main_scroll.fullScroll(View.FOCUS_DOWN);
                    speedo_six.setVisibility(View.VISIBLE);
                    speedo_six.post(new Runnable() {
                        public void run() {
                            speedo_six.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        });


        btn_save_vehcle_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showNext();


            }
        });

        btn_tire_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThird();
            }
        });
        btn_epa_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFourth();
            }
        });
        btn_inside_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFifth();
            }
        });


        btn_speedo_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSix();
            }
        });
        new GetVinNumberFromServer().execute();
        setStartWithVinSpinner();
        getBuyerList();
        return mainView;
    }


    private void getAllValues() {
        if (edit_txt_make.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_txt_make.requestFocus();
            return;
        } else if (edit_txt_year.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_txt_year.requestFocus();
            return;
        } else if (edit_txt_model.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            return;
        } else if (edit_txt_v_type.getText().toString().isEmpty()) {
            edit_txt_v_type.requestFocus();
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            return;
        }
      // edit_txt_vin_num1 = edit_txt_vin_num.getText().toString();
        edit_txt_make1 = edit_txt_make.getText().toString();
        edit_txt_year1 = edit_txt_year.getText().toString();
        edit_txt_model1 = edit_txt_model.getText().toString();
        edit_txt_v_type1 = edit_txt_v_type.getText().toString();
        if (edit_txt_selling_price.getText().toString().trim() != null || !edit_txt_selling_price.getText().toString().trim().equals("")) {
            edit_txt_selling_price1 = edit_txt_selling_price.getText().toString();
        } else {
            edit_txt_selling_price1 = "";
        }
        spinner_vin_satrt1 = spinner_vin_satrt.getSelectedItem().toString();
        spinner_buyer1 = buyerListDtos.get(spinner_buyer.getSelectedItemPosition()).getId();
        dataComplete.onDataComplete(1, "");

        new AddVehicalFragment_New.UploadFileToServer().execute();
    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(getActivity());
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
            if (status_vechile_information) {
                httppost = new HttpPost(CommonURL.URL + "/api/update_firststepdata");
            } else {
                httppost = new HttpPost(CommonURL.URL + "/api/add_car");
            }


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);
                // carimage,registrationfile, bill_copy, recalldoc,  buyer, price, recallfree, scanvin, swith, make, year, model, vid
                // Adding file data to http body
                if (output1 != null) {
                    entity.addPart("carimage", new FileBody(output1));
                } else {
                    entity.addPart("carimage", new FileBody(sourceFile));
                }
                if (output2 != null) {
                    entity.addPart("registrationfile", new FileBody(output2));
                } else {
                    entity.addPart("registrationfile", new FileBody(sourceFile));
                }
                if (output3 != null) {
                    entity.addPart("bill_copy", new FileBody(output3));
                } else {
                    entity.addPart("bill_copy", new FileBody(sourceFile));
                }
                if (output4 != null) {
                    entity.addPart("recalldoc", new FileBody(output4));
                } else {
                    entity.addPart("recalldoc", new FileBody(sourceFile));
                }


                // Extra parameters if you want to pass to server
                entity.addPart("seller",
                        new StringBody(seller_id));
                entity.addPart("buyer", new StringBody(spinner_buyer1));

                entity.addPart("price",
                        new StringBody(edit_txt_selling_price1));

                entity.addPart("appid", new StringBody(CommonURL.APP_ID));

                entity.addPart("recallfree",
                        new StringBody(edit_txt_selling_price1));

                entity.addPart("scanvin",
                        new StringBody(new_Vin_number));

                entity.addPart("swith", new StringBody(spinner_vin_satrt1));

                entity.addPart("make",
                        new StringBody(edit_txt_make1));

                entity.addPart("year", new StringBody(edit_txt_year1));

                entity.addPart("model",
                        new StringBody(edit_txt_model1));

                entity.addPart("vtype", new StringBody(edit_txt_v_type1));

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
                        status_vechile_information = true;
                        Toast.makeText(getActivity(), "vehicle Information are successfully saved", Toast.LENGTH_LONG).show();
                        vid = jsonObject.optString("vid");
                        checckBackPress = true;
                        // filePath=null;
                        radio_lbs.setChecked(true);
                        LoginDB.setVehicleFlag(getActivity(), "1", true);
                        if (linear_nest_first_layout.getVisibility() == View.VISIBLE) {
                            linear_nest_first_layout.setVisibility(View.GONE);
                            neste_first.setVisibility(View.GONE);
                            neste_first.post(new Runnable() {
                                public void run() {
                                    neste_first.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });

                        }
                        linear_neste_second_layout.setVisibility(View.VISIBLE);
                        main_scroll.fullScroll(View.FOCUS_DOWN);
                        neste_second.setVisibility(View.VISIBLE);
                        neste_second.post(new Runnable() {
                            public void run() {
                                neste_second.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                        //  showNext();

                        //  dsfdgfdbg
                     /*  Intent intent=new Intent(getActivity(), AddVehicalAndPayment2.class);
                        intent.putExtra("vid",vid);
                        getActivity().startActivity(intent);*/
                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }


    private void showNext() {
        if (edit_lbs_gvwr.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_lbs_gvwr.requestFocus();
            return;
        } else if (edit_front_gawr_lbs.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_front_gawr_lbs.requestFocus();
            return;
        } else if (edit_rear_gwar_kgs.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_rear_gwar_kgs.requestFocus();
            return;
        } else if (edit_front_gawr.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_front_gawr.requestFocus();
            return;
        } else {
            if (radio_lbs.isChecked()) {
                isvechicle_recall_free = "lbs";
            } else {
                isvechicle_recall_free = "kgs";
            }

            vechicle_weight_lbs_gvwr = edit_front_gawr_lbs.getText().toString().trim();
            vechicle_weight_front_gawr_lbs = edit_lbs_gvwr.getText().toString().trim();
            vechicle_weightrear_gwar_kgs = edit_rear_gwar_kgs.getText().toString().trim();
            vechicle_weightfront_gawr = edit_front_gawr.getText().toString().trim();
            new UploadFileToServerForSecond().execute();
        }
    }

    private void showThird() {

        if (edit_front_tire_size.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_front_tire_size.requestFocus();
            return;
        } else if (edit_rim_size.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_rim_size.requestFocus();
            return;
        } else if (edit_front_tire_presser.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_front_tire_presser.requestFocus();
            return;
        } else if (edit_rear_tire_size.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_rear_tire_size.requestFocus();
            return;
        } else if (edit_rear_rim_size.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_rear_rim_size.requestFocus();
            return;
        } else if (edit_rear_tire_pressure.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_rear_tire_pressure.requestFocus();
            return;
        } else {
            if (tire_radio_yes.isChecked()) {
                str_tire_lable_missing_or_damege_yes_no = "yes";
            } else {
                str_tire_lable_missing_or_damege_yes_no = "no";
            }

            str_edit_front_tire_size = edit_front_tire_size.getText().toString().trim();
            str_tire_edit_rim_size = edit_rim_size.getText().toString().trim();
            str_edit_front_tire_presser = edit_front_tire_presser.getText().toString().trim();
            str_edit_rear_tire_size = edit_rear_tire_size.getText().toString().trim();
            str_edit_rear_rim_size = edit_rear_rim_size.getText().toString().trim();
            str_edit_rear_tire_pressure = edit_rear_tire_pressure.getText().toString().trim();
            new UploadFileToServerForThird_Tire().execute();
        }
    }

    private void showFourth() {
        if (radio_epa_yes.isChecked()) {
            str_radio_epa_yes = "yes";
        } else {
            str_radio_epa_yes = "no";
        }
        new UploadFileToServerForFourth_EPA().execute();

    }

    private void showFifth() {
        if (radio_epa_yes.isChecked()) {
            str_radio_epa_yes = "yes";
        } else {
            str_radio_epa_yes = "no";
        }
        new UploadFileToServerFor_Fifth_Inside().execute();

    }

    private void showSix() {

        if (radio_speedo_km.isChecked()) {
            str_radio_speedo_km = "KM";
        }


        if (radio_speedo_miles.isChecked()) {
            str_radio_speedo_miles = "MILES";
        }

        if (radio_km_to_miles_yes.isChecked()) {
            str_radio_km_to_miles_yes = "Yes Km to Miles";
        }

        if (radio_km_to_miles_no.isChecked()) {
            str_radio_km_to_miles_no = "No Already in Miles";
        }
        if (radio_no_need_km_sticker.isChecked()) {
            str_radio_no_need_km_sticker = "No need km sticker";
        }
        if (edit_km_on_vehicle_speedo.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the field", Toast.LENGTH_LONG).show();
            edit_km_on_vehicle_speedo.requestFocus();
            return;
        }
        str_edit_km_on_vehicle_speedo = edit_km_on_vehicle_speedo.getText().toString().trim();

        new UploadFileToServerFor_Six_Speedo().execute();

    }

    private class UploadFileToServerForSecond extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(getActivity());
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


            if (status_vechile_weaight) {
                httppost = new HttpPost(CommonURL.URL + "/api/update_secondstepdata");
            } else {
                httppost = new HttpPost(CommonURL.URL + "/api/add_carsecondstep");
            }


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                //    File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("appid", new StringBody(CommonURL.APP_ID));
                entity.addPart("vid", new StringBody(vid));
                entity.addPart("weight_on_manufacture", new StringBody(isvechicle_recall_free));
                entity.addPart("gvwr", new StringBody(vechicle_weight_lbs_gvwr));
                entity.addPart("gvwrinlbs", new StringBody(vechicle_weight_front_gawr_lbs));

                // Extra parameters if you want to pass to server
                entity.addPart("frontgawr",
                        new StringBody(vechicle_weightfront_gawr));
                entity.addPart("reargawr", new StringBody(vechicle_weightrear_gwar_kgs));

                entity.addPart("reargawrkgs",
                        new StringBody(vechicle_weightrear_gwar_kgs));


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
                        Toast.makeText(getActivity(), "vehicle Information are successfully saved", Toast.LENGTH_LONG).show();
                        status_vechile_weaight = true;

                        if (linear_neste_second_layout.getVisibility() == View.VISIBLE) {
                            linear_neste_second_layout.setVisibility(View.GONE);
                            neste_second.setVisibility(View.GONE);
                            neste_second.post(new Runnable() {
                                public void run() {
                                    neste_second.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });

                        }
                        linear_neste_third.setVisibility(View.VISIBLE);
                        main_scroll.fullScroll(View.FOCUS_DOWN);
                        neste_third.setVisibility(View.VISIBLE);
                        neste_third.post(new Runnable() {
                            public void run() {
                                neste_third.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });


                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }


    private class UploadFileToServerForThird_Tire extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(getActivity());
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
            if (status_tires) {
                httppost = new HttpPost(CommonURL.URL + "/api/update_thirdstepdata");
            } else {
                httppost = new HttpPost(CommonURL.URL + "/api/add_carthirdstep");
            }


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("appid", new StringBody(CommonURL.APP_ID));
                File sourceFile = new File(filePath);
                // Adding file data to http body
                if (output5 != null) {
                    entity.addPart("tirelableimg", new FileBody(output5));
                } else {
                    entity.addPart("tirelableimg", new FileBody(sourceFile));
                }

                entity.addPart("tirelablemissing", new StringBody(str_tire_lable_missing_or_damege_yes_no));
                entity.addPart("fronttiresize", new StringBody(str_edit_front_tire_size));
                entity.addPart("sizeno", new StringBody(str_tire_edit_rim_size));
                // Extra parameters if you want to pass to server
                entity.addPart("fronttirepressure",
                        new StringBody(str_edit_front_tire_presser));
                entity.addPart("reartiresize", new StringBody(str_edit_rear_tire_size));
                entity.addPart("rearrimsize",
                        new StringBody(str_edit_rear_rim_size));
                entity.addPart("reartirepressure",
                        new StringBody(str_edit_rear_tire_pressure));
                entity.addPart("vid",
                        new StringBody(vid));

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
                        Toast.makeText(getActivity(), "vehicle Information are successfully saved", Toast.LENGTH_LONG).show();

                        status_tires = true;
                        if (linear_neste_third.getVisibility() == View.VISIBLE) {
                            linear_neste_third.setVisibility(View.GONE);
                            neste_third.setVisibility(View.GONE);
                            neste_third.post(new Runnable() {
                                public void run() {
                                    neste_third.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });

                        }
                        layout_epa_nested.setVisibility(View.VISIBLE);
                        main_scroll.fullScroll(View.FOCUS_DOWN);
                        neste_fourth.setVisibility(View.VISIBLE);
                        neste_fourth.post(new Runnable() {
                            public void run() {
                                neste_fourth.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });


                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }

    private class UploadFileToServerForFourth_EPA extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(getActivity());
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

            if (status_epa) {
                httppost = new HttpPost(CommonURL.URL + "/api/update_fourthstepdata");
            } else {
                httppost = new HttpPost(CommonURL.URL + "/api/add_carfourthstep");
            }


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                entity.addPart("appid", new StringBody(CommonURL.APP_ID));
                File sourceFile = new File(filePath);
                // Adding file data to http body
                if (output6 != null) {
                    entity.addPart("frontdriverimg", new FileBody(output6));
                } else {
                    entity.addPart("frontdriverimg", new FileBody(sourceFile));
                }

                entity.addPart("epaarea", new StringBody(str_radio_epa_yes));
                entity.addPart("vid",
                        new StringBody(vid));

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
                        Toast.makeText(getActivity(), "vehicle Information are successfully saved", Toast.LENGTH_LONG).show();
                        status_epa = true;

                        if (layout_epa_nested.getVisibility() == View.VISIBLE) {
                            layout_epa_nested.setVisibility(View.GONE);
                            neste_fourth.setVisibility(View.GONE);
                            neste_fourth.post(new Runnable() {
                                public void run() {
                                    neste_fourth.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });
                        }
                        layout_nested_inside.setVisibility(View.VISIBLE);
                        main_scroll.fullScroll(View.FOCUS_DOWN);
                        inside_fifth.setVisibility(View.VISIBLE);
                        inside_fifth.post(new Runnable() {
                            public void run() {
                                inside_fifth.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });


                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }


    private class UploadFileToServerFor_Fifth_Inside extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(getActivity());
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

            if (status_inside_photo) {
                httppost = new HttpPost(CommonURL.URL + "/api/update_fifthstepdata");
            } else {
                httppost = new HttpPost(CommonURL.URL + "/api/add_carfivestep");
            }


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("appid", new StringBody(CommonURL.APP_ID));
                File sourceFile = new File(filePath);
                // Adding file data to http body
                if (output7 != null) {
                    entity.addPart("manufacturedoc", new FileBody(output7));
                } else {
                    entity.addPart("manufacturedoc", new FileBody(sourceFile));
                }
                if (output8 != null) {
                    entity.addPart("vinplatedoc", new FileBody(output8));
                } else {
                    entity.addPart("vinplatedoc", new FileBody(sourceFile));
                }

                entity.addPart("vid",
                        new StringBody(vid));
                if (output9 != null) {
                    entity.addPart("rearpassangerdoc", new FileBody(output9));
                } else {
                    entity.addPart("rearpassangerdoc", new FileBody(sourceFile));
                }

                if (output10 != null) {
                    entity.addPart("passengerseatbeltdoc", new FileBody(output10));
                } else {
                    entity.addPart("passengerseatbeltdoc", new FileBody(sourceFile));
                }
                if (output11 != null) {
                    entity.addPart("passengerairbagdoc", new FileBody(output11));
                } else {
                    entity.addPart("passengerairbagdoc", new FileBody(sourceFile));
                }

                if (output12 != null) {
                    entity.addPart("driverseatbeltdoc", new FileBody(output12));
                } else {
                    entity.addPart("driverseatbeltdoc", new FileBody(sourceFile));
                }


                entity.addPart("driverairbagdoc", new FileBody(sourceFile));
                entity.addPart("vinplatemissing", new FileBody(sourceFile));
                entity.addPart("manufacturedoc", new FileBody(sourceFile));
                entity.addPart("stock",
                        new StringBody(str_radio_inside_emmission_yes_no));
                entity.addPart("missinglable",
                        new StringBody(str_radio_inside_vin_plate_missing_yes_no));


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
                        Toast.makeText(getActivity(), "vehicle Information are successfully saved", Toast.LENGTH_LONG).show();
                        status_inside_photo = true;

                        if (layout_nested_inside.getVisibility() == View.VISIBLE) {
                            layout_nested_inside.setVisibility(View.GONE);
                            inside_fifth.setVisibility(View.GONE);
                            inside_fifth.post(new Runnable() {
                                public void run() {
                                    inside_fifth.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });
                        }
                        layout_speedo_six.setVisibility(View.VISIBLE);
                        main_scroll.fullScroll(View.FOCUS_DOWN);
                        speedo_six.setVisibility(View.VISIBLE);
                        speedo_six.post(new Runnable() {
                            public void run() {
                                speedo_six.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });


                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }


    private class UploadFileToServerFor_Six_Speedo extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(getActivity());
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
            if (status_speedo) {
                httppost = new HttpPost(CommonURL.URL + "/api/update_speedo");
            } else {
                httppost = new HttpPost(CommonURL.URL + "/api/add_speedo");
            }


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("appid", new StringBody(CommonURL.APP_ID));
                File sourceFile = new File(filePath);
                // Adding file data to http body
                entity.addPart("speedo", new StringBody(str_radio_speedo_km));
                entity.addPart("speedoconverter", new StringBody(str_radio_speedo_km));
                entity.addPart("vid",
                        new StringBody(vid));
                entity.addPart("kmonvehicle", new StringBody(str_radio_speedo_km));
                entity.addPart("speedoimg", new FileBody(sourceFile));
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
                        Toast.makeText(getActivity(), "vehicle Information are successfully saved", Toast.LENGTH_LONG).show();
                        status_speedo = true;

                        if (layout_speedo_six.getVisibility() == View.VISIBLE) {
                            layout_speedo_six.setVisibility(View.GONE);
                            speedo_six.setVisibility(View.GONE);
                            speedo_six.post(new Runnable() {
                                public void run() {
                                    speedo_six.fullScroll(ScrollView.FOCUS_UP);
                                }
                            });
                        }

                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }

    private class GetVinNumberFromServer extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(getActivity());
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
            httppost = new HttpPost(CommonURL.URL+"/api/get_make");


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                //    appid, vid, weight_on_manufacture, gvwr, gvwrinlbs, frontgawr, reargawr, reargawrkgs

                //     File sourceFile = new File(filePath);

                // Adding file data to http body

             /*   entity.addPart("registrationfile", new FileBody(sourceFile));
                entity.addPart("bill_copy", new FileBody(sourceFile));
                entity.addPart("recalldoc", new FileBody(sourceFile));*/

                // Extra parameters if you want to pass to server

                    entity.addPart("vin",
                            new StringBody(new_Vin_number));


              /*  entity.addPart("vid", new StringBody(spinner_buyer1));

                entity.addPart("weight_on_manufacture",
                        new StringBody(edit_txt_selling_price1));

                entity.addPart("appid", new StringBody(CommonURL.APP_ID));

                entity.addPart("recallfree",
                        new StringBody(edit_txt_selling_price1));

                entity.addPart("scanvin",
                        new StringBody(edit_txt_vin_num1));

                entity.addPart("swith", new StringBody(spinner_vin_satrt1));

                entity.addPart("make",
                        new StringBody(edit_txt_make1));

                entity.addPart("year", new StringBody(edit_txt_year1));

                entity.addPart("model",
                        new StringBody(edit_txt_model1));

                entity.addPart("vtype", new StringBody(edit_txt_v_type1));*/

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
                if (jsonObject.has("error")) {
                    Toast.makeText(getActivity(), "No data Found please try another vin", Toast.LENGTH_SHORT).show();

                } else {
                    String make = jsonObject.optString("Make");
                    String ModelYear = jsonObject.optString("ModelYear");
                    String Model = jsonObject.optString("Model");
                    String ProductType = jsonObject.optString("ProductType");
                    String imgaeurl = jsonObject.optString("imgaeurl");
                    if(make!=null&&!make.equalsIgnoreCase("null")){
                       setValue(new_Vin_number, make, Model, ModelYear, ProductType);
                    }else {
                        Toast.makeText(getActivity(),"No Data Avilable Of this VIN Number",Toast.LENGTH_LONG).show();
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }

    private void setStartWithVinSpinner() {
        //  spinner_vin_satrt.setOnItemSelectedListener();


        List<String> categories1 = new ArrayList<String>();
        categories1.add("1,3,5");
        categories1.add("2,4");
        categories1.add("J,W");


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories1);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_vin_satrt.setAdapter(dataAdapter1);
    }


    public void setBuyerSpinner() {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i = 0; i < buyerListDtos.size(); i++) {
            categories.add(buyerListDtos.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_buyer.setAdapter(dataAdapter);
    }


  /*  // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("sss", "sds");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && scanFlag) {
            scanFlag = false;
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                edit_txt_vin_num.setText("" + result.getContents());
            }
        } else if (resultCode == Activity.RESULT_OK) {


        }
    }*/


    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.GET_BUYER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if (aMasterDataDtos.getResponseCode() == 1) {
                buyerListDtos = aMasterDataDtos.getBuyerlist();
                setBuyerSpinner();
            } else {
                Toast.makeText(getActivity(), aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
        }
    }


    private void getBuyerList() {
        AsyncGetTask asyncGetTask = new AsyncGetTask(getActivity(), CallType.GET_BUYER_LIST, AddVehicalFragment_New.this, true, null);
        asyncGetTask.execute();
    }

 /*   private void getVinNumber(final String vin_code) {
        String sha1 = "";
        String apikey = "8ba90e6aa631";
        String secretkey = "262ce290f9";
        //vin_code="1GCRKPEA0CZ160567";
        try {

            //Vin Number 1FMCU0D73BKC34466   2FMTK4J84FBB36055
            //Api Key $apikey = "8ba90e6aa631";
            //Secrete Key $secretkey = "262ce290f9";
            //Sha1 Code 33292a4aa0
            //https://api.vindecoder.eu/2.0/8ba90e6aa631/33292a4aa0/decode/1FMCU0D73BKC34466.json
            sha1 = AeSimpleSHA1.SHA1(vin_code + "|" + apikey + "|" + secretkey);
            sha1 = sha1.substring(0, 10);
            Log.e("SubString", sha1);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        *//*getVehicalDto = new GetVehicalDto();
        getVehicalDto.setVinno(vin_code);
        getVehicalDto.setAppid(CommonURL.APP_ID);*//*
        VINAPISetter vinapiSetter = new VINAPISetter();
        vinapiSetter.setApikey(apikey);
        vinapiSetter.setDecode("decode");
        vinapiSetter.setVin_code(vin_code + ".json");
        vinapiSetter.setSha1(sha1);
      *//*  asyncGetTask = new AsyncGetTask(AddVehicalAndPayment.this, CallType.GET_VIN_DETAILS_API_SERVER, AddVehicalAndPayment.this, true, vinapiSetter);
        asyncGetTask.execute(apikey,sha1,"decode",vin_code+".json");*//*
        AddVehicalFragment_New.GETApiCall getApiCall = new AddVehicalFragment_New.GETApiCall();
       // String api_link = CommonURL.VIN_API_LINK + "/" + apikey + "/" + sha1 + "/" + "decode/" + vin_code + ".json";
        getApiCall.execute();

    }*/

  /*  private class GETApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

        //    String urlString = params[0]; // URL to call
            String result = "";
            // HTTP Get
            try {
                URL url = new URL("http://192.168.0.105/quickcross/api/get_make");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                if (null != inputStream) {
                    result = IOUtils.toString(inputStream);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //EditText dynCount = (EditText)findViewById(R.id.dynamicCountEdit);
            //dynCount.setText(result + " records were found");
            pd.dismiss();
            // Log.i("FromOnPostExecute", result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("error")) {
                    Toast.makeText(getActivity(), "No data Found please try another vin", Toast.LENGTH_SHORT).show();

                } else {
                    String make = jsonObject.optString("Make");
                    String ModelYear = jsonObject.optString("ModelYear");
                    String Model = jsonObject.optString("Model");
                    String ProductType = jsonObject.optString("ProductType");
                    String imgaeurl = jsonObject.optString("imgaeurl");
                    setValue(vin_number, make, Model, ModelYear, ProductType);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }*/


    public void setValue(String vin_num1, String make1, String model1, String year1, String v_type1) {
        // Required empty public constructor
        //edit_txt_vin_num.setText(vin_num1);
        linear_nest_first_layout.setVisibility(View.VISIBLE);
        layout_recall_doc.setBackgroundResource(R.color.image_back_color);
        layout_recall_doc.setPadding(10, 10, 10, 10);
        text_vehicle_info.setTextColor(Color.WHITE);
        main_scroll.fullScroll(View.FOCUS_DOWN);
        neste_first.setVisibility(View.VISIBLE);
        main_scroll.post(new Runnable() {
            public void run() {
                main_scroll.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        edit_scan_vin.setText(vin_num1);
        edit_txt_make.setText(make1);
        edit_txt_year.setText(year1);
        edit_txt_model.setText(model1);
        edit_txt_v_type.setText(v_type1);

        radio_btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_btn_yes.isSelected()) {
                    table_copy_of_recall_doc.setVisibility(View.VISIBLE);
                } else {
                    table_copy_of_recall_doc.setVisibility(View.GONE);
                }

            }
        });
        radio_btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radio_btn_no.isSelected()) {
                    table_copy_of_recall_doc.setVisibility(View.GONE);
                } else {
                    table_copy_of_recall_doc.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_save_recall_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getAllValues();

            }
        });
        // getBuyerList();

    }

    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(DialogInterface.OnClickListener activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((AddVehicalAndPayment2) activity, permission[0])) {
            Toast.makeText((AddVehicalAndPayment2) activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions((AddVehicalAndPayment2) activity, permission, requestCode);
    }

    public static void requestPermission(Fragment fragment, String[] permission, int requestCode) {
        fragment.requestPermissions(permission, requestCode);
    }


    private void handleCameraForPickingPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getActivity())) {
                startDilog();
            } else {
                requestPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ACESS_STORAGE);
            }
        } else {
            startDilog();
        }
    }

    private void startDilog() {
        AlertDialog.Builder myAlertDilog = new AlertDialog.Builder(getActivity());
        myAlertDilog.setTitle("Upload picture option..");
        myAlertDilog.setMessage("Take Photo");
        myAlertDilog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               /* Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                picIntent.setType("image*//*");
                picIntent.putExtra("return_data", true);
                startActivityForResult(picIntent, GALLERY_REQUEST);*/
                getImageFromGallary();
            }
        });
        myAlertDilog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission(Manifest.permission.CAMERA, getActivity())) {
                        //openCameraApplication();
                        getImage();
                    } else {
                        requestPermission(this, new String[]{Manifest.permission.CAMERA}, REQUEST_ACESS_CAMERA);
                    }
                } else {
                    getImage();
                    //openCameraApplication();
                }
            }
        });
        myAlertDilog.show();
    }


    private void openCameraApplication() {
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(picIntent, CAMERA_REQUEST);
        }
    }


/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    uri = data.getData();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;


                    try {
                        // BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                        options.inSampleSize = calculateInSampleSize(options, 100, 100);
                        options.inJustDecodeBounds = false;
                        Bitmap image = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);

                        if (check_img_status == 1) {
                            image_copy_of_recall_doc.setImageBitmap(image);
                            // output1=saveBitmap(image,picturePath);
                            // output1 = new File(picturePath);

                        } else if (check_img_status == 2) {
                            image_upload_carimage.setImageBitmap(image);
                            // output2 = new File(picturePath);

                        } else if (check_img_status == 3) {
                            image_upload_copy_of_registration.setImageBitmap(image);
                            //  output3 = new File(picturePath);
                        } else if (check_img_status == 4) {
                            image_copy_of_bill_salle.setImageBitmap(image);
                            //  output4 = new File(picturePath);
                        } else if (check_img_status == 5) {
                            image_tire_lable.setImageBitmap(image);
                            //   output5 = new File(picturePath);
                        } else if (check_img_status == 6) {
                            image_epa_front_driver_side.setImageBitmap(image);
                            // output6 = new File(picturePath);
                        } else if (check_img_status == 7) {
                            image_photo_of_manufacture_label.setImageBitmap(image);
                            // output7 = new File(picturePath);
                        } else if (check_img_status == 8) {
                            image_photo_of_vin_plate.setImageBitmap(image);
                            //  output8 = new File(picturePath);
                        } else if (check_img_status == 9) {
                            image_photo_of_seat_belt.setImageBitmap(image);
                        } else if (check_img_status == 10) {
                            image_photo_of_Passanger_side_air_bag.setImageBitmap(image);
                        } else if (check_img_status == 11) {
                            image_photo_of_driver_seat_belt.setImageBitmap(image);
                        } else if (check_img_status == 12) {
                            image_photo_of_driver_air_bag.setImageBitmap(image);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    //uri = getImageUri(getActivity(), bitmap);
                    // File finalFile = new File(getRealPathFromUri(uri));
                    //imageViewphoto_of_bill_sale.setImageBitmap(bitmap);
                    if (check_img_status == 1) {
                        image_copy_of_recall_doc.setImageBitmap(bitmap);
                    } else if (check_img_status == 2) {
                        image_upload_carimage.setImageBitmap(bitmap);

                    } else if (check_img_status == 3) {
                        image_upload_copy_of_registration.setImageBitmap(bitmap);
                    } else if (check_img_status == 4) {
                        image_copy_of_bill_salle.setImageBitmap(bitmap);
                    } else if (check_img_status == 5) {
                        image_tire_lable.setImageBitmap(bitmap);
                    } else if (check_img_status == 6) {
                        image_epa_front_driver_side.setImageBitmap(bitmap);
                    } else if (check_img_status == 7) {
                        image_photo_of_manufacture_label.setImageBitmap(bitmap);
                    } else if (check_img_status == 8) {
                        image_photo_of_vin_plate.setImageBitmap(bitmap);
                    } else if (check_img_status == 9) {
                        image_photo_of_seat_belt.setImageBitmap(bitmap);
                    } else if (check_img_status == 10) {
                        image_photo_of_Passanger_side_air_bag.setImageBitmap(bitmap);
                    } else if (check_img_status == 11) {
                        image_photo_of_driver_seat_belt.setImageBitmap(bitmap);
                    } else if (check_img_status == 12) {
                        image_photo_of_driver_air_bag.setImageBitmap(bitmap);
                    }
                } else if (data.getExtras() == null) {

                    Toast.makeText(getActivity(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());
                    //imageViewphoto_of_bill_sale.setImageDrawable(thumbnail);
                    if (check_img_status == 1) {
                        image_copy_of_recall_doc.setImageDrawable(thumbnail);
                    } else if (check_img_status == 2) {
                        image_upload_carimage.setImageDrawable(thumbnail);
                    } else if (check_img_status == 3) {
                        image_upload_copy_of_registration.setImageDrawable(thumbnail);
                    } else if (check_img_status == 4) {
                        image_copy_of_bill_salle.setImageDrawable(thumbnail);
                    } else if (check_img_status == 5) {
                        image_tire_lable.setImageDrawable(thumbnail);
                    } else if (check_img_status == 6) {
                        image_epa_front_driver_side.setImageDrawable(thumbnail);
                    } else if (check_img_status == 7) {
                        image_photo_of_manufacture_label.setImageDrawable(thumbnail);
                    } else if (check_img_status == 8) {
                        image_photo_of_vin_plate.setImageDrawable(thumbnail);
                    } else if (check_img_status == 9) {
                        image_photo_of_seat_belt.setImageDrawable(thumbnail);
                    } else if (check_img_status == 10) {
                        image_photo_of_Passanger_side_air_bag.setImageDrawable(thumbnail);
                    } else if (check_img_status == 11) {
                        image_photo_of_driver_seat_belt.setImageDrawable(thumbnail);
                    } else if (check_img_status == 12) {
                        image_photo_of_driver_air_bag.setImageDrawable(thumbnail);
                    }

                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }*/


    private File saveBitmap(Bitmap bitmap, String path) {
        File file = null;
        if (bitmap != null) {
            file = new File(path);
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
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


    public void getImage() {

        try {

            // create Intent to take a picture and return control to the calling application
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        /*    output = new File(dir, System.currentTimeMillis() + ".jpeg");
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
            startActivityForResult(i, CAMERA_CAPTURE);*/


            if (check_img_status == 1) {
                //  image_copy_of_recall_doc.setImageBitmap(image);
                // output1=saveBitmap(image,picturePath);

                output1 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output1));
                startActivityForResult(i, CAMERA_CAPTURE);
             /*   filePath = output1.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_copy_of_recall_doc.setImageBitmap(bitmap);*/

                //  output1 = output;

            } else if (check_img_status == 2) {

                output2 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output2));
                startActivityForResult(i, CAMERA_CAPTURE);
                //  output2 = output;

            } else if (check_img_status == 3) {
                output3 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output3));
                startActivityForResult(i, CAMERA_CAPTURE);

                // output3 = output;
                //  output3 = new File(picturePath);
            } else if (check_img_status == 4) {

                output4 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output4));
                startActivityForResult(i, CAMERA_CAPTURE);

                //   output4 = output;
                //  output4 = new File(picturePath);
            } else if (check_img_status == 5) {
                output5 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output5));
                startActivityForResult(i, CAMERA_CAPTURE);

                //  output5 = output;
                //   output5 = new File(picturePath);
            } else if (check_img_status == 6) {
                output6 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output6));
                startActivityForResult(i, CAMERA_CAPTURE);

                // output6 = output;
                // output6 = new File(picturePath);
            } else if (check_img_status == 7) {
                output7 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output7));
                startActivityForResult(i, CAMERA_CAPTURE);

                //  output7 = output;
                // output7 = new File(picturePath);
            } else if (check_img_status == 8) {
                output8 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output8));
                startActivityForResult(i, CAMERA_CAPTURE);

                // output8 = output;
                //  output8 = new File(picturePath);
            } else if (check_img_status == 9) {
                output9 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output9));
                startActivityForResult(i, CAMERA_CAPTURE);

                // output9 = output;
            } else if (check_img_status == 10) {
                output10 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output10));
                startActivityForResult(i, CAMERA_CAPTURE);

                // output10 = output;
            } else if (check_img_status == 11) {
                output11 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output11));
                startActivityForResult(i, CAMERA_CAPTURE);

                // output10 = output;
            }
            else if (check_img_status == 12) {
                output12 = new File(dir, System.currentTimeMillis() + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output12));
                startActivityForResult(i, CAMERA_CAPTURE);

                // output10 = output;
            }

            else {

            }


        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void getImageFromGallary() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLRY_IMAGE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
            // setImageFile(image_groupposition, image_childposition, output, image_status);

            //  uri = data.getData();

            if (check_img_status == 1) {
                filePath = output1.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_copy_of_recall_doc.setImageBitmap(bitmap);

                // image_copy_of_recall_doc.setImageBitmap(bitmap);
                // output1=saveBitmap(image,picturePath);
                // output1 = new File(picturePath);
            } else if (check_img_status == 2) {
                // image_upload_carimage.setImageBitmap(bitmap);
                filePath = output2.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_upload_carimage.setImageBitmap(bitmap);
                // output2 = new File(picturePath);
            } else if (check_img_status == 3) {

                filePath = output3.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_upload_copy_of_registration.setImageBitmap(bitmap);
                //  output3 = new File(picturePath);
            } else if (check_img_status == 4) {


                filePath = output4.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_copy_of_bill_salle.setImageBitmap(bitmap);
                //  output4 = new File(picturePath);
            } else if (check_img_status == 5) {

                filePath = output5.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_tire_lable.setImageBitmap(bitmap);
                //   output5 = new File(picturePath);
            } else if (check_img_status == 6) {

                filePath = output6.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_epa_front_driver_side.setImageBitmap(bitmap);
                // output6 = new File(picturePath);
            } else if (check_img_status == 7) {

                filePath = output7.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_photo_of_manufacture_label.setImageBitmap(bitmap);
                // output7 = new File(picturePath);
            } else if (check_img_status == 8) {
                filePath = output8.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_photo_of_vin_plate.setImageBitmap(bitmap);
                //  output8 = new File(picturePath);
            } else if (check_img_status == 9) {
                filePath = output9.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_photo_of_seat_belt.setImageBitmap(bitmap);
            } else if (check_img_status == 10) {

                filePath = output10.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_photo_of_Passanger_side_air_bag.setImageBitmap(bitmap);
            } else if (check_img_status == 11) {

                filePath = output11.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_photo_of_driver_seat_belt.setImageBitmap(bitmap);
            } else if (check_img_status == 12) {

                filePath = output12.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                image_photo_of_driver_air_bag.setImageBitmap(bitmap);
            }

        } else if (requestCode == GALLRY_IMAGE && resultCode == Activity.RESULT_OK) {


            if (data != null) {
                uri = data.getData();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor =
                        getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);

                // output = new File(picturePath);
                try {
                    // BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                    options.inSampleSize = calculateInSampleSize(options, 100, 100);
                    options.inJustDecodeBounds = false;
                    Bitmap image = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);

                    if (check_img_status == 1) {
                        image_copy_of_recall_doc.setImageBitmap(image);
                        // output1=saveBitmap(image,picturePath);
                        output1 = new File(picturePath);

                    } else if (check_img_status == 2) {
                        image_upload_carimage.setImageBitmap(image);
                        output2 = new File(picturePath);

                    } else if (check_img_status == 3) {
                        image_upload_copy_of_registration.setImageBitmap(image);
                        output3 = new File(picturePath);
                    } else if (check_img_status == 4) {
                        image_copy_of_bill_salle.setImageBitmap(image);
                        output4 = new File(picturePath);
                    } else if (check_img_status == 5) {
                        image_tire_lable.setImageBitmap(image);
                        output5 = new File(picturePath);
                    } else if (check_img_status == 6) {
                        image_epa_front_driver_side.setImageBitmap(image);
                        output6 = new File(picturePath);
                    } else if (check_img_status == 7) {
                        image_photo_of_manufacture_label.setImageBitmap(image);
                        output7 = new File(picturePath);
                    } else if (check_img_status == 8) {
                        image_photo_of_vin_plate.setImageBitmap(image);
                        output8 = new File(picturePath);
                    } else if (check_img_status == 9) {
                        image_photo_of_seat_belt.setImageBitmap(image);
                        output9 = new File(picturePath);
                    } else if (check_img_status == 10) {
                        image_photo_of_Passanger_side_air_bag.setImageBitmap(image);
                        output10 = new File(picturePath);
                    } else if (check_img_status == 11) {
                        image_photo_of_driver_seat_belt.setImageBitmap(image);
                        output11 = new File(picturePath);
                    } else if (check_img_status == 12) {
                        image_photo_of_driver_air_bag.setImageBitmap(image);
                        output11 = new File(picturePath);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode == getActivity().RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Cancelled",
                    Toast.LENGTH_SHORT).show();
        }


    }
}