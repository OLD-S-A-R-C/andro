package com.s_a_r_c.applicationprojecttest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.s_a_r_c.applicationprojecttest.Helpers.UserDatabase;
import com.s_a_r_c.applicationprojecttest.dummy.AvatarContent;
import com.s_a_r_c.applicationprojecttest.Helpers.Avatars;
import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;
import com.s_a_r_c.applicationprojecttest.dummy.FinalContent;
import com.s_a_r_c.applicationprojecttest.dummy.SongContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An activity representing a list of PlayLists. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link playListDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */

public class playListListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE_PLAYLIST = "com.example.myfirstapp.MESSAGE";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    String strResultIntent = "";
    String strMotDePasse = "";
    String strCourriel = "";
    String strId = "";
    String strAvatar = "";
    String strAlias = "";
    private boolean mTwoPane;

    private Menu menuDrawer;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_list);

       FinalContent finalContent = new FinalContent();
        finalContent.onCreate();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabRefresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing data", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                FinalContent finalContent = new FinalContent();
                finalContent.onCreate();

            }
        });


        if (findViewById(R.id.playlist_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        DummyContent dummyContent = new DummyContent();
       // dummyContent.refresh();

        String ok = "";

    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
///////////////////////////////////////////////////////////////////////////
        final ListView ListView = (ListView) findViewById(R.id.listViewPlaylist);
        ArrayList<String> playlists = new ArrayList<String>();
        final HashMap<Integer, FinalContent.PlaylistITEM> playlistsMaps = new HashMap<Integer, FinalContent.PlaylistITEM>();
        int count = 0;
        for(FinalContent.PlaylistITEM playlistITEM : FinalContent.ITEMS) {
            playlists.add(playlistITEM.name+" ;"+playlistITEM.id);
            playlistsMaps.put(count, playlistITEM);
            count++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  playlists) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                TextView textView = (TextView) super.getView(position, convertView, parent);

                if (UserDatabase.getInstance(getApplicationContext()).loggedIn())
                    Log.e("LISTVIEW COLORS" , playlistsMaps.get(position).owner + "~" + (UserDatabase.getInstance(getApplicationContext()).retournerInfosUser().get(UserDatabase.USER_ID)));
                if (UserDatabase.getInstance(getApplicationContext()).loggedIn() && playlistsMaps.get(position).owner.equals((UserDatabase.getInstance(getApplicationContext()).retournerInfosUser().get(UserDatabase.USER_ID)))) {
                    textView.setTextColor(Color.parseColor("#00ff00"));
                }

                return textView;
            }

            @Override
            public boolean isEnabled(int position){

                if(playlistsMaps.get(position).active.equals("false") && !(UserDatabase.getInstance(getApplicationContext()).loggedIn() && playlistsMaps.get(position).owner.equals((UserDatabase.getInstance(getApplicationContext()).retournerInfosUser().get(UserDatabase.USER_ID)))))
                    return false;
                else
                    return true;

            }
        };
        ListView.setAdapter(adapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                String[] strSplit = ListView.getItemAtPosition(pos).toString().split(";");
                DummyContent.setStrPlaylistSelected(strSplit[1]);
                Intent intent = new Intent(getBaseContext(), playListDetailActivity.class);
                startActivityForResult(intent,1);
            }
        });
