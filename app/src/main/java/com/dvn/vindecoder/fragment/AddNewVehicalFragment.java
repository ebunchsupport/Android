package com.dvn.vindecoder.fragment;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;

import android.os.Bundle;

import android.app.Fragment;
import android.support.v7.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.BuyerListDto;
import com.dvn.vindecoder.interface_package.DataComplete;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment2;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment3;
import com.dvn.vindecoder.ui.user.GetNewVehicalFragment;
import com.dvn.vindecoder.util.AeSimpleSHA1;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import com.dvn.vindecoder.util.AndroidMultiPartEntity;

public class AddNewVehicalFragment extends Fragment implements AdapterView.OnItemSelectedListener,AsyncCompleteListener {

    public static boolean scanFlag;
    private View mainView;
    private List<BuyerListDto>buyerListDtos;
    private Spinner spinner,spinner_vin_satrt,spinner_buyer;
    private AeSimpleSHA1 aeSimpleSHA1;
    private LinearLayout linear_part1,linear_part2,linear_part3;
    private EditText edit_txt_vin_num,edit_txt_make,edit_txt_year,edit_txt_model,edit_txt_v_type,edit_txt_selling_price;
    private Button first_button_ok;
    private String edit_txt_vin_num1,edit_txt_make1,edit_txt_year1,edit_txt_model1,edit_txt_v_type1,edit_txt_selling_price1,spinner_vin_satrt1,spinner_buyer1;
    private long totalSize;
    private String filePath=null;
    private String seller_id;
    private ProgressBar progressBar;
    private String vid;
    private DataComplete dataComplete;
    private LinearLayout lineardata1,linear_freg_container;
    android.app.FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private static final String DESCRIBABLE_KEY = "describable_key";
    private static final String BOOL_KEY = "bool_key";
    public boolean checckBackPress=false;
    public static AddNewVehicalFragment newInstance(DataComplete dataComplete,boolean scan_var) {
        AddNewVehicalFragment f = new AddNewVehicalFragment();

        // Supply num input as an argument.
        Bundle bundle = new Bundle();
        bundle.putParcelable(DESCRIBABLE_KEY, dataComplete);
        bundle.putBoolean(BOOL_KEY, scan_var);
        f.setArguments(bundle);

        return f;
    }
 /*   public AddNewVehicalFragment(DataComplete dataComplete)
    {
    this.dataComplete=dataComplete;
    }*/

    public void setImagePath(String img_path)
    {
        filePath=img_path;
    }

    public void setValue(String vin_num1,String make1,String model1,String year1,String v_type1) {
        // Required empty public constructor
        edit_txt_vin_num.setText(vin_num1);
        edit_txt_make.setText(make1);
        edit_txt_year.setText(year1);
        edit_txt_model.setText(model1);
        edit_txt_v_type.setText(v_type1);
        getBuyerList();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataComplete = (DataComplete) getArguments().getParcelable(
                DESCRIBABLE_KEY);
        scanFlag=(boolean)getArguments().getBoolean(
                BOOL_KEY);
        mainView = (View) inflater.inflate(R.layout.activity_add_new_vehical, container, false);
        lineardata1=(LinearLayout)mainView.findViewById(R.id.linear_part_1);
        linear_freg_container=(LinearLayout)mainView.findViewById(R.id.fragmentcontainer);
        //linear_freg_container.setVisibility(View.INVISIBLE);
        setFirstLinear(mainView);
        seller_id= LoginDB.getTitle(getActivity(),"value");
        if (scanFlag)
        {
            new IntentIntegrator(getActivity()).initiateScan();
            getActivity().overridePendingTransition(R.anim.on_intent_in, R.anim.on_intent_out);

        }
        linear_freg_container.setVisibility(View.GONE);
        lineardata1.setVisibility(View.VISIBLE);
        fragmentManager = getActivity().getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        return mainView;
    }


