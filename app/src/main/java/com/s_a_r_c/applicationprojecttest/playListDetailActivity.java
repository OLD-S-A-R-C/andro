package com.s_a_r_c.applicationprojecttest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.s_a_r_c.applicationprojecttest.Helpers.UserDatabase;
import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;
import com.s_a_r_c.applicationprojecttest.dummy.FinalContent;
import com.s_a_r_c.applicationprojecttest.dummy.SongContent;
import com.s_a_r_c.applicationprojecttest.dummy.SongContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An activity representing a single playList detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link playListListActivity}.
 */
public class playListDetailActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE_PLAYLIST = "com.example.myfirstapp.MESSAGE";
    String jsonSaved = "";
    String strMotDePasse = "";
    String strCourriel = "";
    String strId = "";
    String strAvatar = "";
    String strAlias = "";
    String strResultIntent;
    String strPlaylistID = "";
    String strTicketID = "";
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
      //  setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPlaylist);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Context context = view.getContext();
                Intent intent = new Intent(context, modifyPlaylist.class);
                context.startActivity(intent);
            }
        });

        final ListView ListView = (ListView) findViewById(R.id.listViewSong);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.setAnchorId(R.id.fabPlaylist);
        fab.setLayoutParams(layoutParams);
        fab.setVisibility(View.GONE);


        for(FinalContent.PlaylistITEM playlistITEM : FinalContent.ITEMS) {
                    if(playlistITEM.owner.equals(DummyContent.getId())&&playlistITEM.id.equals(DummyContent.getStrPlaylistSelected()))
                    {
                        fab.setVisibility(View.VISIBLE);
                    }
        }



        ArrayList<String> playlists = new ArrayList<String>();
        final HashMap<Integer, FinalContent.SongItem> songsMaps = new HashMap<Integer, FinalContent.SongItem>();
        for(FinalContent.PlaylistITEM playlistITEM : FinalContent.ITEMS) {

            if(playlistITEM.id.equals(DummyContent.getStrPlaylistSelected()))
            {
                int count = 0;
                for(FinalContent.SongItem songItem : playlistITEM.SONGITEMS)
                {
                    playlists.add(songItem.strTitre+" ("+songItem.strId+")");
                    songsMaps.put(count, songItem);
                    count++;
                }
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_custom,   playlists) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View rowView = LayoutInflater.from(getApplicationContext()).
                        inflate(R.layout.listview_custom, parent, false);

                TextView text1 = (TextView) rowView.findViewById(R.id.tvTitre);
                ImageView icon = (ImageView) rowView.findViewById(R.id.listviewVignette);

                text1.setText(songsMaps.get(position).strArtiste + " - " + songsMaps.get(position).strTitre);

                if (UserDatabase.getInstance(getApplicationContext()).loggedIn())
                    Log.e("LISTVIEW COLORS" , songsMaps.get(position).strId + "~" + (UserDatabase.getInstance(getApplicationContext()).retournerInfosUser().get(UserDatabase.USER_ID)));
                if (UserDatabase.getInstance(getApplicationContext()).loggedIn() && songsMaps.get(position).strOwnerID.equals((UserDatabase.getInstance(getApplicationContext()).retournerInfosUser().get(UserDatabase.USER_ID)))) {
                    text1.setTextColor(Color.parseColor("#00ff00"));
                }

                if (songsMaps.get(position).strActive.equals("false")) {
                    text1.setText("Inactive" + " - Id : " + songsMaps.get(position).strId);
                } else {
                    byte[] decodedString = Base64.decode(songsMaps.get(position).strVignette, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    icon.setImageBitmap(decodedByte);
                    icon.requestLayout();
                }


                return rowView;
            }

            @Override
            public boolean isEnabled(int position){

                if(songsMaps.get(position).strActive.equals("false") && !(UserDatabase.getInstance(getApplicationContext()).loggedIn() && songsMaps.get(position).strOwnerID.equals((UserDatabase.getInstance(getApplicationContext()).retournerInfosUser().get(UserDatabase.USER_ID)))))
                    return false;
                else
                    return true;

            }
        };
        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                Intent intent = new Intent(getBaseContext(), songContent.class);

                String[] strSplit = ListView.getItemAtPosition(pos).toString().split("[(]");
                DummyContent.setStrSongSelected(strSplit[1].split("[)]")[0]);
                startActivityForResult(intent,1);
            }
        });


        /////////////////////////////////////////
        ///////////////////////////////////////
        Log.e("CONNECTED AS", DummyContent.getAlias() + " USER WITH SONG " + DummyContent.getId());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }
    //////////////////////////////////////////////////////////////////////////////////
}
