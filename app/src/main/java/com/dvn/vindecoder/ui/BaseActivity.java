package com.dvn.vindecoder.ui;

/**
 * Created by palash on 06-12-2016.
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.ui.seller.SellerDetail;
import com.dvn.vindecoder.ui.seller.SignUpAsSellerUSER;
import com.dvn.vindecoder.ui.seller.UnAssignedCarList;
import com.dvn.vindecoder.ui.seller.UserSellerList;
import com.dvn.vindecoder.util.LoginDB;


public abstract class  BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ProgressDialog mProgressDialog;
    private DisplayMetrics mDisplayMetrics;
    private LayoutInflater mLayoutInflater;
    protected int mScreenWidth, mScreenHeight;
    protected ViewStub mTitleBarLayout;
    protected ViewStub mTabBarLayout;
    protected ViewStub mChildStubLayout;
    protected ViewStub mTabBarAdddLayout;
    protected ViewStub mTabBarHomeLayout;
    protected LinearLayout mChildLayout,adContainer;
    int i=0;
    String uid;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    BroadcastReceiver receiver;
    IntentFilter filter;
    public  NavigationView navigationView=null;
    Toolbar toolbar;
    private View nav_header = null;
    private TextView page_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);
        //frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        page_title=(TextView)findViewById(R.id.page_title);
        createBaseTemplate();
        initialize1();

        alertDialogBuilder = new AlertDialog.Builder(BaseActivity.this);

        alertDialogBuilder.setTitle("Alert !");
        alertDialogBuilder.setMessage("There is no internet connection please Connect to Internet ").setCancelable(false);
         alertDialog = alertDialogBuilder.create();
         filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
      //  filter.addAction("android.location.PROVIDERS_CHANGED");

         receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //do something based on the intent's action
                checkInitiateGpsInternet(context);
            }
        };
        this.registerReceiver(receiver, filter);

        initFun();
    }
    protected View inflateChildLayout(int xmlLayoutId) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(this);
        }
        View v=(View)mLayoutInflater.inflate(xmlLayoutId, null);

        //aristocrat helvitica_bold
        return mLayoutInflater.inflate(xmlLayoutId, null);
    }
    private void initialize1() {

        mDisplayMetrics = this.getResources().getDisplayMetrics();

        mScreenWidth = getScreenWidth(mDisplayMetrics);
        mScreenHeight = getScreenHeight(mDisplayMetrics);

        mLayoutInflater = LayoutInflater.from(this);


    }
    private void createBaseTemplate() {

        mTitleBarLayout = ((ViewStub) findViewById(R.id.header_layout_stub));

        mChildStubLayout = ((ViewStub) findViewById(R.id.child_layout_stub));

        View inflatedView = mChildStubLayout.inflate();

        mChildLayout = (LinearLayout) inflatedView
                .findViewById(R.id.child_linearlayout);


    }
    private void initFun() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        ImageView home_btn=(ImageView)findViewById(R.id.home_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BaseActivity.this,SellerDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public void checkInitiateGpsInternet(Context context)
    {
        boolean gps=true;//checkGPS(context);
        boolean net=isOnline(context);

        if( net && gps)
        {
            if(alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        else{

            alertDialog.show();
        }
    }

    public boolean checkGPS(Context context)
    {
        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            //do something
            return true;
        }
        else
        {
            //do something else
            return false;
        }
    }
    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    @Override
    protected void onStop(){
        super.onStop();
        this.unregisterReceiver(receiver);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        this.registerReceiver(receiver, filter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.add_user:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                Intent s_intent = new Intent(BaseActivity.this, SignUpAsSellerUSER.class);
                startActivity(s_intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case R.id.user:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BaseActivity.this, UserSellerList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                break;
            case R.id.logout:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                LoginDB.setLoginFlag(BaseActivity.this, false);
                Intent i = new Intent(BaseActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.on_back_in, R.anim.on_back_out);
                break;
            case R.id.add_un_assigned_car:
                // Toast.makeText(this, "Clicked: Menu No. 3 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                Intent s_intent1 = new Intent(BaseActivity.this, UnAssignedCarList.class);
                startActivity(s_intent1);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
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

            case R.id.share_fb_item:
                sendFbData();
                break;
            default:


            break;
        }
        return true;
    }

    public int getScreenWidth(DisplayMetrics mDisplayMetrics) {
        int width = 0;
        if (mDisplayMetrics != null)
            width = mDisplayMetrics.widthPixels;

        return width;
    }

    public int getScreenHeight(DisplayMetrics mDisplayMetrics) {
        int height = 0;
        if (mDisplayMetrics != null)
            height = mDisplayMetrics.heightPixels;
        return height;
    }

    public abstract void drawChildLayout();



    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public abstract void sendFbData();

    public void setTitle(String title)
    {
        page_title.setText(title);
    }
}
