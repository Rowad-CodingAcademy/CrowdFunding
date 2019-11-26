package com.abood.crowdfunding;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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


public class AddCampaignActivity extends AppCompatActivity {


    EditText campaignTitle, campaignCountry, campaignCost, campaignDescription, campaignLocationt;
    Button campaignStartDateBtn, campaignEndDateBtn, campaignSubmitBtn;
    TextView mTextViewShowUploads;
    ImageView campaignImageView;
    VideoView campaignVideoView;

    private Uri imageUri = null;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private Bitmap compressed;
    private ProgressDialog progressDialog;

    private static final int PICK_VIDEO_REQUEST = 0;
    private Button campaignChooseImageBtn, campaignChooseVideoBtn;
    private ProgressBar mProgressBarImage,mProgressBarVideo;
    private Uri mVideoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_campaign);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        campaignTitle =findViewById(R.id.comp_title_edit_text);
        campaignCountry =findViewById(R.id.comp_country_edit_text);
        campaignCost =findViewById(R.id.comp_target_edit_text);
        campaignDescription =findViewById(R.id.comp_descrption_edit_text);
        campaignLocationt =findViewById(R.id.comp_location_edit_text);
        mTextViewShowUploads=findViewById(R.id.text_view_show_uploads);
        campaignImageView = findViewById(R.id.camp_image_view);
        campaignVideoView = findViewById(R.id.camp_video_view);
        mProgressBarImage = findViewById(R.id.progress_bar_image);
        mProgressBarVideo = findViewById(R.id.progress_bar_video);
        campaignEndDateBtn =findViewById(R.id.comp_end_btn);
        campaignStartDateBtn =findViewById(R.id.comp_start_date_btn);
        campaignSubmitBtn =findViewById(R.id.submit_btn);
        campaignChooseImageBtn =findViewById(R.id.button_choose_image);
        campaignChooseVideoBtn =findViewById(R.id.button_choose_video);

        campaignImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(AddCampaignActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(AddCampaignActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(AddCampaignActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        choseImage();

                    }

                } else {

                    choseImage();

                }
            }
        });

        campaignSubmitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Storing Data...");
                progressDialog.show();

                final String title = campaignTitle.getText().toString();
                final String country = campaignCountry.getText().toString();
                final String cost = campaignCost.getText().toString();
                final String description = campaignDescription.getText().toString();


                if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(country)&&!TextUtils.isEmpty(cost)&&imageUri!=null){

                    File newFile = new File(imageUri.getPath());


                    try {

                        compressed = new Compressor(AddCampaignActivity.this)
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

                                storeData(task, title, country, cost, description);

                            } else {

                                String error = task.getException().getMessage();
                                Toast.makeText(AddCampaignActivity.this, "IMAGE Error: " + error, Toast.LENGTH_LONG).show();

                                progressDialog.dismiss();

                            }

                        }
                    });
                }
            }
        });

    }

    private void choseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(AddCampaignActivity.this);
    }


    private void storeData(Task<UploadTask.TaskSnapshot> task, String title, String country, String cost, String description) {


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
        campaignData.put("campaignApprove", "0");
        campaignData.put("campaignStatus", "1");
        campaignData.put("campaignFund", "0");
        campaignData.put("campaignImage", url.toString());

        firebaseFirestore.collection("Campaigns").add(campaignData).addOnSuccessListener(AddCampaignActivity.this, new OnSuccessListener<DocumentReference>() {

            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();

                Toast.makeText(AddCampaignActivity.this, "Campaigns Data is Stored Successfully", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(AddCampaignActivity.this, CampaignListActivity.class);
                startActivity(mainIntent);
                finish();
            }

        }).addOnFailureListener(AddCampaignActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddCampaignActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog.dismiss();

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

    }

}