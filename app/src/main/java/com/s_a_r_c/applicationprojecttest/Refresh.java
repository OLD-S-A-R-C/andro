package com.s_a_r_c.applicationprojecttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;

public class Refresh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        //RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
        //rb.setRating(5);
    }

    public void refresh()
    {
        finish();
    }
}
