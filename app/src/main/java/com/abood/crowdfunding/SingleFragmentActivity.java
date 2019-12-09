
package com.abood.crowdfunding;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

abstract public class SingleFragmentActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;
    private String userName;
    private String userImage;
    private FirebaseFirestore db;
    private TextView name;
    private TextView email;
    private ImageView userPhoto;

    protected abstract Fragment createFragment();

    FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        initToolbar();
        initNavigationMenu();

        name = navigation_header.findViewById(R.id.name);
        email = navigation_header.findViewById(R.id.email);
        userPhoto = navigation_header.findViewById(R.id.nav_user_photo);
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReferenceUser = db.collection("Users").document(FirebaseAuth.getInstance().getUid());
        documentReferenceUser.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {

                        if (snapshot.exists()) {
                            userName = snapshot.getString("userName");
                            name.setText(userName);
                            email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            userImage = snapshot.getString("userImage");
                            Picasso.get().load(userImage).into(userPhoto);
                        } else {
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        fragmentManager.beginTransaction().replace(R.id.fragment_container, createFragment()).commit();

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
                    menu_navigation.add(1, 1000, 100, FirebaseAuth.getInstance().getCurrentUser().getEmail()).setIcon(R.drawable.ic_account_circle);
                    //menu_navigation.add(1, 2000, 100, "adams.green@mail.com").setIcon(R.drawable.ic_account_circle);
                    menu_navigation.add(1, 1, 100, "Add account").setIcon(R.drawable.ic_add);
                    menu_navigation.add(1, 2, 100, "Manage accounts").setIcon(R.drawable.ic_settings);
                } else {
                    nav_view.inflateMenu(R.menu.activity_campaign_list_drawer);

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

            if (id == R.id.nav_log_out) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent i = new Intent(SingleFragmentActivity.this, LoginActivity.class);
                startActivity(i);
            }

            if (id == R.id.nav_start_project) {
                Intent i = new Intent(SingleFragmentActivity.this, FinalAddCampaign.class);
                startActivity(i);
            }

            if (id == R.id.nav_profile) {
                Intent i = new Intent(SingleFragmentActivity.this, UserProfileActivity.class);
                startActivity(i);
            }

        } else {

            switch (item.getItemId()) {
                case 1000:
                    name.setText(userName);
                    email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    Picasso.get().load(userImage).into(userPhoto);
                    break;
                case 2000:
                    name.setText(userName);
                    email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    Picasso.get().load(userImage).into(userPhoto);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
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
