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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.smarttersstudio.feedbackzone.POJO.Posts;
import com.smarttersstudio.feedbackzone.POJO.Users;

public class ForumActivity extends AppCompatActivity {
    private RecyclerView list;
    private FirebaseAuth mAuth;
    private DatabaseReference postRef;
    private int limit=10,flag=0;
    FirebaseRecyclerAdapter<Posts,PostViewHolder> f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        list=findViewById(R.id.post_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        postRef= FirebaseDatabase.getInstance().getReference().child("post");
        mAuth=FirebaseAuth.getInstance();
        loadMore(limit);
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                if(reachedBottom && flag==0){
                    limit+=10;
                    loadMore(limit);
                    flag=1;
                }
            }
        });
    }
    public void loadMore(int limit){
        Query q=postRef.orderByChild("time").limitToFirst(limit);
        FirebaseRecyclerOptions<Posts> options=new FirebaseRecyclerOptions.Builder<Posts>().setQuery(q,Posts.class).build();
        f=new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model) {
                holder.setAll(model.getName(),model.getText(),model.getDate());
                flag=0;
                holder.gotoComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
    }
    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
    }

    public void goToAddPost(View view) {
        startActivity(new Intent(ForumActivity.this,AddPostActivity.class));
    }
    public  class PostViewHolder extends RecyclerView.ViewHolder{
        View v;
        TextView nameText,postText,dateText,gotoComments;
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
