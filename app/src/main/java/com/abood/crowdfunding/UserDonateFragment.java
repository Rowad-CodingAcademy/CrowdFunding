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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


public class UserDonateFragment extends Fragment {

    RecyclerView userDonationRecyclerView;
    FirebaseFirestore store;
    FirebaseAuth firebaseAuth;
    private FirestoreRecyclerAdapter<DonationModel, UserDonationViewHolder> adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user_donate, container, false);

        userDonationRecyclerView = v.findViewById(R.id.newest_campaign_recycler_view);
        userDonationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();
        store = FirebaseFirestore.getInstance();
        Query query = store.collection("Donation").whereEqualTo("userId", userID);

        FirestoreRecyclerOptions<DonationModel> options = new FirestoreRecyclerOptions.Builder<DonationModel>()
                .setQuery(query, DonationModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<DonationModel, UserDonationViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final UserDonationViewHolder holder, int position, @NonNull DonationModel model) {
                holder.userDonationFund.setText(model.getTargetAmount());
                store.collection("Campaigns").whereEqualTo(store.collection("Campaigns").getId().toString(),model.getCampaignId());

                store.collection("Users").document(model.getCampaignId()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {

                                        if (task.getResult().exists())
                                        {
                                            holder.campaignTitle.setText(task.getResult().getString("campaignTitle"));
                                            //Picasso.get().load(task.getResult().getString("campaignImage")).into(holder.campaignImage);
                                        }
                                    }
                                }
                            }

                        });


            }


            @NonNull
            @Override
            public UserDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_donation_holder, parent, false);
                return new UserDonationViewHolder(view);
            }
        };
        userDonationRecyclerView.setAdapter(adapter);

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


    public class UserDonationViewHolder extends RecyclerView.ViewHolder {

        private View view;

        ImageView campaignImage;
        TextView campaignTitle, campaignOwner, userDonationFund;


        public UserDonationViewHolder(@NonNull View itemView) {
            super(itemView);
            campaignImage = itemView.findViewById(R.id.campaign_image);
            campaignTitle = itemView.findViewById(R.id.project_title);
            campaignOwner = itemView.findViewById(R.id.project_owner_name);
            userDonationFund = itemView.findViewById(R.id.user_donation_cost);

            view = itemView;
        }


    }

}
