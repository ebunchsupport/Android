package com.dvn.vindecoder.ui.seller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.DecodeDto;
import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.dto.SellerVehicalDetailDto;
import com.dvn.vindecoder.dto.VINAPISetter;
import com.dvn.vindecoder.fragment.AddNewVehicalFragment;
import com.dvn.vindecoder.fragment.AddNewVehicalFragmentWithouScan;
import com.dvn.vindecoder.fragment.AddNewVehicalSecond;
import com.dvn.vindecoder.ui.BaseActivity;
import com.dvn.vindecoder.util.AeSimpleSHA1;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.PostResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import com.dvn.vindecoder.interface_package.DataComplete;
@SuppressLint("ParcelCreator")
public class AddVehicalAndPayment extends BaseActivity implements AsyncCompleteListener,DataComplete{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean flagVar;
    private GetVehicalDto getVehicalDto;
    private AddNewVehicalFragment addNewVehicalFragment;
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
    CircleImageView car_image1;
    private String image_path=null;
    Toolbar toolbar;
    private CollapsingToolbarLayout collapsing_toolbar;
    private int mStackLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView( R.layout.activity_add_vehical_and_payment);
        drawChildLayout();
        setTitle("Add Vehical");
        Bundle bundle=getIntent().getExtras();

      //  addNewVehicalFragment=new AddNewVehicalFragment(this);

