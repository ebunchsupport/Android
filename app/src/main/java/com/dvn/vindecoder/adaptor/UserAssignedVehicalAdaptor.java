package com.dvn.vindecoder.adaptor;

/**
 * Created by palash on 18-01-2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.UserAssingCarListDto;

import java.util.List;

public class UserAssignedVehicalAdaptor extends RecyclerView.Adapter<UserAssignedVehicalAdaptor.ViewHolder> {
    private List<UserAssingCarListDto> countries;
    private Context mContext;


    public UserAssignedVehicalAdaptor(List<UserAssingCarListDto> countries, Context mContext) {
        this.countries = countries;
        this.mContext=mContext;
    }

    @Override
    public UserAssignedVehicalAdaptor.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_my_car_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserAssignedVehicalAdaptor.ViewHolder viewHolder, int i) {
        //viewHolder.tv_country.setText(countries.get(i));
      /*  Drawable background = viewHolder.view.getBackground();
        int color = Color.TRANSPARENT;
        if (background instanceof ColorDrawable)
           color = ((ColorDrawable) background).getColor();*/
        if(countries.get(i).getOneTimeIsselected()==false) {
            Log.e("onetime",""+i);
           /* if (!countries.get(i).isSelected() && color == Color.CYAN) {
              //  Log.e("val", "selected");
                viewHolder.view.setBackgroundColor(Color.WHITE);
            }*/
            viewHolder.view.setBackgroundColor(Color.WHITE);
            countries.get(i).setOneTimeIsselected(true);
        }
        String vid=countries.get(i).getId();
        viewHolder.textViewVin_number.setText(countries.get(i).getVinno());
        viewHolder.textViewVinNum.setText("$ "+countries.get(i).getPrice());
        viewHolder.textViewMake.setText(countries.get(i).getMake());
        viewHolder.textViewTransist.setText(countries.get(i).getModel());
        viewHolder.textViewYear.setText(countries.get(i).getYear());
       // viewHolder.view.setBackgroundColor(countries.get(i).isSelected() ? Color.CYAN : Color.WHITE);
        Glide.with(mContext).load((countries.get(i).getImage()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        //progressBarBillVehicalImage.setVisibility(View.GONE);
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
        private TextView textViewVinNum,textViewMake,textViewTransist,textViewYear,textViewVin_number;
        private ImageView vehical_image;
        private View view;
        private ProgressBar progressBar2;
        public ViewHolder(View item_view) {
            super(item_view);
            view=item_view;
           // tv_country = (TextView)view.findViewById(R.id.tv_country);
            textViewVin_number = (TextView)view.findViewById(R.id.textViewVin_number);
            textViewVinNum = (TextView)view.findViewById(R.id.textViewVinNum);
            textViewMake = (TextView)view.findViewById(R.id.textViewMake);
            textViewTransist = (TextView)view.findViewById(R.id.textViewTransist);
            textViewYear = (TextView)view.findViewById(R.id.textViewYear);
            vehical_image=(ImageView)view.findViewById(R.id.vehical_image);
            progressBar2=(ProgressBar) view.findViewById(R.id.progressBar2);
        }
    }

}