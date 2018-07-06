package com.smarttersstudio.feedbackzone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.view.View.GONE;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private TextView nameText,mailText,phoneText,designationText,departmentText;
    private CircleImageView dp;
    private ProgressDialog progressDialog;
    private String name,mail,phone,dept,designation,image,level;
    private Bitmap thumb_bitmap;
    private String uid;
    private FloatingActionButton fab1,fab2;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we are loading your Profile...");
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        fab1=findViewById(R.id.profile_edit);
        fab2=findViewById(R.id.profile_photo);
        logout=findViewById(R.id.profile_logout);
        uid=getIntent().getExtras().getString("uid");
        if(uid==null)
            uid=mAuth.getCurrentUser().getUid();
        else{
            fab2.setVisibility(View.INVISIBLE);
            fab1.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.GONE);
            fab1.setEnabled(false);
            fab2.setEnabled(false);
            logout.setEnabled(false);
        }
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        nameText=findViewById(R.id.profile_name);
        mailText=findViewById(R.id.profile_mail);
        phoneText=findViewById(R.id.profile_phone);
        designationText=findViewById(R.id.profile_designation);
        departmentText=findViewById(R.id.profile_dept);
        dp=findViewById(R.id.profile_dp);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("name").getValue().toString();
                mail=dataSnapshot.child("mail").getValue().toString();
                phone=dataSnapshot.child("phone").getValue().toString();
                image=dataSnapshot.child("image").getValue().toString();
                designation=dataSnapshot.child("designation").getValue().toString();
                dept=dataSnapshot.child("department").getValue().toString();
                level=dataSnapshot.child("level").getValue().toString();
                nameText.setText(name);
                mailText.setText(mail);
                phoneText.setText(phone);
                designationText.setText(designation);
                departmentText.setText(dept);
                Picasso.with(ProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_icon)
                        .into(dp, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile_icon).into(dp);
                            }
                        });
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void logout(View view) {
        mAuth.signOut();
        finish();
    }

    public void upload(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(ProfileActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog.setMessage("Wait while We are updating your Profile Picture..");
                progressDialog.setTitle("Please Wait");
                progressDialog.show();
                Uri resultUri = result.getUri();
                final String uid=mAuth.getCurrentUser().getUid();
                File thumb_filePath=new File(resultUri.getPath());
                try{
                    thumb_bitmap=new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxHeight(200)
                            .setQuality(10)
                            .compressToBitmap(thumb_filePath);
                }catch (Exception e){
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                final byte[] mbyte=byteArrayOutputStream.toByteArray();
                final StorageReference thumbFilePath= FirebaseStorage.getInstance().getReference().child(uid+".jpg");
                UploadTask uploadTask=thumbFilePath.putBytes(mbyte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    if(thumb_task.isSuccessful()) {
                                        final String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("image").setValue(thumb_downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                    Toast.makeText(ProfileActivity.this, "Profile Picture Updated Successfully..", Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(ProfileActivity.this, thumb_task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(ProfileActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void goToEdit(View view) {
        Intent i=new Intent(this,EditActivity.class);
        i.putExtra("name",name);
        i.putExtra("desc",designation);
        i.putExtra("level",level);
        i.putExtra("phone",phone);
        startActivity(i);
    }
}
