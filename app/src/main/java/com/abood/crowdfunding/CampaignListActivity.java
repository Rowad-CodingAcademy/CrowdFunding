package com.abood.crowdfunding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class CampaignListActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    CrowdFundingViewPager adapter;
    int tab;
    private static final String MyPREFERENCES = "Abood";
    private SharedPreferences sharedpreferences;

    FlutterFragment flutterFragment;
    KotlinFragment kotlinFragment;
    AndroidFragment androidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_campaign_list);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tablayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
                tab = tabLayout.getSelectedTabPosition();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_post_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.new_crime:

//                updateUI();

                Intent intent = new Intent(CampaignListActivity.this,AddCampaignActivity.class);
                intent.putExtra("type",tab);
                startActivity(intent);

                return true;

            case R.id.logout:

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                sharedpreferences.edit().remove("username").apply();
                Intent intent2 = new Intent(CampaignListActivity.this,LoginActivity.class);
                startActivity(intent2);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new CrowdFundingViewPager(getSupportFragmentManager());
        flutterFragment = new FlutterFragment();
        kotlinFragment = new KotlinFragment();
        androidFragment = new AndroidFragment();
        adapter.addFragment(flutterFragment,"CALLS");
        adapter.addFragment(kotlinFragment,"CHAT");
        adapter.addFragment(androidFragment,"CONTACTS");
        viewPager.setAdapter(adapter);
    }

}
