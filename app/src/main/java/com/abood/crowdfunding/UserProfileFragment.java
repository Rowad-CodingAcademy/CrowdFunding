package com.abood.crowdfunding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

        View v = inflater.inflate(R.layout.activity_profile, container, false);

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
        final TextView userName = view.findViewById(R.id.user_profile_name);
        final TextView userCampaignNo = view.findViewById(R.id.user_campaign_no);
        final TextView userDonationNo = view.findViewById(R.id.user_donation_no);

        final CollapsingToolbarLayout collapsing_toolbar = view.findViewById(R.id.collapsing_toolbar);

        ((AppBarLayout) view.findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
                image.setScaleX(scale >= 0 ? scale : 0);
                image.setScaleY(scale >= 0 ? scale : 0);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReferenceUser = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                documentReferenceUser.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    userName.setText(snapshot.getString("userName"));
                                } else {
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                db.collection("Campaigns").whereEqualTo("userId", FirebaseAuth.getInstance().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int count = 0;
                                    for (DocumentSnapshot document : task.getResult()) {
                                        count++;
                                        userCampaignNo.setText(String.valueOf(count) + " Campaigns");
                                    }
                                } else {
                                }
                            }
                        });


                db.collection("Donation").whereEqualTo("userId", FirebaseAuth.getInstance().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int count = 0;
                                    for (DocumentSnapshot document : task.getResult()) {
                                        count++;
                                        userDonationNo.setText(String.valueOf(count) + " Donation");
                                    }
                                } else {
                                }
                            }
                        });

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