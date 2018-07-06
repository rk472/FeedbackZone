package com.smarttersstudio.feedbackzone;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class JuniorListActivity extends AppCompatActivity {
    private RecyclerView list;
    private String uid;
    private DatabaseReference juniorRef;
    private FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<Users,JuniorViewHolder> f;
    private LinearLayout no,load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junior_list);
        list=findViewById(R.id.junior_list);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        no=findViewById(R.id.no_junior);
        load=findViewById(R.id.junior_load);
        juniorRef= FirebaseDatabase.getInstance().getReference().child("users");
        juniorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0){
                    no.setVisibility(View.VISIBLE);
                }    else{
                    no.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<Users> options=new FirebaseRecyclerOptions.Builder<Users>().setQuery(juniorRef,Users.class).build();
        f=new FirebaseRecyclerAdapter<Users, JuniorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull JuniorViewHolder holder, int position, @NonNull Users model) {
                final String id=getRef(position).getKey();
                if(model.getManager().equals(uid)){
                    holder.setAll(model.getName(),model.getImage());
                    load.setVisibility(View.GONE);
                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(JuniorListActivity.this,FeedbackListActivity.class);
                            i.putExtra("dept","manager/"+id);
                            startActivity(i);
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
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        list.setHasFixedSize(true);
        juniorRef.keepSynced(true);
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
            Picasso.with(JuniorListActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.vect_profile)
                    .into(dp, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            Picasso.with(JuniorListActivity.this).load(image).placeholder(R.drawable.vect_profile).into(dp);
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
    }
}
