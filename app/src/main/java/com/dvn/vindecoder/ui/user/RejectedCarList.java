package com.dvn.vindecoder.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.adaptor.UserAssignedVehicalAdaptor;
import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.dto.UserAssingCarListDto;
import com.dvn.vindecoder.dto.UserSellerListDto;
import com.dvn.vindecoder.ui.BaseActivity;
import com.dvn.vindecoder.ui.UserBaseActivity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;

import java.util.ArrayList;
import java.util.List;

public class RejectedCarList extends UserBaseActivity implements AdapterView.OnItemSelectedListener,AsyncCompleteListener {

    RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    private SwipeRefreshLayout swipeContainer;
    private TextView error_txt_view,error_txt_view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        setTitle("Rejected car list");

        //getSellerUser();
        setVsibity();
        getUserVehicalList();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list1),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
        initViews();
        setSwipeHandler();
        setError_txt_view();
    }
    private void setSwipeHandler()
    {
        swipeContainer=(SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,
                android.R.color.holo_orange_light,android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserVehicalList();
            }
        });

    }
    private void setError_txt_view()
    {
        error_txt_view=(TextView)findViewById(R.id.error_txt_view);
        error_txt_view1=(TextView)findViewById(R.id.error_txt_view1);
        recyclerView.setVisibility(View.GONE);
    }
    private void setVsibity() {

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linear1);
        linearLayout.setVisibility(View.GONE);
        ok_btn.setVisibility(View.GONE);
        spinner_user_list.setVisibility(View.GONE);

    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {

        String value= LoginDB.getTitle(RejectedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(value);
        getVehicalDto.setStatus("3");
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(RejectedCarList.this, CallType.GET_USER_VEHICAL_ASSIGN_LIST, RejectedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
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
                    adapter = new UserAssignedVehicalAdaptor(vehicalList,RejectedCarList.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    if(swipeContainer.isRefreshing())
                    {
                        swipeContainer.setRefreshing(false);
                    }
                    error_txt_view.setVisibility(View.GONE);
                    error_txt_view1.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
               // ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
                if(swipeContainer.isRefreshing())
                {
                    swipeContainer.setRefreshing(false);
                }
                error_txt_view1.setVisibility(View.VISIBLE);
                error_txt_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        else{
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


    public void setUserSpinner()
    {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
            categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RejectedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
    }

    public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
            spinner_pos=id1;
            String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }


    private void assignUserCar()
    {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
            carid.append(model.getId()+",");
        }
     }

        if(carid.length()<3)
        {
            Toast.makeText(RejectedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
            return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
        final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(RejectedCarList.this, CallType.ASSIGNED_CAR_TO_USER, RejectedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=new Intent(this,UserDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}
/*
    RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.UNASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    private void getSellerUser() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.GET_SELLER_USER_LIST, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.UNASSIGNED_CAR_TO_USER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicalList=aMasterDataDtos.getUnAssingcarlist();
                adapter = new UserAssignedVehicalAdaptor(vehicalList,UnAssignedCarList.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else  if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {

                userList=aMasterDataDtos.getSellerUserList();
                setUserSpinner();
                getUserVehicalList();
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
}
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else  if (type == CallType.ASSIGNED_CAR_TO_USER) {

        //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
        // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

        if(aMasterDataDtos.getResponseCode()==1)
        {


        Intent s_intent1 = getIntent();
        finish();
        startActivity(s_intent1);
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else{
        Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
        }


public void setUserSpinner()
        {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
        categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UnAssignedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
        }

public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

        }

@Override
public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
        spinner_pos=id1;
        String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


private void assignUserCar()
        {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
        carid.append(model.getId()+",");
        }
        }

        if(carid.length()<3)
        {
        Toast.makeText(UnAssignedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
        return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.ASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

        }

        RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.UNASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    private void getSellerUser() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.GET_SELLER_USER_LIST, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.UNASSIGNED_CAR_TO_USER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicalList=aMasterDataDtos.getUnAssingcarlist();
                adapter = new UserAssignedVehicalAdaptor(vehicalList,UnAssignedCarList.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else  if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {

                userList=aMasterDataDtos.getSellerUserList();
                setUserSpinner();
                getUserVehicalList();
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
}
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else  if (type == CallType.ASSIGNED_CAR_TO_USER) {

        //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
        // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

        if(aMasterDataDtos.getResponseCode()==1)
        {


        Intent s_intent1 = getIntent();
        finish();
        startActivity(s_intent1);
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else{
        Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
        }


public void setUserSpinner()
        {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
        categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UnAssignedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
        }

public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

        }

@Override
public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
        spinner_pos=id1;
        String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


private void assignUserCar()
        {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
        carid.append(model.getId()+",");
        }
        }

        if(carid.length()<3)
        {
        Toast.makeText(UnAssignedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
        return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.ASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

        }

        RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.UNASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    private void getSellerUser() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.GET_SELLER_USER_LIST, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.UNASSIGNED_CAR_TO_USER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicalList=aMasterDataDtos.getUnAssingcarlist();
                adapter = new UserAssignedVehicalAdaptor(vehicalList,UnAssignedCarList.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else  if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {

                userList=aMasterDataDtos.getSellerUserList();
                setUserSpinner();
                getUserVehicalList();
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
}
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else  if (type == CallType.ASSIGNED_CAR_TO_USER) {

        //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
        // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

        if(aMasterDataDtos.getResponseCode()==1)
        {


        Intent s_intent1 = getIntent();
        finish();
        startActivity(s_intent1);
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else{
        Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
        }


public void setUserSpinner()
        {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
        categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UnAssignedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
        }

public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

        }

@Override
public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
        spinner_pos=id1;
        String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


private void assignUserCar()
        {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
        carid.append(model.getId()+",");
        }
        }

        if(carid.length()<3)
        {
        Toast.makeText(UnAssignedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
        return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.ASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

        }

        RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.UNASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    private void getSellerUser() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.GET_SELLER_USER_LIST, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.UNASSIGNED_CAR_TO_USER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicalList=aMasterDataDtos.getUnAssingcarlist();
                adapter = new UserAssignedVehicalAdaptor(vehicalList,UnAssignedCarList.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else  if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {

                userList=aMasterDataDtos.getSellerUserList();
                setUserSpinner();
                getUserVehicalList();
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
}
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else  if (type == CallType.ASSIGNED_CAR_TO_USER) {

        //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
        // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

        if(aMasterDataDtos.getResponseCode()==1)
        {


        Intent s_intent1 = getIntent();
        finish();
        startActivity(s_intent1);
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else{
        Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
        }


public void setUserSpinner()
        {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
        categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UnAssignedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
        }

public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

        }

@Override
public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
        spinner_pos=id1;
        String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


private void assignUserCar()
        {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
        carid.append(model.getId()+",");
        }
        }

        if(carid.length()<3)
        {
        Toast.makeText(UnAssignedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
        return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.ASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

        }

        RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.UNASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    private void getSellerUser() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.GET_SELLER_USER_LIST, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.UNASSIGNED_CAR_TO_USER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicalList=aMasterDataDtos.getUnAssingcarlist();
                adapter = new UserAssignedVehicalAdaptor(vehicalList,UnAssignedCarList.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else  if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {

                userList=aMasterDataDtos.getSellerUserList();
                setUserSpinner();
                getUserVehicalList();
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
}
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else  if (type == CallType.ASSIGNED_CAR_TO_USER) {

        //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
        // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

        if(aMasterDataDtos.getResponseCode()==1)
        {


        Intent s_intent1 = getIntent();
        finish();
        startActivity(s_intent1);
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else{
        Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
        }


public void setUserSpinner()
        {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
        categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UnAssignedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
        }

public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

        }

@Override
public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
        spinner_pos=id1;
        String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


private void assignUserCar()
        {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
        carid.append(model.getId()+",");
        }
        }

        if(carid.length()<3)
        {
        Toast.makeText(UnAssignedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
        return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.ASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

        }

        RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.UNASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    private void getSellerUser() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.GET_SELLER_USER_LIST, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.UNASSIGNED_CAR_TO_USER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicalList=aMasterDataDtos.getUnAssingcarlist();
                adapter = new UserAssignedVehicalAdaptor(vehicalList,UnAssignedCarList.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else  if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {

                userList=aMasterDataDtos.getSellerUserList();
                setUserSpinner();
                getUserVehicalList();
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
}
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else  if (type == CallType.ASSIGNED_CAR_TO_USER) {

        //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
        // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

        if(aMasterDataDtos.getResponseCode()==1)
        {


        Intent s_intent1 = getIntent();
        finish();
        startActivity(s_intent1);
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else{
        Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
        }


public void setUserSpinner()
        {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
        categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UnAssignedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
        }

public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

        }

@Override
public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
        spinner_pos=id1;
        String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


private void assignUserCar()
        {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
        carid.append(model.getId()+",");
        }
        }

        if(carid.length()<3)
        {
        Toast.makeText(UnAssignedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
        return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.ASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

        }

        RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.UNASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    private void getSellerUser() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.GET_SELLER_USER_LIST, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.UNASSIGNED_CAR_TO_USER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicalList=aMasterDataDtos.getUnAssingcarlist();
                adapter = new UserAssignedVehicalAdaptor(vehicalList,UnAssignedCarList.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else  if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {

                userList=aMasterDataDtos.getSellerUserList();
                setUserSpinner();
                getUserVehicalList();
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
}
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else  if (type == CallType.ASSIGNED_CAR_TO_USER) {

        //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
        // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

        if(aMasterDataDtos.getResponseCode()==1)
        {


        Intent s_intent1 = getIntent();
        finish();
        startActivity(s_intent1);
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else{
        Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
        }


public void setUserSpinner()
        {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
        categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UnAssignedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
        }

public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

        }

@Override
public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
        spinner_pos=id1;
        String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


private void assignUserCar()
        {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
        carid.append(model.getId()+",");
        }
        }

        if(carid.length()<3)
        {
        Toast.makeText(UnAssignedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
        return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.ASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

        }


        RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.UNASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    private void getSellerUser() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.GET_SELLER_USER_LIST, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.UNASSIGNED_CAR_TO_USER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicalList=aMasterDataDtos.getUnAssingcarlist();
                adapter = new UserAssignedVehicalAdaptor(vehicalList,UnAssignedCarList.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else  if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {

                userList=aMasterDataDtos.getSellerUserList();
                setUserSpinner();
                getUserVehicalList();
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
}
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else  if (type == CallType.ASSIGNED_CAR_TO_USER) {

        //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
        // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

        if(aMasterDataDtos.getResponseCode()==1)
        {


        Intent s_intent1 = getIntent();
        finish();
        startActivity(s_intent1);
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else{
        Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
        }


public void setUserSpinner()
        {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
        categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UnAssignedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
        }

public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

        }

@Override
public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
        spinner_pos=id1;
        String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


private void assignUserCar()
        {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
        carid.append(model.getId()+",");
        }
        }

        if(carid.length()<3)
        {
        Toast.makeText(UnAssignedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
        return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.ASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

        }

        RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

               //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


                adapter.notifyDataSetChanged();
}
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else  if (type == CallType.ASSIGNED_CAR_TO_USER) {

        //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
        // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

        if(aMasterDataDtos.getResponseCode()==1)
        {


        Intent s_intent1 = getIntent();
        finish();
        startActivity(s_intent1);
               /* adapter = new SellerUserAdaptor(vehicalList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }
        else {
        Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
        }
        }
        else{
        Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
        }


public void setUserSpinner()
        {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i=0;i<userList.size();i++)
        {
        categories.add(userList.get(i).getName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UnAssignedCarList.this, android.R.layout.simple_spinner_item,categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_user_list.setAdapter(dataAdapter);
        }

public void onNothingSelected(AdapterView arg0) {
        // TODO Auto-generated method stub

        }

@Override
public void onItemSelected(AdapterView parent, View view, int position, long id) {
        // On selecting a spinner item
        int id1 = view.getId();

        if(id1==R.id.spinner_user_list)
        {
        spinner_pos=id1;
        String item = parent.getItemAtPosition(position).toString();
        }


        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        }


private void assignUserCar()
        {
        //List <String> carid1 = new ArrayList<String>();
        StringBuffer carid=new StringBuffer();
        for (UserAssingCarListDto model : vehicalList) {
        if (model.isSelected()) {
        carid.append(model.getId()+",");
        }
        }

        if(carid.length()<3)
        {
        Toast.makeText(UnAssignedCarList.this,"Please select atleast on car",Toast.LENGTH_LONG).show();
        return;
        }
        String carid2=carid.substring(0,(carid.length()-1));
        Log.e("carid 2",carid2);
final String user_id=userList.get(spinner_pos).getId();
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setUserId(user_id);
        getVehicalDto.setCarlist(carid2);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.ASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();

        }

        RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserAssingCarListDto> vehicalList;
    private List<UserSellerListDto> userList;
    private Spinner spinner_user_list;
    private Button ok_btn;
    private int spinner_pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        initViews();
        getSellerUser();

    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_un_assigned_car_list),
                layoutParams);
        spinner_user_list=(Spinner)findViewById(R.id.spinner_user_list);
        ok_btn=(Button)findViewById(R.id.button_ok);
    }

    @Override
    public void sendFbData() {

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
                    //  getUserVehicalList(vehicalList.get(position).getId());
                    vehicalList.get(position).setSelected(!vehicalList.get(position).isSelected());
                    child.setBackgroundColor(vehicalList.get(position).isSelected() ? Color.CYAN : Color.WHITE);
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


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignUserCar();
            }
        });
    }


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.UNASSIGNED_CAR_TO_USER, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    private void getSellerUser() {
        String value= LoginDB.getTitle(UnAssignedCarList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UnAssignedCarList.this, CallType.GET_SELLER_USER_LIST, UnAssignedCarList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.UNASSIGNED_CAR_TO_USER) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicalList=aMasterDataDtos.getUnAssingcarlist();
                adapter = new UserAssignedVehicalAdaptor(vehicalList,UnAssignedCarList.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ok_btn.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else  if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {

                userList=aMasterD


        */