package com.abood.crowdfunding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.DocumentTransform;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DonationDetailsActivity extends AppCompatActivity {

    ImageView image;

//    ScrollView scrollView;
    EditText targetCostED, cardHolderName, expirationDate, cardNumber, securityCode;
    TextView details, cardNamep, projectTitle, projectDonate;
    public String target_reward;
    public String cardNO;
    public String holder;
    public String expiration;
    public String code;
    Button nextBTN, donateBTN;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    String current;
    LinearLayout detailsLyout;
    AlertDialog.Builder builder ;

    boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_deatails);
        init();



        detailsLyout = findViewById(R.id.details);
        projectTitle = findViewById(R.id.project_title2);
        projectDonate = findViewById(R.id.pledge2);

        donateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDonation();
            }
        });
        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                target_reward = targetCostED.getText().toString();

                if (target_reward.trim().isEmpty()){
                    Toast.makeText(DonationDetailsActivity.this, "Enter Donation Amount", Toast.LENGTH_LONG).show();
                } else {

                    detailsLyout.setVisibility(View.VISIBLE);
                    projectTitle.setText(getIntent().getStringExtra("campaignTitle"));
                    projectDonate.setText("$ "+targetCostED.getText().toString());
                }
            }
        });

    }

    private void addDonation()
    {

        target_reward = targetCostED.getText().toString();
        cardNO = cardNumber.getText().toString();
        holder = cardHolderName.getText().toString();
        expiration = expirationDate.getText().toString();
        code = securityCode.getText().toString();

        if(target_reward.trim().isEmpty() || holder.trim().isEmpty() || cardNO.trim().isEmpty() || expiration.trim().isEmpty() || code.trim().isEmpty())
        {
            Toast.makeText(this, "Fill all fields !", Toast.LENGTH_LONG).show();
            return;
        }

        builder =new AlertDialog.Builder(DonationDetailsActivity.this);

        builder.setTitle("Are you sure you want to continue ?");

        builder.setMessage("You will donate : "+target_reward+" $\nTo campaign : "+getIntent().getStringExtra("campaignTitle"))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!

                        firebaseAuth = FirebaseAuth.getInstance();
                        String userID = firebaseAuth.getCurrentUser().getUid();
                        final String campaignID = getIntent().getStringExtra("campaignID");
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

                                        firebaseFirestore = FirebaseFirestore.getInstance();

                                        firebaseFirestore.collection("Campaigns").document(campaignID).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                                                {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                                    {

                                                        if(task.isSuccessful()) {


                                                            if (task.getResult().exists())
                                                            {

                                                                String fund = task.getResult().getString("campaignFunds");
                                                                current = String.valueOf(Double.parseDouble(fund) + Double.parseDouble(target_reward));
                                                                firebaseFirestore.collection("Campaigns").document(campaignID).update("campaignFunds",current );

                                                                Intent intent = new Intent(DonationDetailsActivity.this,CampaignsListActivity.class);
                                                                startActivity(intent);

                                                            } else {
                                                                Toast.makeText(DonationDetailsActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                                            }

                                                        }

                                                    }
                                                });


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();


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
