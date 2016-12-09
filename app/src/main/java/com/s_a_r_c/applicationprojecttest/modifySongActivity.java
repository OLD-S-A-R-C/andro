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
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class modifySongActivity extends AppCompatActivity {
    String strOwnerID = "";
    String strVignette = "";
    String strMusique = "";
    String jsonSaved = "";
    String strCle = "";
    String strTicketID = "";
    String strTitre = "";
    String strUrl = "";
    String strArtiste = "";
    String strPublique = "";
    String strActive = "";
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


    public void modifySong(View view)
    {
        new DownloadJsonModifyAttept(null).execute("Useless");
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

    private class DownloadJsonModifyAttept extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonModifyAttept(String url) {

            this.url = url;
        }



        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/"+DummyContent.getCourriel());
                Log.e("URL","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/"+DummyContent.getCourriel());
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
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
            //confirmLogin();
            receivedDeleteResponse();
        }
    }
    public void receivedDeleteResponse() {
        Log.e("JSON SAVED", jsonSaved + "Message");

        try {

            JSONObject lireJSON     = new JSONObject(jsonSaved);
            strTicketID =lireJSON.get("idTicket").toString();
            strCle=lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }

        EditText editText = (EditText)findViewById(R.id.editText12);
        strTitre = editText.getText().toString();
        EditText editText8 = (EditText)findViewById(R.id.editText15);
        strArtiste = editText8.getText().toString();
        EditText editText9 = (EditText)findViewById(R.id.editText16);
        strUrl = editText9.getText().toString();
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox5);
        CheckBox checkBox2 = (CheckBox)findViewById(R.id.checkBox6);
        if(checkBox.isChecked())
        {
            strPublique="true";
        }
        else
        {
            strPublique="false";
        }
        if(checkBox2.isChecked())
        {
            strActive="true";
        }
        else
        {
            strActive="false";
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
                //$.md5(Cookies.get('motdepasse') + ticket.cle)

                strTitre = strTitre.replaceAll(" ", "%20");
                strArtiste = strArtiste.replaceAll(" ", "%20");

                String strConfirmation = getMd5Hash(DummyContent.getPassword()+strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=211&confirmation=f006434e24b278ae1f935331e13810b1&action=modifierMusique&p1=134&p2=9&p3=TestTitre1&p4=TestArtiste1&p5=https://www.youtube.com/watch?v=Pw-0pbY9JeU&p6=true&p7=true
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=modifierMusique&p1="+DummyContent.getStrSongSelected()+"&p2="+DummyContent.getId()+"&p3="+strTitre+"&p4="+strArtiste+"&p5="+strUrl+"&p6="+strPublique+"&p7="+strActive);
                Log.e("Error","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=modifierMusique&p1="+DummyContent.getStrSongSelected()+"&p2="+DummyContent.getId()+"&p3="+strTitre+"&p4="+strArtiste+"&p5="+strUrl+"&p6="+strPublique+"&p7="+strActive);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("PUT");
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
            Log.e("FinalResponse",jsonSaved+"");
            finalResponse();
        }
    }
    public void finalResponse()
    {
        try {
            JSONObject lireJSON     = new JSONObject(jsonSaved);
            String strSuccess =lireJSON.get("success").toString();
            if(strSuccess.equals("true"))
            {
                finish();
            }
            else
            {
                Log.e("Final Response","Failure");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
    }
}
