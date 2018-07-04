package com.smarttersstudio.feedbackzone.Fragmets;


import android.app.ProgressDialog;
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
import android.widget.SeekBar;
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


public class FoodBottom extends Fragment {


    private View root;
    private Button submitButton;
    private EditText feedBackText;
    private FirebaseAuth mAuth;
    private DatabaseReference feedbackRef,ratingRef,totalRef,rateRef;
    private ProgressDialog progressDialog;
    private EditText ans1,ans2;
    private SeekBar s1,s2;
    private RadioGroup rg1;
    private RatingBar rb;
    private String rate="0";
    private Float one,two,three,four,five;
    private Button updateButton;
    private FloatingActionButton fab;
    private ScrollView sv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root=inflater.inflate(R.layout.fragment_food_bottom, container, false);
        submitButton=root.findViewById(R.id.food_submit);
        feedBackText=root.findViewById(R.id.food_feedback);
        ans1=root.findViewById(R.id.food_ans1);
        ans2=root.findViewById(R.id.foos_ans2);
        s1=root.findViewById(R.id.food_ans3);
        s2=root.findViewById(R.id.food_ans4);
        rg1=root.findViewById(R.id.food_radio);
        rb=root.findViewById(R.id.food_rate);
        //Fab Hide Test
        fab=getActivity().findViewById(R.id.fab_profile);
        sv=root.findViewById(R.id.food_scroll);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sv.setOnScrollChangeListener(new ScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        fab.hide();
                    } else {
                        fab.show();
                    }
                }
            });
        }
        //Fab end
        mAuth=FirebaseAuth.getInstance();
        feedbackRef= FirebaseDatabase.getInstance().getReference().child("feedback").child("food");
        rateRef= FirebaseDatabase.getInstance().getReference().child("rating").child("food");
        totalRef=FirebaseDatabase.getInstance().getReference().child("total").child("food");
        updateButton=root.findViewById(R.id.update_food);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String like=ans1.getText().toString();
                String dislike=ans2.getText().toString();
                String speed=Integer.toString(s1.getProgress());
                RadioButton r=(RadioButton)root.findViewById(rg1.getCheckedRadioButtonId());
                String sufficient=r.getText().toString();
                String clean=Integer.toString(s2.getProgress());
                String rate1=Integer.toString((int)rb.getRating());
                if(TextUtils.isEmpty(like) ||TextUtils.isEmpty(dislike) || TextUtils.isEmpty(speed)||TextUtils.isEmpty(clean)||TextUtils.isEmpty(sufficient)||TextUtils.isEmpty(rate1) ){
                    Toast.makeText(getActivity(), "Can't rate with empty fields", Toast.LENGTH_SHORT).show();
                }else{
                    Map m=new HashMap<>();
                    m.put("like",like);
                    m.put("dislike",dislike);
                    m.put("speed",speed);
                    m.put("sufficient",sufficient);
                    m.put("clean",clean);
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
        final String uid=mAuth.getCurrentUser().getUid();
        ratingRef=FirebaseDatabase.getInstance().getReference().child("rating").child("food");
        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)){
                    String like=dataSnapshot.child(uid).child("like").getValue().toString();
                    String dislike=dataSnapshot.child(uid).child("dislike").getValue().toString();
                    String speed=dataSnapshot.child(uid).child("speed").getValue().toString();
                    String sufficient=dataSnapshot.child(uid).child("sufficient").getValue().toString();
                    String clean=dataSnapshot.child(uid).child("clean").getValue().toString();
                    rate=dataSnapshot.child(uid).child("rate").getValue().toString();
                    ans1.setText(like);
                    ans2.setText(dislike);
                    s1.setProgress(Integer.parseInt(speed));
                    s2.setProgress(Integer.parseInt(clean));
                    rb.setRating(Float.parseFloat(rate));
                    if(sufficient.equals("YES")){
                        rg1.check(R.id.food_yes);
                    }else {
                        rg1.check(R.id.food_no);
                    }
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
        Map<String, Object> m=new HashMap<>();
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

}
