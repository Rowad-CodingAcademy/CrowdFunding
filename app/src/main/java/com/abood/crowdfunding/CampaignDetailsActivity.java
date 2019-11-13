package com.abood.crowdfunding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import java.io.Serializable;
import java.util.ArrayList;

public class CampaignDetailsActivity extends AppCompatActivity
{

    public  static  final String EXTRA_CAMPAIGN="com.abood.crowdfunding.campaign";
    private ViewPager mViewPager;
    LinearLayout layout_dots;
    Campaigns campaign;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    private ArrayList<String> campaignPhotoes;
    private ProgressBar progress_determinate;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_details);
        mViewPager = findViewById(R.id.campaign_view_pager);
        layout_dots =  findViewById(R.id.layout_dots);
        progress_determinate =  findViewById(R.id.progress_determinate);


        campaign = (Campaigns) getIntent().getSerializableExtra(EXTRA_CAMPAIGN);
        campaignPhotoes = new ArrayList<>();
        campaignPhotoes.add("https://inteng-storage.s3.amazonaws.com/img/iea/nR6bV9jp6o/sizes/learntocodebundle_resize_md.jpg");
        campaignPhotoes.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZhJjWOsPN9z6ADOSviiGDA19FIVaIhsZgI0Sr1vFet5L0mu8Kzg&s");
        campaignPhotoes.add("https://www.codingbytes.com/wp-content/uploads/2019/04/Learn-coding-online.jpeg");
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
        mViewPager.setAdapter(adapter);

        startAutoSlider(adapter.getCount());
        addBottomDots(layout_dots, adapter.getCount(), 0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        runProgressDeterminate();
        // Get the Drawable custom_progressbar
        Drawable draw=getResources().getDrawable(R.drawable.donated_progressbar);
        // set the drawable as progress drawable
        progress_determinate.setProgressDrawable(draw);

    }
    public static Intent newIntent(Context packageContext, Campaigns campaign)
    {
        Intent intent = new Intent(packageContext, CampaignDetailsActivity.class);
        intent.putExtra(EXTRA_CAMPAIGN, (Serializable) campaign);
        return intent;
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = mViewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                mViewPager.setCurrentItem(pos);
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

    private void runProgressDeterminate() {
        final Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                int progress = progress_determinate.getProgress() + 10;
                progress_determinate.setProgress(progress);
                if (progress > 100) {
                    progress_determinate.setProgress(0);
                }
                mHandler.postDelayed(this, 1000);
            }
        };
        mHandler.post(runnable);
    }
}

