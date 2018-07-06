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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smarttersstudio.feedbackzone.POJO.Users;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateManagerActivity extends AppCompatActivity {
    private RecyclerView list;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Users,JuniorViewHolder> f;
    String name,phone,level,dept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_manager);
        mAuth=FirebaseAuth.getInstance();
        level=getIntent().getExtras().getString("level");
        userRef= FirebaseDatabase.getInstance().getReference().child("users");
        list=findViewById(R.id.update_manager_select);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        final String newLevel=Integer.toString(Integer.parseInt(level)+1);
        FirebaseRecyclerOptions<Users> options=new FirebaseRecyclerOptions.Builder<Users>().setQuery(userRef,Users.class).build();
        f=new FirebaseRecyclerAdapter<Users,JuniorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull JuniorViewHolder holder, final int position, @NonNull Users model) {
                if(model.getLevel().equals(newLevel)) {
                    holder.setAll(model.getName(), model.getImage());
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

    }

    public void update_skip(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Give Feedback")
                .setMessage("You can add manager to your profile to give feedback")
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
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
        userRef.child(mAuth.getCurrentUser().getUid()).child("manager").setValue(manager).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateManagerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                list.setEnabled(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateManagerActivity.this, "Manager added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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
            Picasso.with(UpdateManagerActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.vect_profile)
                    .into(dp, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(UpdateManagerActivity.this).load(image).placeholder(R.drawable.vect_profile).into(dp);
                        }
                    });
        }
    }

}
