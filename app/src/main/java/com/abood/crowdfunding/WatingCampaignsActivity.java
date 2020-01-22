package com.abood.crowdfunding;

import androidx.fragment.app.Fragment;

public class WatingCampaignsActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {

        return new WaitingCampaignsFragments();

    }

}
