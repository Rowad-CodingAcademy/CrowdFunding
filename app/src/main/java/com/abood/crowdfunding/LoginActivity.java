package com.abood.crowdfunding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    Button loginBTN,signupBTN;
    EditText userEmail,userPass;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail=findViewById(R.id.new_user_email);
        userPass=findViewById(R.id.new_user_pass);
        loginBTN=findViewById(R.id.login_btn);
        signupBTN=findViewById(R.id.signup_btn);

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth= FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(userEmail.getText().toString(),userPass.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    Intent afterIntnet=new Intent(LoginActivity.this,LoginActivity.class);
                                    startActivity(afterIntnet);
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signUp=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(signUp);


            }
        });
    }
}
