package com.abood.crowdfunding;

import androidx.fragment.app.Fragment;

public class AddCampaignActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_ID =
            "com.Abood.android.Blog.post_id";

    @Override
    protected Fragment createFragment() {

        return new AddCampaignFragment();

    }

}
