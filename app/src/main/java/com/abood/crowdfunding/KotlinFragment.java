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


public class KotlinFragment extends Fragment {

    RecyclerView kotlinRecyclerView;
    KotlinAdapter kotlinAdapter;
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

        kotlinRecyclerView = v.findViewById(R.id.android_recycler_view);
        kotlinRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            posts.add(Campaigns.createTask());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        loadMore();

        updateUI();

        return v;

    }



//    public void loadMore(){
//
//        StringRequest stringRequest = new StringRequest(AppServer.IP+"kotlin.php", new Response.Listener<String>() {
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

        if (kotlinAdapter == null) {

            kotlinAdapter = new KotlinAdapter(posts,getActivity());
            kotlinRecyclerView.setAdapter(kotlinAdapter);

        } else {

            kotlinAdapter.notifyDataSetChanged();

        }
    }




    public class KotlinAdapter extends RecyclerView.Adapter<KotlinViewHolder> {

        ArrayList<Campaigns> posts;
        Context context;

        public KotlinAdapter(ArrayList<Campaigns> posts, Context context) {
            this.posts = posts;
            this.context = context;
        }

        @NonNull
        @Override
        public KotlinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.campaign_holder,parent,false);
            return new KotlinViewHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull KotlinViewHolder holder, int position) {

//            Picasso.get().load(AppServer.IP+posts.get(position).getpImage()).into(holder.postImage);
//
//            checkUser(posts.get(position).getpUser());
//            Picasso.get().load(AppServer.IP+userProfile).into(holder.userProfile);

            holder.user.setText(posts.get(position).getpUser());
            holder.date.setText(posts.get(position).getpDate());
            holder.postQuestion.setText(posts.get(position).getpQuestion());

            holder.postImage.setImageResource(R.drawable.ic_launcher_foreground);
            holder.userProfile.setImageResource(R.drawable.ic_launcher_foreground);


        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }




    public class KotlinViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView userProfile,postImage,deletePost;
        TextView user,date,postQuestion;
        ImageButton like,disslike;
        EditText answer;

        public KotlinViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            userProfile = itemView.findViewById(R.id.user_profile);
            postImage = itemView.findViewById(R.id.post_image);
            deletePost = itemView.findViewById(R.id.delete_post);
            user = itemView.findViewById(R.id.post_user);
            date = itemView.findViewById(R.id.post_date);
            postQuestion = itemView.findViewById(R.id.post_question);
            like = itemView.findViewById(R.id.like_Button);
            disslike = itemView.findViewById(R.id.disslike_Button);
            answer = itemView.findViewById(R.id.post_answer);
            answer.setFocusable(false);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    like.setImageResource(R.drawable.ic_blue_like_24dp);
                    disslike.setImageResource(R.drawable.ic_disslike_24dp);

                }
            });

            disslike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    like.setImageResource(R.drawable.ic_like_24dp);
                    disslike.setImageResource(R.drawable.ic_red_disslike_24dp);

                }
            });

            answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    FragmentManager manager = getFragmentManager();
//                    int position = getAdapterPosition();
//                    AnswerFragment dialog = AnswerFragment.newInstance(posts.get(position).getpId());
//                    dialog.show(manager, DIALOG_ANSWER);

                }
            });


            deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    if (Login.userName.equals(posts.get(getAdapterPosition()).getpUser())) {
//
//                        swipPosition = getAdapterPosition();
////                        posts.remove(getAdapterPosition());
//                        deletePost();
//                        posts.remove(getAdapterPosition());
//                        kotlinAdapter.notifyDataSetChanged();
//
//                    } else {
//                        Toast.makeText(getActivity(), "This is not Your Post...You can`t delete This Post", Toast.LENGTH_SHORT).show();
//                    }
                }
            });

        }

        @Override
        public void onClick(View view) {

            Toast.makeText(getActivity(), "Abood", Toast.LENGTH_SHORT).show();

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
