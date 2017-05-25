package com.dvn.vindecoder.adaptor;

/**
 * Created by palash on 18-01-2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.SellerVehicalListDto;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SellerVehicalAdaptor extends RecyclerView.Adapter<SellerVehicalAdaptor.ViewHolder> {
    private List<SellerVehicalListDto> countries;
    private Context mContext;

    public SellerVehicalAdaptor(List<SellerVehicalListDto> countries, Context mContext) {
        this.countries = countries;
        this.mContext=mContext;
    }

    @Override
    public SellerVehicalAdaptor.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_car_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SellerVehicalAdaptor.ViewHolder viewHolder, int i) {

        //viewHolder.tv_country.setText(countries.get(i));
        viewHolder.textViewVinNum.setText(countries.get(i).getVinno());
        viewHolder.textViewMake.setText(countries.get(i).getMake());
        viewHolder.textViewTransist.setText(countries.get(i).getModel());
        viewHolder.textViewYear.setText(countries.get(i).getYear());
        if(i==countries.size()-1) {
            viewHolder.linearLayout.setVisibility(View.GONE);
        }
        String user_name;
        if (countries.get(i).getUsername()!=null)
        {
            if(!countries.get(i).getUsername().equalsIgnoreCase("null") || !countries.get(i).getUsername().trim().equalsIgnoreCase(""))
            {
                user_name=countries.get(i).getUsername();
            }
            else {
                user_name="";
            }
        }
        else {
            user_name="";
        }
        if(!countries.get(i).getAssign_user().equalsIgnoreCase("0"))
        {
            if(countries.get(i).getStatus().equalsIgnoreCase("1"))
            {
                if(user_name.equalsIgnoreCase(""))
                {
                    viewHolder.txt_status.setText(R.string.car_in_process);
                }
                else
                {
                    viewHolder.txt_status.setText(R.string.car_in_process);
                    viewHolder.txt_status.append(" by "+user_name);
                }
                viewHolder.txt_status.setTextColor(mContext.getResources().getColor(R.color.Yellow_color));
                viewHolder.textViewYear.setTextColor(mContext.getResources().getColor(R.color.Yellow_color));
                viewHolder.img_status.setImageResource(R.drawable.unassigned_icon);
            }
            else if(countries.get(i).getStatus().equalsIgnoreCase("2"))
            {

                if(user_name.equalsIgnoreCase(""))
                {
                    viewHolder.txt_status.setText(R.string.car_complete);
                }
                else
                {
                    viewHolder.txt_status.setText(R.string.car_complete);
                    viewHolder.txt_status.append(" by "+user_name);

                }
               viewHolder.txt_status.setTextColor(mContext.getResources().getColor(R.color.green_color));
                viewHolder.textViewYear.setTextColor(mContext.getResources().getColor(R.color.green_color));
                viewHolder.img_status.setImageResource(R.drawable.assigned_icon);
            }
            else if(countries.get(i).getStatus().equalsIgnoreCase("3"))
            {
                if(user_name.equalsIgnoreCase(""))
                {
                    viewHolder.txt_status.setText(R.string.car_reject);
                }
                else
                {
                    viewHolder.txt_status.setText(R.string.car_reject);
                    viewHolder.txt_status.append(" by "+user_name);
                }
                viewHolder.txt_status.setTextColor(mContext.getResources().getColor(R.color.background_color_bright_red));
                viewHolder.textViewYear.setTextColor(mContext.getResources().getColor(R.color.background_color_bright_red));
                viewHolder.img_status.setImageResource(R.drawable.unassigned_icon);
            }
            else {
                if(user_name.equalsIgnoreCase(""))
                {
                    viewHolder.txt_status.setText(R.string.car_assign);
                }
                else
                {
                    viewHolder.txt_status.setText(R.string.car_assign);
                    viewHolder.txt_status.append(" to "+user_name);
                }
               viewHolder.txt_status.setTextColor(mContext.getResources().getColor(R.color.fb_bg_color));
                viewHolder.textViewYear.setTextColor(mContext.getResources().getColor(R.color.fb_bg_color));
                viewHolder.img_status.setImageResource(R.drawable.assigned_icon);
            }
        }
        else
        {
            viewHolder.txt_status.setText(R.string.car_unassign);
           viewHolder.txt_status.setTextColor(mContext.getResources().getColor(R.color.color_gray));
            viewHolder.textViewYear.setTextColor(mContext.getResources().getColor(R.color.color_gray));
            viewHolder.img_status.setImageResource(R.drawable.unassigned_icon);
        }
       /* String text_val=viewHolder.txt_status.getText().toString();
        viewHolder.txt_status.setText(StringUtils.capitalize(text_val.toLowerCase().trim()));*/
        String text_val=viewHolder.txt_status.getText().toString();
        String [] val=text_val.split(" ");
        viewHolder.txt_status.setText(StringUtils.capitalize(text_val.toLowerCase().trim()));
        if(!text_val.isEmpty() || val.length>0) {

            for (int i1 = 0; i1 < val.length; i1++) {
                String upperString = val[i1].substring(0,1).toUpperCase() + val[i1].substring(1);
            if(i1==0)
            {
                viewHolder.txt_status.setText(upperString + " ");
            }
            else if (i1==val.length-1)
            {
                viewHolder.txt_status.append(upperString);
            }
            else {
                viewHolder.txt_status.append(upperString+ " ");
            }
            }

        }


        Glide.with(mContext).load((countries.get(i).getImage()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        viewHolder.progressBar2.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //progressBarBillVehicalImage.setVisibility(View.GONE);
                        viewHolder.progressBar2.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.vehical_image);

    }


    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_country;
        private TextView textViewVinNum,textViewMake,textViewTransist,textViewYear,txt_status;
        private ImageView vehical_image,img_status;
        private LinearLayout linearLayout;
        private ProgressBar progressBar2;
        public ViewHolder(View view) {
            super(view);

           // tv_country = (TextView)view.findViewById(R.id.tv_country);
            textViewVinNum = (TextView)view.findViewById(R.id.textViewVinNum);
            textViewMake = (TextView)view.findViewById(R.id.textViewMake);
            textViewTransist = (TextView)view.findViewById(R.id.textViewTransist);
            textViewYear = (TextView)view.findViewById(R.id.textViewYear);
            vehical_image=(ImageView)view.findViewById(R.id.vehical_image);
            img_status=(ImageView)view.findViewById(R.id.img_status);
            txt_status=(TextView)view.findViewById(R.id.txt_status);
            linearLayout=(LinearLayout) view.findViewById(R.id.linear1);
            progressBar2=(ProgressBar) view.findViewById(R.id.progressBar2);
        }
    }

}