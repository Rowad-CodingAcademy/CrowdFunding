package com.abood.crowdfunding;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import static com.abood.crowdfunding.CampaignDetailsActivity.EXTRA_CAMPAIGN_UUID;


public class PausedCampaignsFragment extends Fragment {

    RecyclerView pausedCampaignsRecyclerView;
    FirebaseFirestore store;
    private PausedCampaignsAdapter pausedCampaignsAdapter;
    AlertDialog.Builder builder ;


//    private FirestoreRecyclerAdapter<Campaigns, PopularCampaignsViewHolder> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_popular_campaigns, container, false);

        pausedCampaignsRecyclerView = v.findViewById(R.id.popular_campaign_recycler_view);
        pausedCampaignsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        store = FirebaseFirestore.getInstance();
        Query query = store.collection("Campaigns").whereEqualTo("campaignStatus","1");

        FirestoreRecyclerOptions<Campaigns> options = new FirestoreRecyclerOptions.Builder<Campaigns>()
                .setQuery(query, Campaigns.class)
                .build();

        pausedCampaignsAdapter = new PausedCampaignsAdapter(options);



//        adapter = new FirestoreRecyclerAdapter<Campaigns, PopularCampaignsViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull PopularCampaignsViewHolder holder , final int position, @NonNull Campaigns campaign) {
//                holder.setData(campaign.getCampaignTitle(),campaign.getCampaignDescription(),campaign.getCampaignImage());
//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v)
//                    {
//
//                        Intent i = CampaignDetailsActivity.newIntent(getActivity(),getSnapshots().getSnapshot(position).getId());
//                        startActivity(i);
//
//                    }
//                });
//
//            }
//
//            @NonNull
//            @Override
//            public PopularCampaignsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campaign_holder, parent, false);
//                return new PopularCampaignsViewHolder(view);
//            }
//        };
        pausedCampaignsRecyclerView.setAdapter(pausedCampaignsAdapter);


        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        pausedCampaignsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (pausedCampaignsAdapter != null) {
            pausedCampaignsAdapter.stopListening();
        }
    }


    class PausedCampaignsAdapter extends FirestoreRecyclerAdapter<Campaigns, PausedCampaignsViewHolder> {


        public PausedCampaignsAdapter(@NonNull FirestoreRecyclerOptions<Campaigns> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull final PausedCampaignsViewHolder holder , final int position, @NonNull Campaigns campaign) {

            holder.setData(campaign.getCampaignTitle(),campaign.getCampaignDescription(),campaign.getCampaignImage(),campaign.getCampaignCost(),campaign.getCampaignFunds(), campaign.getCampaignDonationDays());

            store.collection("Donation").whereEqualTo("campaignId", getSnapshots().getSnapshot(position).getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int count = 0;
                                for (DocumentSnapshot document : task.getResult()) {
                                    count++;
                                    holder.campaignDoners.setText(String.valueOf(count));
                                }
                            } else {
                            }
                        }
                    });

            builder =new AlertDialog.Builder(getActivity());

            holder.activeCampaign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    builder.setTitle("Are you sure you want to active this campaign ?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!

                                    getSnapshots().getSnapshot(holder.getAdapterPosition()).getReference().update("campaignStatus", "0");

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            })
                            .setCancelable(false);
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();



                }
            });

        }

        @NonNull
        @Override
        public PausedCampaignsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paused_campaign_holder, parent, false);
            return new PausedCampaignsViewHolder(view);
        }
    }


    public class PausedCampaignsViewHolder extends RecyclerView.ViewHolder {


        private View view;

        ImageView campaignImage;
        Button activeCampaign;
        TextView campaignTitle,campaignDescription,campaignRatio,campaignDoners,campaignDays;
        private ProgressBar progress_determinate;
        int mDonationRatio;


        public PausedCampaignsViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            activeCampaign = itemView.findViewById(R.id.active_campaign);

        }

        void setData(String name, String age , String image, String cost, String fund, String donation ) {

            campaignImage = itemView.findViewById(R.id.campaign_image);
            campaignTitle = itemView.findViewById(R.id.campaign_title);
            campaignDescription = itemView.findViewById(R.id.campaign_description);
            campaignRatio = itemView.findViewById(R.id.campaign_ratio_textView);
            campaignDoners = itemView.findViewById(R.id.campaign_donors_textView);
            campaignDays = itemView.findViewById(R.id.campaign_daysToGo_textView);
            progress_determinate = itemView.findViewById(R.id.progress_determinate);

            campaignTitle.setText(name);
            campaignDescription.setText(age);
            campaignDays.setText(donation);

            Double funds = Double.parseDouble(fund);
            Double costs = Double.parseDouble(cost);
            mDonationRatio = new Integer(String.valueOf(Math.round((funds*100)/ costs)));
            campaignRatio.setText(mDonationRatio+"%");

            progress_determinate.setProgress(mDonationRatio);

            Picasso.get().load(image).into(campaignImage);
//            Glide.with(getActivity()).load(image).into(campaignImage);


        }
    }

}
