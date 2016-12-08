package com.s_a_r_c.applicationprojecttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class createNewPlaylist extends AppCompatActivity {
    String jsonSaved = "";
    String strMotDePasse = "";
    String strCourriel = "";
    String strId = "";
    String strAvatar = "";
    String strAlias = "";
    String strTicketID = "";
    String strCle = "";
    String strPublic = "";
    String strActif = "";
    String strPlaylistNom = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_playlist);
        Intent intent = getIntent();
        String strMessage = intent.getStringExtra(playListListActivity.EXTRA_MESSAGE);

        try {

            JSONObject lireJSON     = new JSONObject(strMessage);
            strMotDePasse = lireJSON.get("motdepasse").toString();
            strCourriel = lireJSON.get("courriel").toString();
            strId = lireJSON.get("Id").toString();
            strAvatar = lireJSON.get("avatar").toString();
            strAlias = lireJSON.get("alias").toString();
            setTitle("Modification de "+strAlias);
            Log.e("Logged in as:",strCourriel+" 1");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
    }

    public void receivedModifyResponse()
    {
        Log.e("JSON SAVED",jsonSaved+"Message");
        EditText editText = (EditText)findViewById(R.id.editText11);
        strPlaylistNom = editText.getText().toString();
        strPlaylistNom = strPlaylistNom.replaceAll(" ", "%20");

        CheckBox checkBox1 = (CheckBox)findViewById(R.id.checkBox);
        CheckBox checkBox2 = (CheckBox)findViewById(R.id.checkBox2);

        if(checkBox1.isChecked() == true)
        {strPublic = "true";}
        else
        {strPublic = "false";}

        if(checkBox2.isChecked() == true)
        {strActif = "true";}
        else
        {strActif = "false";}

        try {

            JSONObject lireJSON     = new JSONObject(jsonSaved);
            strTicketID =lireJSON.get("idTicket").toString();
            strCle=lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
        new DownloadJsonModifyComplete(null).execute("Useless");
    }




    public void confirmPlaylistCreation(View view)
    {
        new DownloadJsonModifyAttept(null).execute("Useless");
    }

    private class DownloadJsonModifyAttept extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonModifyAttept(String url) {

            this.url = url;
        }



        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/"+strCourriel);
                Log.e("URL","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/"+strCourriel);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
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
            jsonSaved = result;
            receivedModifyResponse();
        }
    }

    private class DownloadJsonModifyComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonModifyComplete(String url) {

            this.url = url;
        }



        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                String strConfirmation = getMd5Hash(strMotDePasse+strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=26&confirmation=75d1b2cd84e5b34ffc3761ea5c6527b2&action=nouvelleListeDeLecture&p1=9&p2=Jamaica&p3=true&p4=true&p5=12/06/2016 20:05:10
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket="+strTicketID+"&confirmation="+strConfirmation+"&action=nouvelleListeDeLecture&p1="+strId+"&p2="+strPlaylistNom+"&p3="+strPublic+"&p4="+strActif+"&p5=12/06/2016%2020:05:10");
                Log.e("Json","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande? idTicket="+strTicketID+"&confirmation="+strConfirmation+"&action=nouvelleListeDeLecture&p1="+strId+"&p2="+strPlaylistNom+"&p3="+strPublic+"&p4="+strActif+"&p5=12/06/2016%2020:05:10");
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("PUT");
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
            jsonSaved = result;
            Log.e("FinalResponse",jsonSaved+" 1");
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

}
