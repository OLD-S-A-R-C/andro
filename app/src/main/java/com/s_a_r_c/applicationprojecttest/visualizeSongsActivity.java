package com.s_a_r_c.applicationprojecttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;
import com.s_a_r_c.applicationprojecttest.dummy.SongContent;

import java.util.ArrayList;

public class visualizeSongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_songs);
        setTitle("Song List");

        final ListView mListView = (ListView) findViewById(R.id.listView);

        ArrayList<String> playlists = new ArrayList<String>();
        for(SongContent.DummyItem item1 : SongContent.SAVEDITEMS) {
            playlists.add(item1.content);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(visualizeSongsActivity.this, android.R.layout.simple_list_item_1, playlists);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Object listItem = mListView.getItemAtPosition(position);
                Log.e("Item Selected","YEPEYEP"+mListView.getItemAtPosition(position).toString());
               // mListView.getItemAtPosition(position).toString();
            }
        });
     }
}
