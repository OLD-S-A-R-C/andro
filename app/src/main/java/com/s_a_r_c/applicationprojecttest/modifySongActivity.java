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
    String strTargetPlaylist = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_song);

        ArrayList<String> playlists = new ArrayList<String>();
        for (DummyContent.DummyItem item1 : DummyContent.ITEMS) {

            if (!item1.details.equals(DummyContent.getStrPlaylistSelected())) {
                if (item1.owner.equals(DummyContent.getId()))
                    playlists.add(item1.content + " ;" + item1.details);
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
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/afficher/" + DummyContent.getStrSongSelected());
                c = (HttpURLConnection) u.openConnection();
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
                        Log.e("JsonRetrieveError", "Status 400");
                        return null;
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
            // bmImage.setImageBitmap(result);
            jsonSaved = result;
            renderJson();
        }
    }

    public void renderJson() {
        try {

            JSONObject lireJSON = new JSONObject(jsonSaved);
            String strProprietaire = lireJSON.get("proprietaire").toString();
            JSONObject lireJSON2 = new JSONObject(strProprietaire);
            strOwnerID = lireJSON2.get("id").toString();
            strVignette = lireJSON.get("vignette").toString();
            strTitre = lireJSON.get("titre").toString();
            strMusique = lireJSON.get("musique").toString();
            strPublique = lireJSON.get("publique").toString();
            strActive = lireJSON.get("active").toString();
            strArtiste = lireJSON.get("artiste").toString();

            setTitle(strTitre + " - " + strArtiste);

            EditText editText12 = (EditText) findViewById(R.id.editText12);
            editText12.setText(strTitre);
            EditText editText15 = (EditText) findViewById(R.id.editText15);
            editText15.setText(strArtiste);
            EditText editText16 = (EditText) findViewById(R.id.editText16);
            editText16.setText(strMusique);
            CheckBox checkBoxPublique = (CheckBox) findViewById(R.id.checkBox5);
            CheckBox checkBoxActive = (CheckBox) findViewById(R.id.checkBox6);
            if (strPublique.equals("true")) {
                checkBoxPublique.setChecked(true);
            }
            if (strActive.equals("true")) {
                checkBoxActive.setChecked(true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }
    }

    public void deleteSong(View view) {

            new DownloadJsonDeleteAttept(null).execute("Useless");
    }

    public void transferSong(View view) {
        new DownloadJsonTransferAttept(null).execute("Useless");
    }

    public void copySong(View view) {
        new DownloadJsonCopyAttept(null).execute("Useless");
    }


    public void modifySong(View view) {
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
                        Log.e("JsonRetrieveError", "Status 400");
                        return null;
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
            receivedModifyResponse();
        }
    }

    public void receivedModifyResponse() {
        Log.e("JSON SAVED", jsonSaved + "Message");

        try {

            JSONObject lireJSON = new JSONObject(jsonSaved);
            strTicketID = lireJSON.get("idTicket").toString();
            strCle = lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }

        EditText editText = (EditText) findViewById(R.id.editText12);
        strTitre = editText.getText().toString();
        EditText editText8 = (EditText) findViewById(R.id.editText15);
        strArtiste = editText8.getText().toString();
        EditText editText9 = (EditText) findViewById(R.id.editText16);
        strUrl = editText9.getText().toString();
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox5);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox6);
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


        new DownloadJsonModifyComplete(null).execute("Useless");
    }

    private class DownloadJsonModifyComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonModifyComplete(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                //$.md5(Cookies.get('motdepasse') + ticket.cle)

                strTitre = strTitre.replaceAll(" ", "%20");
                strArtiste = strArtiste.replaceAll(" ", "%20");

                String strConfirmation = getMd5Hash(DummyContent.getPassword() + strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=211&confirmation=f006434e24b278ae1f935331e13810b1&action=modifierMusique&p1=134&p2=9&p3=TestTitre1&p4=TestArtiste1&p5=https://www.youtube.com/watch?v=Pw-0pbY9JeU&p6=true&p7=true
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=modifierMusique&p1=" + DummyContent.getStrSongSelected() + "&p2=" + DummyContent.getId() + "&p3=" + strTitre + "&p4=" + strArtiste + "&p5=" + strUrl + "&p6=" + strPublique + "&p7=" + strActive);
                Log.e("Error", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=modifierMusique&p1=" + DummyContent.getStrSongSelected() + "&p2=" + DummyContent.getId() + "&p3=" + strTitre + "&p4=" + strArtiste + "&p5=" + strUrl + "&p6=" + strPublique + "&p7=" + strActive);
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
                        Log.e("JsonRetrieveError", "Status 400");
                        return null;
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
            finalResponse();
        }
    }

    public void finalResponse() {
        try {
            JSONObject lireJSON = new JSONObject(jsonSaved);
            String strSuccess = lireJSON.get("success").toString();
            if (strSuccess.equals("true")) {
                finish();
            } else {
                Log.e("Final Response", "Failure");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }
    }

    private class DownloadJsonCopyAttept extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonCopyAttept(String url) {

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
                        Log.e("JsonRetrieveError", "Status 400");
                        return null;
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
            receivedCopyResponse();
        }
    }

    public void receivedCopyResponse() {
        try {

            JSONObject lireJSON = new JSONObject(jsonSaved);
            strTicketID = lireJSON.get("idTicket").toString();
            strCle = lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }
        String strTargetPlaylistFull = ((Spinner) findViewById(R.id.spinner2)).getSelectedItem().toString();
        strTargetPlaylist = strTargetPlaylistFull.split(";")[1];
        new DownloadJsonCopyComplete(null).execute("Useless");
    }


    private class DownloadJsonCopyComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonCopyComplete(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                //$.md5(Cookies.get('motdepasse') + ticket.cle)

                strTitre = strTitre.replaceAll(" ", "%20");
                strArtiste = strArtiste.replaceAll(" ", "%20");

                String strConfirmation = getMd5Hash(DummyContent.getPassword() + strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=216&confirmation=1c3de967358bc9cd76a2c2f61a57c3dd&action=ajouterMusiqueListe&p1=9&p2=14&p3=135
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=ajouterMusiqueListe&p1=" + DummyContent.getId() + "&p2=" + strTargetPlaylist + "&p3=" + DummyContent.getStrSongSelected());
                Log.e("Error", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=ajouterMusiqueListe&p1=" + DummyContent.getId() + "&p2=" + strTargetPlaylist + "&p3=" + DummyContent.getStrSongSelected());
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
                        Log.e("JsonRetrieveError", "Status 400");
                        return null;
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

            finalResponse();
        }
    }

    private class DownloadJsonTransferAttept extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonTransferAttept(String url) {

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
                        Log.e("JsonRetrieveError", "Status 400");
                        return null;
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
            receivedTransferResponse();
        }
    }

    public void receivedTransferResponse() {
        try {

            JSONObject lireJSON = new JSONObject(jsonSaved);
            strTicketID = lireJSON.get("idTicket").toString();
            strCle = lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }
        String strTargetPlaylistFull = ((Spinner) findViewById(R.id.spinner1)).getSelectedItem().toString();
        strTargetPlaylist = strTargetPlaylistFull.split(";")[1];
        new DownloadJsonTransferComplete(null).execute("Useless");
    }


    private class DownloadJsonTransferComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonTransferComplete(String url) {

            this.url = url;
        }


        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                //$.md5(Cookies.get('motdepasse') + ticket.cle)

                strTitre = strTitre.replaceAll(" ", "%20");
                strArtiste = strArtiste.replaceAll(" ", "%20");

                String strConfirmation = getMd5Hash(DummyContent.getPassword() + strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=216&confirmation=1c3de967358bc9cd76a2c2f61a57c3dd&action=ajouterMusiqueListe&p1=9&p2=14&p3=135
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=transfererMusiqueListe&p1=" + DummyContent.getId() + "&p2=" + DummyContent.getStrPlaylistSelected() + "&p3=" + strTargetPlaylist + "&p4=" + DummyContent.getStrSongSelected());
                Log.e("Error", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=transfererMusiqueListe&p1=" + DummyContent.getId() + "&p2=" + DummyContent.getStrPlaylistSelected() + "&p3=" + strTargetPlaylist + "&p4=" + DummyContent.getStrSongSelected());
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
                        Log.e("JsonRetrieveError", "Status 400");
                        return null;
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

            finalResponse();
        }
    }
    ////////////////////////////////////////////////////////////

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
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString+"\n");}
                        bufferedReader1.close();
                        Log.e("bbbbbbbbbbbbbbbbbbbbbbb", stringBuilder1.toString());
                        return null;
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
            Log.e("strTicketID",strTicketID+" ticket");
            strCle = lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7", e.toString());
        }
        String strTargetPlaylistFull = ((Spinner) findViewById(R.id.spinner2)).getSelectedItem().toString();
        strTargetPlaylist = strTargetPlaylistFull.split(";")[1];
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

                String strConfirmation = getMd5Hash(DummyContent.getPassword() + strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=337&confirmation=26b15445192a07d528d0e70c2c58264d&action=supprimerMusiqueListe&p1=9&p2=16&p3=148
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=supprimerMusiqueListe&p1=" + DummyContent.getId() + "&p2=" + DummyContent.getStrPlaylistSelected() + "&p3=" + DummyContent.getStrSongSelected());
                Log.e("Error", "http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLectureMusique/commande?idTicket=" + strTicketID + "&confirmation=" + strConfirmation + "&action=supprimerMusiqueListe&p1=" + DummyContent.getId() + "&p2=" + DummyContent.getStrPlaylistSelected() + "&p3=" + DummyContent.getStrSongSelected());
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
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString+"\n");}
                        bufferedReader1.close();
                        Log.e("bbbbbbbbbbbbbbbbbbbbbbb", stringBuilder1.toString());
                        return null;
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

            finalResponse();
        }
    }
}