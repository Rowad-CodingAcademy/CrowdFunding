package com.abood.crowdfunding;

import androidx.fragment.app.Fragment;

public class CampaignsListActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {

        return new CampaignsListFragment();

    }

}
