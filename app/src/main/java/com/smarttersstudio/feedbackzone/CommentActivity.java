package com.smarttersstudio.feedbackzone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.feedbackzone.POJO.Posts;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    private EditText commentText;
    private Button commentButton;
    private FirebaseAuth mAuth;
    private TextView postName,postText,postDate;
    private String uid,post_id,post_name,post_text,post_date,name;
    private int flag=0;
    private RecyclerView list;
    private DatabaseReference userRef,commentRef;
    private FirebaseRecyclerAdapter<Posts,CommentViewHolder> f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentButton=findViewById(R.id.comment_button);
        commentText=findViewById(R.id.comment_edit);
        list=findViewById(R.id.comment_list);
        postDate=findViewById(R.id.comment_post_date);
        postName=findViewById(R.id.comment_post_name);
        postText=findViewById(R.id.comment_post_text);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        post_id=getIntent().getExtras().getString("post_id");
        post_name=getIntent().getExtras().getString("post_name");
        post_date=getIntent().getExtras().getString("post_date");
        post_text=getIntent().getExtras().getString("post_text");
        postDate.setText(post_date);
        postText.setText(post_text);
        postName.setText(post_name);
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        commentRef=FirebaseDatabase.getInstance().getReference().child("comments").child(post_id);
        commentButton.setEnabled(false);
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s) && flag==1)commentButton.setEnabled(false);
                else commentButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=dataSnapshot.child("name").getValue().toString();
                flag=1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<Posts> options=new FirebaseRecyclerOptions.Builder<Posts>().setQuery(commentRef.orderByChild("time"),Posts.class).build();
        f=new FirebaseRecyclerAdapter<Posts, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull final Posts model) {
                holder.setAll(model.getName(),model.getText(),model.getDate());
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(CommentActivity.this,ProfileActivity.class);
                        i.putExtra("uid",model.getUid());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.comment_row,parent,false);
                return new CommentViewHolder(v);
            }
        };
        list.setAdapter(f);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        userRef.keepSynced(true);
        commentRef.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
    }

    public void comment(final View view) {
        view.setEnabled(false);
        String comment=commentText.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String d=formatter.format(date);
        Map m=new HashMap();
        m.put("name",name);
        m.put("uid",uid);
        m.put("text",comment);
        m.put("date",d);
        m.put("time",-1 * new Date().getTime());
        commentRef.push().updateChildren(m).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(CommentActivity.this, "comment successful", Toast.LENGTH_SHORT).show();
                commentText.setText("");
                view.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CommentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                view.setEnabled(true);
            }
        });

    }
    public class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView nameText,postText,dateText;
        View v;
        public CommentViewHolder(View itemView) {
            super(itemView);
            v=itemView;
            nameText=v.findViewById(R.id.comment_name);
            postText=v.findViewById(R.id.comment_text);
            dateText=v.findViewById(R.id.comment_date);
        }
        public void setAll(String name,String post,String date){
            nameText.setText(name);
            postText.setText(post);
            dateText.setText(date);
        }
    }
}
