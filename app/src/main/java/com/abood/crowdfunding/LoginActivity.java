package com.abood.crowdfunding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity
{

    EditText userEmail,userPassword;
    Button loginBtn,signupBtn;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        loginBtn = findViewById(R.id.login_btn);
        signupBtn = findViewById(R.id.signup_btn);

        checkFieldsForEmptyValues();

        // set listeners
        userEmail.addTextChangedListener(mTextWatcher);
        userPassword.addTextChangedListener(mTextWatcher);

        loginBtn.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Logging in ...");
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(),userPassword.getText().toString())
                 .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task)
                     {

                      if(task.isSuccessful()) {

//                          firebaseFirestore = FirebaseFirestore.getInstance();
//                          firebaseFirestore.collection("Users")
//                                  .whereEqualTo("userId",firebaseAuth.getUid())
//                                  .whereEqualTo("userType","10000").get()
//                                  .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                      @Override
//                                      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
////                                          String type = queryDocumentSnapshots.getDocuments().get().getString("userType");
//                                          Toast.makeText(LoginActivity.this, "True", Toast.LENGTH_SHORT).show();
//
//                                      }
//                                  }).addOnFailureListener(new OnFailureListener() {
//                              @Override
//                              public void onFailure(@NonNull Exception e) {
//
//                                  Toast.makeText(LoginActivity.this, "False", Toast.LENGTH_SHORT).show();
//
//                              }
//                          });

//                          firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).get()
//                                  .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                      @Override
//                                      public void onComplete(@NonNull Task<DocumentSnapshot> task)
//                                      {
//
//                                          if(task.isSuccessful()) {
//
//                                              if (task.getResult().exists()) {
//
//                                                  task.getResult().getReference();
//                                                  String type = task.getResult().getString("userType");
//
//                                              }
//                                          }
//
//                                      }
//                                  });
//
                          Intent afterIntnet=new Intent(LoginActivity.this, CampaignsListActivity.class);
                          startActivity(afterIntnet);
                          progress.dismiss();
                          Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();

                      }
                      else {
                          progress.dismiss();

                          final AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
                          dialog.setTitle("Logging Failed");
                          dialog.setMessage(task.getException().getMessage());
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
                            }});
            }


        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signUp=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(signUp);


            }
        });
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
        }
    };

    private void checkFieldsForEmptyValues()
    {
        if(!userEmail.getText().toString().equals("") && !userPassword.getText().toString().equals(""))
        {
            loginBtn.setEnabled(true);
            loginBtn.setTextColor(getResources().getColor(R.color.enabledButtonTextColor));
        }
        else
        {
            loginBtn.setEnabled(false);
            loginBtn.setTextColor(getResources().getColor(R.color.disabledButtonTextColor));


        }

    }


}
