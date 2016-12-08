package com.s_a_r_c.applicationprojecttest.dummy;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.s_a_r_c.applicationprojecttest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent extends Application{
    String jsonSaved = "";
    public static String strID = "";
    public static String strAlias = "";
    public static String strPassword = "";
    public static String strCourriel = "";
    public static String strSongSelected = "";
    public static String strPlaylistSelected = "";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        new DownloadJson(null).execute("Useless");
        SongContent songContent = new SongContent();
        songContent.onCreate();
        new DownloadListDeLecture(null).execute("Useless");
    }
    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
         //   addItem(createDummyItem(i+100,"Playlist"));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position, String strName, String strId) {
        return new DummyItem(String.valueOf(position), strName+" " + position, makeDetails(position),strId);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public String details;
        public String strListeDeLecture= "";

        public DummyItem(String id, String content, String details, String strId) {
            this.id = id;
            this.content = content;
            this.details = strId;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    private class DownloadJson extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJson(String url) {

            this.url = url;
        }

        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/getListesDeLecture");
                c = (HttpURLConnection) u.openConnection();
                c.connect();
                int status = c.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        //disconnect error
                    }
                }
            }
            return null;

        }

        protected void onPostExecute(String result) {
            // bmImage.setImageBitmap(result);
            jsonSaved = result;
            createList();
        }
    }
    public void createList()
    {
        // Add some sample items.
        //  getJSON("http:\\/\\/w1.cgodin.qc.ca:8080\\/420-5PA");
        // Log.i("Json",     getJSON("http://w1.cgodin.qc.ca:8080/420-5PA"));
        try {

            JSONObject lireJSON     = new JSONObject(jsonSaved);

          ///////  int nbElements =  Integer.parseInt( lireJSON.get("NbFilms").toString());
           JSONArray jsonArray = lireJSON.getJSONArray("Elements");
            int nbElements = lireJSON.getJSONArray("Elements").length();
           JSONObject jsonMovie = new JSONObject();
            for(int i = 0; i<nbElements;i++)
            {
                jsonMovie = jsonArray.getJSONObject(i);
               String strNom =jsonMovie.get("Nom").toString();
                String strId =jsonMovie.get("Id").toString();
                Log.e("MOVIELIST1",strNom+" "+strId);
                addItem(createDummyItem(i,strNom,strId));
                //String strTitre =jsonMovie.get("titre").toString();
               // String strAnnonce=jsonMovie.get("affiche").toString();
               // String strResume=jsonMovie.get("resume").toString();
               // addItem(createDummyItem(i,strTitre,strAffiche,strAnnonce,strResume));
            }

            //   jsonMovie = jsonArray.getJSONObject(intElement);
            //   testText.setText(jsonMovie.get("Affiche").toString());
            //   textView2.setText(jsonMovie.get("Titre").toString());
            //  URL url = new URL(jsonMovie.get("Affiche").toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }


    }
    private class DownloadListDeLecture extends AsyncTask<String, Void, String> {
        String url;

        public DownloadListDeLecture(String url) {

            this.url = url;
        }

        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/getListesdelectureMusique");
                c = (HttpURLConnection) u.openConnection();
                c.connect();
                int status = c.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        //disconnect error
                    }
                }
            }
            return null;

        }

        protected void onPostExecute(String result) {
            // bmImage.setImageBitmap(result);
            jsonSaved = result;
            createListeDeLecture();
        }
    }
    public void createListeDeLecture()
    {
        try {

            JSONObject lireJSON     = new JSONObject(jsonSaved);

            ///////  int nbElements =  Integer.parseInt( lireJSON.get("NbFilms").toString());
            JSONArray jsonArray = lireJSON.getJSONArray("Elements");
            int nbElements = lireJSON.getJSONArray("Elements").length();
            JSONObject jsonMovie = new JSONObject();
            for(int i = 0; i<nbElements;i++)
            {
                jsonMovie = jsonArray.getJSONObject(i);
                String strListeDeLecture =jsonMovie.get("ListeDeLecture").toString();
                String strMusique =jsonMovie.get("Musique").toString();
               // Log.e("StrLicDeLecture",strListeDeLecture+":"+strMusique);
                for(DummyItem item1 : ITEMS) {
                    if(item1.details.equals(strListeDeLecture))
                    {
                        item1.strListeDeLecture += strMusique+";";
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
    }
    public void connectUser(String alias, String courriel, String password, String ID )
    {
        Log.e("DUMMYCONTENTCONNECTED"," LOGGED IN "+alias);
        strAlias = alias;
        strCourriel = courriel;
        strPassword = password;
        strID = ID;
    }
    public void setStrSongSelected(String strElement)
    {
        strSongSelected = strElement;
    }
//Test 
    public void setStrPlaylistSelected(String strElement)
    {
        strPlaylistSelected = strElement;
    }

    public String getAlias()
    {
        return strAlias;
    }
    public String getCourriel()
    {
        return strCourriel;
    }
    public String getPassword()
    {
        return strPassword;
    }
    public String getId()
    {
        return strID;
    }
    public String getStrSongSelected()
    {
        return strSongSelected;
    }
    public String getStrPlaylistSelected()
    {
        return strPlaylistSelected;
    }
}