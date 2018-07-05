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

import com.google.firebase.auth.FirebaseAuth;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        final SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        DotsIndicator dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            finishAffinity();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void prev(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
    }

    public void next(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
    }
    public void openProfile(View v){
        startActivity(new Intent(this,ProfileActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser()==null){
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                    finish();
                }
                //startActivity(new Intent(HomeActivity.this,ForumActivity.class));
            }
        });

    }

    public void showAllReview(View view) {
        String tag=getSupportFragmentManager().findFragmentById(R.id.main_container).getTag();
        Intent i=new Intent(HomeActivity.this,FeedbackListActivity.class);
        i.putExtra("dept",tag);
        startActivity(i);
    }
}
