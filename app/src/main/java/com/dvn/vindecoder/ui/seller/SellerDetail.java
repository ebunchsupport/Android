package com.dvn.vindecoder.ui.seller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.adaptor.SellerVehicalAdaptor;
import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.dto.MainBean;
import com.dvn.vindecoder.dto.RecentListResponsModel;
import com.dvn.vindecoder.dto.SellerVehicalListDto;
import com.dvn.vindecoder.ui.LoginActivity;
import com.dvn.vindecoder.ui.SignUpAsSeller;
import com.dvn.vindecoder.ui.user.GetAllVehicalDetails;
import com.dvn.vindecoder.ui.user.UserDetail;
import com.dvn.vindecoder.util.AndroidMultiPartEntity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.DBHelper;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;
import com.dvn.vindecoder.util.UIUtility;
import com.dvn.vindecoder.util.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SellerDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AsyncCompleteListener {
    private Button loginBtn;
    private TextView page_title;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private List<SellerVehicalListDto> vehicalList;
    private List<RecentListResponsModel> listResponsModels;

    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private NavigationView navigationView = null;
    Toolbar toolbar;
    private View nav_header = null;
    private SwipeRefreshLayout swipeContainer;
    private TextView error_txt_view, error_txt_view1;
    private com.melnykov.fab.FloatingActionButton fab;
    DBHelper mydb;
    private long totalSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mydb = new DBHelper(SellerDetail.this);
        page_title = (TextView) findViewById(R.id.page_title);
        page_title.setText("Seller Detail");
        ImageView home_btn = (ImageView) findViewById(R.id.home_btn);
       // home_btn.setVisibility(View.GONE);
        initFun();
        initViews();
        setSwipeHandler();
        getVehicalList();
                /*fab = (FloatingActionButton)findViewById(R.id.fab);*/
        fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.attachToRecyclerView(recyclerView);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        home_btn.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
    }

    private void setSwipeHandler() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVehicalList();
            }
        });
        setError_txt_view();

    }

    private void setError_txt_view() {
        error_txt_view = (TextView) findViewById(R.id.error_txt_view);
        error_txt_view1 = (TextView) findViewById(R.id.error_txt_view1);
        recyclerView.setVisibility(View.GONE);
    }

    private void initFun() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START);
