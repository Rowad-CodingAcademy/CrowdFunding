package com.abood.crowdfunding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class RegisterActivity extends AppCompatActivity
{

    EditText newUserEmail,newUserPassword;
    Button signupBtn;
    FirebaseAuth firebaseAuth;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        newUserEmail = findViewById(R.id.new_user_email);
        newUserPassword = findViewById(R.id.new_user_password);
        signupBtn = findViewById(R.id.signup_btn);

        checkFieldsForEmptyValues();

        // set listeners
        newUserEmail.addTextChangedListener(mTextWatcher);
        newUserPassword.addTextChangedListener(mTextWatcher);


        signupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                progress = new ProgressDialog(RegisterActivity.this);
                progress.setMessage("Creating Your Account ...");
                progress.setCancelable(false);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(newUserEmail.getText().toString(),newUserPassword.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    progress.dismiss();
                                    Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                                }
                                else
                                    {
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
        if(!newUserEmail.getText().toString().equals("") && !newUserPassword.getText().toString().equals(""))
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
