package com.dvn.vindecoder.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.dvn.vindecoder.interface_package.DataComplete;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Palash on 1/30/2017.
 */

public class ImageConversionAsynchronus extends AsyncTask<String, String, String> {
    ProgressDialog pd;
    private Context context;
    DataComplete dataComplete;
    private int postion;
    ImageView imageView;
    String doc_name;
    Bitmap bitmap;
    public ImageConversionAsynchronus(Context mContext, DataComplete dataComplete, int postion, ImageView imageView, String doc_name) {
        this.context=mContext;
        this.dataComplete=dataComplete;
        this.postion=postion;
        this.imageView=imageView;
        this.doc_name=doc_name;

        bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
    }


    @Override
    protected void onPreExecute() {
       /* pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();*/

        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... params) {

        String image_path=null;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            //byte[] byteArray = bytes.toByteArray();
       // File destination = new File(Environment.getExternalStorageDirectory(),(doc_name+".jpg"));
            File destination = new File(context.getCacheDir(),(doc_name+".jpg"));
        FileOutputStream fo;
        try {
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image_path=destination.getAbsolutePath();
        return image_path;
        }
        catch (Exception ex)
        {
            return null;
        }


    }
    @Override
    protected void onPostExecute(String result) {
        //pd.dismiss();
        if(result == null)
        {
            dataComplete.onDataComplete(0,result);
        }
        else {
            dataComplete.onDataComplete(postion,result);
        }
    }
}
