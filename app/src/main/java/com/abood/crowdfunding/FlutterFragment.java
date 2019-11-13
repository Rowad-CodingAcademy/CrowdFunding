package com.abood.crowdfunding;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FlutterFragment extends Fragment {

    RecyclerView flutterRecyclerView;
    FlutterAdapter flutterAdapter;
    ArrayList<Campaigns> posts = new ArrayList<>();
    private static final String DIALOG_ANSWER = "DialogAnswer";
    String id;
    int swipPosition;
    String userProfile;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.new_crime:

//                updateUI();
                Intent intent = new Intent(getActivity(),AddCampaignActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_android, container, false);

        flutterRecyclerView = v.findViewById(R.id.android_recycler_view);
        flutterRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            posts.add(Campaigns.createTask());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        loadMore();

        return v;

    }



//    public void loadMore(){
//
//        StringRequest stringRequest = new StringRequest(AppServer.IP+"flutter.php", new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//
//                try {
//
//                    JSONArray jsonArray = new JSONArray(response);
//                    for (int i=0;i<jsonArray.length();i++){
//
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        posts.add(Posts.createTask(jsonObject));
//
//                    }
//
//                    updateUI();
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getActivity(), "Connection Erorr"+error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    public void updateUI() {

        if (flutterAdapter == null) {

            flutterAdapter = new FlutterAdapter(posts,getActivity());
            flutterRecyclerView.setAdapter(flutterAdapter);

        } else {

            flutterAdapter.notifyDataSetChanged();

        }
    }



    public class FlutterAdapter extends RecyclerView.Adapter<FlutterViewHolder> {

        ArrayList<Campaigns> posts;
        Context context;

        public FlutterAdapter(ArrayList<Campaigns> posts, Context context) {
            this.posts = posts;
            this.context = context;
        }

        @NonNull
        @Override
        public FlutterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.campaign_holder,parent,false);
            return new FlutterViewHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull FlutterViewHolder holder, int position) {

//            Picasso.get().load(AppServer.IP+posts.get(position).getpImage()).into(holder.postImage);
//
//            checkUser(posts.get(position).getpUser());
//            Picasso.get().load(AppServer.IP+posts.get(position).getpImage()).into(holder.userProfile);

            holder.campaignImage.setImageResource(R.drawable.campaign);

            holder.campaignTitle.setText(posts.get(position).getpUser());
            holder.campaignDescription.setText(posts.get(position).getpDate());


        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }




    public class FlutterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView campaignImage;
        TextView campaignTitle,campaignDescription;


        public FlutterViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            campaignImage = itemView.findViewById(R.id.campaign_image);
            campaignTitle = itemView.findViewById(R.id.campaign_title);
            campaignDescription = itemView.findViewById(R.id.campaign_description);

        }

        @Override
        public void onClick(View view) {

            //Toast.makeText(getActivity(), "Abood", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(),CampaignDetailsActivity.class);
            startActivity(i);

        }
    }



//    public void deletePost(){
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppServer.IP+"deletePost.php", null, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getActivity(), "connection Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> info = new HashMap<>();
//                id = posts.get(swipPosition).getpId();
//
//                info.put("pid",id);
//
//                return info;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
//
//    }
//
//
//    public void checkUser(final String name){
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppServer.IP+"getProfile.php", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    userProfile = jsonObject.getString("u_profile");
//
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(getActivity(), "connection Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }){
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> info = new HashMap<>();
//                info.put("username",name);
//
//                return info;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
//
//
//    }

}
