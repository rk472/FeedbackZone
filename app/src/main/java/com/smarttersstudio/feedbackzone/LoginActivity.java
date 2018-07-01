package com.smarttersstudio.feedbackzone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText emailText,passwordText;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText=findViewById(R.id.login_email);
        passwordText=findViewById(R.id.login_password);
        mAuth=FirebaseAuth.getInstance();
    }

    public void login(View view) {
        String email=emailText.getText().toString();
        String password=passwordText.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Fields can't be blank....", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        finish();
    }

    public void goToForgot(View view) {
        startActivity(new Intent(LoginActivity.this,ForgotActivity.class));
    }

    public void goToSignup(View view) {
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
    }
}
