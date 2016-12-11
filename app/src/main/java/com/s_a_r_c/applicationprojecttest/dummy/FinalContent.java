package com.s_a_r_c.applicationprojecttest.dummy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
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

import com.s_a_r_c.applicationprojecttest.CriticalErrorLandingActivity;
import com.s_a_r_c.applicationprojecttest.playListListActivity;
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
    public static List<PlaylistITEM> ITEMS = new ArrayList<PlaylistITEM>();
    public  static List<SongItem> SONGITEMSPRIMAL = new ArrayList<SongItem>();

    public static class PlaylistITEM {
        public String id;
        public String name;
        public String active;
        public String publique;
        public String strListeDeLecture= "";
        public String owner = "";
        public  List<SongItem> SONGITEMS = new ArrayList<SongItem>();

        public PlaylistITEM(String strNom, String strId, String strOwner, String strPublique,String strActive) {
            this.name = strNom;
            this.id = strId;
            this.owner = strOwner;
            this.publique = strPublique;
            this.active = strActive;
            Log.e("New Playlist Created","Name:"+name+" Id:"+strId+" Owner:"+strOwner);
        }
    }

    public static class SongItem {
        public String strId;
        public String strOwnerID;
        public String strVignette;
        public String strTitre;
        public String strMusique;
        public String strPublique;
        public String strActive;
        public String strArtiste;

        public SongItem(String strId,String strOwnerID,String strVignette,String strTitre,String strMusique,String strPublique,String strActive,String strArtiste) {
            this.strId = strId;
            this.strOwnerID = strOwnerID;
            this.strVignette =strVignette;
            this.strTitre =strTitre;
            this.strMusique =strMusique;
            this.strPublique =strPublique;
            this.strActive =strActive;
            this.strArtiste =strArtiste;
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();
        mContext = this;
        ITEMS = new ArrayList<PlaylistITEM>();
        SONGITEMSPRIMAL = new ArrayList<SongItem>();

        if(DummyContent.getId().equals(""))
        {
            Log.e("STATUSLOGIN","OFFLINE");
            new DownloadJson(null).execute("Useless");
        }
        else
        {
            Log.e("STATUSLOGIN","ONLINE");
            new DownloadJsonDeleteAttept(null).execute("Useless");
        }
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
            if (jsonSaved != null)
                createList();
            else {
                Intent intent = new Intent(mContext, CriticalErrorLandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
    public void createList()
    {
        ITEMS = new ArrayList<PlaylistITEM>();
        try {
            JSONObject lireJSON     = new JSONObject(jsonSaved);
            Log.e("JSONSAVED",jsonSaved+"1");
            JSONArray jsonArray = lireJSON.getJSONArray("Elements");
            int nbElements = lireJSON.getJSONArray("Elements").length();
            JSONObject jsonMovie;
            for(int i = 0; i<nbElements;i++)
            {

                jsonMovie = jsonArray.getJSONObject(i);
                String strNom =jsonMovie.get("Nom").toString();
                String strId =jsonMovie.get("Id").toString();
                String strOwnerID =jsonMovie.get("Proprietaire").toString();
                String strActive = jsonMovie.get("Active").toString();
                String strPublique = jsonMovie.get("Publique").toString();
                PlaylistITEM  playlistITEM= new PlaylistITEM(strNom, strId, strOwnerID, strPublique, strActive);
                ITEMS.add(playlistITEM);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
        new DownloadListDeLecture(null).execute("Useless");
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
            createListeDeLecture();
        }
    }

    public void createListeDeLecture()
    {
        Log.e("DownloadListDeLecture","DownloadListDeLecture has been called ITEMSIZE:"+ITEMS.size());
        try {
            JSONObject lireJSON = new JSONObject(jsonSaved);
            JSONArray jsonArray = lireJSON.getJSONArray("Elements");
            int nbElements = lireJSON.getJSONArray("Elements").length();
            JSONObject jsonMovie;
            for(int i = 0; i<nbElements;i++)
            {
                jsonMovie = jsonArray.getJSONObject(i);
                String strListeDeLecture =jsonMovie.get("ListeDeLecture").toString();
                String strMusique =jsonMovie.get("Musique").toString();

                for(PlaylistITEM playlistITEM : ITEMS)
                {
                    if(playlistITEM.id.equals(strListeDeLecture))
                    {
                        playlistITEM.strListeDeLecture += strMusique+";";
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
        for(PlaylistITEM playlistITEM : ITEMS)
        {
            Log.e("PLAYLIST CREATRED","Name:"+playlistITEM.name+" List"+playlistITEM.strListeDeLecture);
        }

        if(DummyContent.getId().equals(""))
        {
            new DownloadJsonMusic(null).execute("Useless");
        }
        else
        {
            new DownloadJsonMusicAttept(null).execute("Useless");
        }






    }
    private class DownloadJsonMusic extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonMusic(String url) {

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
            createListMusic();
        }
    }
    public void createListMusic()
    {
        SONGITEMSPRIMAL = new ArrayList<SongItem>();
        try {

            JSONObject lireJSON     = new JSONObject(jsonSaved);
            JSONArray jsonArray = lireJSON.getJSONArray("Elements");
            int nbElements = lireJSON.getJSONArray("Elements").length();
            JSONObject jsonMovie;
            for(int i = 0; i<nbElements;i++)
            {


                jsonMovie = jsonArray.getJSONObject(i);
                String strID = jsonMovie.get("Id").toString();
                String strOwnerID = jsonMovie.get("Proprietaire").toString();
                String strVignette = jsonMovie.get("Vignette").toString();
                String strTitre = jsonMovie.get("Titre").toString();
                String strMusique = jsonMovie.get("Musique").toString();
                String strPublique = jsonMovie.get("Publique").toString();
                String strActive = jsonMovie.get("Active").toString();
                String strArtiste = jsonMovie.get("Artiste").toString();
                Log.e("SONGITEMPRIMAL","NEW SONG: "+strID+" "+strOwnerID+" "+strVignette+" "+strTitre+" "+strMusique+" "+strPublique+" "+strActive+" "+strArtiste);
                SongItem songItem = new SongItem(strID,strOwnerID,strVignette,strTitre,strMusique,strPublique,strActive,strArtiste);
                SONGITEMSPRIMAL.add(songItem);

                Log.e("createListMusic","New song:"+strTitre+" Owner Id:"+strOwnerID);
                //addItem(createDummyItem(i,strNom,strId));
              /*  for(PlaylistITEM playlistITEM : ITEMS) {
                    playlistITEM.SONGITEMS = new ArrayList<SongItem>();
                }

*/
                for(PlaylistITEM playlistITEM : ITEMS)
                {
                   // playlistITEM.SONGITEMS = new ArrayList<SongItem>();
                 //   playlistITEM.SONGITEMS = new ArrayList<SongItem>();
                    String[] strListeDeLecture = playlistITEM.strListeDeLecture.split(";");
                    for(String strSingleListeDeLecture : strListeDeLecture)
                    {
                        if(strSingleListeDeLecture.equals(strID))
                        {
                            playlistITEM.SONGITEMS.add(songItem);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
        for(PlaylistITEM playlistITEM : ITEMS)
        {
            Log.e("PLAYLIST CREATRED","Name:"+playlistITEM.name+" List:"+playlistITEM.strListeDeLecture+" Size:"+playlistITEM.SONGITEMS.size());
        }
    }
    private class DownloadJsonDeleteAttept extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonDeleteAttept(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/" + DummyContent.getCourriel());
                Log.e("URL", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/" + DummyContent.getCourriel());
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
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString);}
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError",c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
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
            if (jsonSaved != null) {
                receivedDeleteResponse();
            }

        }
    }

    public void receivedDeleteResponse() {
        try {

            JSONObject lireJSON = new JSONObject(jsonSaved);
            strTicketID = lireJSON.get("idTicket").toString();
            strCle = lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }

        new DownloadJsonDeleteComplete(null).execute("Useless");
    }


    private class DownloadJsonDeleteComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonDeleteComplete(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                String strConfirmation = DummyContent.encryptMD5(DummyContent.getPassword() + strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=337&confirmation=26b15445192a07d528d0e70c2c58264d&action=supprimerMusiqueListe&p1=9&p2=16&p3=148
                //Playlist Public+Owner : http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" +ticket.idTicket + "&confirmation=" + md5(motdepasse + ticket.cle) +"&action=afficherToutesLesListes&p1=" + utilisateurID
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=afficherToutesLesListes&p1=" + DummyContent.getId());
                Log.e("Error", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=afficherToutesLesListes&p1=" + DummyContent.getId());
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
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString);}
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError",c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
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
            Log.e("FinalResponse", jsonSaved + "11111111");

            //finalResponse();
            createList();
        }
    }




    private class DownloadJsonMusicAttept extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonMusicAttept(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/" + DummyContent.getCourriel());
                Log.e("URL", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/" + DummyContent.getCourriel());
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
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString);}
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError",c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
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
            if (jsonSaved != null) {
                receivedeMusicResponse();
            }

        }
    }

    public void receivedeMusicResponse() {
        try {

            JSONObject lireJSON = new JSONObject(jsonSaved);
            strTicketID = lireJSON.get("idTicket").toString();
            strCle = lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }

        new DownloadJsonMusicComplete(null).execute("Useless");
    }


    private class DownloadJsonMusicComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonMusicComplete(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                String strConfirmation = DummyContent.encryptMD5(DummyContent.getPassword() + strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=337&confirmation=26b15445192a07d528d0e70c2c58264d&action=supprimerMusiqueListe&p1=9&p2=16&p3=148
                //Playlist Public+Owner : http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" +ticket.idTicket + "&confirmation=" + md5(motdepasse + ticket.cle) +"&action=afficherToutesLesListes&p1=" + utilisateurID
               // "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" + ticket.idTicket + "&confirmation=" + $.md5(Cookies.get('motdepasse') + ticket.cle) +"&action=afficherToutesLesMusiques&p1=" + Cookies.get('utilisateurID')
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=afficherToutesLesMusiques&p1=" + DummyContent.getId());
                Log.e("Error", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=afficherToutesLesMusiques&p1=" + DummyContent.getId());
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
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString);}
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError",c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
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
            Log.e("FinalResponse", jsonSaved + "11111111");

            //finalResponse();
            createListMusic();
        }
    }

}
