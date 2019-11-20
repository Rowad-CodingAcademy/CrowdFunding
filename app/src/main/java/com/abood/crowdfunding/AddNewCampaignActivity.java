package com.abood.crowdfunding;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class AddNewCampaignActivity extends AppCompatActivity {

    NonSwipeableViewPager viewPager;
    PageAdapter pageAdapter;

    CoordinatorLayout coordinator;
    LinearLayout layout_dots;

    ImageButton nextBtn,previousBtn;
    Button skipBtn, finishBtn;

    int page = 0;   //  to track page position


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_campaign);

        pageAdapter = new PageAdapter(getSupportFragmentManager());

        nextBtn = findViewById(R.id.intro_btn_next);
        previousBtn = findViewById(R.id.intro_btn_previous);
        skipBtn = findViewById(R.id.intro_btn_skip);
        finishBtn = findViewById(R.id.intro_btn_finish);
        coordinator = findViewById(R.id.main_content);
        layout_dots = findViewById(R.id.image_layout_dots);


        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.container);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(page);
        addBottomDots(layout_dots, page);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                page = position;

                addBottomDots(layout_dots, position);
                nextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                finishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);

                previousBtn.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                skipBtn.setVisibility(position == 0 ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                viewPager.setCurrentItem(page, true);
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page -= 1;
                viewPager.setCurrentItem(page, true);
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMain = new Intent(AddNewCampaignActivity.this, LoginActivity.class);
                startActivity(toMain);
                finish();

            }
        });


    }


    private void addBottomDots(LinearLayout layout_dots, int current) {
        ImageView[] dots = new ImageView[3];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 0, 10, 0);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            dots[i].setColorFilter(ContextCompat.getColor(this, R.color.grey_40), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
            dots[current].setColorFilter(ContextCompat.getColor(this, R.color.grey_40), PorterDuff.Mode.SRC_ATOP);
        }
    }

//    public void goToNextPage() {
//        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//    }

}
