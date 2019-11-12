package com.abood.crowdfunding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

public class CampaignListActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    private ViewPager viewPager;
    CrowdFundingViewPager adapter;
    private TabLayout tabLayout;


    int tab;
    private static final String MyPREFERENCES = "Abood";
    private SharedPreferences sharedpreferences;

    FlutterFragment flutterFragment;
    KotlinFragment kotlinFragment;
    AndroidFragment androidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_campain_list);

        initToolbar();
        initNavigationMenu();


        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tablayout);

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
                tab = tabLayout.getSelectedTabPosition();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_post_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.new_crime:

//                updateUI();

                Intent intent = new Intent(CampaignListActivity.this,AddCampaignActivity.class);
                intent.putExtra("type",tab);
                startActivity(intent);

                return true;

            case R.id.logout:

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                sharedpreferences.edit().remove("username").apply();
                Intent intent2 = new Intent(CampaignListActivity.this,LoginActivity.class);
                startActivity(intent2);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupViewPager(ViewPager viewPager)
    {
        adapter = new CrowdFundingViewPager(getSupportFragmentManager());
        flutterFragment = new FlutterFragment();
        kotlinFragment = new KotlinFragment();
        androidFragment = new AndroidFragment();
        adapter.addFragment(flutterFragment,"CALLS");
        adapter.addFragment(kotlinFragment,"CHAT");
        adapter.addFragment(androidFragment,"CONTACTS");
        viewPager.setAdapter(adapter);
    }



    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.pink_600));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initNavigationMenu() {
        final NavigationView nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                updateCounter(nav_view);
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                onItemNavigationClicked(item);
                return true;
            }
        });


        updateCounter(nav_view);
        menu_navigation = nav_view.getMenu();

        // navigation header
        navigation_header = nav_view.getHeaderView(0);
        (navigation_header.findViewById(R.id.bt_account)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean is_hide = toggleArrow(view);
                is_account_mode = is_hide;
                menu_navigation.clear();
                if (is_hide) {
                    menu_navigation.add(1, 1000, 100, "evans.collins@mail.com").setIcon(R.drawable.ic_account_circle);
                    menu_navigation.add(1, 2000, 100, "adams.green@mail.com").setIcon(R.drawable.ic_account_circle);
                    menu_navigation.add(1, 1, 100, "Add account").setIcon(R.drawable.ic_add);
                    menu_navigation.add(1, 2, 100, "Manage accounts").setIcon(R.drawable.ic_settings);
                } else {
                    nav_view.inflateMenu(R.menu.activity_campain_list_drawer);
                    updateCounter(nav_view);
                }
            }
        });
    }

    private void onItemNavigationClicked(MenuItem item) {
        if (!is_account_mode) {
            Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
            actionBar.setTitle(item.getTitle());
            drawer.closeDrawers();

            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_help) {
//                getSupportActionBar().setTitle("Home");
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
//                Intent i = new Intent(this,MainActivity.class);
//                startActivity(i);
            }

        } else {
            TextView name =  navigation_header.findViewById(R.id.name);
            TextView email =  navigation_header.findViewById(R.id.email);
            CircularImageView userPhoto =  navigation_header.findViewById(R.id.nav_user_photo);
            switch (item.getItemId()) {
                case 1000:
                    name.setText("Evans Collins");
                    email.setText(item.getTitle().toString());
                    userPhoto.setImageResource(R.drawable.photo_male_5);
                    break;
                case 2000:
                    name.setText("Adams Green");
                    email.setText(item.getTitle().toString());
                    userPhoto.setImageResource(R.drawable.photo_male_2);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    private void updateCounter(NavigationView nav) {
        if (is_account_mode) return;
        Menu m = nav.getMenu();
        ((TextView) m.findItem(R.id.nav_all_inbox).getActionView().findViewById(R.id.text)).setText("75");
        ((TextView) m.findItem(R.id.nav_inbox).getActionView().findViewById(R.id.text)).setText("68");

        TextView badgePrioInbx = m.findItem(R.id.nav_priority_inbox).getActionView().findViewById(R.id.text);
        badgePrioInbx.setText("3 new");
        badgePrioInbx.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        TextView badgeSocial = m.findItem(R.id.nav_social).getActionView().findViewById(R.id.text);
        badgeSocial.setText("51 new");
        badgeSocial.setBackgroundColor(getResources().getColor(R.color.green_500));

        ((TextView) m.findItem(R.id.nav_spam).getActionView().findViewById(R.id.text)).setText("13");
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
