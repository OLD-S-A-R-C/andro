package com.s_a_r_c.applicationprojecttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Refresh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);


    }

    public void refresh()
    {
        finish();
    }
}