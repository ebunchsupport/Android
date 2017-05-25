package com.dvn.vindecoder.fragment;

import android.app.Activity;
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
import com.dvn.vindecoder.dto.DecodeDto;
import com.dvn.vindecoder.dto.VINAPISetter;
import com.dvn.vindecoder.interface_package.DataComplete;
import com.dvn.vindecoder.ui.seller.AddVehicalAndPayment2;
import com.dvn.vindecoder.ui.user.GetNewVehicalFragment;
import com.dvn.vindecoder.util.AeSimpleSHA1;
import com.dvn.vindecoder.util.AndroidMultiPartEntity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class AddNewVehicalFragmentWithouScan extends Fragment implements AdapterView.OnItemSelectedListener,AsyncCompleteListener {
    public String vin_number,make,model,year,v_type;
    public  boolean scanFlag;
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
    private String vid="";
    private boolean checckBackPress=false;
    private DataComplete dataComplete;
    private LinearLayout lineardata1,linear_freg_container;
    private Button button_scan_vin;
    private ArrayList<DecodeDto>decodeDtoList=new ArrayList<DecodeDto>();
    private LinearLayout other_data_container;
    private static final String DESCRIBABLE_KEY = "describable_key";
    private static final String BOOL_KEY = "bool_key";
   /* public AddNewVehicalFragmentWithouScan(DataComplete dataComplete)
    {
    this.dataComplete=dataComplete;
    }*/
    public static AddNewVehicalFragmentWithouScan newInstance(DataComplete dataComplete,boolean scan_var) {
        AddNewVehicalFragmentWithouScan f = new AddNewVehicalFragmentWithouScan();

        // Supply num input as an argument.
        Bundle bundle = new Bundle();
        bundle.putParcelable(DESCRIBABLE_KEY, dataComplete);
        bundle.putBoolean(BOOL_KEY, scan_var);
        f.setArguments(bundle);

        return f;
    }
    public void setImagePath(String img_path)
    {
        filePath=img_path;
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
        mainView = (View) inflater.inflate(R.layout.activity_add_new_vehical_without_scan, container, false);
        lineardata1=(LinearLayout)mainView.findViewById(R.id.linear_part_1);
        linear_freg_container=(LinearLayout)mainView.findViewById(R.id.fragmentcontainer);
        //linear_freg_container.setVisibility(View.INVISIBLE);
        setFirstLinear(mainView);
        seller_id= LoginDB.getTitle(getActivity(),"value");
        linear_freg_container.setVisibility(View.GONE);
        lineardata1.setVisibility(View.VISIBLE);
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
        button_scan_vin=(Button)view.findViewById(R.id.button_scan_vin);
        other_data_container=(LinearLayout)view.findViewById(R.id.other_data_container);
        other_data_container.setVisibility(View.GONE);
        spinner_buyer.setOnItemSelectedListener(this);
        first_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllValues();
            }
        });

        button_scan_vin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_txt_vin_num.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getActivity(),"Please Fill Vin Number Firstly",Toast.LENGTH_LONG).show();
                    return;
                }
                String sha1_var=edit_txt_vin_num.getText().toString().trim();
                getVinNumber(sha1_var);

            }
        });
        setStartWithVinSpinner();
        getBuyerList();
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
        AsyncGetTask asyncGetTask = new AsyncGetTask(getActivity(), CallType.GET_BUYER_LIST, AddNewVehicalFragmentWithouScan.this, true, null);
        asyncGetTask.execute();
    }



    private void getAllValues()
    {
        if(edit_txt_vin_num.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(),"Please fill all the field",Toast.LENGTH_LONG).show();
            edit_txt_vin_num.requestFocus();
            return;
        }
        else if(edit_txt_make.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(),"Please fill all the field",Toast.LENGTH_LONG).show();
            edit_txt_make.requestFocus();
            return;
        }
        else if(edit_txt_year.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(),"Please fill all the field",Toast.LENGTH_LONG).show();
            edit_txt_year.requestFocus();
            return;
        }
        else if(edit_txt_model.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(),"Please fill all the field",Toast.LENGTH_LONG).show();
            return;
        }
        else if(edit_txt_v_type.getText().toString().isEmpty())
        {
            edit_txt_v_type.requestFocus();
        Toast.makeText(getActivity(),"Please fill all the field",Toast.LENGTH_LONG).show();
            return;
        }
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
            dataComplete.onDataComplete(1,"");

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
                entity.addPart("type",
                        new StringBody(""+1));
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
                entity.addPart("vid",
                        new StringBody(vid));
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
                        LoginDB.setVehicleFlag(getActivity(),"1",true);
                        Intent intent=new Intent(getActivity(), AddVehicalAndPayment2.class);
                        intent.putExtra("vid",vid);
                        getActivity().startActivity(intent);
                    }
                    else{
                        if(jsonObject.has("message")) {
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

    private class GETApiCall extends AsyncTask<String, String, String>
    {
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
                    Toast.makeText(getActivity(),"No data Found please try another vin",Toast.LENGTH_SHORT).show();

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

                    setValue(vin_number,make,model,year,v_type);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void setValue(String vin_num1,String make1,String model1,String year1,String v_type1) {
        // Required empty public constructor
        //edit_txt_vin_num.setText(vin_num1);
        other_data_container.setVisibility(View.VISIBLE);
        edit_txt_make.setText(make1);
        edit_txt_year.setText(year1);
        edit_txt_model.setText(model1);
        edit_txt_v_type.setText(v_type1);

       // getBuyerList();

    }
















}
