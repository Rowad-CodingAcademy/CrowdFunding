package com.abood.crowdfunding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class ApproveNewCampaignsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference campaignRef = db.collection("Campaigns");
    private ManageNewCampaignAdapter campaignAdapter;


    private Campaigns mCampaign;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_approve_new_campaigns, container, false);

        Query query = campaignRef.whereEqualTo("campaignApprove", "0");
        FirestoreRecyclerOptions<Campaigns> options = new FirestoreRecyclerOptions.Builder<Campaigns>()
                .setQuery(query,Campaigns.class)
                .build();

        campaignAdapter = new ManageNewCampaignAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.approve_campaign_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(campaignAdapter);
        return v;

    }


    @Override
    public void onStart() {
        super.onStart();
        campaignAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        campaignAdapter.stopListening();
    }




    class ManageNewCampaignAdapter extends FirestoreRecyclerAdapter<Campaigns, ManageNewCampaignAdapter.CompaignViewHolder> {


        public ManageNewCampaignAdapter(@NonNull FirestoreRecyclerOptions<Campaigns> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull CompaignViewHolder holder, final int position, @NonNull Campaigns model) {

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            Picasso.get().load(model.getCampaignImage()).into(holder.campaignImage);
            holder.campaignTitle.setText(model.getCampaignTitle());
            holder.campaignDescription.setText(model.getCampaignDescription());

            holder.resumeBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateStatusToReject(position);

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
            Button refuseBTN, resumeBTN;

            public CompaignViewHolder(@NonNull View itemView) {
                super(itemView);

                campaignImage = itemView.findViewById(R.id.new_camp_img);
                campaignTitle = itemView.findViewById(R.id.new_camp_title);
                campaignDescription = itemView.findViewById(R.id.new_camp_desc);
                refuseBTN = itemView.findViewById(R.id.reject_campaign);
                resumeBTN = itemView.findViewById(R.id.resume_campaign);

            }
        }
    }

}
