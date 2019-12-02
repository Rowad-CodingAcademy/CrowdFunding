package com.abood.crowdfunding;

import androidx.fragment.app.Fragment;

public class ClosedCampaignsActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {

        return new ClosedCampaignsFragment();

    }

}
