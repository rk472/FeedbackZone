package com.smarttersstudio.feedbackzone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private EditText nameText,mailText,phoneText,passText,postText;
    private Spinner dept,level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameText=findViewById(R.id.create_name);
        mailText=findViewById(R.id.create_mail);
        phoneText=findViewById(R.id.create_phone);
        passText=findViewById(R.id.create_pass);
        postText=findViewById(R.id.create_post);
        dept=findViewById(R.id.create_dept);
        level=findViewById(R.id.create_level);
    }

    public void create(View view) {
        String name=nameText.getText().toString();
        String mail=mailText.getText().toString();
        String phone=phoneText.getText().toString();
        String d=dept.getSelectedItem().toString();
        String pass=passText.getText().toString();
        String post=postText.getText().toString();
        String l=level.getSelectedItem().toString().split(" ")[2];
        if(TextUtils.isEmpty(name) ||TextUtils.isEmpty(mail) ||TextUtils.isEmpty(pass) ||TextUtils.isEmpty(phone) ||TextUtils.isEmpty(post) ){
            Toast.makeText(this, "All Fields must be filled", Toast.LENGTH_SHORT).show();
        }else{

            Intent i=new Intent(SignupActivity.this,ConfirmCreateActivity.class);
            i.putExtra("name",name);
            i.putExtra("phone",phone);
            i.putExtra("mail",mail);
            i.putExtra("level",l);
            i.putExtra("department",d);
            i.putExtra("designation",post);
            i.putExtra("pass",pass);
            startActivityForResult(i,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            finish();
        }
    }
}
