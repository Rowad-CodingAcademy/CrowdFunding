package com.abood.crowdfunding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity
{

    EditText userEmail,userPassword;
    Button loginBtn,signupBtn;
    private ActionBar actionBar;
    private Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ProgressDialog progress;
    static String token;
    Drawable correctDrawable;
    Drawable errorDrawable;
    boolean isCorrectEmail=false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initToolbar();

        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        loginBtn = findViewById(R.id.login_btn);
        signupBtn = findViewById(R.id.signup_btn);

//        checkFieldsForEmptyValues();

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

                                                      FirebaseMessaging.getInstance().subscribeToTopic("Admins");
                                                      token = FirebaseInstanceId.getInstance().getToken();
                                                      Intent intent = new Intent(LoginActivity.this,AdminDashboardActivity.class);
                                                      startActivity(intent);

                                                  }else if (type.equals("1")){

                                                      FirebaseMessaging.getInstance().subscribeToTopic("Users");
//                                                      token = FirebaseInstanceId.getInstance().getToken();
                                                      Intent intent = new Intent(LoginActivity.this,CampaignsListActivity.class);
                                                      startActivity(intent);

                                                  }

                                                  progress.dismiss();

                                              }
                                          } else {

                                              Toast.makeText(LoginActivity.this, "Error AAA", Toast.LENGTH_SHORT).show();

                                          }
                                      }
                                  });

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

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.pink_600));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
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
//            checkFieldsForEmptyValues();
            correctDrawable = getResources().getDrawable(R.drawable.ic_check_circle);
            correctDrawable.setBounds(0, 0, correctDrawable.getIntrinsicWidth(), correctDrawable.getIntrinsicHeight());
            errorDrawable = getResources().getDrawable(R.drawable.ic_error);
            errorDrawable.setBounds(0, 0, correctDrawable.getIntrinsicWidth(), correctDrawable.getIntrinsicHeight());

            if (editable.length() > 0 && userEmail.length() > 0) {

                CharSequence email = userEmail.getText().toString();

                isValidEmail(email);

                if (isCorrectEmail) {
                    userEmail.setCompoundDrawables(null,null,correctDrawable,null);
                } else if(userEmail.length()!=0) {
                    userEmail.setError("Enter Correct Email",null);
                    userEmail.setCompoundDrawables(null,null,errorDrawable,null);
                }

                if(userPassword.length()!=0 && userPassword.length()>=6)
                    userPassword.setCompoundDrawables(null,null,correctDrawable,null);
                else if(userPassword.length()!=0){
                    userPassword.setCompoundDrawables(null,null,errorDrawable,null);
                    userPassword.setError("password must be more than 6 character",null);}
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

//    private void checkFieldsForEmptyValues()
//    {
//        if(!userEmail.getText().toString().equals("") && !userPassword.getText().toString().equals(""))
//        {
//            loginBtn.setEnabled(true);
//            loginBtn.setTextColor(getResources().getColor(R.color.enabledButtonTextColor));
//        }
//        else
//        {
//            loginBtn.setEnabled(false);
//            loginBtn.setTextColor(getResources().getColor(R.color.disabledButtonTextColor));
//
//
//        }
//
//    }




}
