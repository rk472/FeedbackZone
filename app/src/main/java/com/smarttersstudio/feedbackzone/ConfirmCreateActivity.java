package com.smarttersstudio.feedbackzone;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.feedbackzone.POJO.Users;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class ConfirmCreateActivity extends AppCompatActivity {
    private RecyclerView list;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Users,JuniorViewHolder> f;
    private String name,mail,phone,pass,level,post,dept;
    private LinearLayout load,no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_create);
        mAuth=FirebaseAuth.getInstance();
        name=getIntent().getExtras().getString("name");
        mail=getIntent().getExtras().getString("mail");
        phone=getIntent().getExtras().getString("phone");
        pass=getIntent().getExtras().getString("pass");
        level=getIntent().getExtras().getString("level");
        post=getIntent().getExtras().getString("designation");
        dept=getIntent().getExtras().getString("department");
        no=findViewById(R.id.no_manager);
        load=findViewById(R.id.manager_load);
        userRef= FirebaseDatabase.getInstance().getReference().child("users");
        list=findViewById(R.id.manager_select);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        final String newLevel=Integer.toString(Integer.parseInt(level)+1);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0){
                    no.setVisibility(View.VISIBLE);
                }else{
                    no.setVisibility(GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<Users> options=new FirebaseRecyclerOptions.Builder<Users>().setQuery(userRef,Users.class).build();
        f=new FirebaseRecyclerAdapter<Users,JuniorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull JuniorViewHolder holder, final int position, @NonNull Users model) {
                if(model.getLevel().equals(newLevel)) {
                    holder.setAll(model.getName(), model.getImage());
                    load.setVisibility(GONE);
                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            create(getRef(position).getKey());
                        }
                    });
                }else{
                    holder.v.setLayoutParams(new ViewGroup.LayoutParams(0,0));
                }
            }

            @NonNull
            @Override
            public JuniorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.junior_row,parent,false);
                return new JuniorViewHolder(v);
            }
        };
        list.setAdapter(f);
        userRef.keepSynced(true);


    }

    public void skip(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Give Feedback")
                .setMessage("You can add manager to your profile to give feedback")
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                create("no");
                            }
                        }).setNegativeButton("add now",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
    }

    void create(final String manager){
        list.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(mail,pass).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfirmCreateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Map m=new HashMap();
                m.put("name",name);
                m.put("phone",phone);
                m.put("mail",mail);
                m.put("level",level);
                m.put("department",dept);
                m.put("designation",post);
                m.put("image","no");
                m.put("manager",manager);
                String uid=authResult.getUser().getUid();
                userRef.child(uid).updateChildren(m).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(ConfirmCreateActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConfirmCreateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        list.setEnabled(true);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();

    }

    public class JuniorViewHolder extends RecyclerView.ViewHolder{
        CircleImageView dp;
        View v;
        TextView nameText;
        public JuniorViewHolder(View itemView) {
            super(itemView);
            v=itemView;
            dp=v.findViewById(R.id.junior_dp);
            nameText=v.findViewById(R.id.junior_name);
        }
        public void setAll(String name, final String image){
            nameText.setText(name);
            Picasso.with(ConfirmCreateActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.vect_profile)
                    .into(dp, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(ConfirmCreateActivity.this).load(image).placeholder(R.drawable.vect_profile).into(dp);
                        }
                    });
        }
    }

}
