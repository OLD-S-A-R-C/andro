package com.s_a_r_c.applicationprojecttest.dummy;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;


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
public class DummyContent extends Application{
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

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        new DownloadJson(null).execute("Useless");
        SongContent songContent = new SongContent();
        songContent.onCreate();
        new DownloadListDeLecture(null).execute("Useless");

        //get avatar
        new DownloadListAvatars(null).execute("Useless");

        FinalContent finalContent = new FinalContent();
        finalContent.onCreate();
    }

    public void refresh()
    {
        ITEMS.clear();
        if(!strID.equals("")) {
           //new DownloadJson(null).execute("Useless");
           new DownloadJsonDeleteAttept(null).execute("Useless");
        }
        else
        {
            new DownloadJson(null).execute("Useless");
        }
        //songContent.refresh();
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

    private static DummyItem createDummyItem(int position, String strName, String strId, String strOwner) {
        return new DummyItem(String.valueOf(position), strName+" " + position, makeDetails(position),strId, strOwner);
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
        public String owner = "";

        public DummyItem(String id, String content, String details, String strId, String strOwner) {
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

    private class DownloadJson extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJson(String url) {

            this.url = url;
        }

        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                //Playlist Public+Owner : http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" +ticket.idTicket + "&confirmation=" + md5(motdepasse + ticket.cle) +"&action=afficherToutesLesListes&p1=" + utilisateurID
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

                String strOwnerID =jsonMovie.get("Proprietaire").toString();
                addItem(createDummyItem(i,strNom,strId,strOwnerID));
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

    private class DownloadListAvatars extends AsyncTask<String, Void, String> {
        String url;

        public DownloadListAvatars(String url) {

            this.url = url;
        }

        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Avatar/getAvatar");
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
            createListAvatars(jsonSaved);
        }
    }

    public void createListeDeLecture()
    {
        try {

            JSONObject lireJSON = new JSONObject(jsonSaved);
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
                   // Log.e("CREATELISTEDELECTURE",item1.content+"");
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }

        for(DummyItem item1 : ITEMS) {

        }
    }


    public void createListAvatars(String json)
    {
        try {
            Log.d("AVATARAAAAAAAAAAAAAA", " jjjjj");
            JSONObject lireJSON = new JSONObject(json);
            JSONArray jsonArray = lireJSON.getJSONArray("Elements");
            int nbElements = lireJSON.getJSONArray("Elements").length();
            JSONObject jsonAvatar = new JSONObject();
            for(int i = 0; i<nbElements;i++)
            {
                jsonAvatar = jsonArray.getJSONObject(i);
                int id = Integer.valueOf(jsonAvatar.get("Id").toString());
                String strNom = jsonAvatar.get("Nom").toString();
                String strAvatarB64 = jsonAvatar.get("Avatar").toString();
                AvatarContent newAvatar = new AvatarContent();
                newAvatar.setAvatar(id, strNom, strAvatarB64);
                Avatars.getInstance().getListAvatars().put(strNom, newAvatar);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }

        for(DummyItem item1 : ITEMS) {

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
    public static void setStrSongSelected(String strElement)
    {
        strSongSelected = strElement;
    }

    public static void setStrPlaylistSelected(String strElement)
    {
        strPlaylistSelected = strElement;
    }

    public static String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
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
            receivedDeleteResponse();
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

                String strConfirmation = getMd5Hash(DummyContent.getPassword() + strCle);
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

}
