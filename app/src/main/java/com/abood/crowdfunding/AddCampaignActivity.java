package com.abood.crowdfunding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AddCampaignActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 0;
    EditText campTitleEditText,campCountryEditText,campTargetEditText,campDescriptionEditText,campLocationEditText;
    Button campDateStartBTN,campDateEndBTN,campSubmitBTN;
    TextView mTextViewShowUploads;
    ImageView mImageView;
    VideoView mVideoView;
    private Button mButtonChooseImage,mButtonChooseVideo;

    private ProgressBar mProgressBarImage,mProgressBarVideo;

    private Uri mImageUri;
    private Uri mVideoUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_campaign);

        campTitleEditText=findViewById(R.id.comp_title_edit_text);
        campCountryEditText=findViewById(R.id.comp_country_edit_text);
        campTargetEditText=findViewById(R.id.comp_target_edit_text);
        campDescriptionEditText=findViewById(R.id.comp_descrption_edit_text);
        campLocationEditText=findViewById(R.id.comp_location_edit_text);
        mTextViewShowUploads=findViewById(R.id.text_view_show_uploads);
        mImageView = findViewById(R.id.camp_image_view);
        mVideoView = findViewById(R.id.camp_video_view);
        mProgressBarImage = findViewById(R.id.progress_bar_image);
        mProgressBarVideo = findViewById(R.id.progress_bar_video);
        campDateEndBTN=findViewById(R.id.comp_end_btn);
        campDateStartBTN=findViewById(R.id.comp_start_date_btn);
        campSubmitBTN=findViewById(R.id.submit_btn);
        mButtonChooseImage=findViewById(R.id.button_choose_image);
        mButtonChooseVideo=findViewById(R.id.button_choose_video);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        mButtonChooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooserVideo();
            }
        });

        campSubmitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(AddCampaignActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    //uploadFile();
                }
            }
        });


    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openFileChooserVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            // Picasso.with().load(mImageUri).into(mImageView);
            mImageView.setImageURI(mImageUri);
        }
        else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mVideoUri = data.getData();

            // Picasso.with().load(mImageUri).into(mImageView);
            mVideoView.setVideoURI(mVideoUri);
        }
    }

    // retrive image extention...
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private String getVideoExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

   /* private void uploadFile() {
        if (mImageUri != null&&mVideoUri!=null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));


            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBarImage.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(AddCampaignActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Campaign upload = new Campaign(campTitleEditText.getText().toString().trim(),
                                    campDescriptionEditText.getText().toString().trim(),campTargetEditText.getText().toString().trim(),
                                    campLocationEditText.getText().toString().trim(),campCountryEditText.getText().toString().trim(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString()
                                    );
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddCampaignActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBarImage.setProgress((int) progress)
                            ;
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }*/
}
