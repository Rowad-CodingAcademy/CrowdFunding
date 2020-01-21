package com.abood.crowdfunding;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class CampaignsListFragment extends Fragment {

    private ViewPager viewPager;
    CampaignPagerAdapter adapter;
    private TabLayout tabLayout;

    PopularCampaignsFragment popularCampaignsFragment;
    NewestCampaignsFragment newestCampaignsFragment;
    EndingSoonCampaignsFragment endingSoonCampaignsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("aaa", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("aaa", msg);
//                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.activity_campaign_list, container, false);

        viewPager = v.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = v.findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return v;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search:

//                Intent intent = new Intent(CampaignsListFragment.this,AddCampaignActivity.class);
//                intent.putExtra("type",tab);
//                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new CampaignPagerAdapter(getActivity().getSupportFragmentManager());
        popularCampaignsFragment = new PopularCampaignsFragment();
        newestCampaignsFragment = new NewestCampaignsFragment();
        endingSoonCampaignsFragment = new EndingSoonCampaignsFragment();
        adapter.addFragment(popularCampaignsFragment,"POPULAR");
        adapter.addFragment(newestCampaignsFragment,"NEWEST");
        adapter.addFragment(endingSoonCampaignsFragment,"ENDING SOON");
        viewPager.setAdapter(adapter);
    }

}
