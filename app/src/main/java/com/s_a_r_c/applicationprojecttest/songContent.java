package com.s_a_r_c.applicationprojecttest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class songContent extends AppCompatActivity {
String jsonSaved = "";
    String strOwnerID = "";
    String strVignette = "";
    String strTitre = "";
    String strMusique = "";
    String strPublique = "";
    String strActive = "";
    String strArtiste = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_content);

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

    try
    { new DownloadSong(null).execute("Useless");}
        catch (Exception e)
        {
            finish();
        }


    }
    private class DownloadSong extends AsyncTask<String, Void, String> {
        String url;

        public DownloadSong(String url) {

            this.url = url;
        }

        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                DummyContent dummyContent = new DummyContent();
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/afficher/"+DummyContent.getStrSongSelected());
                c = (HttpURLConnection) u.openConnection();
                c.connect();
                int intStatusRetrieved = c.getResponseCode();
                String strString;
                switch (intStatusRetrieved) {
                    case 200:
                        InputStreamReader inputStreamReader =new InputStreamReader(c.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((strString = bufferedReader.readLine()) != null){stringBuilder.append(strString+"\n");}
                        bufferedReader.close();
                        return stringBuilder.toString();
                    case 400:
                        Log.e("JsonRetrieveError","Status 400");
                        return null;
                }}
            catch (Exception ex) {return ex.toString();} finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {Log.e("JsonRetrieveError","Error fielded");}
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            // bmImage.setImageBitmap(result);
            jsonSaved = result;
            renderJson();
        }
    }
    public void renderJson()
    {
        try {

            JSONObject lireJSON     = new JSONObject(jsonSaved);
            String strProprietaire = lireJSON.get("proprietaire").toString();
            JSONObject lireJSON2     = new JSONObject(strProprietaire);
            strOwnerID = lireJSON2.get("id").toString();
             strVignette =lireJSON.get("vignette").toString();
             strTitre =lireJSON.get("titre").toString();
             strMusique =lireJSON.get("musique").toString();
             strPublique =lireJSON.get("publique").toString();
             strActive =lireJSON.get("active").toString();
            strArtiste =lireJSON.get("artiste").toString();

            setTitle(strTitre + " - "+strArtiste);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
    }


}
