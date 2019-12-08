package com.abood.crowdfunding;

import androidx.fragment.app.Fragment;

public class UserProfileActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {

        return new UserProfileFragment();

    }

}
