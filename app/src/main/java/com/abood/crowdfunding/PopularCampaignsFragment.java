package com.abood.crowdfunding;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class PopularCampaignsFragment extends Fragment {

    RecyclerView popularCampaignsRecyclerView;
    FirebaseFirestore store;
    private PopularCampaignAdapter popularCampaignAdapter;
    String id;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_popular_campaigns, container, false);

        popularCampaignsRecyclerView = v.findViewById(R.id.popular_campaign_recycler_view);
        popularCampaignsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        store = FirebaseFirestore.getInstance();
        Query query = store.collection("Campaigns").whereEqualTo("campaignApprove","1")
                .whereEqualTo("campaignStatus","0").whereGreaterThanOrEqualTo("campaignFundsRate","100");

        FirestoreRecyclerOptions<Campaigns> options = new FirestoreRecyclerOptions.Builder<Campaigns>()
                .setQuery(query, Campaigns.class)
                .build();

        popularCampaignAdapter = new PopularCampaignAdapter(options);
        popularCampaignsRecyclerView.setAdapter(popularCampaignAdapter);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        popularCampaignAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (popularCampaignAdapter != null) {
            popularCampaignAdapter.stopListening();
        }
    }


    class PopularCampaignAdapter extends FirestoreRecyclerAdapter<Campaigns, PopularCampaignsViewHolder> {


        public PopularCampaignAdapter(@NonNull FirestoreRecyclerOptions<Campaigns> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull final PopularCampaignsViewHolder holder , final int position, @NonNull Campaigns campaign) {

            id = getSnapshots().getSnapshot(position).getId();

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
                                Toast.makeText(getActivity(), task.getException()+"", Toast.LENGTH_SHORT).show();
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
        public PopularCampaignsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campaign_holder, parent, false);
            return new PopularCampaignsViewHolder(view);
        }
    }


    public class PopularCampaignsViewHolder extends RecyclerView.ViewHolder {

        private View view;

        ImageView campaignImage;
        TextView campaignTitle,campaignDescription,campaignRatio,campaignDoners,campaignDays;
        private ProgressBar progress_determinate;
        int mDonationRatio;


        public PopularCampaignsViewHolder(@NonNull View itemView) {
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

            store.collection("Campaigns").document(id).update("campaignFundsRate",String.valueOf(mDonationRatio ));


            Picasso.get().load(image).into(campaignImage);
//            Glide.with(getActivity()).load(image).into(campaignImage);

        }
    }

}
