package com.smarttersstudio.feedbackzone;

import android.content.Intent;
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
import com.smarttersstudio.feedbackzone.Fragmets.AdministrationBottom;
import com.smarttersstudio.feedbackzone.Fragmets.FoodBottom;
import com.smarttersstudio.feedbackzone.Fragmets.FoodTop;
import com.smarttersstudio.feedbackzone.Fragmets.ManagerBottom;
import com.smarttersstudio.feedbackzone.Fragmets.SecurityBottom;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

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
                int c = 0;
                switch(position){
                    case 0:
                        f = new FoodBottom();
                        break;
                    case 1:
                        f = new AdministrationBottom();
                        break;
                    case 2:
                        f = new ManagerBottom();
                        break;
                    case 3:
                        f = new SecurityBottom();
                        break;
                }
                fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container,f);
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

    public void openProfile(View view) {

    }
}
