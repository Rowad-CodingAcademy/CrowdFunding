package com.abood.crowdfunding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class DonationDetailsActivity extends AppCompatActivity {

    ImageView image;

    Button nextBTN;

    LinearLayout detailsLyout;

    boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_deatails);
        initComponent();


        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailsLyout.setVisibility(View.VISIBLE);

            }
        });

    }


    private void initComponent() {

        image = findViewById(R.id.image);
        nextBTN = findViewById(R.id.next_btn);
        detailsLyout = findViewById(R.id.details);
        detailsLyout.setVisibility(View.INVISIBLE);

    }


}
