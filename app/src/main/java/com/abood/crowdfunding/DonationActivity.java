package com.abood.crowdfunding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class DonationActivity extends AppCompatActivity {

    public static Intent newIntent(Context context, String campaignId)
    {
        return new Intent(context,DonationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
    }
}
