package com.s_a_r_c.applicationprojecttest;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;

import java.util.ArrayList;

public class modifySongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_song);



        ArrayList<String> playlists = new ArrayList<String>();
        for(DummyContent.DummyItem item1 : DummyContent.ITEMS) {
                playlists.add(item1.content);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playlists);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);

        UserDatabase userDb = new UserDatabase(getApplicationContext());
        //userDb.retournerInfosUser();
        DummyContent dummyContent = new DummyContent();
       Log.e("/////////////////DBTEST","//////////////"+dummyContent.getAlias());
    }

}
