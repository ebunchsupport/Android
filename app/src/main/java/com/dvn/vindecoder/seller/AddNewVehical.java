package com.dvn.vindecoder.seller;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dvn.vindecoder.R;
import com.dvn.vindecoder.gallery.GalleryActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddNewVehical extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView scan_code_imageView;
    private CropImageView imageView8,imageView2,imageView3;
    private EditText scan_txt;
    private Button btn_display_exp;
    private Bitmap globalBitmap;
    private File postedImageFile;
    private Uri mImageUri;
    private String globalFilePath;
    private boolean scanFlag;
    private CropImageView mCropImageView;
    private Uri mCropImageUri;
    private int image_count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vehical);
        //mCropImageView = (CropImageView)  findViewById(R.id.CropImageView);
        String[] ITEMS = {"Buyer1", "Buyer 2", "Buyer 3", "Buyer 4", "Buyer 5", "Buyer 6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
      //  Spinner spinner1 = (Spinner) findViewById(R.id.is_free_spinner3);


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        //spinner1.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList <String>();
        categories.add("Buyer");
        categories.add("Buyer1");
        categories.add("Buyer2");
        categories.add("Buyer3");
        categories.add("Buyer4");
        categories.add("Buyer5");
        categories.add("Buyer6");

        List<String> categories1 = new ArrayList <String>();
        categories1.add("Is Vehicle Recall Free");
        categories1.add("Yes");
        categories1.add("No");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        //spinner1.setAdapter(dataAdapter1);
        //setContent();
        dialougForBarCode();
    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();
        /*if(id1==R.id.spinner)
        {
            String item = parent.getItemAtPosition(position).toString();
        }
        if(id1==R.id.is_free_spinner3)
        {
            String item = parent.getItemAtPosition(position).toString();
        }
*/
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

    }
/*
    private void setContent()
    {
        imageView8=(CropImageView)findViewById(R.id.imageView8);
        imageView2=(CropImageView)findViewById(R.id.imageView2);
        imageView3=(CropImageView)findViewById(R.id.imageView3);
        scan_code_imageView=(ImageView)findViewById(R.id.scan_code_imageView);
        scan_txt=(EditText)findViewById(R.id.scan_txt);
        btn_display_exp=(Button)findViewById(R.id.btn_display_exp);
        btn_display_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewVehical.this,GalleryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
            }
        });

        scan_code_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanFlag=true;
                new IntentIntegrator(AddNewVehical.this).initiateScan();
                overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
            }
        });

        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_count=8;
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_count=2;
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_count=3;
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null && scanFlag) {
            scanFlag=false;
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                scan_txt.setText(""+result.getContents());
            }
        }
        else  if (resultCode == Activity.RESULT_OK) {
            Uri imageUri =  getPickImageResultUri(data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                if(image_count==8)
                {
                    imageView8.setImageUriAsync(imageUri);
                }
                else if(image_count==2)
                {
                    imageView2.setImageUriAsync(imageUri);
                }
                else if(image_count==3)
                {
                    imageView3.setImageUriAsync(imageUri);
                }
                //mCropImageView.setImageUriAsync(imageUri);
            }
        }
       /*else if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                onCaptureImageResult(data);
                //uploadDone();

            } else if (requestCode == 2 && resultCode == RESULT_OK
                    && null != data) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == 0 && resultCode == RESULT_OK
                    && null != data) {


            }
        }
         else {
            super.onActivityResult(requestCode, resultCode, data);
        }*/
