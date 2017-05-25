package com.dvn.vindecoder.ui.seller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.adaptor.SellerUserAdaptor;
import com.dvn.vindecoder.adaptor.SellerVehicalAdaptor;
import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.dto.UserSellerListDto;
import com.dvn.vindecoder.ui.BaseActivity;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.LoginDB;
import com.dvn.vindecoder.util.PostResponse;

import java.util.List;

public class UserSellerList extends BaseActivity implements AsyncCompleteListener {
    RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private GetVehicalDto getVehicalDto;
    private AsyncGetTask asyncGetTask;
    private List<UserSellerListDto> vehicalList;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_seller_list);
        drawChildLayout();
        setTitle("User list");
        getUserVehicalList();
    }

    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_user_seller_list),
                layoutParams);
        initViews();
        setSwipeHandler();
    }

    @Override
    public void sendFbData() {

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
                   // getUserVehicalList(vehicalList.get(position).getId());
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


    private void getUserVehicalList() {
        String value= LoginDB.getTitle(UserSellerList.this,"value");
        Log.e("value",value);
        getVehicalDto = new GetVehicalDto();
        getVehicalDto.setSeller_id(value);
        getVehicalDto.setAppid(CommonURL.APP_ID);
        asyncGetTask = new AsyncGetTask(UserSellerList.this, CallType.GET_SELLER_USER_LIST, UserSellerList.this, true, getVehicalDto);
        asyncGetTask.execute();
    }

    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.GET_SELLER_USER_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                if(adapter!=null)
                {
                    vehicalList.clear();
                    adapter.notifyDataSetChanged();
                    vehicalList.addAll(aMasterDataDtos.getSellerUserList());
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                }
                else{
                    vehicalList=aMasterDataDtos.getSellerUserList();
                    adapter = new SellerUserAdaptor(vehicalList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
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
}
