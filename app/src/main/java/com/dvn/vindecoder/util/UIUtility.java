package com.dvn.vindecoder.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dvn.vindecoder.R;
import com.dvn.vindecoder.ui.seller.SellCarInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by palash on 23-06-2016.
 */
public class UIUtility {

    /**
     * This method is used to show default toast notification for the given user
     * message.
     *
     * @param context the context
     * @param message the message
     */
    public static boolean isStringnotEmpty(String value) {
        if(value!= null && !value.isEmpty() && !value.equals("null"))
        {
            return true;
        }
        return false;
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void displayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Display toast on top.
     *
     * @param context the context
     * @param message the message
     */
    public static void displayToastOnTop(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.show();
    }

    /**
     * Display toast.
     *
     * @param context the context
     * @param message the message
     */
    public static void displayToast(Fragment context, String message) {
        Toast.makeText(context.getActivity(), message, Toast.LENGTH_LONG)
                .show();
    }

    /**
     * This method is used to get Textview with center gravity for the given
     * text and textSize.
     *
     * @param context the context
     * @param text    the text
     * @param size    the size
     * @return the text view
     */
    public static TextView getTextView(Context context, String text, float size) {

        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(size);
        tv.setTextColor(Color.BLACK);
        tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        return tv;
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     *
     * @param context The application's environment.
     * @param action  The Intent action to check for availability.
     * @return True if an Intent with the specified action can be sent and
     * responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * This method is used to get the font face which is used across the
     * application.
     *
     * @param context
     *            the context
     * @return fonr
     */
    /*
	 * public static Typeface getFontFace(Context context) {
	 *
	 * Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" +
	 * ApplicationConstant.FONT_FACE);
	 *
	 * return font; }
	 */

    /**
     * Enable place holder.
     *
     * @param context the context
     */
    public static void enablePlaceHolder(Context context) {

    }

    /**
     * Copy stream.
     *
     * @param is the is
     * @param os the os
     */
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * To proper case.
     *
     * @param name the name
     * @return the string
     */
    public static String toProperCase(String name) {
        return name.substring(0, 1).toUpperCase()
                + name.substring(1).toLowerCase();
    }

    /**
     * Gets the resized bitmap.
     *
     * @param bm        the bm
     * @param newHeight the new height
     * @param newWidth  the new width
     * @return the resized bitmap
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // re-create new bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

    /**
     * Not empty.
     *
     * @param s the s
     * @return true, if successful
     */
    public static boolean notEmpty(String s) {
        return (s != null && s.length() > 0);
    }

    /**
     * Function to split string after comma(,).
     *
     * @param str the str
     * @return the splited value
     */
    public static ArrayList<String> getSplitedValue(String str) {

        ArrayList<String> sArrayList = new ArrayList<String>();
        String strc = "";
        String strAt = "";

        StringTokenizer st = new StringTokenizer(str, ",");
        strAt = st.nextToken().toString();
        sArrayList.add(strAt);
        if (st.countTokens() > 1) {
            while (st.hasMoreElements()) {

                String data = st.nextElement().toString();
                strc += data + ",";

            }
            sArrayList.add(strc);
        }

        return sArrayList;

    }

    /**
     * Show next screen.
     *
     * @param currentContext the current context
     * @param nextScreen     the next screen
     */
    public static void showNextScreen(Context currentContext,
                                      @SuppressWarnings("rawtypes") Class nextScreen) {
        Intent intent = new Intent(currentContext, nextScreen);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        currentContext.startActivity(intent);
    }

    /**
     * Gets the orientation.
     *
     * @param context  the context
     * @param photoUri the photo uri
     * @return the orientation
     */
    public static int getOrientation(Context context, Uri photoUri) {

		/* it's on the external media. */
        int rotation = 0;
        if (photoUri.getScheme().equals("content")) {
            Cursor cursor = context
                    .getContentResolver()
                    .query(photoUri,
                            new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                            null, null, null);

            if (cursor.getCount() != 1) {
                return -1;
            }

            cursor.moveToFirst();
            return cursor.getInt(0);
        } else if (photoUri.getScheme().equals("file")) {
            try {
                ExifInterface exif = new ExifInterface(photoUri.getPath());
                rotation = (int) exifOrientationToDegrees(exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL));
                return rotation;
            } catch (IOException e) {
                Log.e("Utility", "Error checking exif", e);
            }
        }
        return rotation;

    }

    /**
     * Exif orientation to degrees.
     *
     * @param exifOrientation the exif orientation
     * @return the float
     */
    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /**
     * Display a simple alert dialog with the given text and title.
     *
     * @param context Android context in which the dialog should be displayed
     * @param title   Alert dialog title
     * @param text    Alert dialog message
     */
    public static void showAlert(Context context, String title, String text) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(text);
        alertBuilder.create().show();
    }

