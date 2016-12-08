package com.s_a_r_c.applicationprojecttest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.s_a_r_c.applicationprojecttest.dummy.SongContent;

import java.util.List;

/**
 * A fragment representing a single playList detail screen.
 * This fragment is either contained in a {@link playListListActivity}
 * in two-pane mode (on tablets) or a {@link playListDetailActivity}
 * on handsets.
 */
public class playListDetailFragment extends Fragment {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    String strtext = "";
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private SongContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public playListDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playlist_detail, container, false);

        strtext = getArguments().getString("userData");
        Log.e("strArgument123",strtext+"");
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = SongContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
                Log.e("SongSelected",mItem.content);

                Intent intent = new Intent(getContext(), songContent.class);
                Log.e("strTextSent","//////////////"+strtext);
                intent.putExtra(EXTRA_MESSAGE, mItem.content+";"+mItem.details+";"+mItem.id+";"+strtext);
                startActivity(intent);

            }
       }

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
          //  ((TextView) rootView.findViewById(R.id.playlist_detail)).setText(mItem.details);
        }

        return rootView;
    }


}
