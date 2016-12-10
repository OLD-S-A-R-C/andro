package com.s_a_r_c.applicationprojecttest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;
import com.s_a_r_c.applicationprojecttest.dummy.SongContent;
import com.s_a_r_c.applicationprojecttest.dummy.SongContent;

import org.json.JSONException;
import org.json.JSONObject;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        String strMessage = intent.getStringExtra(playListListActivity.EXTRA_MESSAGE);

        strResultIntent = strMessage;
        Log.e("strSent","ExtraMessage"+strMessage+" ExtraMessagePlaylist"+strPlaylistID);
        if(!strMessage.equals("null")) {
            try {
                JSONObject lireJSON = new JSONObject(strMessage);
                strMotDePasse = lireJSON.get("motdepasse").toString();
                strCourriel = lireJSON.get("courriel").toString();
                strId = lireJSON.get("Id").toString();
                strAvatar = lireJSON.get("avatar").toString();
                strAlias = lireJSON.get("alias").toString();
                strPlaylistID = lireJSON.get("action").toString();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("labo7", e.toString());
            }
        }
        /////////////////////////////////////////
           SongContent songContent = new SongContent();
           songContent.refreshList(strPlaylistID);



        ///////////////////////////////////////
        Log.e("CONNECTED AS",strAlias+" USER WITH SONG "+strId);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(strAlias.equals(""))
        {
            fab.setVisibility(View.GONE);
        }
        else
        {
            fab.setVisibility(View.VISIBLE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), modifyPlaylist.class);
                String message = strResultIntent;
                intent.putExtra(EXTRA_MESSAGE, strResultIntent);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own detail action (songDetailActivity)", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            //Bundle arguments = new Bundle();
            /*
            arguments.putString(playListDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(playListDetailFragment.ARG_ITEM_ID));*/
            Bundle bundle = new Bundle();
            bundle.putString("userData", strCourriel+";"+strMotDePasse+";"+strId);
            playListDetailFragment fragment = new playListDetailFragment();



            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.playlist_detail_container, fragment).commit();
        }
        View recyclerView = findViewById(R.id.playlist_list1);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.playlist_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            Log.e("MTWOPAINE","Selectec");
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, playListListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //////////////////////////////////////////////////////////////////////////////////
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        SongContent songContent = new SongContent();
        for(DummyContent.DummyItem item1 : DummyContent.ITEMS) {

           // Log.e("EQUALSDUMMYCONTENT",item1.details+" size:"+DummyContent.ITEMS.size()+" "+item1.strListeDeLecture);
            if(item1.details.equals(DummyContent.getStrPlaylistSelected())) {
        //        Log.e("strListeDeLecture", "" + item1.strListeDeLecture);
          //      Log.e("PURGELIST////","///////////"+item1.strListeDeLecture);
                songContent.purgeSongs(item1.strListeDeLecture);

            }
        }
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(SongContent.ITEMS));
      //  Log.e("RecyclerView","////////////////////////////"+SongContent.ITEMS.size());
        songContent.rebuildItems();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<SongContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<SongContent.DummyItem> items) {
            mValues = items;
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

                        Bundle arguments = new Bundle();
                        arguments.putString(playListDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        playListDetailFragment fragment = new playListDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.playlist_detail_container, fragment)
                                .commit();

                    } else {
                        /*
                        Context context = v.getContext();
                        Intent intent = new Intent(context, playListDetailActivity.class);
                        intent.putExtra(playListDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);*/
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
            public SongContent.DummyItem mItem;

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
    //////////////////////////////////////////////////////////////////////////////////
}
