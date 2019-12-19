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
    private FirebaseFirestore firebaseFirestore;


    private Campaigns mCampaign;
    AlertDialog.Builder builder ;


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
        protected void onBindViewHolder(@NonNull final CompaignViewHolder holder, final int position, @NonNull Campaigns model) {

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            Picasso.get().load(model.getCampaignImage()).into(holder.campaignImage);
            holder.campaignTitle.setText(model.getCampaignTitle());
            holder.campaignDescription.setText(model.getCampaignDescription());
            holder.campaignCost.setText(model.getCampaignCost());
            holder.campaignDays.setText(model.getCampaignDonationDays());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = CampaignDetailsActivity.newIntent(getActivity(),getSnapshots().getSnapshot(position).getId());
                    startActivity(i);

                }
            });

            builder =new AlertDialog.Builder(getActivity());
            holder.resumeBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    builder.setTitle("Are you sure you want to approve this campaign ?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!

                                    updateStatusToAccepted(holder.getAdapterPosition());


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();


                }
            });

            holder.refuseBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    builder.setTitle("Are you sure you want to reject this campaign ?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!

                                    updateStatusToReject(holder.getAdapterPosition());


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();

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
//            firebaseFirestore = FirebaseFirestore.getInstance();
//            firebaseFirestore.collection("Campaigns").document(getSnapshots().getSnapshot(position).getId()).update("campaignApprove", "1");

        }

        void updateStatusToReject(int position) {
            getSnapshots().getSnapshot(position).getReference().update("campaignApprove", "2");
//            Toast.makeText(getActivity(),position+"", Toast.LENGTH_SHORT).show();

        }

        public class CompaignViewHolder extends RecyclerView.ViewHolder {

            ImageView campaignImage;
            TextView campaignTitle, campaignDescription, campaignCost, campaignDays;
            Button refuseBTN, resumeBTN;

            public CompaignViewHolder(@NonNull View itemView) {
                super(itemView);

                campaignImage = itemView.findViewById(R.id.new_camp_img);
                campaignTitle = itemView.findViewById(R.id.new_camp_title);
                campaignDescription = itemView.findViewById(R.id.new_camp_desc);
                campaignCost = itemView.findViewById(R.id.campaign_cost);
                campaignDays = itemView.findViewById(R.id.campaign_days);
                refuseBTN = itemView.findViewById(R.id.reject_campaign);
                resumeBTN = itemView.findViewById(R.id.resume_campaign);

            }
        }
    }

}