        boolean scanFlag=false;
        if(bundle!=null)
        {
            scanFlag=bundle.getBoolean("boolean");
        }
        if (savedInstanceState == null) {
            // Do first time initialization -- add initial fragment.
            addNewVehicalFragment = AddNewVehicalFragment.newInstance(this,scanFlag);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment1, addNewVehicalFragment).commit();
        }
        else
        {
            mStackLevel = savedInstanceState.getInt("level");
        }
      collapsing_toolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
   /*   toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

    }
    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_add_vehical_and_payment),
                layoutParams);
        car_image=(CircleImageView)findViewById(R.id.car_image);
        car_image1=(CircleImageView)findViewById(R.id.car_image1);

        car_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCameraForPickingPhoto();
            }
        });
        car_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCameraForPickingPhoto();
            }
        });
        setScrollContent();
    }

    @Override
    public void sendFbData() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
       // adapter.addFragment(addNewVehicalFragment, "Add New Vehical");
        //adapter.addFragment(new AddNewVehicalSecond(), "Payment Details");
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
    public void onDataComplete(int position,String Image_path) {
        Bitmap bitmap = ((BitmapDrawable)car_image.getDrawable()).getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(getCacheDir(),"car.jpg");
        FileOutputStream fo;
        try {
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image_path=destination.getAbsolutePath();
       // car_image.setImageBitmap(bitmap);
        addNewVehicalFragment.setImagePath(image_path);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Activity sss1", "sds");
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    image_path=uri.getPath();
                    addNewVehicalFragment.setImagePath(image_path);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    try {
                        // BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                        options.inSampleSize =calculateInSampleSize(options, 100, 100);
                        options.inJustDecodeBounds = false;
                        Bitmap image = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                        // File mm=new File(image_path);
                        car_image.setImageBitmap(image);
                        car_image1.setImageBitmap(image);

                        Bitmap bitmap = ((BitmapDrawable)car_image.getDrawable()).getBitmap();
                       /* ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        File destination = new File(getCacheDir(),"car.jpg");
                        FileOutputStream fo;
                        try {
                            fo = new FileOutputStream(destination);
                            fo.write(bytes.toByteArray());
                            fo.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        image_path=destination.getAbsolutePath();
                       // car_image.setImageBitmap(bitmap);
                        addNewVehicalFragment.setImagePath(image_path);*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("data")) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File destination = new File(getCacheDir(),"car.jpg");
                    FileOutputStream fo;
                    try {
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image_path=destination.getAbsolutePath();
                          car_image.setImageBitmap(bitmap);
                    car_image1.setImageBitmap(bitmap);
                    addNewVehicalFragment.setImagePath(image_path);
                } else if (data.getExtras() == null) {

                    Toast.makeText(getApplicationContext(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());
                    //img_view_drivingLicense.setImageDrawable(thumbnail);
                       car_image.setImageDrawable(thumbnail);
                    car_image1.setImageDrawable(thumbnail);

                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null && AddNewVehicalFragment.scanFlag) {
                AddNewVehicalFragment.scanFlag = false;
                if (result.getContents() == null) {
                    finish();
                    Toast.makeText(AddVehicalAndPayment.this, "Cancelled", Toast.LENGTH_LONG).show();

                } else {
                    //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    //scan_txt.setText(""+result.getContents());
                    Log.e("result", result.getContents());
                    // getVinNumber(result.getContents());
                    vin_number = result.getContents();
                    getVinNumber(vin_number);

                }
            }
        }
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

    private void getVinNumber(final String vin_code) {
        String sha1="";
        String apikey = "8ba90e6aa631";
        String secretkey = "262ce290f9";
        //vin_code="1GCRKPEA0CZ160567";
        try {

            //Vin Number 1FMCU0D73BKC34466   2FMTK4J84FBB36055
            //Api Key $apikey = "8ba90e6aa631";
            //Secrete Key $secretkey = "262ce290f9";
            //Sha1 Code 33292a4aa0
            //https://api.vindecoder.eu/2.0/8ba90e6aa631/33292a4aa0/decode/1FMCU0D73BKC34466.json
             sha1= AeSimpleSHA1.SHA1(vin_code+"|"+apikey+"|"+secretkey);
            sha1=sha1.substring(0,10);
            Log.e("SubString",sha1);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*getVehicalDto = new GetVehicalDto();
        getVehicalDto.setVinno(vin_code);
        getVehicalDto.setAppid(CommonURL.APP_ID);*/
        VINAPISetter vinapiSetter=new VINAPISetter();
        vinapiSetter.setApikey(apikey);
                vinapiSetter.setDecode("decode");
        vinapiSetter.setVin_code(vin_code+".json");
                vinapiSetter.setSha1(sha1);
      /*  asyncGetTask = new AsyncGetTask(AddVehicalAndPayment.this, CallType.GET_VIN_DETAILS_API_SERVER, AddVehicalAndPayment.this, true, vinapiSetter);
        asyncGetTask.execute(apikey,sha1,"decode",vin_code+".json");*/
        GETApiCall getApiCall=new GETApiCall();
        String api_link=CommonURL.VIN_API_LINK+"/"+apikey+"/"+sha1+"/"+"decode/"+vin_code+".json";
        getApiCall.execute(api_link);

    }

    private class GETApiCall extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
                pd = new ProgressDialog(AddVehicalAndPayment.this);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();

            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {

            String urlString = params[0]; // URL to call
            String result = "";
            // HTTP Get
            try {
                URL url = new URL(urlString);
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
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.has("error")) {
                    Toast.makeText(AddVehicalAndPayment.this,"No data Found",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    String jsonObject1 = jsonObject.optString("decode");
                    JSONArray jsonArray = new JSONArray(jsonObject1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        DecodeDto decodeDtoList1 = new DecodeDto();
                        decodeDtoList1.setLabel(jsonObject2.optString("label"));
                        decodeDtoList1.setValue(jsonObject2.optString("value"));
                        decodeDtoList.add(decodeDtoList1);
                    }

                    String model_year = "Model Year";
                    String model1 = "Model";
                    String make1 = "Make";
                    String prod_type = "Product Type";
                    for (int i = 0; i < decodeDtoList.size(); i++) {
                        if (decodeDtoList.get(i).getLabel().trim().equalsIgnoreCase(model_year)) {
                           // Log.e("GER LAbel" + decodeDtoList.get(i).getLabel(), "" + decodeDtoList.get(i).getValue());
                            year=decodeDtoList.get(i).getValue();
                        } else if (decodeDtoList.get(i).getLabel().trim().equalsIgnoreCase(model1)) {
                           // Log.e("GER LAbel" + decodeDtoList.get(i).getLabel(), "" + decodeDtoList.get(i).getValue());
                            model=decodeDtoList.get(i).getValue();
                        } else if (decodeDtoList.get(i).getLabel().trim().equalsIgnoreCase(make1)) {
                           // Log.e("GER LAbel" + decodeDtoList.get(i).getLabel(), "" + decodeDtoList.get(i).getValue());
                            make=decodeDtoList.get(i).getValue();
                        } else if (decodeDtoList.get(i).getLabel().trim().equalsIgnoreCase(prod_type)) {
                            Log.e("GER LAbel" + decodeDtoList.get(i).getLabel(), "" + decodeDtoList.get(i).getValue());
                            v_type=decodeDtoList.get(i).getValue();
                        }

                    }

                    addNewVehicalFragment.setValue(vin_number,make,model,year,v_type);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
public void setViewPagerHeight()
{
    viewPager = new ViewPager(AddVehicalAndPayment.this) {
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            View view = getChildAt(this.getCurrentItem());
            if (view != null) {
                view.measure(widthMeasureSpec, heightMeasureSpec);
            }
            setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
        }

        private int measureHeight(int measureSpec, View view) {
            int result = 0;
            int specMode = MeasureSpec.getMode(measureSpec);
            int specSize = MeasureSpec.getSize(measureSpec);

            if (specMode == MeasureSpec.EXACTLY) {
                result = specSize;
            } else {
                // set the height from the base view if available
                if (view != null) {
                    result = view.getMeasuredHeight();
                }
                if (specMode == MeasureSpec.AT_MOST) {
                    result = Math.min(result, specSize);
                }
            }
            return result;
        }
    };
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
        AlertDialog.Builder myAlertDilog = new AlertDialog.Builder(AddVehicalAndPayment.this);
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
                    if(checkPermission(Manifest.permission.CAMERA,AddVehicalAndPayment.this)){
                        openCameraApplication();
                    }else{
                        requestPermission(AddVehicalAndPayment.this,new String[]{Manifest.permission.CAMERA},REQUEST_ACESS_CAMERA);
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


    private Uri getImageUri(AddVehicalAndPayment yourActivity, Bitmap bitmap) {
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=new Intent(this,SellerDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    private void setScrollContent()
    {

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        final RelativeLayout img_container1=(RelativeLayout)findViewById(R.id.img_container1);
        // load the animation
        final Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);
        // start the animation
        // set animation listener
        animFadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
           /* if(img_container1.getVisibility() == View.VISIBLE)
            {
                img_container1.setVisibility(View.GONE);
            }*/
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    // Log.e("val","-1  choti imge ko hide kerna h");
                    img_container1.setVisibility(View.GONE);
                }
                if (scrollRange + verticalOffset == 0) {
                    //  collapsingToolbarLayout.setTitle("Title");
                    // Log.e("val","0 choti imge ko show kerna h");
                    img_container1.setVisibility(View.VISIBLE);
                    img_container1.startAnimation(animFadein);
                    isShow = true;
                } else if(isShow) {
                    // collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    //Log.e("val","Showing choti imge ko hide kerna h");
                    img_container1.setVisibility(View.GONE);

                    isShow = false;
                }
            }
        });
    }
}
