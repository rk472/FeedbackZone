package com.smarttersstudio.feedbackzone.Fragmets;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.feedbackzone.HomeActivity;
import com.smarttersstudio.feedbackzone.ProfileActivity;
import com.smarttersstudio.feedbackzone.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManagerTop extends Fragment {
    private View root;
    private AppCompatActivity main;
    private LinearLayout err;
    private CircleImageView dp;
    private TextView nameText;
    private DatabaseReference managerRef,userRef;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_manager_top, container, false);
        main=(AppCompatActivity)getActivity();
        mAuth=FirebaseAuth.getInstance();
        nameText=root.findViewById(R.id.manager_name);
        dp=root.findViewById(R.id.manager_dp);
        err=root.findViewById(R.id.manager_error);
        String uid=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               String m=dataSnapshot.child("manager").getValue().toString();
               if(!m.equals("no")){
                   err.setVisibility(View.GONE);
                   managerRef=FirebaseDatabase.getInstance().getReference().child("users").child(m);
                   managerRef.keepSynced(true);
                   managerRef.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                          String name=dataSnapshot.child("name").getValue().toString();
                          final String url=dataSnapshot.child("image").getValue().toString();
                          nameText.setText(name);
                           Picasso.with(getActivity()).load(url).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.vect_profile)
                                   .into(dp, new Callback() {
                                       @Override
                                       public void onSuccess() {
                                       }

                                       @Override
                                       public void onError() {
                                           Picasso.with(getActivity()).load(url).placeholder(R.drawable.vect_profile).into(dp);
                                       }
                                   });
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.keepSynced(true);
        return root;
    }


}
