package com.abood.crowdfunding;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class UserCampaignsFragments extends Fragment {

    RecyclerView popularCampaignsRecyclerView;
    FirebaseFirestore store;
    FirebaseAuth firebaseAuth;

    private FirestoreRecyclerAdapter<Campaigns, PopularCampaignsViewHolder> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user_campaigns, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();
        popularCampaignsRecyclerView = v.findViewById(R.id.popular_campaign_recycler_view);
        popularCampaignsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        store = FirebaseFirestore.getInstance();
        Query query = store.collection("Campaigns").whereEqualTo("userId", userID);

        FirestoreRecyclerOptions<Campaigns> options = new FirestoreRecyclerOptions.Builder<Campaigns>()
                .setQuery(query, Campaigns.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Campaigns, PopularCampaignsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PopularCampaignsViewHolder holder, final int position, @NonNull Campaigns campaign) {
                holder.setData(campaign.getCampaignTitle(), campaign.getCampaignDescription(), campaign.getCampaignImage());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = CampaignDetailsActivity.newIntent(getActivity(), getSnapshots().getSnapshot(position).getId());
                        startActivity(i);

                    }
                });

            }

            @NonNull
            @Override
            public PopularCampaignsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campaign_holder, parent, false);
                return new PopularCampaignsViewHolder(view);
            }
        };
        popularCampaignsRecyclerView.setAdapter(adapter);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }


    public class PopularCampaignsViewHolder extends RecyclerView.ViewHolder {

        private View view;

        ImageView campaignImage;
        TextView campaignTitle, campaignDescription;


        public PopularCampaignsViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;

        }

        void setData(String name, String age, String image) {

            campaignImage = itemView.findViewById(R.id.campaign_image);
            campaignTitle = itemView.findViewById(R.id.campaign_title);
            campaignDescription = itemView.findViewById(R.id.campaign_description);

            campaignTitle.setText(name);
            campaignDescription.setText(age);
            Picasso.get().load(image).into(campaignImage);
//            Glide.with(getActivity()).load(image).into(campaignImage);


        }
    }

}
