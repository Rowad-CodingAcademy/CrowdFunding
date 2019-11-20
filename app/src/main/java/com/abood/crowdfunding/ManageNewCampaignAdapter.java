package com.abood.crowdfunding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ManageNewCampaignAdapter extends FirestoreRecyclerAdapter<Campaigns, ManageNewCampaignAdapter.CompaignViewHolder> {


    public ManageNewCampaignAdapter(@NonNull FirestoreRecyclerOptions<Campaigns> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CompaignViewHolder holder, final int position, @NonNull Campaigns model) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Picasso.get().load(model.getCampaignImage()).into(holder.campaignImage);
        holder.campaignTitle.setText(model.getCampaignTitle());
        holder.campaignDescription.setText(model.getCampaignDescription());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_compaign_to_approve_cardview, parent, false);
        return new CompaignViewHolder(v);
    }


    void updateStatusToAccepted(int position) {
        getSnapshots().getSnapshot(position).getReference().update("campaignApprove", "1");
    }

    void updateStatusToReject(int position) {
        getSnapshots().getSnapshot(position).getReference().update("campaignApprove", "2");
    }

    public class CompaignViewHolder extends RecyclerView.ViewHolder {

        ImageView campaignImage;
        TextView campaignTitle, campaignDescription;
        ImageView acceptBTN, refuseBTN;

        public CompaignViewHolder(@NonNull View itemView) {
            super(itemView);

            campaignImage = itemView.findViewById(R.id.new_camp_img);
            campaignTitle = itemView.findViewById(R.id.new_camp_title);
            campaignDescription = itemView.findViewById(R.id.new_camp_desc);
            acceptBTN = itemView.findViewById(R.id.accept_btn);
            refuseBTN = itemView.findViewById(R.id.reject_campaign);
        }
    }
}
