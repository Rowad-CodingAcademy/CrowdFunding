package com.abood.crowdfunding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class AdminDashboardFragment extends Fragment {

    private ViewPager viewPager;
    CampaignPagerAdapter adapter;
    private TabLayout tabLayout;

    ApproveNewCampaignsFragment approveNewCampaignsFragment;
    ManageCampaignsFragment manageCampaignsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.activity_admin_campaign_list, container, false);


        viewPager = v.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = v.findViewById(R.id.tablayout);
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return v;

    }


    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new CampaignPagerAdapter(getActivity().getSupportFragmentManager());
        approveNewCampaignsFragment = new ApproveNewCampaignsFragment();
        manageCampaignsFragment = new ManageCampaignsFragment();
        adapter.addFragment(approveNewCampaignsFragment,"APPROVE");
        adapter.addFragment(manageCampaignsFragment,"MANAGE");
        viewPager.setAdapter(adapter);
    }

}
