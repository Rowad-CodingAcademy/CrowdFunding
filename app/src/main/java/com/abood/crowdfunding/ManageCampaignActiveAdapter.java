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
import com.squareup.picasso.Picasso;

public class ManageCampaignActiveAdapter extends FirestoreRecyclerAdapter<Campaigns, ManageCampaignActiveAdapter.CompaignViewHolder> {


    public ManageCampaignActiveAdapter(@NonNull FirestoreRecyclerOptions<Campaigns> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ManageCampaignActiveAdapter.CompaignViewHolder holder, final int position, @NonNull Campaigns model) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Picasso.get().load(model.getCampaignImage()).into(holder.campaignImage);
        holder.campaignTitle.setText(model.getCampaignTitle());
        holder.campaignDescription.setText(model.getCampaignDescription());
        
        holder.pauseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusToPaused(position);
            }
        });

        holder.resumeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateStatusToResume(position);
            }
        });
    }



    @NonNull
    @Override
    public ManageCampaignActiveAdapter.CompaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_compaign_to_approve_cardview, parent, false);
        return new CompaignViewHolder(v);
    }


    void updateStatusToPaused(int position) {
        getSnapshots().getSnapshot(position).getReference().update("campaignActive", "0");
    }

    void updateStatusToResume(int position) {
        getSnapshots().getSnapshot(position).getReference().update("campaignActive", "1");
    }

    public class CompaignViewHolder extends RecyclerView.ViewHolder {

        ImageView campaignImage;
        TextView campaignTitle, campaignDescription;
        ImageView acceptBTN, refuseBTN, pauseBTN, resumeBTN;

        public CompaignViewHolder(@NonNull View itemView) {
            super(itemView);

            campaignImage = itemView.findViewById(R.id.new_camp_img);
            campaignTitle = itemView.findViewById(R.id.new_camp_title);
            campaignDescription = itemView.findViewById(R.id.new_camp_desc);

            refuseBTN = itemView.findViewById(R.id.reject_campaign);

            resumeBTN = itemView.findViewById(R.id.resume_campaign);
            pauseBTN.setVisibility(View.VISIBLE);
            resumeBTN.setVisibility(View.VISIBLE);
            acceptBTN.setVisibility(View.INVISIBLE);
            refuseBTN.setVisibility(View.INVISIBLE);
        }
    }
}