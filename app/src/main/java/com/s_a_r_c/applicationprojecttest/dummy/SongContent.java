package com.s_a_r_c.applicationprojecttest.dummy;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
public class SongContent extends Application{
    String jsonSaved = "";
    String strPlaylistID = "";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Log.e("songContent","///////////////////////////////Called");
        new DownloadJson(null).execute("Useless");

    }
    public void refreshList(String strID)
    {
        super.onCreate();
        mContext = this;
        Log.e("songContent","///////////////////////////////Refresh");
        strPlaylistID = strID;


       // new DownloadJson(null).execute("Useless");
    }
    /**
     * An array of sample (dummy) items.
     */
    public static  List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static  List<DummyItem> SAVEDITEMS = new ArrayList<DummyItem>();

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
        SAVEDITEMS.add(item);
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
        public final String details;

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

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/getMusique");
                c = (HttpURLConnection) u.openConnection();
                c.connect();
                int intStatusRetrieved = c.getResponseCode();
                String strString;
                switch (intStatusRetrieved) {
                    case 200:
                        InputStreamReader  inputStreamReader =new InputStreamReader(c.getInputStream());
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
               String strNom =jsonMovie.get("Titre").toString();
                String strId =jsonMovie.get("Id").toString();
                Log.e("SONGLIST1",strNom+" "+strId);
                addItem(createDummyItem(i,strNom,strId));

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }


    }
    public void purgeSongs(String strListeDeLecture)
    {

        List<DummyItem> PURGEDITEMS = new ArrayList<DummyItem>();
        String[] strSplit = strListeDeLecture.split(";");

        for(DummyItem item1 : SongContent.ITEMS) {

            for(String strId : strSplit) {

                if(item1.details.equals(strId))
                {
                    PURGEDITEMS.add(item1);
                }


            }


        }
        SongContent.ITEMS = PURGEDITEMS;
    }

    public void rebuildItems()
    {
        SongContent.ITEMS = SongContent.SAVEDITEMS;
    }

}
