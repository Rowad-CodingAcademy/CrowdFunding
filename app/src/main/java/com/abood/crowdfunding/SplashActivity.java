package com.abood.crowdfunding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity
{

    private final int SPLASH_DISPLAY_LENGTH = 2500;

    private FirebaseAuth firebaseAuth;
    ImageView splashLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashLogoImageView =findViewById(R.id.splash_logo_imageview);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        splashLogoImageView.startAnimation(animation);



        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                // Check if user is signed in (non-null) and update UI accordingly.
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                updateUI(currentUser);

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public  void updateUI(FirebaseUser currentUser)
    {
        if(currentUser==null)
        {
            Intent loginIntent=new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else
        {
            Intent userIntent=new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(userIntent);
            finish();
        }
    }

}
