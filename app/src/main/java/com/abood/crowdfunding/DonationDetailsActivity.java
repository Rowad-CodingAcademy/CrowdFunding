package com.abood.crowdfunding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.DocumentTransform;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DonationDetailsActivity extends AppCompatActivity {

    ImageView image;

//    ScrollView scrollView;
    EditText targetCostED;
    TextView details;
    EditText cardHolderName;
    EditText expirationDate;
    EditText cardNumber;
    TextView cardName;
    EditText securityCode;
    public String target_reward;
    public String cardNO;
    public String holder;
    public String expiration;
    public String code;
    Button nextBTN;
    Button donateBTN;
    FirebaseAuth firebaseAuth;

    LinearLayout detailsLyout;

    boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_deatails);
        init();



        detailsLyout = findViewById(R.id.details);
        donateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target_reward = targetCostED.getText().toString();
                cardNO = cardNumber.getText().toString();
                holder = cardHolderName.getText().toString();
                expiration = expirationDate.getText().toString();
                code = securityCode.getText().toString();
                addDonation();
            }
        });
        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailsLyout.setVisibility(View.VISIBLE);
            }
        });

    }

    private void addDonation() {

        if(target_reward.trim().isEmpty() || holder.trim().isEmpty() || cardNO.trim().isEmpty() || expiration.trim().isEmpty() || code.trim().isEmpty())
        {
            Toast.makeText(this, "Fill all fields !", Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();
        String campaignID = getIntent().getStringExtra("campaignID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> donation = new HashMap<>();
        donation.put("targetAmount", target_reward);
        donation.put("cardNo", cardNO);
        donation.put("holderName", holder);
        donation.put("expirationDate", expiration);
        donation.put("code", code);
        donation.put("userId", userID);
        donation.put("campaignId", campaignID);
        donation.put("timestamp", new Date());

        Task<DocumentReference> documentReferenceTask = db.collection("Donation")
                .add(donation)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(DonationDetailsActivity.this, "Donation has added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    private void init() {
        nextBTN = findViewById(R.id.next_btn);
        targetCostED = findViewById(R.id.target_cost_ED);
        cardNumber = findViewById(R.id.card_no);
        cardHolderName = findViewById(R.id.card_holder_name);
        expirationDate = findViewById(R.id.expiration_date);
        securityCode = findViewById(R.id.security_code);
        donateBTN = findViewById(R.id.donate_btn);
    }


}
