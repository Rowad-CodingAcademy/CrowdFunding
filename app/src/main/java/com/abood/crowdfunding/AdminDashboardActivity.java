package com.abood.crowdfunding;

import androidx.fragment.app.Fragment;

public class AdminDashboardActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {

        return new AdminDashboardFragment();

    }

}