    private void setFirstLinear(View view)
    {
        linear_part1=(LinearLayout)view.findViewById(R.id.linear_part_1);
        edit_txt_vin_num=(EditText)view.findViewById(R.id.edit_txt_vin_num);
        edit_txt_make=(EditText)view.findViewById(R.id.edit_txt_make);
        edit_txt_year=(EditText)view.findViewById(R.id.edit_txt_year);
        edit_txt_model=(EditText)view.findViewById(R.id.edit_txt_model);
        edit_txt_v_type=(EditText)view.findViewById(R.id.edit_txt_v_type);
        edit_txt_selling_price=(EditText)view.findViewById(R.id.edit_txt_selling_price);
        spinner_vin_satrt=(Spinner) view.findViewById(R.id.spinner_vin_satrt);
        spinner_buyer=(Spinner) view.findViewById(R.id.spinner_buyer);
        first_button_ok=(Button)view.findViewById(R.id.first_button_ok);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        spinner_buyer.setOnItemSelectedListener(this);
        first_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllValues();
            }
        });
        setStartWithVinSpinner();
    }

    private void setStartWithVinSpinner()
    {
        spinner_vin_satrt.setOnItemSelectedListener(this);


        List<String> categories1 = new ArrayList <String>();
        categories1.add("1,3,5");
        categories1.add("2,4");
        categories1.add("J,W");



        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories1);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_vin_satrt.setAdapter(dataAdapter1);
    }



    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();
        if(id1==R.id.spinner_vin_satrt)
        {
            String item = parent.getItemAtPosition(position).toString();
        }
        if(id1==R.id.spinner_buyer)
        {
            String item = parent.getItemAtPosition(position).toString();
        }

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void setBuyerSpinner()
    {
        // Spinner Drop down elements
        List<String> categories = new ArrayList <String>();
        for (int i=0;i<buyerListDtos.size();i++)
        {
            categories.add(buyerListDtos.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_buyer.setAdapter(dataAdapter);
    }

    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

    }


    // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.e("sss","sds");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null && scanFlag) {
            scanFlag=false;
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                edit_txt_vin_num.setText(""+result.getContents());
            }
        }
        else  if (resultCode == Activity.RESULT_OK) {


        }
    }









    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.GET_BUYER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                buyerListDtos=aMasterDataDtos.getBuyerlist();
                setBuyerSpinner();
            }
            else {
                Toast.makeText(getActivity(),aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(),"Data Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBuyerList() {
        AsyncGetTask asyncGetTask = new AsyncGetTask(getActivity(), CallType.GET_BUYER_LIST, AddNewVehicalFragment.this, true, null);
        asyncGetTask.execute();
    }



    private void getAllValues()
    {
        edit_txt_vin_num1=edit_txt_vin_num.getText().toString();
        edit_txt_make1=edit_txt_make.getText().toString();
        edit_txt_year1=edit_txt_year.getText().toString();
        edit_txt_model1=edit_txt_model.getText().toString();
        edit_txt_v_type1=edit_txt_v_type.getText().toString();
        if(edit_txt_selling_price.getText().toString().trim()!=null ||!edit_txt_selling_price.getText().toString().trim().equals(""))
        {
            edit_txt_selling_price1=edit_txt_vin_num.getText().toString();
        }
        else {
            edit_txt_selling_price1="";
        }
        spinner_vin_satrt1=spinner_vin_satrt.getSelectedItem().toString();
        spinner_buyer1=buyerListDtos.get(spinner_buyer.getSelectedItemPosition()).getId();
       // if(filePath==null)
        {
            dataComplete.onDataComplete(1,"");
        }
        new UploadFileToServer().execute();
    }





    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

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
            if(checckBackPress){
                httppost = new HttpPost(CommonURL.URL+"/api/update_firststepdata");
            }
            else {
                httppost = new HttpPost(CommonURL.URL+"/api/add_car");
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

                // Adding file data to http body
                entity.addPart("carimage", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("seller",
                        new StringBody(seller_id));
                entity.addPart("buyer", new StringBody(spinner_buyer1));

                entity.addPart("price",
                        new StringBody(edit_txt_selling_price1));
                entity.addPart("appid", new StringBody(CommonURL.APP_ID));

                entity.addPart("scanvin",
                        new StringBody(edit_txt_vin_num1));
                entity.addPart("swith", new StringBody(spinner_vin_satrt1));

                entity.addPart("make",
                        new StringBody(edit_txt_make1));
                entity.addPart("year", new StringBody(edit_txt_year1));

                entity.addPart("model",
                        new StringBody(edit_txt_model1));
                entity.addPart("vtype", new StringBody(edit_txt_v_type1));
                entity.addPart("type",
                        new StringBody(""+1));
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
            progressBar.setVisibility(View.GONE);
            // showing the server response in an alert dialog
            try {
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.has("response_code"))
                {
                    if(jsonObject.optString("response_code").equals("1"))
                    {
                        Toast.makeText(getActivity(),"vehicle Information are successfully saved",Toast.LENGTH_LONG).show();
                        vid=jsonObject.optString("vid");
                        checckBackPress=true;
                       // filePath=null;
                        Intent intent=new Intent(getActivity(), AddVehicalAndPayment2.class);
                        intent.putExtra("vid",vid);
                        getActivity().startActivity(intent);
                    }
                    else{
                        if(jsonObject.has("message")) {
                            Toast.makeText(getActivity(), jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                       // Toast.makeText(getActivity(),"Vin is invalid or already inserted please try another",Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


           // showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }






   /* private class UploadFileToServerScond extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible

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
            if(checckBackPress){
                httppost = new HttpPost(CommonURL.URL+"/api/update_secondstepdata");
            }
            else {
                httppost =  new HttpPost(CommonURL.URL+"/api/add_carsecondstep");
            }

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(bill_copy_image_path);

                // Adding file data to http body
                entity.addPart("bill_copy", new FileBody(sourceFile));
                File sourceFile1 = new File(registrationfile_path);

                // Adding file data to http body
                entity.addPart("registrationfile", new FileBody(sourceFile1));

                File sourceFile2 = new File(recalldoc_image);

                // Adding file data to http body
                entity.addPart("recalldoc", new FileBody(sourceFile2));

                File sourceFile3 = new File(manufacturedoc_image);

                // Adding file data to http body
                entity.addPart("manufacturedoc", new FileBody(sourceFile3));

                // Extra parameters if you want to pass to server
                entity.addPart("vid",
                        new StringBody(vid));
                entity.addPart("recallfree", new StringBody(isRecallfree));

                entity.addPart("missinglable",
                        new StringBody(missinglable));
                entity.addPart("appid", new StringBody(CommonURL.APP_ID));

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
            progressDialog.dismiss();
            // showing the server response in an alert dialog
            try {
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.has("response_code"))
                {
                    if(jsonObject.optString("response_code").equals("1"))
                    {
                        checckBackPress=true;
                        Toast.makeText(getActivity(),"vehicle Information are successfully saved",Toast.LENGTH_LONG).show();
                        vid=jsonObject.optString("vid");
                        Intent intent=new Intent(getActivity(), AddVehicalAndPayment3.class);
                        intent.putExtra("vid",vid);
                        startActivity(intent);
                        *//*vid=jsonObject.optString("vid");

                        Intent intent=new Intent(getActivity(), AddVehicalAndPayment2.class);
                        intent.putExtra("vid",vid);
                        getActivity().startActivity(intent);*//*
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            // showAlert(result);

            super.onPostExecute(result);
        }

    }*/



















}
