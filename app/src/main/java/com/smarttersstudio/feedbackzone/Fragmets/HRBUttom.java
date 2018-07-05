package com.smarttersstudio.feedbackzone.Fragmets;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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


public class HRBUttom extends Fragment {
    private Button submitButton,updateButton;
    private EditText feedBackText;
    private FirebaseAuth mAuth;
    private DatabaseReference feedbackRef,rateRef,totalRef;
    private View root;
    private ProgressDialog progressDialog;
    private RadioGroup r1,r2,r3,r4,r5;
    private String rate="0";
    private RatingBar rb;
    private Float one,two,three,four,five;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_hrbuttom, container, false);
        submitButton=root.findViewById(R.id.HR_submit);
        feedBackText=root.findViewById(R.id.HR_feedback);
        feedbackRef= FirebaseDatabase.getInstance().getReference().child("feedback").child("HR");
        rateRef=FirebaseDatabase.getInstance().getReference().child("rating").child("HR");
        totalRef=FirebaseDatabase.getInstance().getReference().child("total").child("HR");
        mAuth= FirebaseAuth.getInstance();
        final String uid=mAuth.getCurrentUser().getUid();
        r1=root.findViewById(R.id.HR_radio_1);
        r2=root.findViewById(R.id.HR_radio_2);
        r3=root.findViewById(R.id.HR_radio_3);
        r4=root.findViewById(R.id.HR_radio_4);
        r5=root.findViewById(R.id.HR_radio_5);
        rb=root.findViewById(R.id.HR_rate);
        updateButton=root.findViewById(R.id.HR_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rr1=root.findViewById(r1.getCheckedRadioButtonId());
                RadioButton rr2=root.findViewById(r2.getCheckedRadioButtonId());
                RadioButton rr3=root.findViewById(r3.getCheckedRadioButtonId());
                RadioButton rr4=root.findViewById(r4.getCheckedRadioButtonId());
                RadioButton rr5=root.findViewById(r5.getCheckedRadioButtonId());
                String rate1=Integer.toString((int)rb.getRating());
                if(rr1==null ||rr2==null ||rr3==null ||rr4==null ||rr5==null ||rate1.equals("0")){
                    Toast.makeText(getActivity(), "All questions must be answered", Toast.LENGTH_SHORT).show();
                }else{
                    String polite=rr1.getText().toString();
                    String bias=rr2.getText().toString();
                    String competent=rr3.getText().toString();
                    String pragmatic=rr4.getText().toString();
                    String notify=rr5.getText().toString();
                    Map m=new HashMap();
                    m.put("polite",polite);
                    m.put("bias",bias);
                    m.put("pragmatic",pragmatic);
                    m.put("competent",competent);
                    m.put("notify",notify);
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
                    rateRef.child(uid).updateChildren(m).addOnCompleteListener(new OnCompleteListener() {
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
        rateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)){
                    String polite=dataSnapshot.child(uid).child("polite").getValue().toString();
                    String bias=dataSnapshot.child(uid).child("bias").getValue().toString();
                    String competant=dataSnapshot.child(uid).child("competent").getValue().toString();
                    String pragmatic=dataSnapshot.child(uid).child("pragmatic").getValue().toString();
                    String notify=dataSnapshot.child(uid).child("notify").getValue().toString();
                    rate=dataSnapshot.child(uid).child("rate").getValue().toString();
                    if(polite.equals("yes")) r1.check(R.id.HR_yes_1); else r1.check(R.id.HR_no_1);
                    if(bias.equals("yes")) r2.check(R.id.HR_yes_2); else r2.check(R.id.HR_no_2);
                    if(competant.equals("yes")) r3.check(R.id.HR_yes_3); else r3.check(R.id.HR_no_3);
                    if(pragmatic.equals("yes")) r4.check(R.id.HR_yes_4); else r4.check(R.id.HR_no_4);
                    if(notify.equals("yes")) r5.check(R.id.HR_yes_5); else r5.check(R.id.HR_no_5);
                    rb.setRating(Float.parseFloat(rate));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                progressDialog.hide();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


}
