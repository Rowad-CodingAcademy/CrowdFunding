package com.abood.crowdfunding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
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

public class RegisterActivity extends AppCompatActivity
{

    ImageView userProfile;
    FloatingActionButton newUserProfile;
    EditText newUserName, newUserEmail,newUserPassword;
    Button signupBtn;
    private ActionBar actionBar;
    private Toolbar toolbar;
    ProgressDialog progress;
    Intent intent;

    private Uri imageUri = null;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private Bitmap compressed;

    Drawable correctDrawable;
    Drawable errorDrawable;
    boolean isCorrectEmail=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initToolbar();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        newUserProfile = findViewById(R.id.user_profile);
        userProfile = findViewById(R.id.upload_new_photo);
        newUserName = findViewById(R.id.new_user_name);
        newUserEmail = findViewById(R.id.new_user_email);
        newUserPassword = findViewById(R.id.new_user_password);
        signupBtn = findViewById(R.id.signup_btn);

        checkFieldsForEmptyValues();

        newUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(RegisterActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        choseImage();

                    }

                } else {

                    choseImage();

                }
            }
        });

        // set listeners
        newUserEmail.addTextChangedListener(mTextWatcher);
        newUserPassword.addTextChangedListener(mTextWatcher);


        signupBtn.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View view) {

            progress = new ProgressDialog(RegisterActivity.this);
            progress.setMessage("Creating Your Account...");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();

             firebaseAuth.createUserWithEmailAndPassword(newUserEmail.getText().toString(),newUserPassword.getText().toString())
                  .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

//                            if(imageUri!=null){
                                user_id = firebaseAuth.getCurrentUser().getUid();

                                File newFile = new File(imageUri.getPath());

                                try {

                                    compressed = new Compressor(RegisterActivity.this)
                                            .setMaxHeight(200)
                                            .setMaxWidth(200)
                                            .setQuality(500)
                                            .compressToBitmap(newFile);

                                } catch (IOException e) {

                                    e.printStackTrace();

                                }

                                final String name = newUserName.getText().toString();

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                compressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] thumbData = byteArrayOutputStream.toByteArray();
                                UploadTask image_path = storageReference.child("userImages").child(user_id + ".jpg").putBytes(thumbData);
                                image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        if (task.isSuccessful()) {

                                            storeData(task, name);

                                        } else {

                                            String error = task.getException().getMessage();
                                            Toast.makeText(RegisterActivity.this, "IMAGE Error: " + error, Toast.LENGTH_LONG).show();

                                            progress.dismiss();

                                        }

                                    }
                                });
                        }
                        else {
                            progress.dismiss();

                            final AlertDialog.Builder dialog=new AlertDialog.Builder(RegisterActivity.this);
                            dialog.setMessage(  task.getException().getMessage());
                            dialog.setCancelable(true);

                            dialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    dialogInterface.cancel();
                                }
                            });

                            dialog.create();
                            dialog.show();
                        }
                   }
                  });
            }
        });
}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.game_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.btn:
//                newGame();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.pink_600));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void choseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(RegisterActivity.this);
}


    private void storeData(Task<UploadTask.TaskSnapshot> task, final String name) {


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
        campaignData.put("userName", name);
        campaignData.put("userImage", url.toString());
        campaignData.put("userType", "1");

        firebaseFirestore.collection("Users").add(campaignData).addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<DocumentReference>() {

            @Override
            public void onSuccess(DocumentReference documentReference) {
                progress.dismiss();

                Toast.makeText(RegisterActivity.this, "Welcome "+name, Toast.LENGTH_SHORT).show();
                intent = new Intent(RegisterActivity.this,CampaignsListActivity.class);
                startActivity(intent);

            }

        }).addOnFailureListener(RegisterActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        progress.dismiss();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                userProfile.setImageURI(imageUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }


    private TextWatcher mTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
            correctDrawable = getResources().getDrawable(R.drawable.ic_check_circle);
            correctDrawable.setBounds(0, 0, correctDrawable.getIntrinsicWidth(), correctDrawable.getIntrinsicHeight());
            errorDrawable = getResources().getDrawable(R.drawable.ic_error);
            errorDrawable.setBounds(0, 0, correctDrawable.getIntrinsicWidth(), correctDrawable.getIntrinsicHeight());

            if (editable.length() > 0 && newUserEmail.length() > 0) {

                CharSequence email = newUserEmail.getText().toString();

                isValidEmail(email);

                if (isCorrectEmail) {
                    newUserEmail.setCompoundDrawables(null,null,correctDrawable,null);
                } else if(newUserEmail.length()!=0) {
                    newUserEmail.setError("Enter Correct Email",null);
                    newUserEmail.setCompoundDrawables(null,null,errorDrawable,null);
                }

                if(newUserPassword.length()!=0 && newUserPassword.length()>=6)
                    newUserPassword.setCompoundDrawables(null,null,correctDrawable,null);
                else if(newUserPassword.length()!=0){
                    newUserPassword.setCompoundDrawables(null,null,errorDrawable,null);
                    newUserPassword.setError("password must be more than 6 character",null);}
            }

        }
    };

    public  boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return isCorrectEmail=false;
        } else {
            return
                    isCorrectEmail= android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



    private void checkFieldsForEmptyValues()
    {
        if(!newUserName.getText().toString().equals("") && !newUserEmail.getText().toString().equals("") && !newUserPassword.getText().toString().equals("") && imageUri != null)
        {
            signupBtn.setEnabled(true);
            signupBtn.setTextColor(getResources().getColor(R.color.enabledButtonTextColor));
        }
        else
        {
            signupBtn.setEnabled(false);
            signupBtn.setTextColor(getResources().getColor(R.color.disabledButtonTextColor));


        }

    }
}