    /**
     * Gets the unique id.
     *
     * @return the unique id
     */
    public static String getUniqueId() {
        UUID uniqueKey = UUID.randomUUID();

        return uniqueKey.toString();
    }

    /**
     * Gets the constraint string.
     *
     * @param str the str
     * @return the constraint string
     */
    public static String getConstraintString(String str) {

        try {

            if (str.length() > 13) {
                return str.substring(0, 13) + "...";
            } else {
                return str;

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return str;

    }

    /**
     * @param context
     * @param v
     */

    public static void hideSoftKeyboard(Context context, View v) {
        if (v != null && v instanceof EditText) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        if (v != null && v instanceof Button) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);
    }

    /**
     * @param appContext
     * @param initialText
     * @param textColor
     * @return
     */
    public static TextView getTextView3(Context appContext, String initialText,
                                        int textColor) {
        TextView textView = new TextView(appContext);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textParams);
        textView.setTextColor(textColor);
        // textView.setGravity(gravity);
        textView.setTextSize(15.0f);
        // textView.setMaxEms(15);
        return textView;
    }

    /**
     * This method is used to get CheckBox
     *
     * @param context         the appContext
     * @param backgroundColor
     * @param textColor
     * @param gravity
     * @return CheckBox
     */
    public static ToggleButton getToggleButton(Context appContext, int gravity) {
        ToggleButton toggle = new ToggleButton(appContext);
        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams toggleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        toggle.setLayoutParams(toggleParams);
        toggle.setGravity(gravity);
        toggle.setText("No");
        toggle.setTextOn("Yes");
        toggle.setTextOff("No");
        // toggle.setClickable(true);
        return toggle;
    }

