package com.s_a_r_c.applicationprojecttest;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.s_a_r_c.applicationprojecttest.Helpers.Avatars;
import com.s_a_r_c.applicationprojecttest.Helpers.RechercheYoutube;
import com.s_a_r_c.applicationprojecttest.dummy.AvatarContent;
import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addSongActivity extends AppCompatActivity {
    String jsonSaved = "";
    String strCle = "";
    String strTicketID = "";
    String strTitre = "";
    String strUrl = "";
    String strArtiste = "";
    String strPublique = "";
    String strActive = "";

    private GoogleApiClient client;

    List<SearchResult> testingList;

    ArrayList<YoutubeVideo> lstVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        EditText editText = (EditText) findViewById(R.id.etURLNewMusic);
        editText.setText("https://www.youtube.com/watch?v=Pw-0pbY9JeU");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void createSong(View view) {
        EditText etTitleNewMusic = (EditText) findViewById(R.id.etTitleNewMusic);
        EditText etArtistNewMusic = (EditText) findViewById(R.id.etArtistNewMusic);
        EditText etURLNewMusic = (EditText) findViewById(R.id.etURLNewMusic);

        if (etTitleNewMusic.getText().toString().trim().equals("")) {
            hideKeyboardShowToast("Titre invalide");
        } else if (etArtistNewMusic.getText().toString().trim().equals("")) {
            hideKeyboardShowToast("Artiste invalide");
        } else if (etURLNewMusic.getText().toString().trim().equals("") || !checkIfYoutubeURL(etURLNewMusic.getText().toString().trim())) {
            hideKeyboardShowToast("URL invalide");
        } else {
            new DownloadJsonAddAttept(null).execute("Useless");
        }


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("addSong Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    private class DownloadJsonAddAttept extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonAddAttept(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/" + DummyContent.getCourriel());
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                int intStatusRetrieved = c.getResponseCode();
                String strString;
                switch (intStatusRetrieved) {
                    case 200:
                        InputStreamReader inputStreamReader = new InputStreamReader(c.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((strString = bufferedReader.readLine()) != null) {
                            stringBuilder.append(strString + "\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    case 400:
                        strString = "";
                        InputStreamReader inputStreamReader1 = new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null) {
                            stringBuilder1.append(strString);
                        }
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError", c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
                    case 500:
                        return "{\"success\":\"false\",\"reason\":\"" + "Une erreur est survenue !" + "\"}";
                }
            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Log.e("JsonRetrieveError", "Error fielded");
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            jsonSaved = result;
            //confirmLogin();
            receivedDeleteResponse();
        }
    }

    public void receivedDeleteResponse() {
        Log.e("JSON SAVED", jsonSaved + "Message");

        try {

            JSONObject lireJSON = new JSONObject(jsonSaved);
            strTicketID = lireJSON.get("idTicket").toString();
            strCle = lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }

        EditText editText = (EditText) findViewById(R.id.etTitleNewMusic);
        strTitre = editText.getText().toString();
        EditText editText8 = (EditText) findViewById(R.id.etArtistNewMusic);
        strArtiste = editText8.getText().toString();
        EditText editText9 = (EditText) findViewById(R.id.etURLNewMusic);
        strUrl = editText9.getText().toString().replace(" ", "%20");
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox7);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox8);
        if (checkBox.isChecked()) {
            strPublique = "true";
        } else {
            strPublique = "false";
        }
        if (checkBox2.isChecked()) {
            strActive = "true";
        } else {
            strActive = "false";
        }


        new DownloadJsonAddComplete(null).execute("Useless");
    }

    private class DownloadJsonAddComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonAddComplete(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                //$.md5(Cookies.get('motdepasse') + ticket.cle)

                strTitre = strTitre.replaceAll(" ", "%20");
                strArtiste = strArtiste.replaceAll(" ", "%20");

                String strConfirmation = DummyContent.encryptMD5(DummyContent.getPassword() + strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=184&confirmation=99fee649e6b6e7680a094d49cec71961&action=nouvelleMusique&p1=9&p2=testestTitre&p3=testtestArtiste&p4=testTestURL&p5=true&p6=true
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=181&confirmation=cc8e336a813dff682d64349fa4374724&action=nouvelleMusique&p1=9&p2=TestTitre&p3=TestArtiste&p4=https://www.youtube.com/watch?v=Pw-0pbY9JeU&p5=true&p6=true
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=nouvelleMusique&p1=" + DummyContent.getId() + "&p2=" + strTitre + "&p3=" + strArtiste + "&p4=" + strUrl.replace(" ", "%20") + "&p5=" + strPublique + "&p6=" + strActive);
                Log.e("Error", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=nouvelleMusique&p1=" + DummyContent.getId() + "&p2=" + strTitre + "&p3=" + strArtiste + "&p4=" + strUrl.replace(" ", "%20") + "&p5=" + strPublique + "&p6=" + strActive);

                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("PUT");
                c.connect();
                int intStatusRetrieved = c.getResponseCode();
                String strString;
                switch (intStatusRetrieved) {
                    case 200:
                        InputStreamReader inputStreamReader = new InputStreamReader(c.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((strString = bufferedReader.readLine()) != null) {
                            stringBuilder.append(strString + "\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    case 400:
                        strString = "";
                        InputStreamReader inputStreamReader1 = new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null) {
                            stringBuilder1.append(strString);
                        }
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError", c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
                    case 500:
                        return "{\"success\":\"false\",\"reason\":\"" + "Une erreur est survenue !" + "\"}";
                }
            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Log.e("JsonRetrieveError", "Error fielded");
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            jsonSaved = result;
            Log.e("FinalResponse", jsonSaved + "");
            if (result != null || jsonSaved != null) {
                finalResponse();
            }

        }
    }

    public void finalResponse() {
        try {
            JSONObject lireJSON = new JSONObject(jsonSaved);
            String strSuccess = lireJSON.get("success").toString();
            if (strSuccess.equals("true")) {
                hideKeyboardShowToast(lireJSON.get("reason").toString());
                finish();
            } else {
                hideKeyboardShowToast(lireJSON.get("reason").toString());
                Log.e("Final Response", "Failure");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }
    }

    public void searchVideo(View view) {

        EditText etYoutubeSearch = (EditText) findViewById(R.id.etYoutubeSearch);
        Spinner spinerYoutube = (Spinner) findViewById(R.id.spinnerYoutubeSearch);

        if (etYoutubeSearch.getText().toString().trim().equals("")) {
            hideKeyboardShowToast("Veuillez taper une recherche YouTube");
        } else {
            new DownloadYoutube(null).execute(etYoutubeSearch.getText().toString().trim());
        }

    }

    public void addVideo(View view) {

        Spinner spinerYoutube = (Spinner) findViewById(R.id.spinnerYoutubeSearch);
        EditText etTitleNewMusic = (EditText) findViewById(R.id.etTitleNewMusic);
        EditText etArtistNewMusic = (EditText) findViewById(R.id.etArtistNewMusic);

        if (spinerYoutube.getSelectedItem() == null){
            hideKeyboardShowToast("Veuillez sélectionner une vidéo");
        } else if (etTitleNewMusic.getText().toString().trim().equals("")) {
            hideKeyboardShowToast("Titre manquant");
        } else if (etArtistNewMusic.getText().toString().trim().equals("")) {
            hideKeyboardShowToast("Artiste manquant");
        } else {
            new DownloadJsonAddAtteptYoutube(null).execute("Useless");
        }

    }

    private Boolean checkIfYoutubeURL(String strURL) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(strURL);

        if (matcher.find() && Patterns.WEB_URL.matcher(strURL).matches()) {
            return true;
        } else {
            return false;
        }
    }

    private void hideKeyboardShowToast(String strMessage) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Toast toast = Toast.makeText(this, strMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

    private class DownloadYoutube extends AsyncTask<String, Void, List<SearchResult>> {
        String url;

        public DownloadYoutube(String url) {

            this.url = url;
        }


        protected List<SearchResult> doInBackground(String... url) {

            try {
                EditText etYoutubeSearch = (EditText) findViewById(R.id.etYoutubeSearch);

                YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                }).setApplicationName("youtube-cmdline-search-sample").build();

                YouTube.Search.List search = youtube.search().list("id,snippet");
                search.setKey("AIzaSyBUaqxxbastsk_rfIKiHo-AzYBO7jtD90I");
                search.setQ(url[0]);
                search.setType("video");
                search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
                search.setMaxResults(5L);


                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();
                if (searchResultList != null) {
                    return testingList = searchResultList;
                }
            } catch (Exception ex) {
                return null;
            }
            return null;
        }

        protected void onPostExecute(List<SearchResult> result) {
            if (result != null)
                prettyPrint(result.iterator());
        }
    }

    private void prettyPrint(Iterator<SearchResult> iteratorSearchResults) {
        if (!iteratorSearchResults.hasNext()) {
            hideKeyboardShowToast("Aucun résultat");
        }

        int compteur = 0;
        lstVideos = new ArrayList<YoutubeVideo>();
        while (iteratorSearchResults.hasNext()) {
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();
            if (rId.getKind().equals("youtube#video")) {
                Log.d("videoid", rId.getVideoId());
                YoutubeVideo newVideo = new YoutubeVideo(compteur, rId.getVideoId(), singleVideo.getSnippet().getTitle());
                lstVideos.add(newVideo);
            }
        }

        Collection<YoutubeVideo> vals = lstVideos;
        HashMap<Integer, Integer> arrayMap = new HashMap<Integer, Integer>();
        YoutubeVideo[] array = new YoutubeVideo[vals.size()];
        for (int i = 0; i < vals.size(); i ++)
            array[i] = lstVideos.get(i);

        ArrayAdapter<YoutubeVideo> adapter = new ArrayAdapter<YoutubeVideo>(this,
                android.R.layout.simple_spinner_item,
                array);

        Spinner spinnerYoutubeSearch = (Spinner) findViewById(R.id.spinnerYoutubeSearch);
        spinnerYoutubeSearch.setAdapter(adapter);

    }

    public class YoutubeVideo {
        public int id;
        public String videoid;
        public String title;

        public YoutubeVideo(int id, String videoid, String title) {
            this.id = id;
            this.videoid = videoid;
            this.title = title;
        }

        public String toString() {
            return title;
        }
    }

    private class DownloadJsonAddAtteptYoutube extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonAddAtteptYoutube(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/" + DummyContent.getCourriel());
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                int intStatusRetrieved = c.getResponseCode();
                String strString;
                switch (intStatusRetrieved) {
                    case 200:
                        InputStreamReader inputStreamReader = new InputStreamReader(c.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((strString = bufferedReader.readLine()) != null) {
                            stringBuilder.append(strString + "\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    case 400:
                        strString = "";
                        InputStreamReader inputStreamReader1 = new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null) {
                            stringBuilder1.append(strString);
                        }
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError", c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
                    case 500:
                        return "{\"success\":\"false\",\"reason\":\"" + "Une erreur est survenue !" + "\"}";
                }
            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Log.e("JsonRetrieveError", "Error fielded");
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            jsonSaved = result;
            //confirmLogin();
            receivedAddYoutubeResponse();
        }
    }

    public void receivedAddYoutubeResponse() {
        Log.e("JSON SAVED", jsonSaved + "Message");

        try {

            JSONObject lireJSON = new JSONObject(jsonSaved);
            strTicketID = lireJSON.get("idTicket").toString();
            strCle = lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }

        Spinner spinerYoutube = (Spinner) findViewById(R.id.spinnerYoutubeSearch);

        EditText editText = (EditText) findViewById(R.id.etTitleNewMusic);
        strTitre = editText.getText().toString();
        EditText editText8 = (EditText) findViewById(R.id.etArtistNewMusic);
        strArtiste = editText8.getText().toString();
        strUrl = "https://www.youtube.com/watch?v=";
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox7);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox8);
        if (checkBox.isChecked()) {
            strPublique = "true";
        } else {
            strPublique = "false";
        }
        if (checkBox2.isChecked()) {
            strActive = "true";
        } else {
            strActive = "false";
        }

        if (spinerYoutube.getSelectedItem() != null) {
            new DownloadJsonAddYoutubeComplete(null).execute("https://www.youtube.com/watch?v=" + lstVideos.get(spinerYoutube.getSelectedItemPosition()).videoid);
        }

    }

    private class DownloadJsonAddYoutubeComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonAddYoutubeComplete(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                //$.md5(Cookies.get('motdepasse') + ticket.cle)

                strTitre = strTitre.replaceAll(" ", "%20");
                strArtiste = strArtiste.replaceAll(" ", "%20");

                String strConfirmation = DummyContent.encryptMD5(DummyContent.getPassword() + strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=184&confirmation=99fee649e6b6e7680a094d49cec71961&action=nouvelleMusique&p1=9&p2=testestTitre&p3=testtestArtiste&p4=testTestURL&p5=true&p6=true
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=181&confirmation=cc8e336a813dff682d64349fa4374724&action=nouvelleMusique&p1=9&p2=TestTitre&p3=TestArtiste&p4=https://www.youtube.com/watch?v=Pw-0pbY9JeU&p5=true&p6=true
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=nouvelleMusique&p1=" + DummyContent.getId() + "&p2=" + strTitre.replace(" ", "%20") + "&p3=" + strTitre.replace(" ", "%20") + "&p4=" + url[0].replace(" ", "%20") + "&p5=" + strPublique + "&p6=" + strActive);
                Log.e("Error", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=nouvelleMusique&p1=" + DummyContent.getId() + "&p2=" + strTitre + "&p3=" + strArtiste + "&p4=" + url[0].replace(" ", "%20") + "&p5=" + strPublique + "&p6=" + strActive);

                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("PUT");
                c.connect();
                int intStatusRetrieved = c.getResponseCode();
                String strString;
                switch (intStatusRetrieved) {
                    case 200:
                        InputStreamReader inputStreamReader = new InputStreamReader(c.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((strString = bufferedReader.readLine()) != null) {
                            stringBuilder.append(strString + "\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    case 400:
                        strString = "";
                        InputStreamReader inputStreamReader1 = new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null) {
                            stringBuilder1.append(strString);
                        }
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError", c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
                    case 500:
                        return "{\"success\":\"false\",\"reason\":\"" + "Une erreur est survenue !" + "\"}";
                }
            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Log.e("JsonRetrieveError", "Error fielded");
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            jsonSaved = result;
            Log.e("FinalResponse", jsonSaved + "");
            if (result != null || jsonSaved != null) {
                finalResponse();
            }

        }
    }

}
