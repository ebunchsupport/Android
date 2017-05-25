package com.dvn.vindecoder.adaptor;

/**
 * Created by palash on 19-01-2017.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.SellerVehicalListDto;
import com.dvn.vindecoder.dto.UserSellerListDto;

import java.util.List;

import static org.apache.http.util.TextUtils.isBlank;


public class SellerUserAdaptor extends RecyclerView.Adapter<SellerUserAdaptor.ViewHolder> {
    private List<UserSellerListDto> userList;

    public SellerUserAdaptor(List<UserSellerListDto> userList) {
        this.userList = userList;
    }

    @Override
    public SellerUserAdaptor.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seller_user_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //viewHolder.tv_country.setText(userList.get(i));
       // Log.e("Name",userList.get(i).getName());
        String name=userList.get(i).getName();
        String phone=userList.get(i).getContact();
        String mobile=userList.get(i).getMobile();
        String add=userList.get(i).getAddress();
        if(!isBlank(name)) {
            viewHolder.textViewName.setText("" + name);
        }
        if(!isBlank(mobile))
        {
            viewHolder.textViewMobile.setText(""+mobile);
        }

        if(isBlank(phone))
        {
            viewHolder.textViewLandLine.setText("xxx");
        }
        else {
            viewHolder.textViewLandLine.setText(""+phone);
        }
        if(!isBlank(add))
        viewHolder.textViewAddress.setText(""+add);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_country;
        private TextView textViewName,textViewMobile,textViewLandLine,textViewAddress;
        public ViewHolder(View view) {
            super(view);

            // tv_country = (TextView)view.findViewById(R.id.tv_country);
            textViewName = (TextView)view.findViewById(R.id.textViewName);
            textViewMobile = (TextView)view.findViewById(R.id.textViewMobile);
            textViewLandLine = (TextView)view.findViewById(R.id.textViewLandLine);
            textViewAddress = (TextView)view.findViewById(R.id.textViewAddress);
        }
    }

}