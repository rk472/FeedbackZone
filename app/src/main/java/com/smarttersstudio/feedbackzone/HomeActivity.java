package com.smarttersstudio.feedbackzone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarttersstudio.feedbackzone.Adapters.SwipeAdapter;
import com.smarttersstudio.feedbackzone.Fragmets.AccountButtom;
import com.smarttersstudio.feedbackzone.Fragmets.AdministrationBottom;
import com.smarttersstudio.feedbackzone.Fragmets.FoodBottom;
import com.smarttersstudio.feedbackzone.Fragmets.HRBUttom;
import com.smarttersstudio.feedbackzone.Fragmets.ManagerBottom;
import com.smarttersstudio.feedbackzone.Fragmets.OtherButtom;
import com.smarttersstudio.feedbackzone.Fragmets.SecurityBottom;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class HomeActivity extends AppCompatActivity {
    private  ViewPager viewPager;
    private FragmentTransaction fragmentTransaction;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private TextView gotoReview;
    private String level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid();
        gotoReview=findViewById(R.id.show_review_button);
        gotoReview.setVisibility(View.GONE);
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                level=dataSnapshot.child("level").getValue().toString();
                gotoReview.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        final SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        DotsIndicator dotsIndicator =  findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Fragment f = null;
                String tag = null;
                switch(position){
                    case 0:
                        f = new FoodBottom();
                        tag="food";
                        break;
                    case 1:
                        f = new AdministrationBottom();
                        tag="admin";
                        break;
                    case 5:
                        f = new ManagerBottom();
                        tag="manager";
                        break;
                    case 4:
                        f = new SecurityBottom();
                        tag="security";
                        break;
                    case 6:
                        f=new OtherButtom();
                        tag="other";
                        break;
                    case 2:
                        f=new AccountButtom();
                        tag="accounts";
                        break;
                    case 3:
                        f=new HRBUttom();
                        tag="HR";
                        break;
                }
                fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container,f,tag);
                fragmentTransaction.commit();

            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        userRef.keepSynced(true);
    }


    public void prev(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
    }

    public void next(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
    }
    public void openProfile(View v){
        Intent i=new Intent(this,ProfileActivity.class);
        i.putExtra("uid",mAuth.getCurrentUser().getUid());
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser()==null){
                    Intent i=new Intent(HomeActivity.this,LoginActivity.class);
                    i.putExtra("name","feedback");
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    public void showAllReview(View view) {
        String tag=getSupportFragmentManager().findFragmentById(R.id.main_container).getTag();
        if(tag.equals("manager")){
            if(level.equals("1"))
                Toast.makeText(this, "You are not a senior yet to get reviews...", Toast.LENGTH_SHORT).show();
            else{
                Intent i = new Intent(HomeActivity.this, FeedbackListActivity.class);
                i.putExtra("dept", tag+"/"+mAuth.getCurrentUser().getUid());
                startActivity(i);
            }
        }else{
            Intent i = new Intent(HomeActivity.this, FeedbackListActivity.class);
            i.putExtra("dept", tag);
            startActivity(i);
        }
    }
}
