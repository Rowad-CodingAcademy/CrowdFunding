package com.abood.crowdfunding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadPDF extends AppCompatActivity {
    Button selectFile,upload;
    TextView notification;
    FirebaseStorage storage;
    FirebaseFirestore store;
    Uri pdfURi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        notification=findViewById(R.id.notification);
        selectFile=findViewById(R.id.select_file);
        upload =  findViewById(R.id.upload);
        storage= FirebaseStorage.getInstance();
        store= FirebaseFirestore.getInstance();
        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(ContextCompat.checkSelfPermission(UploadPDF.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectPDF();
                }
                else
                {
                    ActivityCompat.requestPermissions(UploadPDF.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfURi!=null) {
                    uploadFile(pdfURi);
                }
                else
                {
                    Toast.makeText(UploadPDF.this, "Select a file", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void uploadFile(Uri pdfURi) {
        String fileName=System.currentTimeMillis()+"";
        StorageReference storageReference=storage.getReference();//returns root path
        storageReference.child("uploads").child(fileName).putFile(pdfURi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadPDF.this, "Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPDF.this, "not Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


            }
        });
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


    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (requestCode == 1&&resultCode==RESULT_OK&&result!=null) {

            pdfURi=result.getData();
        }
        else
        {
            Toast.makeText(this, "filed", Toast.LENGTH_SHORT).show();
        }
    }

    }

