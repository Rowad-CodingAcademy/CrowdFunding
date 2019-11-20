package com.abood.crowdfunding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {


    private int numOfTabs;

    public  PageAdapter (FragmentManager fm) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FirstCampaignInfoFragment();
            case 1:
                return new SecondCampaignInfoFragment();
            case 2:
                return new ThirdCampaignInfoFragment();
            default:
                return null;
        }
    }
}
