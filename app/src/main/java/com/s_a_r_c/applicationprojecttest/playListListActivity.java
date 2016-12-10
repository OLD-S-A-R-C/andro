package com.s_a_r_c.applicationprojecttest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_list);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.playlist_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.playlist_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        DummyContent dummyContent = new DummyContent();
       // dummyContent.refresh();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.e("DUMMYCONTENTITEMSSIZE","SIZE:"+DummyContent.ITEMS.size());

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
        Log.e("DUMMYCONTENTITEMSSIZE","SIZE:"+DummyContent.ITEMS.size());

    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
            Log.e("DUMMYCONTENTITEMSSIZE","SIZE:"+DummyContent.ITEMS.size());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.playlist_list_content, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        /*
                        Bundle arguments = new Bundle();
                        arguments.putString(playListDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        playListDetailFragment fragment = new playListDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.playlist_detail_container, fragment)
                                .commit();
*/
                    } else {
                        DummyContent dummyContent = new DummyContent();
                      //  dummyContent.refresh();

                        Log.e("MTWOPANEACTIVITY","/////////////////SELECTEDFALSE");
                        Context context = v.getContext();
                        Intent intent = new Intent(context, playListDetailActivity.class);
                       intent.putExtra(playListDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        Log.e("STRLICTEDELECTURE",holder.mItem.content+" "+holder.mItem.strListeDeLecture);
                        dummyContent.setStrPlaylistSelected(holder.mItem.details);
                        String message = "{\"action\":\""+holder.mItem.details+"\",\"success\":\"true\",\"Id\":\"" +strId+ "\",\"courriel\":\"" +strCourriel+ "\",\"motdepasse\":\"" +strMotDePasse+ "\",\"alias\":\"" +strAlias+ "\",\"avatar\":\"" +strAvatar+ "\"}";
                        intent.putExtra(EXTRA_MESSAGE, message);
                        context.startActivity(intent);

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
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

        DummyContent dummyContent = new DummyContent();
        dummyContent.refresh();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Log.e("nav_camera","Selected");
            Intent intent = new Intent(this, visualizeSongsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Log.e("nav_gallery","Selected");
            Intent intent = new Intent(this, createUserActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            String message = "Message";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivityForResult(intent,1);
            Log.e("nav_slideshow","Selected");
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, ModifyUserActivity.class);
            String message = strResultIntent;
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
            Log.e("nav_manage","Selected");
        } else if (id == R.id.nav_share) {
            Log.e("nav_share","Selected");
            Intent intent = new Intent(this, createNewPlaylist.class);
            String message = strResultIntent;
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
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
                    setTitle("Utilisateur: "+strAlias);
                    TextView textview = (TextView)findViewById(R.id.textView);
                    textview.setText(strCourriel);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("labo7",e.toString());
                }
            }
        }
        Log.e("StrSuccess",strResultIntent);
    }

}
