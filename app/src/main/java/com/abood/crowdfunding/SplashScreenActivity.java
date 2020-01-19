package com.abood.crowdfunding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2500;
    private FirebaseAuth firebaseAuth;
    ImageView splashLogoImageView;
    private FirebaseFirestore db;
    boolean isAdmin = false;
    String type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashLogoImageView = findViewById(R.id.splash_logo_imageview);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        splashLogoImageView.startAnimation(animation);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                firebaseAuth = FirebaseAuth.getInstance();
                db = FirebaseFirestore.getInstance();

                if (firebaseAuth.getCurrentUser() != null) {
                    Task<QuerySnapshot> query = db.collection("Users").whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            type = document.getString("userType");

                                            isAdmin = type.equals("0");

                                            updateUI(firebaseAuth.getCurrentUser(), isAdmin);
                                        }
                                    }
                                }
                            });
                } else {

                    updateUI(firebaseAuth.getCurrentUser(), isAdmin);
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    public void updateUI(FirebaseUser currentUser, boolean isAdmin) {

        if (currentUser == null || !isAdmin) {
            Intent loginIntent = new Intent(SplashScreenActivity.this, CampaignsListActivity.class);
            startActivity(loginIntent);
            finish();
        } else if (isAdmin) {
            Intent userIntent = new Intent(SplashScreenActivity.this, AdminDashboardActivity.class);
            startActivity(userIntent);
            finish();
        }
    }

}
