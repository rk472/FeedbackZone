package com.smarttersstudio.feedbackzone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {
    private Button b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        b1 = findViewById(R.id.goFeedback);
        b2 = findViewById(R.id.goForum);
        Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        b1.startAnimation(animationFadeIn);
        b2.startAnimation(animationFadeIn);
    }

    public void goToFeedBack(View view) {
        Intent i=new Intent(this,LoginActivity.class);
        i.putExtra("name","feedback");
        startActivity(i);
    }

    public void gotoForum(View view) {
        Intent i=new Intent(this,LoginActivity.class);
        i.putExtra("name","forum");
        startActivity(i);
    }
}