//    }

    private void dialougForBarCode() {

        final CharSequence[] options = { "Bar Code Scan", "Insert Manually"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewVehical.this);
        builder.setTitle("Alert !");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Bar Code Scan"))
                {
                    scanFlag=true;
                    new IntentIntegrator(AddNewVehical.this).initiateScan();
                    overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);
                }
              /*  else if (options[item].equals("Choose from Gallery"))
                {   /*
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/
                  /*  Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                }*/
                else if (options[item].equals("Insert Manually")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewVehical.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    cameraIntent();
                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);*/
                }
                else if (options[item].equals("Choose from Gallery"))
                {   /*
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            final boolean isKitKat = Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN;
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            String filePath = "";
            if (isKitKat){
                filePath = getPath(AddNewVehical.this,selectedImage);
            }
            else{
                Cursor cursor = AddNewVehical.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            }


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath, options);
            globalBitmap = yourSelectedImage;
           // Glide.with(AddNewVehical.this).load(filePath).into(profile_image);
          /*  try {
                postedImageFile = saveBitMap(AddNewVehical.this, yourSelectedImage, "8c795ba3-0845-49fe-b6f3-f285cfd86259");
                // byteArrayBody=saveBitMap1(AddNewVehical.this, yourSelectedImage, "images");
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap;
        try {
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            bitmap = BitmapFactory.decodeFile(mImageUri.getPath(), options);

//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            bitmap = BitmapFactory.decodeFile(mImageUri.getPath(), bmOptions);
            postedImageFile = saveBitMap(AddNewVehical.this, bitmap,"palash1");
            //byteArrayBody=saveBitMap1(AddNewVehical.this, bitmap, "images");
//            bitmap.setImageBitmap(mImageBitmap);
//            Uri mImageUri2 = getImageUri2(context, postedImageFile);
            //profile_image.setImageBitmap(bitmap);
//            Glide.with(context).load(mImageUri2).into(imgUpload);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AddNewVehical.this, "Error while capturing", Toast.LENGTH_SHORT).show();

        }
    }


    private static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private File saveBitMap(Context context, Bitmap bmp,String imageName) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + context.getPackageName() + "/" + imageName + ".jpeg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }


    private void cameraIntent() {
        int RESULT_LOAD_IMG;

        RESULT_LOAD_IMG = 1;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File tempDir = null;
        try {
            String tempFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + AddNewVehical.this.getPackageName() + "/addedPost.jpeg";
            globalFilePath = tempFileName;
            tempDir = new File(tempFileName);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            tempDir.delete();
            tempDir.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AddNewVehical.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        mImageUri = getOutputMediaFileUri();
//        mImageUri = Uri.fromFile(tempDir);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        //start camera intent
        startActivityForResult(cameraIntent, RESULT_LOAD_IMG);
    }
    /**
     * Creating file uri to store image
     */
    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /*
     * returning image
     */
    private File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                AddNewVehical.this.getPackageName());

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(AddNewVehical.this.getPackageName(), "Oops! Failed create "
                        + AddNewVehical.this.getPackageName() + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                +"ss" +".jpeg");
        return mediaFile;
    }



    /**
     * The class connects with server and uploads the photo
     *
     *
     */



    /**
     * The class connects with server and uploads the photo
     *
     *
     */


        ///PAlash Image Setting
    /**
     * On load image button click, start pick  image chooser activity.
     */
    public void onLoadImageClick(View view) {
        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    /**
     * Crop the image and set it back to the  cropping view.
     */
    public void onCropImageClick(View view) {
        Bitmap cropped =  mCropImageView.getCroppedImage(500, 500);
        if (cropped != null) {// mCropImageView.setImageBitmap(cropped);
            if (image_count == 8) {
                imageView8.setImageBitmap(cropped);
            } else if (image_count == 2) {
                imageView2.setImageBitmap(cropped);
            } else if (image_count == 3) {
                imageView3.setImageBitmap(cropped);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //mCropImageView.setImageUriAsync(mCropImageUri);
            if (image_count == 8) {
                imageView8.setImageUriAsync(mCropImageUri);
            } else if (image_count == 2) {
                imageView2.setImageUriAsync(mCropImageUri);
            } else if (image_count == 3) {
                imageView3.setImageUriAsync(mCropImageUri);
            }
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create a chooser intent to select the  source to get image from.<br/>
     * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the  intent chooser.
     */
    public Intent getPickImageChooserIntent() {

// Determine Uri of camera image to  save.
        Uri outputFileUri =  getCaptureImageOutputUri();

        List<Intent> allIntents = new  ArrayList<>();
        PackageManager packageManager =  getPackageManager();

// collect all camera intents
        Intent captureIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam =  packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new  Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new  Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new  Intent(galleryIntent);
            intent.setComponent(new  ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent =  allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))  {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

// Create a chooser from the main  intent
        Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");

// Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture  by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new  File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public Uri getPickImageResultUri(Intent  data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null  && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ?  getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
