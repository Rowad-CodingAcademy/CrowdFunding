package com.abood.crowdfunding;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ClosedCampaignsFragment extends Fragment {

    RecyclerView closedCampaignsRecyclerView;
    FirebaseFirestore store;
    private ClosedCampaignsAdapter closedCampaignsAdapter;

//    private FirestoreRecyclerAdapter<Campaigns, PopularCampaignsViewHolder> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_popular_campaigns, container, false);

        closedCampaignsRecyclerView = v.findViewById(R.id.popular_campaign_recycler_view);
        closedCampaignsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        store = FirebaseFirestore.getInstance();
        Query query = store.collection("Campaigns").whereEqualTo("campaignStatus","2");

        FirestoreRecyclerOptions<Campaigns> options = new FirestoreRecyclerOptions.Builder<Campaigns>()
                .setQuery(query, Campaigns.class)
                .build();

        closedCampaignsAdapter = new ClosedCampaignsAdapter(options);



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
        closedCampaignsRecyclerView.setAdapter(closedCampaignsAdapter);


        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        closedCampaignsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (closedCampaignsAdapter != null) {
            closedCampaignsAdapter.stopListening();
        }
    }


    class ClosedCampaignsAdapter extends FirestoreRecyclerAdapter<Campaigns, ClosedCampaignsViewHolder> {


        public ClosedCampaignsAdapter(@NonNull FirestoreRecyclerOptions<Campaigns> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull final ClosedCampaignsViewHolder holder , final int position, @NonNull Campaigns campaign) {

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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    Intent i = CampaignDetailsActivity.newIntent(getActivity(),getSnapshots().getSnapshot(position).getId());
                    startActivity(i);

                }
            });

        }

        @NonNull
        @Override
        public ClosedCampaignsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campaign_holder, parent, false);
            return new ClosedCampaignsViewHolder(view);
        }
    }


    public class ClosedCampaignsViewHolder extends RecyclerView.ViewHolder {

        private View view;

        ImageView campaignImage;
        TextView campaignTitle,campaignDescription,campaignRatio,campaignDoners,campaignDays;
        private ProgressBar progress_determinate;
        int mDonationRatio;


        public ClosedCampaignsViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;

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
