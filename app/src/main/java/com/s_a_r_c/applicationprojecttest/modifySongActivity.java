package com.s_a_r_c.applicationprojecttest;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class modifySongActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_modify_song);

        ArrayList<String> playlists = new ArrayList<String>();
        for(DummyContent.DummyItem item1 : DummyContent.ITEMS) {

            if(!item1.details.equals(DummyContent.getStrPlaylistSelected()))
            {
                playlists.add(item1.content);
            }

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playlists);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setAdapter(adapter);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);

        DummyContent dummyContent = new DummyContent();
        new DownloadSong(null).execute("Useless");
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

            EditText editText12 = (EditText)findViewById(R.id.editText12);
            editText12.setText(strTitre);
            EditText editText15 = (EditText)findViewById(R.id.editText15);
            editText15.setText(strArtiste);
            EditText editText16 = (EditText)findViewById(R.id.editText16);
            editText16.setText(strMusique);
            CheckBox checkBoxPublique = (CheckBox)findViewById(R.id.checkBox5);
            CheckBox checkBoxActive = (CheckBox)findViewById(R.id.checkBox6);
            if(strPublique.equals("true"))
            {
                checkBoxPublique.setChecked(true);
            }
            if(strActive.equals("true"))
            {
                checkBoxActive.setChecked(true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
    }
    public void deleteSong(View view)
    {}
    public void transferSong(View view)
    {}
    public void copySong(View view)
    {}
}