    /**
     * @param appContext
     * @param initialText
     * @param gravity
     * @param textColor
     * @return
     */
    public static TextView getTextView2(Context appContext, String initialText,
                                        int gravity, int textColor) {
        TextView textView = new TextView(appContext);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textParams);
        textView.setText(initialText);
        // textView.setBackgroundResource(backgroundColor);
        textView.setTextColor(textColor);
        textView.setPadding(10, 0, 0, 0);
        textView.setGravity(gravity);
        textView.setTextSize(15.0f);
        textView.setEms(11);
        return textView;
    }

    /**
     * @return
     */
    public static boolean isNetworkAvailable(Context cont) {

        Context context = cont;

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param context
     * @return
     */
    public static boolean isNetworkActivated(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * This method is used to get Date from passed string
     *
     * @param dateString
     * @return
     */

    public static Date getDateFromString(String dateString) {

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = null;

        try {
            convertedDate = inputFormat.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    /***
     * This method is used to get Date and time from passed string
     *
     * @param dateString
     * @return
     */
    public static Date getDateTimeFromString(String dateString) {

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:S");
        Date convertedDate = null;

        try {
            convertedDate = inputFormat.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    /**
     * @param date
     * @return
     */
    public static String getDateFromDate(Date date) {
        try {
            return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @param date
     * @return
     */
    public static String getDateFromDateTime(Date date) {
        try {
            return DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                    DateFormat.DEFAULT).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @param date
     * @return
     */
    public static String getTimeFromDate(Date date) {
        try {
            return DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @param services
     * @param values
     * @return
     */
    public static String getMultiply(String services, String values) {
        try {
            int ser = Integer.parseInt(services);
            int val = Integer.parseInt(values);
            return String.valueOf(ser * val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param services
     * @param values
     * @return
     */
    public static String getMultiply(String services, float values) {
        try {
            int ser = Integer.parseInt(services);
            return String.valueOf(ser * values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param date
     * @return
     */

    @SuppressLint("SimpleDateFormat")
    public static boolean compareTwoDate(String date) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = sdf.parse(date);
            @SuppressWarnings("deprecation")
            Date date2 = new Date(new Date().getYear(), new Date().getMonth(),
                    new Date().getDate());
            if (date1.equals(date2)) {
                return false;
            } else if (date1.before(date2)) {
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * This utility method is used to compare current date and time with passed
     * argument date and time
     *
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean compareTwoDateTime(String date) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:S");
            Date date1 = sdf.parse(date);
            Date dateInit = new Date();
            @SuppressWarnings("deprecation")
            Date date2 = new Date(dateInit.getYear(), dateInit.getMonth(),
                    dateInit.getDate(), dateInit.getHours(),
                    dateInit.getMinutes(), dateInit.getSeconds());
            if (date1.equals(date2)) {
                return false;
            } else if (date1.before(date2)) {
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean compareDate(String d1, String d2) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            System.out.println(sdf.format(date2));
            if (date1.equals(date2)) {
                return false;
            } else if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @param d1
     * @param d2
     * @return
     */
    public static boolean compareDate(String d1, Date d2) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = sdf.parse(d1);
            Date date2 = d2;
            System.out.println(sdf.format(date2));
            if (date1.equals(date2)) {
                return false;
            } else if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @param d1
     * @param d2
     * @return
     */
    public static boolean compareDate(Date d1, String d2) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = d1;
            Date date2 = sdf.parse(d2);
            System.out.println(sdf.format(date2));
            if (date1.equals(date2)) {
                return false;
            } else if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean compareAfterDate(String date) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = sdf.parse(date);
            Date date2 = new Date(new Date().getYear(), new Date().getMonth(),
                    new Date().getDay());
            System.out.println(sdf.format(date2));
            if (date1.equals(date2)) {
                return false;
            } else if (date1.after(date2)) {
                System.out.println("Date1 is before Date2");
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @param d1
     * @param d2
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean timeIsBefore(Date d1, Date d2) {
        DateFormat f = new SimpleDateFormat("HH:mm:ss.SSS");
        return f.format(d1).compareTo(f.format(d2)) < 0;
    }

    public static boolean compareAfterDateTime(String date) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:S");
            Date date1 = sdf.parse(date);
            Date dateInit = new Date();
            @SuppressWarnings("deprecation")
            Date date2 = new Date(dateInit.getYear(), dateInit.getMonth(),
                    dateInit.getDate(), dateInit.getHours(),
                    dateInit.getMinutes(), dateInit.getSeconds());
            if (date1.equals(date2)) {
                return false;
            } else if (date1.after(date2)) {
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static void showDialog(Activity activity, String img_url){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.imageviewdialoug);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        ImageView all_img_container,clost_img_btn;
        all_img_container=(ImageView)dialog.findViewById(R.id.all_img_container);
        clost_img_btn=(ImageView)dialog.findViewById(R.id.clost_img_btn);
        final ProgressBar progressBar_image_Large=(ProgressBar)dialog.findViewById(R.id.progressBar_image_Large);
        try {
            Glide.with(activity).load((img_url))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar_image_Large.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar_image_Large.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(all_img_container);
        }
        catch (Exception ex)
        {

        }
        //Button dialogButton = (Button) dialog.findViewById(R.id.clost_img_btn);
        clost_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
/*
    public static NotificationDto getGCMData(String str) {

        try {
           /* NotificationDto notificationDto = new NotificationDto();
            String[] splittedValue = str.split(":");
            String subject = splittedValue[1].split(",")[0];
            notificationDto.setSubject(subject);
            String id = splittedValue[2].split(",")[0];
            notificationDto.setId(id);
            String type = splittedValue[3].split(",")[0];
            notificationDto.setType(type);
            try {
                String cityId = splittedValue[4].split(",")[0];
                Log.e("ravi:", cityId);
                char  result=	cityId.charAt(0);
                notificationDto.setCityId(""+result);
                //	String  result = cityId.replaceAll("[^\\p{L}\\p{Z}]","");
                Log.e("ravi:", "" + result);
                String image = splittedValue[5].split(",")[0];
                image = image.replaceAll("[}]","");
                notificationDto.setImage(""+image);
                Log.e("ravi:", image);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return notificationDto;
        } catch (Exception e) {
        }
        return null;

    }*/
/*
    public static NotificationDto getGCMDataJson(String str) {

        try {
            NotificationDto notificationDto = new NotificationDto();
            try {
                JSONObject json = new JSONObject( str);
                notificationDto.setSubject(json.getString("subject"));
                notificationDto.setId(json.getString("id"));
                notificationDto.setType(json.getString("type"));
                notificationDto.setCityId(json.getString("city_id"));
                notificationDto.setImage(json.getString("image"));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return notificationDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static DisplayImageOptions getImageLoaderOptions(int loader) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(loader).cacheInMemory()
                .showImageForEmptyUri(loader).showImageOnFail(loader)
                .showImageOnLoading(loader).cacheOnDisc()
                .imageScaleType(ImageScaleType.EXACTLY).build();
        return options;
    }*/

    public static void deleteCache(Context context) {
        try {

            File dir = context.getCacheDir();
           // deleteDir(dir);
            if(deleteDir(dir)){
                Log.e("Delete","Clear Cahe");
            }
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}

