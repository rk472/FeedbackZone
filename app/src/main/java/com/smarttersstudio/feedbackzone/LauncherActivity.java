package com.smarttersstudio.feedbackzone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        startActivity(new Intent(LauncherActivity.this,SplashActivity.class));
        finish();
    }
}
