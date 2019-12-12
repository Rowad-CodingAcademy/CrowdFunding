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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

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
        setContentView(R.layout.activity_login);

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

                          firebaseFirestore = FirebaseFirestore.getInstance();

                          Task<QuerySnapshot> query = firebaseFirestore.collection("Users").whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                                  .get()
                                  .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                          if (task.isSuccessful()) {
                                              for (QueryDocumentSnapshot document : task.getResult()) {


                                                  String type = document.getString("userType");

                                                  if (type.equals("0")){

                                                      Intent intent = new Intent(LoginActivity.this,AdminDashboardActivity.class);
                                                      startActivity(intent);

                                                  }else if (type.equals("1")){

                                                      Intent intent = new Intent(LoginActivity.this,CampaignsListActivity.class);
                                                      startActivity(intent);

                                                  }

                                              }
                                          } else {

                                          }
                                      }
                                  });

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
