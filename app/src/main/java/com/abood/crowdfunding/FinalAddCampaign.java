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
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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

public class FinalAddCampaign extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST = 0;
    private Uri imageUri = null;
    private Uri mVideoUri;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    FirebaseStorage storage;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private Bitmap compressed;
    private ProgressDialog progressDialog;
    Button selectFile,upload;
    TextView notification;
    Uri pdfURi;

    EditText campaignTitle, campaignCountry, campaignCost, campaignDescription, campaignLocationt,campaignType,campaignDonationDays ;
    Button campaignStartDateBtn, campaignEndDateBtn, campaignNextBtn,campaignAddBtn,campaignNextBtnToLastPage;
    FloatingActionButton  campaignChooseImageBtn,campaignChooseVideoBtn;
    ImageView campaignImageView;
    VideoView campaignVideoView;
    ImageButton nextBTN,prevBTN;

    ViewFlipper mViewFlipper;
    private int currentSignUpViewNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        storage= FirebaseStorage.getInstance();

        setContentView(R.layout.activity_final_add_campaign);
        campaignTitle =findViewById(R.id.camp_title_edit_text);
        campaignCountry =findViewById(R.id.camp_country_edit_text);
        campaignCost =findViewById(R.id.camp_target_edit_text);
        campaignDescription =findViewById(R.id.camp_description_edit_text);
        campaignLocationt =findViewById(R.id.camp_location_edit_text);
        campaignDonationDays =findViewById(R.id.donation_date_edit_text);
        campaignImageView = findViewById(R.id.image_view);
        campaignType=findViewById(R.id.camp_type_edit_text);
        campaignAddBtn =findViewById(R.id.add_campaign_btn);
        campaignChooseImageBtn =findViewById(R.id.upload_new_photo);
        campaignChooseVideoBtn =findViewById(R.id.upload_pdf_file);
        mViewFlipper=findViewById(R.id.viewFlipper);
        notification=findViewById(R.id.notification);
        selectFile=findViewById(R.id.select_file);
        upload =  findViewById(R.id.upload);
//        campaignNextBtnToLastPage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                moveToView2();
//            }
//        });
//        campaignNextBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                moveToView2();
//            }
//        });


        campaignChooseImageBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(FinalAddCampaign.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(FinalAddCampaign.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(FinalAddCampaign.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

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
            public void onClick(View arg0) {

                if(ContextCompat.checkSelfPermission(FinalAddCampaign.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectPDF();
                }
                else
                {
                    ActivityCompat.requestPermissions(FinalAddCampaign.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });

        campaignAddBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Storing Data...");
                progressDialog.show();

                final String title = campaignTitle.getText().toString();
                final String country = campaignCountry.getText().toString();
                final String cost = campaignCost.getText().toString();
                final String description = campaignDescription.getText().toString();
                final String donatioDays = campaignDonationDays.getText().toString();
                final String type = campaignType.getText().toString();
                final String location = campaignLocationt.getText().toString();


                if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(country)&&!TextUtils.isEmpty(cost)&&imageUri!=null&&pdfURi!=null){

                    File newFile = new File(imageUri.getPath());

                    try {

                        compressed = new Compressor(FinalAddCampaign.this)
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
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                String fileName=System.currentTimeMillis()+"";
                                StorageReference storageReference = storage.getReference();//returns root path
                                storageReference.child("campaignsPDF").child(fileName).putFile(pdfURi)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                storeData(task, taskSnapshot, title, country, cost, description,location,type,donatioDays);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FinalAddCampaign.this, "not Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                                    }
                                });

                            } else {

                                String error = task.getException().getMessage();
                                Toast.makeText(FinalAddCampaign.this, "IMAGE Error: " + error, Toast.LENGTH_LONG).show();

                                progressDialog.dismiss();

                            }

                        }
                    });

                }
            }
        });

        nextBTN = findViewById(R.id.btn_next);
        prevBTN = findViewById(R.id.btn_previous);


        nextBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToView2();
            }
        });


        prevBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToView1();
            }
        });



    }

    private void storeData(Task<UploadTask.TaskSnapshot> task, UploadTask.TaskSnapshot taskSnapshot, String title, String country, String cost, String description, String location, String type, String donation) {


        Task<Uri> download_uri;
        Uri url = null;

        Task<Uri> pdf_download_uri;
        Uri pdf_url_ = null;

        if (task != null && taskSnapshot != null) {

            download_uri = task.getResult().getMetadata().getReference().getDownloadUrl();
            while (!download_uri.isComplete()) ;
            url = download_uri.getResult();

            pdf_download_uri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
            while (!pdf_download_uri.isComplete()) ;
            pdf_url_ = pdf_download_uri.getResult();

        } else {

            download_uri = null;
            pdf_download_uri = null;

        }

        Map<String, String> campaignData = new HashMap<>();
        campaignData.put("userId", user_id);
        campaignData.put("campaignTitle", title);
        campaignData.put("campaignCountry", country);
        campaignData.put("campaignCost", cost);
        campaignData.put("campaignDescription", description);
        campaignData.put("campaignLocation",location);
        campaignData.put("campaignType",type);
        campaignData.put("campaignDonationDays", donation);
        campaignData.put("campaignApprove", "0");
        campaignData.put("campaignStatus", "0");
        campaignData.put("campaignCategory", "0");
        campaignData.put("campaignFunds", "0");
        campaignData.put("campaignDonors", "0");
        campaignData.put("campaignImage", url.toString());
        campaignData.put("campaignPdf", pdf_url_.toString());

        firebaseFirestore.collection("Campaigns").add(campaignData).addOnSuccessListener(FinalAddCampaign.this, new OnSuccessListener<DocumentReference>() {

            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();

                Toast.makeText(FinalAddCampaign.this, "Campaigns Data is Stored Successfully", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(FinalAddCampaign.this, CampaignsListActivity.class);
                startActivity(mainIntent);
                finish();
            }

        }).addOnFailureListener(FinalAddCampaign.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FinalAddCampaign.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog.dismiss();

    }

    private void choseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(FinalAddCampaign.this);
    }
    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9&&grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            selectPDF();
        }
        else
        {
            Toast.makeText(this, "please,provide permission", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectPDF()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1);
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

        if (requestCode == 1&&resultCode==RESULT_OK&&data!=null) {

            pdfURi = data.getData();
        }
        else
        {
            Toast.makeText(this, "filed", Toast.LENGTH_SHORT).show();
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