////////////////////////////////////////////////////////////////////////////
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if (UserDatabase.getInstance(getApplicationContext()).loggedIn()) {
            nav_Menu.findItem(R.id.submenu_actions).setVisible(true);
            nav_Menu.findItem(R.id.menu_edit_profile).setVisible(true);
            nav_Menu.findItem(R.id.menu_logout).setVisible(true);
            nav_Menu.findItem(R.id.menu_new_user).setVisible(false);
            nav_Menu.findItem(R.id.menu_login).setVisible(false);
        } else {
            nav_Menu.findItem(R.id.submenu_actions).setVisible(false);
            nav_Menu.findItem(R.id.menu_edit_profile).setVisible(false);
            nav_Menu.findItem(R.id.menu_logout).setVisible(false);
            nav_Menu.findItem(R.id.menu_new_user).setVisible(true);
            nav_Menu.findItem(R.id.menu_login).setVisible(true);
        }

        try {
            Log.d("AVATAR", "A LAIDE DE USERDATABASE");
            byte[] decodedString = Base64.decode((String) UserDatabase.getInstance(this).retournerInfosUser().get(UserDatabase.USER_AVATAR_B64), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView img = (ImageView) findViewById(R.id.ivDrawerAvatar);
            img.setImageBitmap(decodedByte);
            img.requestLayout();
            img.getLayoutParams().height = 200;
            img.getLayoutParams().width = 200;
            img.requestLayout();

            TextView tvEmail = (TextView) findViewById(R.id.tvDrawerEmail);
            tvEmail.setText((String) UserDatabase.getInstance(this).retournerInfosUser().get(UserDatabase.USER_EMAIL));
        }catch (Exception e) {
            Log.d("AVATAR CRASH", e.getMessage());
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {



        int id = item.getItemId();

        if (id == R.id.menu_import_song) {
            Log.e("nav_camera","Selected");
            Intent intent = new Intent(this, visualizeSongsActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.menu_your_song) {
            Log.e("nav_camera","Selected");
            DummyContent.setStrSoloMusic("true");
            Intent intent = new Intent(this, visualizeSongsActivity.class);
            startActivity(intent);

        }else if (id == R.id.menu_new_user) {
            Log.e("nav_gallery","Selected");
            Intent intent = new Intent(this, createUserActivity.class);
            startActivity(intent);

        } else if (id == R.id.menu_login) {


            Intent intent = new Intent(this, DisplayMessageActivity.class);
            String message = "Message";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivityForResult(intent,1);
            Log.e("nav_slideshow","Selected");



        } else if (id == R.id.menu_edit_profile) {
            Intent intent = new Intent(this, ModifyUserActivity.class);
            String message = strResultIntent;
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
            Log.e("nav_manage","Selected");
        } else if (id == R.id.menu_new_playlist) {
            Log.e("nav_share","Selected");
            Intent intent = new Intent(this, createNewPlaylist.class);
            String message = strResultIntent;
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        } else if (id == R.id.menu_new_song) {
            Log.e("nav_send","Selected");
            Intent intent = new Intent(this, addSongActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                strResultIntent = data.getStringExtra("jsonSavedTransfer");

                try {

                    JSONObject lireJSON     = new JSONObject(strResultIntent);
                    strMotDePasse = lireJSON.get("motdepasse").toString();
                    strCourriel = lireJSON.get("courriel").toString();
                    strId = lireJSON.get("Id").toString();
                    strAvatar = lireJSON.get("avatar").toString();
                    strAlias = lireJSON.get("alias").toString();
                    TextView textview = (TextView)findViewById(R.id.tvDrawerEmail);
                    textview.setText(strCourriel);
                    setDrawerAvatar(lireJSON.get("avatar").toString(), R.id.ivDrawerAvatar);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("labo7",e.toString());
                }
            }
        }
        Log.e("StrSuccess",strResultIntent);
    }

    private void setDrawerAvatar(String avatarId, int idComponent) {
        AvatarContent avatarFound = Avatars.getInstance().getListAvatarsById().get(Integer.valueOf(avatarId));
        if (avatarFound != null) {
            ImageView avatar = (ImageView) findViewById(R.id.ivDrawerAvatar);
            byte[] decodedString = Base64.decode(avatarFound.getAvatarB64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView img = (ImageView) findViewById(R.id.ivDrawerAvatar);
            img.setImageBitmap(decodedByte);
            img.requestLayout();
            img.getLayoutParams().height = 200;
            img.getLayoutParams().width = 200;
            img.requestLayout();
        } else {
            Log.v("ERREUR DRAWER AVAR", "WAIT WHAT");
        }

    }

}
