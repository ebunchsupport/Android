package com.dvn.vindecoder.ui.seller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.DecodeDto;
import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.dto.SellerVehicalDetailDto;
import com.dvn.vindecoder.fragment.AddNewVehicalFragment;
import com.dvn.vindecoder.fragment.AddNewVehicalFragmentWithouScan;
import com.dvn.vindecoder.fragment.AddNewVehicalSecond;
import com.dvn.vindecoder.interface_package.DataComplete;
import com.dvn.vindecoder.ui.BaseActivity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.PostResponse;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
@SuppressLint("ParcelCreator")
public class AddVehicalAndPayment2 extends BaseActivity implements AsyncCompleteListener,DataComplete{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean flagVar;
    private GetVehicalDto getVehicalDto;
    private AddNewVehicalSecond addNewVehicalFragment;
    private AsyncGetTask asyncGetTask=null;
    private SellerVehicalDetailDto vehicledetails=null;
    private ArrayList<DecodeDto>decodeDtoList=new ArrayList<DecodeDto>();
    private PostResponse postResponse;
    public String vin_number,make,model,year,v_type;
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE=3;
    private static final int REQUEST_ACESS_CAMERA=2;
    private Uri uri;
    CircleImageView car_image;
    private String image_path=null;
    private CollapsingToolbarLayout collapsing_toolbar;
    private String v_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView( R.layout.activity_add_vehical_and_payment);
        drawChildLayout();
        setTitle("Add Vehical");
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            v_id=bundle.getString("vid");
        }
        if (savedInstanceState == null) {
            // Do first time initialization -- add initial fragment.
            addNewVehicalFragment = AddNewVehicalSecond.newInstance(v_id);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment1, addNewVehicalFragment).commit();
        }
        collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);

    }
    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_add_vehical_and_payment2),
                layoutParams);
        car_image=(CircleImageView)findViewById(R.id.car_image);

        car_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCameraForPickingPhoto();
            }
        });
    }

    @Override
    public void sendFbData() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFragment(addNewVehicalFragment, "Add New Vehical");
       // adapter.addFragment(new AddNewVehicalFragment(this), "Payment Details");
        // adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDataComplete(int position,String path) {
        collapsing_toolbar.setVisibility(View.GONE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private int currentPage;
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            //Log.e("fragment Position",":"+""+position);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Activity sss1", "sds");

    }

    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.GET_VIN_DETAILS_API_SERVER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

           /* if(aMasterDataDtos.getError())
            {
                //vehicledetails=aMasterDataDtos.getVehicledetails().get(0);
                Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();


            }*/
            //else
            {
                String model_year = "Model Year";
                for (int i=0;i<decodeDtoList.size();i++)
                {
                    if(decodeDtoList.get(i).getLabel().trim().contains(model_year))
                    {
                        Log.e("GER LAbel"+decodeDtoList.get(i).getLabel(),""+decodeDtoList.get(i).getValue());
                    }
                }
            }
        } else {
            Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = this.getContentResolver().query(tempUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private Uri getImageUri(AddDriverActivity yourActivity, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(yourActivity.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }



    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(AppCompatActivity activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
            Toast.makeText(activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    public static void requestPermission(Fragment fragment, String[] permission, int requestCode) {
        fragment.requestPermissions(permission, requestCode);
    }

    private void handleCameraForPickingPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
                startDilog();
            } else {
                requestPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ACESS_STORAGE);
            }
        } else {
            startDilog();
        }
    }

    private void startDilog() {
        AlertDialog.Builder myAlertDilog = new AlertDialog.Builder(AddVehicalAndPayment2.this);
        myAlertDilog.setTitle("Upload picture option..");
        myAlertDilog.setMessage("Take Photo");
        myAlertDilog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
                picIntent.setType("image/*");
                picIntent.putExtra("return_data",true);
                startActivityForResult(picIntent,GALLERY_REQUEST);
            }
        });
        myAlertDilog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkPermission(Manifest.permission.CAMERA,AddVehicalAndPayment2.this)){
                        openCameraApplication();
                    }else{
                        requestPermission(AddVehicalAndPayment2.this,new String[]{Manifest.permission.CAMERA},REQUEST_ACESS_CAMERA);
                    }
                }else{
                    openCameraApplication();
                }
            }
        });
        myAlertDilog.show();
    }

    private void openCameraApplication() {
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(picIntent, CAMERA_REQUEST);
        }
    }


    private Uri getImageUri(AddVehicalAndPayment2 yourActivity, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(yourActivity.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_ACESS_STORAGE && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            startDilog();
        }
        if(requestCode==REQUEST_ACESS_CAMERA && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            openCameraApplication();
        }
    }


}
