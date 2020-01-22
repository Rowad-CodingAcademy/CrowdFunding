package com.abood.crowdfunding;

import androidx.fragment.app.Fragment;

public class RejectedCampaignsActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {

        return new RejectedCampaignsFragments();

    }

}
