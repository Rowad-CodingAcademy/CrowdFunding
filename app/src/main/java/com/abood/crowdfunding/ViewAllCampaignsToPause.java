package com.abood.crowdfunding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewAllCampaignsToPause extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference campaignRef = db.collection("Campaigns");
    private ManageCampaignActiveAdapter campaignAdapter;


    private Campaigns mCampaign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_compaign_to_manage);
        setUpResyclerView();
    }

    private void setUpResyclerView() {
        Query query = campaignRef.orderBy("campaignCost", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Campaigns> options = new FirestoreRecyclerOptions.Builder<Campaigns>()
                .setQuery(query,Campaigns.class)
                .build();

        campaignAdapter = new ManageCampaignActiveAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.compaign_manage_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(campaignAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        campaignAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        campaignAdapter.stopListening();
    }
}
