package com.smarttersstudio.feedbackzone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.feedbackzone.POJO.Posts;
import com.smarttersstudio.feedbackzone.POJO.Users;

import static android.view.View.GONE;

public class ForumActivity extends AppCompatActivity {
    private RecyclerView list;
    private FirebaseAuth mAuth;
    private DatabaseReference postRef;
    private FirebaseRecyclerAdapter<Posts,PostViewHolder> f;
    private LinearLayout load,no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        list=findViewById(R.id.post_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        postRef= FirebaseDatabase.getInstance().getReference().child("post");
        mAuth=FirebaseAuth.getInstance();
        no=findViewById(R.id.no_post);
        load=findViewById(R.id.post_load);
        postRef.addValueEventListener(new ValueEventListener() {
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
        loadMore();
    }
    public void loadMore(){
        Query q=postRef.orderByChild("time");
        FirebaseRecyclerOptions<Posts> options=new FirebaseRecyclerOptions.Builder<Posts>().setQuery(q,Posts.class).build();
        f=new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, final int position, @NonNull final Posts model) {
                holder.setAll(model.getName(),model.getText(),model.getDate());
                load.setVisibility(GONE);
                holder.gotoComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String post_id=getRef(position).getKey();
                        Intent i=new Intent(ForumActivity.this,CommentActivity.class);
                        i.putExtra("post_id",post_id);
                        i.putExtra("post_name",model.getName());
                        i.putExtra("post_text",model.getText());
                        i.putExtra("post_date",model.getDate());
                        startActivity(i);
                    }
                });
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(ForumActivity.this,ProfileActivity.class);
                        i.putExtra("uid",model.getUid());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.post_row,parent,false);
                return new PostViewHolder(v);
            }
        };
        list.setAdapter(f);
        postRef.keepSynced(true);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser()==null){
                    Intent i=new Intent(ForumActivity.this,LoginActivity.class);
                    i.putExtra("name","forum");
                    startActivity(i);
                    finish();
                }
            }
        });
        f.startListening();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Intent i = new Intent(ForumActivity.this,ProfileActivity.class);
            i.putExtra("uid",mAuth.getCurrentUser().getUid().toString());
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
    public void goToAddPost(View view) {
        startActivity(new Intent(ForumActivity.this,AddPostActivity.class));
    }
    public  class PostViewHolder extends RecyclerView.ViewHolder{
        View v;
        TextView nameText,postText,dateText;
        Button gotoComments;
        public PostViewHolder(View itemView) {
            super(itemView);
            v=itemView;
            nameText=v.findViewById(R.id.post_name);
            postText=v.findViewById(R.id.post_text);
            dateText=v.findViewById(R.id.post_date);
            gotoComments=v.findViewById(R.id.go_to_comments_button);
        }
        public void setAll(String name,String post,String date){
            nameText.setText(name);
            postText.setText(post);
            dateText.setText(date);
        }
    }
}
