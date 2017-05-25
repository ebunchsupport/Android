package com.dvn.vindecoder.ui.seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dvn.vindecoder.R;
import com.dvn.vindecoder.dto.GetVehicalDto;
import com.dvn.vindecoder.dto.SellerVehicalDetailDto;
import com.dvn.vindecoder.ui.BaseActivity;
import com.dvn.vindecoder.ui.SellerBaseActivity;
import com.dvn.vindecoder.ui.user.GetAllVehicalDetails;
import com.dvn.vindecoder.ui.user.UserDetail;
import com.dvn.vindecoder.util.AsyncCompleteListener;
import com.dvn.vindecoder.util.AsyncGetTask;
import com.dvn.vindecoder.util.CallType;
import com.dvn.vindecoder.util.CommonURL;
import com.dvn.vindecoder.util.PostResponse;
import com.dvn.vindecoder.util.UIUtility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class SellCarInfo extends SellerBaseActivity implements AsyncCompleteListener {
    private TextView textViewVinNum,textViewPrice,textViewMake,textViewYear,textViewModel
            ,textViewVType,textViewManuf,textViewWeightOnManu,textViewgvwr,textViewgvwrInLbs,textViewfrontgawr,
            textViewreargawr,textViewRecall_free,textViewIstire,textViewfronttire,textViewfrontrimsize,textViewfronttirepressure
            ,textViewfrontreartiresize,textViewrearrimsize,textViewreartirepressure,textView_is_epa,textView_vinplate,textView_emisssions,
            textView_speedo,textView_speedo_converter,textView_km_miles,textView_ri;
    private String imageUrl="http://ebunchapps.com/quickcross/assets/upload/vehicleinfo/";
    private  GetVehicalDto getVehicalDto;
    private ImageView imageViewBillCopy,imageViewManuFa,imageViewcopy_of_reg,imageViewcopy_of_recalldoc,imageViewimage_of_tire,
            imageView_image_frontdriverside,imageView_image_vinplate,imageView_image_rearandpassengerside,imageView_image_passengerseatbelt,
            imageView_image_passengerairbag,imageView_image_driverseatbelt,imageView_image_driverairbag,imageView_speedo_image;
    private CircleImageView profile_image;
    private CircleImageView profile_image1;

    private ProgressBar progressBarBillCopy,progressBarManuFa,progressBarcopy_of_reg,progressBarcopy_of_recalldoc,progressBarimage_of_tire,
            progressBar_image_frontdriverside,progressBar_image_vinplate,progressBar_image_rearandpassengerside,progressBar_image_passengerseatbelt,
            progressBar_image_passengerairbag,progressBar_image_driverseatbelt,progressBar_image_driverairbag,progressBar_speedo_image,progressBarBillVehicalImage,progressBarBillVehicalImage1,progressBar_image_Large;
   // private PhotoViewAttacher mAttacher;
    private Menu menu;
    int scrnPos;
   // private Toolbar toolbar;
    private List<SellerVehicalDetailDto> vehicledetails;

    private AsyncGetTask asyncGetTask;
    String value;
  //  private CollapsingToolbarLayout collapsingToolbarLayout = null;
     private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout=null;
    private RelativeLayout rel_img_conatiner=null;
    private ImageView all_img_container,clost_img_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawChildLayout();
        setTitle("Car information");
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.cordinat_main);
        rel_img_conatiner=(RelativeLayout)findViewById(R.id.img_conatiner) ;
        all_img_container=(ImageView)findViewById(R.id.all_img_container);
        clost_img_btn=(ImageView)findViewById(R.id.clost_img_btn);
        //mAttacher = new PhotoViewAttacher(all_img_container);
        rel_img_conatiner.setVisibility(View.GONE);
       /* collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);*/
        // If you later call mImageView.setImageDrawable/setImageBitmap/setImageResource/etc then you just need to call
        //mAttacher.update();
        //rel_img_conatiner.setVisibility(View.VISIBLE);
        ///SlideToAbove();
        showOverflowMenu(true);
        clost_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_img_conatiner.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
            }
        });

        setTextView();
        setImageView();
        setProgressBar();
        setScrollContent();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {

           if(bundle.containsKey("vin_id")) {
               value = bundle.getString("vin_id");
               scrnPos=bundle.getInt("screen_pos");
              /* if(bundle.containsKey("assign_user") && scrnPos==1)
               {
                   setFab();
               }*/
               getVehicalDto = new GetVehicalDto();
               getVehicalDto.setId(value);
               getVehicalDto.setAppid(CommonURL.APP_ID);
               asyncGetTask = new AsyncGetTask(SellCarInfo.this, CallType.GET_USER_VEHICAL_LIST, SellCarInfo.this, true, getVehicalDto);
               asyncGetTask.execute();
           }
            else if(bundle.containsKey("VehicalDetails"))
           {
               vehicledetails=new ArrayList<SellerVehicalDetailDto>  ();
               SellerVehicalDetailDto sellerVehicalDetailDto=(SellerVehicalDetailDto) getIntent().getSerializableExtra("VehicalDetails");
               vehicledetails.add(sellerVehicalDetailDto);
               if(vehicledetails.size()>0)
               {
                   insertDataInTextView();
                   insertDataInImageView();
               }

           }
        }

    }


    @Override
    public void drawChildLayout() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mChildLayout.addView(
                inflateChildLayout(R.layout.activity_sell_car_info),
                layoutParams);
    }

    @Override
    public void sendFbData() {

        String urlToShare = (imageUrl + vehicledetails.get(0).getImage());
       String sendText="Vin Number :"+vehicledetails.get(0).getVinno();
        sendText+="\nPrice :"+vehicledetails.get(0).getPrice();
        sendText+="\nMake :"+vehicledetails.get(0).getMake();
        sendText+="\nYear :"+vehicledetails.get(0).getYear();
        sendText+="\nModel :"+vehicledetails.get(0).getModel();
        sendText+="\nVehical Type :"+vehicledetails.get(0).getVehicleType();
        sendText+="\nManufacture Label:"+vehicledetails.get(0).getManufactureLable();
        sendText+="\nWeight on manufacture label :"+vehicledetails.get(0).getWeightOnManufacture();
        sendText+="\nGVRW :"+vehicledetails.get(0).getGvwr();
        sendText+="\nGVWR in LBS :"+vehicledetails.get(0).getGvwrinlbs();
        sendText+="\nFront GAWR :"+vehicledetails.get(0).getFrontgawr();
        sendText+="\nRear GAWR :"+vehicledetails.get(0).getReargawr();
        sendText+="\n RecalFree:"+vehicledetails.get(0).getRecallFree();
        sendText+="\n Tire Free:"+vehicledetails.get(0).getIstire();
        sendText+="\n Front Tire:"+vehicledetails.get(0).getFronttire();
        sendText+="\n Front trim sizeTire:"+vehicledetails.get(0).getFrontrimsize();

        sendText+="\n Front Tire Pressure:"+vehicledetails.get(0).getFronttirepressure();
        sendText+="\n Rear Rim Size:"+vehicledetails.get(0).getRearrimsize();
        sendText+="\n Rear Tire Pressure:"+vehicledetails.get(0).getReartirepressure();
        sendText+="\n Is EPA:"+vehicledetails.get(0).getIsEpa();


        sendText+="\n Vin Plate:"+vehicledetails.get(0).getVinplate();
        sendText+="\n Emisssions:"+vehicledetails.get(0).getEmisssions();
        sendText+="\n Speedo:"+vehicledetails.get(0).getSpeedo();
        sendText+="\n Speedo Converter:"+vehicledetails.get(0).getSpeedoConverter();

        sendText+="\n KM Miles:"+vehicledetails.get(0).getKmMiles();
        sendText+="\n RI:"+vehicledetails.get(0).getRi();


        try {
            Intent intent1 = new Intent();
            intent1.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
            intent1.setAction("android.intent.action.SEND");
            intent1.setType("text/plain");
            intent1.putExtra("android.intent.extra.TEXT", urlToShare+"\n"+sendText);
           // intent1.putExtra("android.intent.extra.TEXT", sendText);
            startActivity(intent1);
        } catch (Exception e) {
            // If we failed (not native FB app installed), try share through SEND
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl+"\n"+sendText));
           // intent.putExtra();
            ///intent.putExtra(Intent.ACTION_VIEW, Uri.parse(sendText));
            startActivity(intent);
        }

    }

    @Override
    public void ChkScreen() {
        if (scrnPos==1)
        {
            Intent intent=new Intent(this,SellerDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
         else if (scrnPos==2)
        {
            Intent intent=new Intent(this,UserDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


    public void setTextView()
    {
        textViewVinNum=(TextView)findViewById(R.id.textViewVinNum);
        textViewPrice=(TextView)findViewById(R.id.textViewPrice);
        textViewMake=(TextView)findViewById(R.id.textViewMake);
        textViewYear=(TextView)findViewById(R.id.textViewYear);
        textViewModel=(TextView)findViewById(R.id.textViewModel);
        textViewVType=(TextView)findViewById(R.id.textViewVType);
        textViewManuf=(TextView)findViewById(R.id.textViewManuf);
        textViewWeightOnManu=(TextView)findViewById(R.id.textViewWeightOnManu);
        textViewgvwr=(TextView)findViewById(R.id.textViewgvwr);
        textViewgvwrInLbs=(TextView)findViewById(R.id.textViewgvwrInLbs);
        textViewfrontgawr=(TextView)findViewById(R.id.textViewfrontgawr);
        textViewreargawr=(TextView)findViewById(R.id.textViewreargawr);
        textViewRecall_free=(TextView)findViewById(R.id.textViewRecall_free);
        textViewIstire=(TextView)findViewById(R.id.textViewIstire);
        textViewfronttire=(TextView)findViewById(R.id.textViewfronttire);
        textViewfrontrimsize=(TextView)findViewById(R.id.textViewfrontrimsize);
        textViewfronttirepressure=(TextView)findViewById(R.id.textViewfronttirepressure);
        textViewfrontreartiresize=(TextView)findViewById(R.id.textViewfrontreartiresize);
        textViewrearrimsize=(TextView)findViewById(R.id.textViewrearrimsize);
        textViewreartirepressure=(TextView)findViewById(R.id.textViewreartirepressure);
        textView_is_epa=(TextView)findViewById(R.id.textView_is_epa);
        textView_vinplate=(TextView)findViewById(R.id.textView_vinplate);
        textView_emisssions=(TextView)findViewById(R.id.textView_emisssions);
        textView_speedo=(TextView)findViewById(R.id.textView_speedo);
        textView_speedo_converter=(TextView)findViewById(R.id.textView_speedo_converter);
        textView_km_miles=(TextView)findViewById(R.id.textView_km_miles);
        textView_ri=(TextView)findViewById(R.id.textView_ri);
    }


    public void setImageView()
    {
        profile_image=(CircleImageView)findViewById(R.id.profile_image);
        profile_image1=(CircleImageView)findViewById(R.id.profile_image1);
        imageViewBillCopy=(ImageView)findViewById(R.id.imageViewBillCopy);
        imageViewManuFa=(ImageView)findViewById(R.id.imageViewManuFa);
        imageViewcopy_of_reg=(ImageView)findViewById(R.id.imageViewcopy_of_reg);
        imageViewcopy_of_recalldoc=(ImageView)findViewById(R.id.imageViewcopy_of_recalldoc);
        imageViewimage_of_tire=(ImageView)findViewById(R.id.imageViewimage_of_tire);
        imageView_image_frontdriverside=(ImageView)findViewById(R.id.imageView_image_frontdriverside);
        imageView_image_vinplate=(ImageView)findViewById(R.id.imageView_image_vinplate);
        imageView_image_rearandpassengerside=(ImageView)findViewById(R.id.imageView_image_rearandpassengerside);
        imageView_image_passengerseatbelt=(ImageView)findViewById(R.id.imageView_image_passengerseatbelt);
        imageView_image_passengerairbag=(ImageView)findViewById(R.id.imageView_image_passengerairbag);
        imageView_image_driverseatbelt=(ImageView)findViewById(R.id.imageView_image_driverseatbelt);
        imageView_image_driverairbag=(ImageView)findViewById(R.id.imageView_image_driverairbag);
        imageView_speedo_image   =(ImageView)findViewById(R.id.imageView_speedo_image);

       profile_image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               setLargeImageView((imageUrl + vehicledetails.get(0).getImage()));

           }
       });
        profile_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLargeImageView((imageUrl + vehicledetails.get(0).getImage()));

            }
        });
                imageViewBillCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLargeImageView((imageUrl + vehicledetails.get(0).getBillCopy()));
                    }
                });
        imageViewManuFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLargeImageView((imageUrl + vehicledetails.get(0).getImageOfManufacture()));
            }
        });
                imageViewcopy_of_reg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLargeImageView((imageUrl + vehicledetails.get(0).getCopyOfReg()));
                    }
                });
        imageViewcopy_of_recalldoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLargeImageView((imageUrl+vehicledetails.get(0).getCopyOfRecalldoc()));
            }
        });
                imageViewimage_of_tire.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLargeImageView((imageUrl + vehicledetails.get(0).getImageOfTire()));
                    }
                });
                imageView_image_frontdriverside.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLargeImageView(imageUrl + vehicledetails.get(0).getImageFrontdriverside());
                    }
                });
                        imageView_image_vinplate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setLargeImageView((imageUrl + vehicledetails.get(0).getImageVinplate()));
                            }
                        });
        imageView_image_rearandpassengerside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLargeImageView((imageUrl + vehicledetails.get(0).getImageRearandpassengerside()));
            }
        });
                imageView_image_passengerseatbelt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLargeImageView((imageUrl + vehicledetails.get(0).getImagePassengerseatbelt()));
                    }
                });
                imageView_image_passengerairbag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLargeImageView((imageUrl + vehicledetails.get(0).getImagePassengerairbag()));
                    }
                });
                        imageView_image_driverseatbelt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setLargeImageView((imageUrl + vehicledetails.get(0).getImageDriverseatbelt()));
                            }
                        });
        imageView_image_driverairbag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLargeImageView((imageUrl + vehicledetails.get(0).getImageDriverairbag()));
            }
        });
        imageView_speedo_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLargeImageView((imageUrl + vehicledetails.get(0).getSpeedoImage()));
            }
        });
    }


    private void setProgressBar()
    {
        progressBarBillVehicalImage=(ProgressBar)findViewById(R.id.progressBarBillVehicalImage);
        progressBarBillVehicalImage1=(ProgressBar)findViewById(R.id.progressBarBillVehicalImage1);
        progressBarBillCopy=(ProgressBar)findViewById(R.id.progressBarBillCopy);
        progressBarManuFa=(ProgressBar)findViewById(R.id.progressBarManuFa);
        progressBarcopy_of_reg=(ProgressBar)findViewById(R.id.progressBarcopy_of_reg);
        progressBarcopy_of_recalldoc=(ProgressBar)findViewById(R.id.progressBarcopy_of_recalldoc);
        progressBarimage_of_tire=(ProgressBar)findViewById(R.id.progressBarimage_of_tire);
        progressBar_image_frontdriverside=(ProgressBar)findViewById(R.id.progressBar_image_frontdriverside);
        progressBar_image_vinplate=(ProgressBar)findViewById(R.id.progressBar_image_vinplate);
        progressBar_image_rearandpassengerside=(ProgressBar)findViewById(R.id.progressBar_image_rearandpassengerside);
        progressBar_image_passengerseatbelt=(ProgressBar)findViewById(R.id.progressBar_image_passengerseatbelt);
        progressBar_image_passengerairbag=(ProgressBar)findViewById(R.id.progressBar_image_passengerairbag);
        progressBar_image_driverseatbelt=(ProgressBar)findViewById(R.id.progressBar_image_driverseatbelt);
        progressBar_image_driverairbag=(ProgressBar)findViewById(R.id.progressBar_image_driverairbag);
        progressBar_speedo_image=(ProgressBar)findViewById(R.id.progressBar_speedo_image);
        progressBar_image_Large=(ProgressBar)findViewById(R.id.progressBar_image_Large);
    }
    @Override
    public void onAsyncCompleteListener(PostResponse aMasterDataDtos, CallType type) {
        if (type == CallType.GET_USER_VEHICAL_LIST) {

            //Log.e("Show Error Type",""+aMasterDataDtos.getResponseCode());
            // Log.e("Show Error Type",""+aMasterDataDtos.getMessage());

            if(aMasterDataDtos.getResponseCode()==1)
            {
                vehicledetails=aMasterDataDtos.getVehicledetails();
                if(vehicledetails.size()>0)
                {
                    insertDataInTextView();
                    insertDataInImageView();
                }
                else {
                    openDialoug();

                }

            }
            else {
                Toast.makeText(this,aMasterDataDtos.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"Data Not Found", Toast.LENGTH_SHORT).show();
        }
    }


    public void insertDataInTextView()
    {
        textViewVinNum.setText(vehicledetails.get(0).getVinno());
        textViewPrice.setText(vehicledetails.get(0).getPrice());
        textViewMake.setText(vehicledetails.get(0).getMake());
        textViewYear.setText(vehicledetails.get(0).getYear());
        textViewModel.setText(vehicledetails.get(0).getModel());
        textViewVType.setText(vehicledetails.get(0).getVehicleType());
        textViewManuf.setText(vehicledetails.get(0).getManufactureLable());
        textViewWeightOnManu.setText(vehicledetails.get(0).getWeightOnManufacture());
        textViewgvwr.setText(vehicledetails.get(0).getGvwr());
        textViewgvwrInLbs.setText(vehicledetails.get(0).getGvwrinlbs());
        textViewfrontgawr.setText(vehicledetails.get(0).getFrontgawr());
        textViewreargawr.setText(vehicledetails.get(0).getReargawr());
        textViewRecall_free.setText(vehicledetails.get(0).getRecallFree());
        textViewIstire.setText(vehicledetails.get(0).getIstire());
        textViewfronttire.setText(vehicledetails.get(0).getFronttire());
        textViewfrontrimsize.setText(vehicledetails.get(0).getFrontrimsize());
        textViewfronttirepressure.setText(vehicledetails.get(0).getFronttirepressure());
        textViewfrontreartiresize.setText(vehicledetails.get(0).getReartiresize());
        textViewrearrimsize.setText(vehicledetails.get(0).getRearrimsize());
        textViewreartirepressure.setText(vehicledetails.get(0).getReartirepressure());
        textView_is_epa.setText(vehicledetails.get(0).getIsEpa());
        textView_vinplate.setText(vehicledetails.get(0).getVinplate());
        textView_emisssions.setText(vehicledetails.get(0).getEmisssions());
        textView_speedo.setText(vehicledetails.get(0).getSpeedo());
        textView_speedo_converter.setText(vehicledetails.get(0).getSpeedoConverter());
        textView_km_miles.setText(vehicledetails.get(0).getKmMiles());
        textView_ri.setText(vehicledetails.get(0).getRi());
    }

    public void insertDataInImageView()
    {

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImage()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBarBillVehicalImage.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBarBillVehicalImage.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_image);
        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImage()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBarBillVehicalImage1.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBarBillVehicalImage1.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile_image1);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getBillCopy()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBarBillCopy.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBarBillCopy.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewBillCopy);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImageOfManufacture()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBarManuFa.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBarManuFa.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewManuFa);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getCopyOfReg()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBarcopy_of_reg.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBarcopy_of_reg.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewcopy_of_reg);

