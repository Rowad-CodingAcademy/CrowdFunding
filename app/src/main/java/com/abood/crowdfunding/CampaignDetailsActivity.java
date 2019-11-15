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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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
    Campaign campaign;
    Users user;
    int mDonationRatio;
    int mDaysToGo;
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

        //campaignPhotoes=campaign.getCampImageUrl();

        campaignPhotoes = new ArrayList<>();
        campaignPhotoes.add("https://inteng-storage.s3.amazonaws.com/img/iea/nR6bV9jp6o/sizes/learntocodebundle_resize_md.jpg");
        campaignPhotoes.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZhJjWOsPN9z6ADOSviiGDA19FIVaIhsZgI0Sr1vFet5L0mu8Kzg&s");
        campaignPhotoes.add("https://www.codingbytes.com/wp-content/uploads/2019/04/Learn-coding-online.jpeg");

        campaign=new Campaign("Campaign Title","Loot of Lima is a multiplayer deduction puzzle, where you ask questions and piece together information to find buried treasure before your opponents.\n" +
                "\n" +
                "Deduction games are games like Clue. In Clue, you try to figure out the who/where/how of a murder. To do so, you eliminate all whos/wheres/hows that are not possible answers.\n" +
                "\n" +
                "Loot of Lima is similar, except:\n" +
                "\n" +
                "1. Loot of Lima uses a clever coordinate system to give you the sense of searching different spots on Cocos Island.\n" +
                "\n" +
                "2. Loot of Lima is much harder. Not harder to learn, but harder to solve the head scratching puzzle of where the treasure is buried"
                ,new Date(),new Date(),campaignPhotoes,27,10000.0,3000.0);
        //mTitleTV.setText(campaign.getCampTitle());

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

        mDonationRatio=new Integer(String.valueOf(Math.round((campaign.getCampDonated()*100)/campaign.getCampCost())));

        mDonationRatioTV.setText(mDonationRatio+"%");

        setUpProgressBar();

        mDonorsTV.setText(String.valueOf(campaign.getCampDownersNum()));

/*
        mDaysToGo=getDaysDifference(campaign.getCampStart(),campaign.getCampEnd());
        if(mDaysToGo==0)
        {
            mDaysToGoImageView.setImageResource(R.drawable.ic_alarm_on_black_24dp);
            mDaysToGoTV.setText("finished");
        }
        else
        {
            mDaysToGoTV.setText(mDaysToGo);

        }
*/
        mDescriptionTV.setText(campaign.getCampDescription());

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

}

