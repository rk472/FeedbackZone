package com.smarttersstudio.feedbackzone.Fragmets;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.feedbackzone.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AdministrationBottom extends Fragment {

    private Button submitButton;
    private EditText feedBackText;
    private FirebaseAuth mAuth;
    private DatabaseReference feedbackRef,totalRef,ratingRef;
    private RadioGroup r1,r2,r3;
    private EditText ans;
    private RatingBar rb;
    private String rate="0";
    private Float one,two,three,four,five;
    private Button updateButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root=inflater.inflate(R.layout.fragment_administration_bottom, container, false);
        submitButton=root.findViewById(R.id.admin_submit);
        feedBackText=root.findViewById(R.id.admin_feedback);
        feedbackRef= FirebaseDatabase.getInstance().getReference().child("feedback").child("admin");
        mAuth=FirebaseAuth.getInstance();
        totalRef=FirebaseDatabase.getInstance().getReference().child("total").child("admin");
        r1=root.findViewById(R.id.admin_radio_1);
        ratingRef=FirebaseDatabase.getInstance().getReference().child("rating").child("admin");
        r2=root.findViewById(R.id.admin_radio_2);
        r3=root.findViewById(R.id.admin_radio_3);
        ans=root.findViewById(R.id.admin_ans);
        rb=root.findViewById(R.id.admin_rate);
        updateButton=root.findViewById(R.id.admin_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rr1=root.findViewById(r1.getCheckedRadioButtonId());
                RadioButton rr2=root.findViewById(r2.getCheckedRadioButtonId());
                RadioButton rr3=root.findViewById(r3.getCheckedRadioButtonId());
                String comp=rr1.getText().toString();
                String balance=rr2.getText().toString();
                String action=rr3.getText().toString();
                String change=ans.getText().toString();
                String rate1=Integer.toString((int)rb.getRating());
                if(TextUtils.isEmpty(comp) ||TextUtils.isEmpty(balance) ||TextUtils.isEmpty(action) ||TextUtils.isEmpty(change) ||TextUtils.isEmpty(rate1) ){
                    Toast.makeText(getActivity(), "Can't rate with empty fields", Toast.LENGTH_SHORT).show();
                }else{
                    Map m=new HashMap();
                    m.put("compensation",comp);
                    m.put("balance",balance);
                    m.put("action",action);
                    m.put("change",change);
                    m.put("rate",rate1);
                    switch (Integer.parseInt(rate1)){
                        case 1:
                            one++;
                            break;
                        case 2:
                            two++;
                            break;
                        case 3:
                            three++;
                            break;
                        case 4:
                            four++;
                            break;
                        case 5:
                            five++;

                    }
                    switch (Integer.parseInt(rate)){
                        case 1:
                            one--;
                            break;
                        case 2:
                            two--;
                            break;
                        case 3:
                            three--;
                            break;
                        case 4:
                            four--;
                            break;
                        case 5:
                            five--;

                    }
                    final String uid=mAuth.getCurrentUser().getUid();
                    ratingRef.child(uid).updateChildren(m).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            totalRef.child("one").setValue(Float.toString(one));
                            totalRef.child("two").setValue(Float.toString(two));
                            totalRef.child("three").setValue(Float.toString(three));
                            totalRef.child("four").setValue(Float.toString(four));
                            totalRef.child("five").setValue(Float.toString(five));
                            Toast.makeText(getActivity(), "Rating Successfully updated...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        totalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                one=Float.parseFloat(dataSnapshot.child("one").getValue().toString());
                two=Float.parseFloat(dataSnapshot.child("two").getValue().toString());
                three=Float.parseFloat(dataSnapshot.child("three").getValue().toString());
                four=Float.parseFloat(dataSnapshot.child("four").getValue().toString());
                five=Float.parseFloat(dataSnapshot.child("five").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final String uid=mAuth.getCurrentUser().getUid();

        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)){
                    String comp=dataSnapshot.child(uid).child("compensation").getValue().toString();
                    String balance=dataSnapshot.child(uid).child("balance").getValue().toString();
                    String action=dataSnapshot.child(uid).child("action").getValue().toString();
                    String change=dataSnapshot.child(uid).child("change").getValue().toString();
                    rate=dataSnapshot.child(uid).child("rate").getValue().toString();
                    if(comp.equalsIgnoreCase("Yes")) r1.check(R.id.admin_yes_1); else r1.check(R.id.admin_no_1);
                    if(balance.equalsIgnoreCase("Yes")) r2.check(R.id.admin_yes_2); else r2.check(R.id.admin_no_2);
                    if(action.equalsIgnoreCase("Yes")) r2.check(R.id.admin_yes_3); else r3.check(R.id.admin_no_3);
                    ans.setText(change);
                    rb.setRating(Float.parseFloat(rate));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String feedback=feedBackText.getText().toString().trim();
                if(TextUtils.isEmpty(feedback)){
                    Toast.makeText(getActivity(), "can't give empty feedback..", Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Give Feedback")
                            .setMessage("Do You want to show your name ?")
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String uid=mAuth.getCurrentUser().getUid();
                                            giveFeedBack(feedback,uid);
                                        }
                                    }).setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    giveFeedBack(feedback,"1");
                                }
                            }).show();
                }
            }
        });
       return root;
    }
    void giveFeedBack(String feedback,String uid){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String d=formatter.format(date);
        Map m=new HashMap();
        m.put("uid",uid);
        m.put("feedback",feedback);
        m.put("date",d);
        feedbackRef.push().updateChildren(m).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getActivity(), "FeedBack successfully posted...", Toast.LENGTH_SHORT).show();
                feedBackText.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
