package com.smarttersstudio.feedbackzone.Fragmets;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.feedbackzone.JuniorListActivity;
import com.smarttersstudio.feedbackzone.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ManagerBottom extends Fragment {

    private View root;
    private Button submitButton,junior_button;
    private EditText feedBackText;
    private FirebaseAuth mAuth;
    private DatabaseReference feedbackRef,userRef;
    private ProgressDialog progressDialog;
    private String managerUid,uid;
    private int level;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_manager_bottom, container, false);
        submitButton=root.findViewById(R.id.manager_submit);
        feedBackText=root.findViewById(R.id.manager_feedback);
        junior_button=root.findViewById(R.id.junior_button);
        feedBackText.setEnabled(false);
        submitButton.setEnabled(false);
        junior_button.setEnabled(false);
        mAuth= FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        userRef=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                managerUid=dataSnapshot.child("manager").getValue().toString();
                level=Integer.parseInt(dataSnapshot.child("level").getValue().toString());
                feedbackRef= FirebaseDatabase.getInstance().getReference().child("feedback").child("manager").child(managerUid);
                if(level>1) {
                    junior_button.setEnabled(true);
                }
                if(!managerUid.equals("no")){
                    feedBackText.setEnabled(true);
                    submitButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        junior_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(level>2){
                    Intent i=new Intent(getActivity(), JuniorListActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getActivity(), "Your juniors don't have anyone to get feedback's like you", Toast.LENGTH_SHORT).show();
                }
            }
        });

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait while we are logging you in..");
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String feedback=feedBackText.getText().toString().trim();
                if(TextUtils.isEmpty(feedback)){
                    Toast.makeText(getActivity(), "can't give empty feedback..", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                   giveFeedBack(feedback);
                }
            }
        });
        return root;
    }
    void giveFeedBack(String feedback){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String d=formatter.format(date);
        Map m=new HashMap();
        m.put("uid","1");
        m.put("feedback",feedback);
        m.put("date",d);
        feedbackRef.push().updateChildren(m).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getActivity(), "FeedBack successfully posted...", Toast.LENGTH_SHORT).show();
                feedBackText.setText("");
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }


}
