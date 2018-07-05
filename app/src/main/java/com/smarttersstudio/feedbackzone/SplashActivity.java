package com.smarttersstudio.feedbackzone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
