package com.smarttersstudio.feedbackzone;

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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.feedbackzone.POJO.FeedBack;

public class ManagerActivity extends AppCompatActivity {
    private RecyclerView list;
    private LinearLayout lin;
    private FirebaseAuth mAuth;
    private DatabaseReference feedbackRef;
    private FirebaseRecyclerAdapter<FeedBack,FeedBackViewHoldewr> f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        list=findViewById(R.id.manager_list);
        lin=findViewById(R.id.manager_list_error);
        mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid();
        feedbackRef= FirebaseDatabase.getInstance().getReference().child("feedback").child("manager").child(uid);
        FirebaseRecyclerOptions<FeedBack> options=new FirebaseRecyclerOptions.Builder<FeedBack>().setQuery(feedbackRef,FeedBack.class).build();
        f= new FirebaseRecyclerAdapter<FeedBack, FeedBackViewHoldewr>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FeedBackViewHoldewr holder, int position, @NonNull FeedBack model) {
                lin.setVisibility(View.GONE);
                holder.setAll("",model.getDate(),model.getFeedback());
                holder.nameText.setVisibility(View.GONE);
            }
            @NonNull
            @Override
            public FeedBackViewHoldewr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(getApplicationContext()).inflate(R.layout.feedback_row,parent,false);
                return new FeedBackViewHoldewr(v);
            }
        };
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        list.setAdapter(f);

    }
    public static class FeedBackViewHoldewr extends RecyclerView.ViewHolder{
        View v;
        TextView nameText,feedBackText,dateText;
        public FeedBackViewHoldewr(View itemView) {
            super(itemView);
            v=itemView;
            nameText=v.findViewById(R.id.feedback_name);
            feedBackText=v.findViewById(R.id.feedback_text);
            dateText=v.findViewById(R.id.feedback_date);
        }
        void setAll(String name,String date,String text){
            nameText.setText(name);
            feedBackText.setText(text);
            dateText.setText(date);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        f.startListening();
    }

}
