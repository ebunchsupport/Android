package com.dvn.vindecoder.ui.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.adaptor.SellerVehicalAdaptor;
import com.dvn.vindecoder.adaptor.UserAssignedVehicalAdaptor;
import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.dto.UserAssingCarListDto;
import com.dvn.vindecoder.ui.LoginActivity;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPaymentWithoutScan;
import com.dvn.vindecoder.ui.seller.SelectBarCode_OR_InsertManually;
import com.dvn.vindecoder.ui.seller.SellCarInfo;
import com.dvn.vindecoder.ui.seller.SellerDetail;
import com.dvn.vindecoder.ui.seller.SignUpAsSellerUSER;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;
import com.dvn.vindecoder.util.UIUtility;
import com.dvn.vindecoder.util.Utils;

import java.util.HashMap;
import java.util.List;

public class UserDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,AsyncCompleteListener{
    private Button loginBtn;
    private TextView page_title;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private List<UserAssingCarListDto> vehicalList;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private  NavigationView navigationView=null;
    Toolbar toolbar;
    private View nav_header = null;
    private SwipeRefreshLayout swipeContainer;
    private TextView error_txt_view,error_txt_view1;
    private com.melnykov.fab.FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        page_title=(TextView)findViewById(R.id.page_title);
        page_title.setText("User Detail");
        ImageView home_btn=(ImageView)findViewById(R.id.home_btn);
        home_btn.setVisibility(View.GONE);
        initFun();
        fab = (com.melnykov.fab.FloatingActionButton)findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
       fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        initViews();
        setSwipeHandler();
        getVehicalList();
        setError_txt_view();
    }

    private void setError_txt_view()
    {
        error_txt_view=(TextView)findViewById(R.id.error_txt_view);
        error_txt_view1=(TextView)findViewById(R.id.error_txt_view1);
        recyclerView.setVisibility(View.GONE);
    }
    private void initFun() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
//           getActionBar().setBackgroundDrawable(getResources().);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        nav_header = navigationView.getHeaderView(0);
        HashMap<String, String> values = Utils.getUserPrefrens(this);
        TextView textName = (TextView) nav_header.findViewById(R.id.txt_user_name);
        TextView textemail = (TextView) nav_header.findViewById(R.id.txt_user_email);

        textName.setText(values.get("name"));
        textemail.setText(values.get("email"));
        }
    private void setSwipeHandler()
    {
        swipeContainer=(SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,
                android.R.color.holo_orange_light,android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVehicalList();
            }
        });

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                selectDateDalog();

            /*    Intent intent1 = new Intent(UserDetail.this,SelectBarCode_OR_InsertManually.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);*/
               // animateFAB();
                break;
            case R.id.fab1:

                animateFAB();
                Intent intent = new Intent(UserDetail.this,SignUpAsSellerUSER.class);
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
                selectDateDalog();
                break;
        }
    }




    public void selectDateDalog() {

        final Dialog dialog = new Dialog(UserDetail.this);
        dialog.setTitle("Next Services Date");
        dialog.setContentView(R.layout.dailog_select_option_barcode_imsertmannually);
        dialog.setCancelable(false);
        final Button bar_code_btn = (Button) dialog.findViewById(R.id.bar_code_btn);
        final Button insert_manually_btn = (Button) dialog.findViewById(R.id.insert_manually_btn);
        final ImageView clost_img_btn = (ImageView) dialog.findViewById(R.id.clost_img_btn);

bar_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserDetail.this,AddVehicalAndPayment.class);
                intent.putExtra("boolean",true);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
            }
        });

        insert_manually_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(UserDetail.this,AddVehicalAndPaymentWithoutScan.class);
                intent.putExtra("boolean",false);
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


    public void animateFAB(){

        if(isFabOpen){

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
            Log.d("palash","open");

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
                Intent intent=new Intent(UserDetail.this,GetAllVehicalDetails.class);
                    intent.putExtra("v_id",vehicalList.get(position).getId());
                    startActivity(intent);
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
    public void onBackPressed()
    {
        super.onBackPressed();
     //   UIUtility.deleteCache(UserDetail.this);
    }

    private void getVehicalList() {
        String value= LoginDB.getTitle(UserDetail.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(value);
        getVehicalDto.setStatus("0");
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UserDetail.this, CallType.GET_USER_VEHICAL_ASSIGN_LIST, UserDetail.this, true, getVehicalDto);
        asyncGetTask.execute();
    }

    private void getUserVehicalList(final String value) {
        /*String value= LoginDB.getTitle(SellerDetail.this,"value");
        Log.e("value",value);*/
        // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UserDetail.this, SellCarInfo.class);
        intent.putExtra("vin_id",value);
        intent.putExtra("screen_pos",2);
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
        if (type == CallType.GET_USER_VEHICAL_ASSIGN_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                if(adapter!=null)
                {
                    vehicalList.clear();
                    adapter.notifyDataSetChanged();
                    vehicalList.addAll(aMasterDataDtos.getAssingcarlist());
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                    error_txt_view.setVisibility(View.GONE);
                    error_txt_view1.setVisibility(View.GONE);

                    recyclerView.setVisibility(View.VISIBLE);
                }
                else{
                    vehicalList=aMasterDataDtos.getAssingcarlist();
                    adapter = new UserAssignedVehicalAdaptor(vehicalList,UserDetail.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if(swipeContainer.isRefreshing())
                    {
                        swipeContainer.setRefreshing(false);
                    }
                    error_txt_view.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
                if(swipeContainer.isRefreshing())
                {
                    swipeContainer.setRefreshing(false);
                }
                error_txt_view.setVisibility(View.VISIBLE);
                error_txt_view1.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
            if(swipeContainer.isRefreshing())
            {
                swipeContainer.setRefreshing(false);
            }
            error_txt_view1.setVisibility(View.VISIBLE);
            error_txt_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.complete_car:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                Intent s_intent = new Intent(UserDetail.this, CompleteCarList.class);
                startActivity(s_intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case R.id.rejected_car:
                Intent r_intent = new Intent(UserDetail.this, RejectedCarList.class);
                startActivity(r_intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case R.id.process_car:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                Intent p_intent = new Intent(UserDetail.this, ProcessCarList.class);
                startActivity(p_intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;

            case R.id.logout:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetail.this);
                builder.setTitle(getString(R.string.app_name));
                builder.setMessage("Are you sure you want to logout");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UserDetail.this);
                        preferences.edit().clear().commit();

                        LoginDB.setLoginFlag(UserDetail.this, false);
                        Intent i = new Intent(UserDetail.this, LoginActivity.class);
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
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.rateBtn:
                launchMarket();
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
            Toast.makeText(this, "unable to find market app", Toast.LENGTH_LONG).show();
        }
    }




    /*String text = "";
 for (Model model : mModelList) {
   if (model.isSelected()) {
     text += model.getText();
   }
 }
Log.d("TAG","Output : " + text);
*/
}
