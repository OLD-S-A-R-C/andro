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
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
public class FinalContent extends Application{
    String jsonSaved = "";
    public static String strID = "";
    public static String strAlias = "";
    public static String strPassword = "";
    public static String strCourriel = "";
    public static String strSongSelected = "";
    public static String strPlaylistSelected = "";
    public String strTicketID = "";
    public String strCle = "";
    //public static String strOwner = "";
    private static Context mContext;
    public static final List<PlaylistITEM> ITEMS = new ArrayList<PlaylistITEM>();

    public static class PlaylistITEM {
        public final String id;
        public final String content;
        public  List<SongItem> SONGITEMS = new ArrayList<SongItem>();
        public String details;
        public String strListeDeLecture= "";
        public String owner = "";

        public PlaylistITEM(String id, String content, String details, String strId, String strOwner) {
            this.id = id;
            this.content = content;
            this.details = strId;
            this.owner = strOwner;
        }

        @Override
        public String toString() {
            return content;
        }
        public void setSongItem(List<SongItem> songList)
        {
            SONGITEMS = songList;
        }
    }

    public static class SongItem {
        public final String id;
        public final String content;
        public String details;
        public String owner = "";

        public SongItem(String id, String content, String details, String strId, String strOwner) {
            this.id = id;
            this.content = content;
            this.details = strId;
            this.owner = strOwner;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        new DownloadJson(null).execute("Useless");
    }

    public static String getAlias()
    {
        return strAlias;
    }
    public static String getCourriel()
    {
        return strCourriel;
    }
    public static String getPassword()
    {
        return strPassword;
    }
    public static String getId()
    {
        return strID;
    }
    public static String getStrSongSelected()
    {
        return strSongSelected;
    }
    public static String getStrPlaylistSelected()
    {
        return strPlaylistSelected;
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
            jsonSaved = result;
            createList();
        }
    }
    public void createList()
    {
        try {
            JSONObject lireJSON     = new JSONObject(jsonSaved);
            JSONArray jsonArray = lireJSON.getJSONArray("Elements");
            int nbElements = lireJSON.getJSONArray("Elements").length();
            JSONObject jsonMovie = new JSONObject();
            for(int i = 0; i<nbElements;i++)
            {
                jsonMovie = jsonArray.getJSONObject(i);
                String strNom =jsonMovie.get("Nom").toString();
                String strId =jsonMovie.get("Id").toString();
                String strOwnerID =jsonMovie.get("Proprietaire").toString();
                //addItem(createDummyItem(i,strNom,strId,strOwnerID));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }


    }


}
