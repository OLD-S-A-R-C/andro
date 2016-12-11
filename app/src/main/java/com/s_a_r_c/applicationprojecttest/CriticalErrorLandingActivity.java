package com.s_a_r_c.applicationprojecttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class CriticalErrorLandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critical_error_landing);
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
