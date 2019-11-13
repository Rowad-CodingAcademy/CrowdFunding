package com.abood.crowdfunding;


import android.content.Context;
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
import org.json.JSONException;
import java.util.ArrayList;


public class EndingSoonCampaignsFragment extends Fragment {

    RecyclerView endingSoonCampaignsRecyclerView;
    EndingSoonCampaignsAdapter endingSoonCampaignsAdapter;
    ArrayList<Campaigns> campaigns = new ArrayList<>();
    String id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


//        @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.new_crime:
//
//                Intent intent = new Intent(getActivity(),AddCampaignActivity.class);
//                startActivity(intent);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_ending_soon_campaigns, container, false);

        endingSoonCampaignsRecyclerView = v.findViewById(R.id.android_recycler_view);
        endingSoonCampaignsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            campaigns.add(Campaigns.createTask());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        endingSoonCampaignsAdapter = new EndingSoonCampaignsAdapter(campaigns,getActivity());
        endingSoonCampaignsRecyclerView.setAdapter(endingSoonCampaignsAdapter);

        return v;

    }


    public class EndingSoonCampaignsAdapter extends RecyclerView.Adapter<EndingSoonCampaignsViewHolder> {

        ArrayList<Campaigns> campaigns;
        Context context;

        public EndingSoonCampaignsAdapter(ArrayList<Campaigns> campaigns, Context context) {
            this.campaigns = campaigns;
            this.context = context;
        }

        @NonNull
        @Override
        public EndingSoonCampaignsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.campaign_holder,parent,false);
            return new EndingSoonCampaignsViewHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull EndingSoonCampaignsViewHolder holder, int position) {

//            Picasso.get().load(AppServer.IP+campaigns.get(position).getpImage()).into(holder.postImage);

            holder.campaignImage.setImageResource(R.drawable.campaign);
            holder.campaignTitle.setText(campaigns.get(position).getpUser());
            holder.campaignDescription.setText(campaigns.get(position).getpDate());

        }

        @Override
        public int getItemCount() {
            return campaigns.size();
        }
    }




    public class EndingSoonCampaignsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView campaignImage;
        TextView campaignTitle,campaignDescription;


        public EndingSoonCampaignsViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            campaignImage = itemView.findViewById(R.id.campaign_image);
            campaignTitle = itemView.findViewById(R.id.campaign_title);
            campaignDescription = itemView.findViewById(R.id.campaign_description);

        }

        @Override
        public void onClick(View view) {

            Intent i = new Intent(getActivity(),CampaignDetailsActivity.class);
            startActivity(i);

        }
    }

}
