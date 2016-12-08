package com.s_a_r_c.applicationprojecttest;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class songContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_content);
        Intent intent = getIntent();
        String strMessage = intent.getStringExtra(playListListActivity.EXTRA_MESSAGE);

        String[] strSplit = strMessage.split(";");
        setTitle(strSplit[0].toString()+" "+strSplit[3]);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSong);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Context context = view.getContext();
                Intent intent = new Intent(context, modifySongActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
