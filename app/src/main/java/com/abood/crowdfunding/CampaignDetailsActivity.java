package com.abood.crowdfunding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CampaignDetailsActivity extends AppCompatActivity
{

    public  static  final String EXTRA_CAMPAIGN_UUID="com.abood.crowdfunding.campaignId";
    private TextView mTitleTV,ownerNameTV,mDonationRatioTV,mDonorsTV,mDaysToGoTV,mDescriptionTV;
    private ViewPager mPhotoViewPager;
    LinearLayout layout_dots;
    Button donateBtn;
    ImageView ownerPhotoImageView,mDaysToGoImageView;
    private ProgressBar progress_determinate;
    UUID campaignId;
    Campaigns campaigns;
    Users user;
    int mDonationRatio;
    int mDaysToGo;
    String dateDiff;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    private ArrayList<String> campaignPhotoes;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_details);

        mTitleTV=findViewById(R.id.campaign_title_textView);
        mPhotoViewPager = findViewById(R.id.campaign_photos_viewPager);
        layout_dots =  findViewById(R.id.layout_dots);
        donateBtn=findViewById(R.id.donate_btn);
        ownerPhotoImageView=findViewById(R.id.owner_photo_circleImageView);
        ownerNameTV=findViewById(R.id.owner_name_textView);
        progress_determinate =  findViewById(R.id.progress_determinate);
        mDonationRatioTV=findViewById(R.id.campaign_ratio_textView);
        mDonorsTV=findViewById(R.id.campaign_donors_textView);
        mDaysToGoTV=findViewById(R.id.campaign_daysToGo_textView);
        mDaysToGoImageView=findViewById(R.id.campaign_daysToGo_imageView);
        mDescriptionTV=findViewById(R.id.campaign_description_textView);


        campaignId = (UUID) getIntent().getSerializableExtra(EXTRA_CAMPAIGN_UUID);

        //campaignPhotoes=campaigns.getCampImageUrl();

        campaignPhotoes = new ArrayList<>();
        campaignPhotoes.add("https://inteng-storage.s3.amazonaws.com/img/iea/nR6bV9jp6o/sizes/learntocodebundle_resize_md.jpg");
        campaignPhotoes.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZhJjWOsPN9z6ADOSviiGDA19FIVaIhsZgI0Sr1vFet5L0mu8Kzg&s");
        campaignPhotoes.add("https://www.codingbytes.com/wp-content/uploads/2019/04/Learn-coding-online.jpeg");


        setUpViewPager();

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = DonationActivity.newIntent(CampaignDetailsActivity.this,campaignId);
                startActivity(i);
            }
        });


        user=new Users("Bushra","https://www.clipartwiki.com/clipimg/detail/174-1742152_computer-icons-user-clip-art-transparent-user-icon.png");

        Glide.with(CampaignDetailsActivity.this)
                .load(user.getuPhotoUrl())
                .asBitmap()
                .centerCrop()
                .error(R.color.grey_20)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(ownerPhotoImageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(CampaignDetailsActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius((float) 10); // radius for corners
                        view.setImageDrawable(circularBitmapDrawable);
                    }
                });



        ownerNameTV.setText(user.getuName());

        mDonationRatio=new Integer(String.valueOf(Math.round((0.3*100)/ 3000)));

        mDonationRatioTV.setText(mDonationRatio+"%");

        setUpProgressBar();

        mDonorsTV.setText("3000");

        //dateDiff=getDateDifference(getFormattedDate(campaigns.getCampEnd()));
        dateDiff=getDateDifference("2019-11-18-11-30-10");

        if(dateDiff.matches("finished"))
        {
            mDaysToGoImageView.setImageResource(R.drawable.ic_alarm_on_black_24dp);
            mDaysToGoTV.setText(dateDiff);
        }
        else
        {
            mDaysToGoTV.setText(dateDiff);
        }



        mDescriptionTV.setText("Android is a mobile operating system based on a modified version of the Linux kernel and " +
                "other open source software, designed primarily for touchscreen mobile devices such as smartphones and " +
                "tablets. Android is developed by a consortium of developers known as the Open Handset Alliance, with the " +
                "main contributor and commercial marketer being Google.[10] Initially developed by Android Inc., which Google " +
                "bought in 2005, Android was unveiled in 2007, with the first commercial Android device launched in September 2008. " +
                "The current stable version is Android 10, released on September 3, 2019. The core Android source code is known as " +
                "Android Open Source Project (AOSP), which is primarily licensed under the Apache License. This has allowed variants " +
                "of Android to be developed on a range of other electronics, such as game consoles, digital cameras, PCs and others, " +
                "each with a specialized user interface. Some well known derivatives include Android TV for televisions and Wear OS " +
                "for wearables, both developed by Google.");

    }
    public static Intent newIntent(Context packageContext, UUID campaignId)
    {
        Intent intent = new Intent(packageContext, CampaignDetailsActivity.class);
        intent.putExtra(EXTRA_CAMPAIGN_UUID,  campaignId);
        return intent;
    }

    private void setUpViewPager()
    {

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentStatePagerAdapter adapter=new FragmentStatePagerAdapter(fragmentManager)
        {
            @Override
            public Fragment getItem(int position)
            {
                String photoUrl = campaignPhotoes.get(position);
                return PhotoFragment.newInstance(photoUrl);
            }
            @Override
            public int getCount() {
                return campaignPhotoes.size();
            }
        };
        mPhotoViewPager.setAdapter(adapter);
        startAutoSlider(adapter.getCount());
        addBottomDots(layout_dots, adapter.getCount(), 0);
        mPhotoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos)
            {
                addBottomDots(layout_dots, adapter.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setUpProgressBar()
    {
        progress_determinate.setProgress(mDonationRatio);

        // Get the Drawable custom_progressbar
        Drawable draw=getResources().getDrawable(R.drawable.donated_progressbar);
        // set the drawable as progress drawable
        progress_determinate.setProgressDrawable(draw);
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = mPhotoViewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                mPhotoViewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current)
    {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }


        String getDateDifference(String  endDate)
    {


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        Date currentDate = new Date();
        try {
            Date eDate;

            eDate = dateFormat.parse(endDate);

            Long diff = eDate.getTime() - currentDate.getTime();


            if (diff<= 0)
            {
                dateDiff="finished";

            } else {
                long days = (int) TimeUnit.MILLISECONDS.toDays(diff);
                if (days != 0) {
                    dateDiff= days + "Days ";

                }
                long hours = (int) (TimeUnit.MILLISECONDS.toHours(diff) - TimeUnit.DAYS.toHours(days));

                if (hours != 0) {
                    dateDiff= hours + "Hours ";

                }
                long minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hours)));

                if (minutes != 0) {
                    dateDiff= minutes + "Minutes ";

                }

                long seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(diff) - TimeUnit.MINUTES.toSeconds(minutes));

                if (seconds != 0) {
                    dateDiff= seconds +"Seconds";

                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateDiff;
    }

    String getFormattedDate(Date date)
    {
        long year=date.getYear();
        long month=date.getMonth();
        long day=date.getDay();
        long hour=date.getHours();
        long minute=date.getMinutes();
        long second=date.getSeconds();
        return year+"-"+month+"-"+day+"-"+hour+"-"+minute+"-"+second;
    }




}

