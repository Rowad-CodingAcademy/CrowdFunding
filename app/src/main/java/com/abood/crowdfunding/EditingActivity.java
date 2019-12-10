package com.abood.crowdfunding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static com.abood.crowdfunding.CampaignDetailsActivity.EXTRA_CAMPAIGN_UUID;

public class EditingActivity extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST = 0;
    private Uri imageUri = null;
    private Uri mVideoUri;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private Bitmap compressed;
    private ProgressDialog progressDialog;
    String campaignId;
    String photoUrl;


    EditText campaignTitle, campaignCountry, campaignCost, campaignDescription, campaignLocationt,campaignType;
    Button campaignStartDateBtn, campaignEndDateBtn, campaignNextBtn,campaignAddBtn,campaignNextBtnToLastPage;
    FloatingActionButton campaignChooseImageBtn,campaignChooseVideoBtn;
    ImageView campaignImageView;
    VideoView campaignVideoView;

    ViewFlipper mViewFlipper;
    private int currentSignUpViewNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        campaignId =  getIntent().getStringExtra(EXTRA_CAMPAIGN_UUID);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        setContentView(R.layout.activity_editing);

        campaignTitle =findViewById(R.id.camp_title_edit_text);
        campaignCountry =findViewById(R.id.camp_country_edit_text);
        campaignCost =findViewById(R.id.camp_target_edit_text);
        campaignDescription =findViewById(R.id.camp_description_edit_text);
        campaignLocationt =findViewById(R.id.camp_location_edit_text);
        campaignImageView = findViewById(R.id.image_view);
        campaignVideoView = findViewById(R.id.video_view);
        campaignEndDateBtn =findViewById(R.id.comp_end_btn);
        campaignStartDateBtn =findViewById(R.id.comp_start_date_btn);
        campaignType=findViewById(R.id.camp_type_edit_text);
        campaignAddBtn =findViewById(R.id.add_campaign_btn);
        campaignChooseImageBtn =findViewById(R.id.upload_new_photo);
        campaignChooseVideoBtn =findViewById(R.id.upload_new_video);
        mViewFlipper=findViewById(R.id.viewFlipper);
        campaignNextBtnToLastPage=findViewById(R.id.next_btn_to_last_page);



        firebaseFirestore.collection("Campaigns").document(campaignId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {

                        if(task.isSuccessful()) {

                            progressDialog.dismiss();

                            if (task.getResult().exists())
                            {

                                campaignTitle.setText(task.getResult().getString("campaignTitle"));
                                campaignDescription.setText(task.getResult().getString("campaignDescription"));
                                campaignCountry.setText(task.getResult().getString("campaignCountry"));
                                campaignLocationt.setText(task.getResult().getString("campaignLocation"));
                                campaignCost.setText(task.getResult().getString("campaignCost"));


                                Glide.with(EditingActivity.this).load(task.getResult().getString("campaignImage")).into(campaignImageView);


                            } else {
                                Toast.makeText(EditingActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });




        campaignNextBtnToLastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToView2();
            }
        });
        campaignNextBtn=findViewById(R.id.next_btn_to_next_page);
        campaignNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moveToView2();
            }
        });


        campaignChooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(EditingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(EditingActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(EditingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        choseImage();

                    }

                } else {

                    choseImage();

                }
            }
        });

        campaignChooseVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(EditingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(EditingActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(EditingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        chooseVideo();

                    }

                } else {

                    chooseVideo();

                }
            }
        });

        campaignAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Storing Data...");
                progressDialog.show();

                final String title = campaignTitle.getText().toString();
                final String country = campaignCountry.getText().toString();
                final String cost = campaignCost.getText().toString();
                final String description = campaignDescription.getText().toString();
                final String type = campaignType.getText().toString();
                final String location = campaignLocationt.getText().toString();


                if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(country)&&!TextUtils.isEmpty(cost)&&imageUri!=null){

                    File newFile = new File(imageUri.getPath());


                    try {

                        compressed = new Compressor(EditingActivity.this)
                                .setMaxHeight(125)
                                .setMaxWidth(125)
                                .setQuality(50)
                                .compressToBitmap(newFile);

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    compressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] thumbData = byteArrayOutputStream.toByteArray();
                    UploadTask image_path = storageReference.child("campaignImages").child(System.currentTimeMillis() + ".jpg").putBytes(thumbData);
                    image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {


                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                storeData(task, title, country, cost, description,location,type);

                            } else {

                                String error = task.getException().getMessage();
                                Toast.makeText(EditingActivity.this, "IMAGE Error: " + error, Toast.LENGTH_LONG).show();

                                progressDialog.dismiss();

                            }

                        }
                    });
                }
            }
        });
    }

    private void storeData(Task<UploadTask.TaskSnapshot> task, String title, String country, String cost, String description,String location,String type) {


        Task<Uri> download_uri;
        Uri url = null;

        if (task != null) {

            download_uri = task.getResult().getMetadata().getReference().getDownloadUrl();
            while (!download_uri.isComplete()) ;
            url = download_uri.getResult();

        } else {

            download_uri = null;

        }

        Map<String, String> campaignData = new HashMap<>();
        campaignData.put("userId", user_id);
        campaignData.put("campaignTitle", title);
        campaignData.put("campaignCountry", country);
        campaignData.put("campaignCost", cost);
        campaignData.put("campaignDescription", description);
        campaignData.put("campaignLocation",location);
        campaignData.put("campaignType",type);
        campaignData.put("campaignApprove", "0");
        campaignData.put("campaignStatus", "1");
        campaignData.put("campaignCategory", "1");
        campaignData.put("campaignDays", "1");
        campaignData.put("campaignFunds", "0");
        campaignData.put("campaignDonors", "0");
        campaignData.put("campaignImage", url.toString());

        firebaseFirestore.collection("Campaigns").add(campaignData).addOnSuccessListener(EditingActivity.this, new OnSuccessListener<DocumentReference>() {

            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();

                Toast.makeText(EditingActivity.this, "Campaigns Data is Stored Successfully", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(EditingActivity.this, CampaignsListActivity.class);
                startActivity(mainIntent);
                finish();
            }

        }).addOnFailureListener(EditingActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog.dismiss();

    }

    private void choseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(EditingActivity.this);
    }
    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                campaignImageView.setImageURI(imageUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
        else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mVideoUri = data.getData();

            // Picasso.with().load(mImageUri).into(mImageView);
            campaignVideoView.setVideoURI(mVideoUri);
        }

    }

    private void moveToView1() {
        mViewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        mViewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        mViewFlipper.showPrevious();
        currentSignUpViewNumber--;
    }
    private void moveToView2() {
        mViewFlipper.setInAnimation(this, R.anim.slide_in_right);
        mViewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        mViewFlipper.showNext();
        currentSignUpViewNumber++;

    }
}
