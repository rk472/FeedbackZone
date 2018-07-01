package com.smarttersstudio.feedbackzone;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailText;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth=FirebaseAuth.getInstance();
        emailText=findViewById(R.id.forgot_email);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we are sending the mail..");
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    public void reset(View view) {
        progressDialog.show();
        String email=emailText.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email can't be empty...", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.hide();
                    Toast.makeText(ForgotActivity.this, "Mail sent Successfully...", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.hide();
                    Toast.makeText(ForgotActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
