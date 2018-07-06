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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    private String level,name,designation,phone;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private EditText nameText,descText,phoneText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        level=getIntent().getExtras().getString("level");
        name=getIntent().getExtras().getString("name");
        designation=getIntent().getExtras().getString("desc");
        phone=getIntent().getExtras().getString("phone");
        nameText=findViewById(R.id.edit_name);
        phoneText=findViewById(R.id.edit_phone);
        descText=findViewById(R.id.edit_desc);
        nameText.setText(name);
        phoneText.setText(phone);
        descText.setText(designation);
        mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
    }

    public void update(final View view) {
        view.setEnabled(false);
        String name1=nameText.getText().toString();
        String desc1=descText.getText().toString();
        String phone1=phoneText.getText().toString();
        if(TextUtils.isEmpty(name1) || TextUtils.isEmpty(desc1) || TextUtils.isEmpty(phone1)){
            Toast.makeText(this, "Cant update empty fields..", Toast.LENGTH_SHORT).show();
        }else{
            Map m=new HashMap();
            m.put("name",name1);
            m.put("designation",desc1);
            m.put("phone",phone1);
            userRef.updateChildren(m).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(EditActivity.this, "profile Successfully updated..", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    view.setEnabled(true);
                }
            });
        }
    }

    public void updateManager(View view) {
        Intent i=new Intent(this,UpdateManagerActivity.class);
        i.putExtra("level",level);
        startActivity(i);
    }
}
