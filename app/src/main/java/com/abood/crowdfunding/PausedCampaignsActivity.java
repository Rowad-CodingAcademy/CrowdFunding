package com.abood.crowdfunding;

import androidx.fragment.app.Fragment;

public class PausedCampaignsActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {

        return new PausedCampaignsFragment();

    }

}
