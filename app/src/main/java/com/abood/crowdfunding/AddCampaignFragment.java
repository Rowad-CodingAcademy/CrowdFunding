package com.abood.crowdfunding;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class AddCampaignFragment extends Fragment {

    private Campaigns posts;
    private EditText question;
    private ImageView postImage,camera,gallary;
    private Button save;
    int CAMERAS = 0;
    int GALLERY = 1;
    String ConvertImage ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_campaign, container, false);

        question = v.findViewById(R.id.question);


        camera = v.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAMERAS);

            }
        });

        gallary = v.findViewById(R.id.gallary);
        gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , GALLERY);

            }
        });

        postImage = v.findViewById(R.id.post_image);


        save = v.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!question.getText().toString().equals("") && postImage.getDrawable() != null){

//                    addPost();
                    getActivity().finish();

                } else {
                    Toast.makeText(getActivity(), "Enter Question and Image", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:

                if (resultCode == RESULT_OK && imageReturnedIntent != null) {
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    postImage.setImageBitmap(selectedImage);

                    ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                    byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                    ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

                    Log.d("Abood3",ConvertImage);

                }
                break;

            case 1:

                if(resultCode == RESULT_OK){

                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        postImage.setImageURI(selectedImage);

                        ByteArrayOutputStream byteArrayOutputStreamObject = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
                        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
                        ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

                        Log.d("Abood3",ConvertImage);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }
                break;
        }
    }


//    public void addPost(){
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppServer.IP+"addPosts.php", null, new Response.ErrorListener() {
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
//                String name = Login.userName;
//                Date date = new Date();
//                String formatDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);
//                String ques = question.getText().toString();
//
//                int type = getActivity().getIntent().getExtras().getInt("type");
//                String pType = "" ;
//                switch (type){
//                    case 0:
//                        pType = "flutter";
//                        break;
//                    case 1:
//                        pType = "kotlin";
//                        break;
//                    case 2:
//                        pType = "android";
//                        break;
//                }
//
//                info.put("name",name);
//                info.put("date",formatDate);
//                info.put("type",pType);
//                info.put("question",ques);
//                info.put("action","0");
//                info.put("image",ConvertImage);
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
