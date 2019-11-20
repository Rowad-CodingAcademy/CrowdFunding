package com.abood.crowdfunding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageNewCampaignAdapter extends FirestoreRecyclerAdapter<Campaign, ManageNewCampaignAdapter.CompaignViewHolder> {


    public ManageNewCampaignAdapter(@NonNull FirestoreRecyclerOptions<Campaign> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CompaignViewHolder holder, final int position, @NonNull Campaign model) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        holder.campaignTitle.setText(model.getCampOwnerId().toString());
        holder.campaignDescription.setText(model.getCampDescription());
        holder.acceptBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusToAccepted(position);
            }
        });

        holder.refuseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateStatusToReject(position);
            }
        });
    }



    @NonNull
    @Override
    public CompaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_compaign_to_approve, parent, false);
        return new CompaignViewHolder(v);
    }


    void updateStatusToAccepted(int position) {
        getSnapshots().getSnapshot(position).getReference().update("status", "true");
    }

    void updateStatusToReject(int position) {
        getSnapshots().getSnapshot(position).getReference().update("status", "false");
    }

    public class CompaignViewHolder extends RecyclerView.ViewHolder {

        ImageView campaignImage;
        TextView campaignTitle, campaignDescription;
        ImageView acceptBTN, refuseBTN;

        public CompaignViewHolder(@NonNull View itemView) {
            super(itemView);

            campaignImage = itemView.findViewById(R.id.campaign_image);
            campaignTitle = itemView.findViewById(R.id.campaign_title);
            campaignDescription = itemView.findViewById(R.id.campaign_description);
            acceptBTN = itemView.findViewById(R.id.accept_btn);
            refuseBTN = itemView.findViewById(R.id.reject_campaign);
        }
    }
}
