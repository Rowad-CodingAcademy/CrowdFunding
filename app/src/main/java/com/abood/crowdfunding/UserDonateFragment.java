package com.abood.crowdfunding;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


public class UserDonateFragment extends Fragment {

    RecyclerView userDonationRecyclerView;
    FirebaseFirestore store;
    FirebaseAuth firebaseAuth;
    private UserDonationAdapter userDonationAdapter;
    String userId;

    public interface OnItemClickListener {
        void onItemClick(View view, DonationModel obj, int position);
    }


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
        final String userID = firebaseAuth.getCurrentUser().getUid();
        store = FirebaseFirestore.getInstance();
        Query query = store.collection("Donation").whereEqualTo("userId", userID);

        FirestoreRecyclerOptions<DonationModel> options = new FirestoreRecyclerOptions.Builder<DonationModel>()
                .setQuery(query, DonationModel.class)
                .build();

        userDonationAdapter = new UserDonationAdapter(options);
        userDonationRecyclerView.setAdapter(userDonationAdapter);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        userDonationAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (userDonationAdapter != null) {
            userDonationAdapter.stopListening();
        }
    }

    class UserDonationAdapter extends FirestoreRecyclerAdapter<DonationModel, UserDonationViewHolder> {


        private AdapterListExpand.OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(final AdapterListExpand.OnItemClickListener mItemClickListener) {
            this.mOnItemClickListener = mItemClickListener;
        }


        public UserDonationAdapter(@NonNull FirestoreRecyclerOptions<DonationModel> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull final UserDonationViewHolder holder, final int position, @NonNull final DonationModel model) {
            holder.userDonationFund.setText(model.getTargetAmount());
            DocumentReference documentReference = store.collection("Campaigns").document(model.getCampaignId());
            documentReference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot snapshot) {

                            if (snapshot.exists()) {
                                holder.campaignTitle.setText(snapshot.getString("campaignTitle"));
                                String imgURL = snapshot.getString("campaignImage");
                                userId = snapshot.getString("userId");
                                Picasso.get().load(imgURL).into(holder.campaignImage);


                            } else {
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

//                Task<QuerySnapshot> query = store.collection("Users").whereEqualTo("userId", model.getUserId())
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                        holder.campaignOwner.setText(document.getString("userName"));
//
//                                    }
//                                }
//                            }
//                        });

            store.collection("Users").whereEqualTo("userId", model.getUserId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

//                                    holder.campaignOwner.setText(task.getResult().getDocuments().get(position).getString("userName"));

                                for (DocumentSnapshot document : task.getResult()) {

                                    holder.campaignOwner.setText(document.getString("userName"));

                                }
                            } else {
                            }
                        }
                    });

            if (holder instanceof UserDonationViewHolder) {
                final UserDonationViewHolder view = (UserDonationViewHolder) holder;

                view.campaignTitle.setText(model.getTargetAmount());

                int image = R.drawable.image_12;
                displayImageOriginal(getActivity(), view.campaignImage,image);
                view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(view, model, position);
                        }
                    }
                });

                view.bt_expand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean show = toggleLayoutExpand(!model.expanded, v, view.lyt_expand);
                        model.expanded = show;
                    }
                });


                // void recycling view
                if(model.expanded){
                    view.lyt_expand.setVisibility(View.VISIBLE);
                } else {
                    view.lyt_expand.setVisibility(View.GONE);
                }
                toggleArrow(model.expanded, view.bt_expand, false);

            }

        }

        @NonNull
        @Override
        public UserDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_donation_holder2, parent, false);
            return new UserDonationViewHolder(view);
        }

        private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
            toggleArrow(show, view);
            if (show) {
                ViewAnimation.expand(lyt_expand);
            } else {
                ViewAnimation.collapse(lyt_expand);
            }
            return show;
        }


        public boolean toggleArrow(boolean show, View view) {
            return toggleArrow(show, view, true);
        }


        public boolean toggleArrow(boolean show, View view, boolean delay) {
            if (show) {
                view.animate().setDuration(delay ? 200 : 0).rotation(180);
                return true;
            } else {
                view.animate().setDuration(delay ? 200 : 0).rotation(0);
                return false;
            }
        }


        public void displayImageOriginal(Context ctx, ImageView img, @DrawableRes int drawable) {
            try {
                Glide.with(ctx).load(drawable)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(img);
            } catch (Exception e) {
            }
        }

    }


    public class UserDonationViewHolder extends RecyclerView.ViewHolder {

        private View view;

        ImageView campaignImage;
        TextView campaignTitle, campaignOwner, userDonationFund;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;


        public UserDonationViewHolder(@NonNull View itemView) {
            super(itemView);
            campaignImage = itemView.findViewById(R.id.project_image);
            campaignTitle = itemView.findViewById(R.id.project_title);
            campaignOwner = itemView.findViewById(R.id.project_owner_name);
            userDonationFund = itemView.findViewById(R.id.user_donation_cost);
            bt_expand =  itemView.findViewById(R.id.bt_expand);
            lyt_expand =  itemView.findViewById(R.id.lyt_expand);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);
            view = itemView;
        }


    }

}