//           getActionBar().setBackgroundDrawable(getResources().);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        nav_header = navigationView.getHeaderView(0);
        HashMap<String, String> values = Utils.getUserPrefrens(this);
        TextView textName = (TextView) nav_header.findViewById(R.id.txt_user_name);
        TextView textemail = (TextView) nav_header.findViewById(R.id.txt_user_email);
        new GetRecentListToServer().execute();
        textName.setText(values.get("name"));
        textemail.setText(values.get("email"));



      /*  final Menu menu = navigationView.getMenu();


        final SubMenu subMenu = menu.addSubMenu("RECENT ITEM");
        for (int i = 1; i <= listResponsModels.size(); i++) {
            if(i<=5){
                subMenu.add(i,i,i,listResponsModels.get(i).getMake()+listResponsModels.get(i).getModel());
            }

            if(i==6){
                subMenu.add(i,i,i,"Logout");
            }
        }*/

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.home_btn:
                Intent intent1 = new Intent(SellerDetail.this, BarcodeScanner.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
             //   selectDateDalog();
                break;
            case R.id.fab:
                selectDateDalog();
              /*  Intent intent1 = new Intent(SellerDetail.this,SelectBarCode_OR_InsertManually.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);*/
                // animateFAB();
                break;
            case R.id.fab1:

                animateFAB();
                Intent intent = new Intent(SellerDetail.this, SignUpAsSellerUSER.class);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                Log.d("palash", "Fab 1");
                break;
            case R.id.fab2:
              /*  animateFAB();
                Intent intent1 = new Intent(SellerDetail.this,SelectBarCode_OR_InsertManually.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                Log.d("palash", "Fab 2");*/
                break;
        }
    }


    public void selectDateDalog() {

        final Dialog dialog = new Dialog(SellerDetail.this);
        dialog.setTitle("Add Vehical");
        dialog.setContentView(R.layout.dailog_select_option_barcode_imsertmannually);
        dialog.setCancelable(false);
        final Button bar_code_btn = (Button) dialog.findViewById(R.id.bar_code_btn);
        final Button insert_manually_btn = (Button) dialog.findViewById(R.id.insert_manually_btn);
        final ImageView clost_img_btn = (ImageView) dialog.findViewById(R.id.clost_img_btn);

        bar_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerDetail.this, BarcodeScanner.class);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
              /*  Intent intent = new Intent(SellerDetail.this, AddVehicalAndPayment.class);
                intent.putExtra("boolean", true);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);*/
            }
        });

        insert_manually_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerDetail.this, AddVehicalAndPaymentWithoutScan.class);
                intent.putExtra("boolean", false);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
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

    public void animateFAB() {

        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("palash", "close");
        } else {
            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("palash", "open");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    private void initViews() {

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    //  Toast.makeText(getApplicationContext(),""+ vehicalList.get(position).getVinno(), Toast.LENGTH_SHORT).show();
                    getUserVehicalList(vehicalList.get(position).getId(), vehicalList.get(position).getStatus());

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        UIUtility.deleteCache(SellerDetail.this);
    }

    private void getVehicalList() {
        String value = LoginDB.getTitle(SellerDetail.this, "value");
        Log.e("value", value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(SellerDetail.this, CallType.GET_VEHICAL_LIST, SellerDetail.this, true, getVehicalDto);
        asyncGetTask.execute();

    }

    private void getUserVehicalList(final String value, String assign_user) {
        /*String value= LoginDB.getTitle(SellerDetail.this,"value");
        Log.e("value",value);*/
        // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SellerDetail.this, SellCarInfo.class);
        intent.putExtra("vin_id", value);
        intent.putExtra("screen_pos", 1);
        int var = Integer.parseInt(assign_user);
        if (var == 0) {
            intent.putExtra("assign_user", assign_user);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);

       /* getVehicalDto = new GetVehicalDto();
        getVehicalDto.setId(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(SellerDetail.this, CallType.GET_USER_VEHICAL_LIST, SellerDetail.this, true, getVehicalDto);
        asyncGetTask.execute();*/
    }

    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {

        if (type == CallType.GET_VEHICAL_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if (aMasterDataDtos.getResponseCode() == 1) {
                if (adapter != null) {
                    vehicalList.clear();
                    adapter.notifyDataSetChanged();
                    vehicalList.addAll(aMasterDataDtos.getSellerdata());
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                    error_txt_view.setVisibility(View.GONE);
                    error_txt_view1.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    vehicalList = aMasterDataDtos.getSellerdata();
                    adapter = new SellerVehicalAdaptor(vehicalList, SellerDetail.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                    error_txt_view.setVisibility(View.GONE);
                    error_txt_view1.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {

                Toast.makeText(this, aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();

                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                error_txt_view.setVisibility(View.VISIBLE);
                error_txt_view1.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show();

            if (swipeContainer.isRefreshing()) {
                swipeContainer.setRefreshing(false);
            }
            error_txt_view.setVisibility(View.VISIBLE);
            error_txt_view1.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
       // int idd="6";
        switch (item.getItemId()) {
            case R.id.add_user:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                Intent s_intent = new Intent(SellerDetail.this, SignUpAsSellerUSER.class);
                startActivity(s_intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case R.id.user:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SellerDetail.this, UserSellerList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case 1:
                Intent intent1 = new Intent(SellerDetail.this, SellCarInfo.class);
                intent1.putExtra("vin_id", listResponsModels.get(0).getVehicle_id());
                intent1.putExtra("screen_pos", 1);
                int var = Integer.parseInt(listResponsModels.get(0).getStatus());
                if (var == 0) {
                    intent1.putExtra("assign_user", listResponsModels.get(0).getStatus());
                }
                startActivity(intent1);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case 2:
                Intent intent2 = new Intent(SellerDetail.this, SellCarInfo.class);
                intent2.putExtra("vin_id", listResponsModels.get(1).getVehicle_id());
                intent2.putExtra("screen_pos", 1);
                int var1 = Integer.parseInt(listResponsModels.get(1).getStatus());
                if (var1 == 0) {
                    intent2.putExtra("assign_user", listResponsModels.get(1).getStatus());
                }
                startActivity(intent2);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case 3:
                Intent intent3 = new Intent(SellerDetail.this, SellCarInfo.class);
                intent3.putExtra("vin_id", listResponsModels.get(2).getVehicle_id());
                intent3.putExtra("screen_pos", 1);
                int var2 = Integer.parseInt(listResponsModels.get(2).getStatus());
                if (var2 == 0) {
                    intent3.putExtra("assign_user", listResponsModels.get(2).getStatus());
                }
                startActivity(intent3);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case 4:
                Intent intent4 = new Intent(SellerDetail.this, SellCarInfo.class);
                intent4.putExtra("vin_id", listResponsModels.get(3).getVehicle_id());
                intent4.putExtra("screen_pos", 1);
                int var3 = Integer.parseInt(listResponsModels.get(3).getStatus());
                if (var3 == 0) {
                    intent4.putExtra("assign_user", listResponsModels.get(3).getStatus());
                }
                startActivity(intent4);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case 5:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(SellerDetail.this);
                builder.setTitle(getString(R.string.app_name));
                builder.setMessage("Are you sure you want to logout");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SellerDetail.this);
                        preferences.edit().clear().commit();

                        LoginDB.setLoginFlag(SellerDetail.this, false);
                        Intent i = new Intent(SellerDetail.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.on_back_in, R.anim.on_back_out);

                        //  finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.show();
                TextView messageText = (TextView) alertDialog.findViewById(android.R.id.message);
                messageText.setTextSize(14);
                messageText.setGravity(Gravity.CENTER_VERTICAL);
                break;


            case R.id.shareBtn:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.rateBtn:
                launchMarket();
                break;

            case R.id.add_un_assigned_car:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                Intent s_intent1 = new Intent(SellerDetail.this, UnAssignedCarList.class);
                startActivity(s_intent1);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            default:

                break;
        }
        return true;
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }



    private class GetRecentListToServer extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = new ProgressDialog(SellerDetail.this);
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

            httppost = new HttpPost(CommonURL.URL + "/api/get_latestcar");


            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                String user_id = LoginDB.getTitle(SellerDetail.this, "value");
                entity.addPart("appid", new StringBody(CommonURL.APP_ID));
                // Adding file data to http body
                entity.addPart("userid", new StringBody(user_id));

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
                        MainBean mainBean = new Gson().fromJson(result, MainBean.class);
                        listResponsModels=mainBean.getVehicledetails();
                        final Menu menu = navigationView.getMenu();
                        final SubMenu subMenu = menu.addSubMenu(1,1,1,"RECENT ITEM");

                       // menu.getItem("title_addby").setIcon(R.mipmap.icon_logout);
                        for (int i = 1; i <= listResponsModels.size(); i++) {
                            if(i<=4){
                                subMenu.add(i,i,i,listResponsModels.get(i).getMake()+listResponsModels.get(i).getModel()+listResponsModels.get(i).getVehicle_type());
                              //  subMenu.setHeaderIcon(R.mipmap.icon_assign_car);
                               // menu.getItem(1).setIcon(R.mipmap.icon_logout);
                               subMenu.findItem(i).setIcon(R.mipmap.icon_right_tick);
                            }

                            if(i==5){
                                subMenu.add(i,i,i,"Logout");
                                subMenu.findItem(i).setIcon(R.mipmap.icon_logout);
                            }
                        }

                    } else {
                        if (jsonObject.has("message")) {
                            Toast.makeText(SellerDetail.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(SellerDetail.this, result, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }

}