//
        Glide.with(SellCarInfo.this).load((imageUrl+vehicledetails.get(0).getCopyOfRecalldoc()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBarcopy_of_recalldoc.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBarcopy_of_recalldoc.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewcopy_of_recalldoc);
//
        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImageOfTire()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBarimage_of_tire.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBarimage_of_tire.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewimage_of_tire);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImageFrontdriverside()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar_image_frontdriverside.setVisibility(View.GONE);
                        imageView_image_frontdriverside.setImageDrawable(getResources().getDrawable(R.drawable.add_camera));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar_image_frontdriverside.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView_image_frontdriverside);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImageVinplate()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar_image_vinplate.setVisibility(View.GONE);
                        imageView_image_vinplate.setImageDrawable(getResources().getDrawable(R.drawable.add_camera));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar_image_vinplate.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView_image_vinplate);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImageRearandpassengerside()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar_image_rearandpassengerside.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar_image_rearandpassengerside.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView_image_rearandpassengerside);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImagePassengerseatbelt()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar_image_passengerseatbelt.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar_image_passengerseatbelt.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView_image_passengerseatbelt);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImagePassengerairbag()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar_image_passengerairbag.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar_image_passengerairbag.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView_image_passengerairbag);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImageDriverseatbelt()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar_image_driverseatbelt.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar_image_driverseatbelt.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView_image_driverseatbelt);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getImageDriverairbag()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar_image_driverairbag.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar_image_driverairbag.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView_image_driverairbag);

        Glide.with(SellCarInfo.this).load((imageUrl + vehicledetails.get(0).getSpeedoImage()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar_speedo_image.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar_speedo_image.setVisibility(View.GONE);
                        return false;
                    }
                }).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView_speedo_image);

    }


    private void setCollospingToolBar()
    {
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Quick Cross");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


    }
        public void setLargeImageView(String img_url)
        {
            UIUtility.showDialog(SellCarInfo.this, img_url);
        }
    @Override
    public void onBackPressed()
    {
        if(rel_img_conatiner.isShown())
        {
            coordinatorLayout.setEnabled(true);
            rel_img_conatiner.setVisibility(View.GONE);
            return;
        }
        else {
            if (scrnPos==1)
            {
                Intent intent=new Intent(this,SellerDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if (scrnPos==2)
            {
                Intent intent=new Intent(this,UserDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
        super.onBackPressed();
    }



    public void showOverflowMenu(boolean showMenu){
        /*menu=navigationView.getMenu();
        menu.setGroupVisible(R.id.share_fb_group, showMenu);*/
    }


    private void setScrollContent()
    {

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        final RelativeLayout img_container1=(RelativeLayout)findViewById(R.id.img_container1);
        // load the animation
        final Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fadein);
        // start the animation
        // set animation listener
        animFadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
           /* if(img_container1.getVisibility() == View.VISIBLE)
            {
                img_container1.setVisibility(View.GONE);
            }*/
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                   // Log.e("val","-1  choti imge ko hide kerna h");
                    img_container1.setVisibility(View.GONE);
                }
                if (scrollRange + verticalOffset == 0) {
                  //  collapsingToolbarLayout.setTitle("Title");
                   // Log.e("val","0 choti imge ko show kerna h");
                    img_container1.setVisibility(View.VISIBLE);
                    img_container1.startAnimation(animFadein);
                    isShow = true;
                } else if(isShow) {
                   // collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    //Log.e("val","Showing choti imge ko hide kerna h");
                    img_container1.setVisibility(View.GONE);

                    isShow = false;
                }
            }
        });
    }

    public void openDialoug(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("No data available at this moment");
        alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                onBackPressed();
                            }

                        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setFab()
    {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        NestedScrollView scroll_view=(NestedScrollView)findViewById(R.id.scroll_view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellCarInfo.this, GetAllVehicalSellerDetails.class);
                intent.putExtra("user_type",1);
                intent.putExtra("v_id",vehicledetails.get(0).getPkId());
                startActivity(intent);
            }
        });
    }
}
