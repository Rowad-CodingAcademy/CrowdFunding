package com.abood.crowdfunding;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {


    private ViewPager viewPager;
    CampaignPagerAdapter adapter;
    private TabLayout tabLayout;

    UserCampaignsFragments userCampaignsFragments;
    UserDonateFragment userDonateFragment;

    int tab;
    private boolean is_account_mode = false;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.activity_profile, container, false);

        view = v;

        initComponent();

        viewPager = v.findViewById(R.id.profile_viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = v.findViewById(R.id.profile_tablayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
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
                tab = tabLayout.getSelectedTabPosition();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return v;

    }


    private void initComponent() {

        final CircleImageView image = view.findViewById(R.id.image);

        final CollapsingToolbarLayout collapsing_toolbar = view.findViewById(R.id.collapsing_toolbar);

        ((AppBarLayout) view.findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
                image.setScaleX(scale >= 0 ? scale : 0);
                image.setScaleY(scale >= 0 ? scale : 0);

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new CampaignPagerAdapter(getActivity().getSupportFragmentManager());
        userCampaignsFragments = new UserCampaignsFragments();
        userDonateFragment = new UserDonateFragment();
        adapter.addFragment(userCampaignsFragments, "CAMPAIGN");
        adapter.addFragment(userDonateFragment, "DONATE");
        viewPager.setAdapter(adapter);
    }

    public static boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }


}