package com.s_a_r_c.applicationprojecttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;
import com.s_a_r_c.applicationprojecttest.dummy.FinalContent;
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
        for(FinalContent.SongItem songItem : FinalContent.SONGITEMSPRIMAL) {
            playlists.add(songItem.strTitre+" ;"+songItem.strId);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(visualizeSongsActivity.this, android.R.layout.simple_list_item_1, playlists);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Item Selected","YEPEYEP"+mListView.getItemAtPosition(position).toString());

                String[] strSplit = mListView.getItemAtPosition(position).toString().split(";");
                DummyContent.setStrPlaylistSelected("");
                DummyContent.setStrSongSelected(strSplit[1]);
                if(!DummyContent.getId().equals("")) {
                    Intent intent = new Intent(getBaseContext(), modifySongActivity.class);
                    startActivity(intent);
                }
            }
        });
     }
}
