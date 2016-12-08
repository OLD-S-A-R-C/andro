package com.s_a_r_c.applicationprojecttest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ModifyUserActivity extends AppCompatActivity {
    String jsonSaved = "";
    String strMotDePasse = "";
    String strCourriel = "";
    String strId = "";
    String strAvatar = "";
    String strAlias = "";
    String strTicketID = "";
    String strCle = "";
    String MDstrMotDePasse = "";
    String MDstrCourriel = "";
    String MDstrId = "";
    String MDstrAvatar = "";
    String MDstrAlias = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

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
            EditText editText = (EditText) findViewById(R.id.editText4);
            editText.setText(strAlias);
            EditText editText2 = (EditText) findViewById(R.id.editText5);
            editText2.setText(strCourriel);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
    }

    public void sendModifyLogin(View view)
    {
        new DownloadJsonModifyAttept(null).execute("Useless");
        EditText EDalias = (EditText)findViewById(R.id.editText4);
        EditText EDcourriel = (EditText)findViewById(R.id.editText5);
        EditText EDpwd = (EditText)findViewById(R.id.editText6);

        MDstrAlias=EDalias.getText().toString();
        MDstrCourriel=EDcourriel.getText().toString();
        MDstrMotDePasse=EDpwd.getText().toString();
        if(MDstrMotDePasse.equals(""))
        {
            MDstrMotDePasse=strMotDePasse;
        }
        else
        {
            MDstrMotDePasse = getMd5Hash(MDstrMotDePasse);
        }
    }

    public void receivedModifyResponse()
    {
        Log.e("JSON SAVED",jsonSaved+"Message");



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
            //confirmLogin();
            receivedModifyResponse();
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

    private class DownloadJsonModifyComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonModifyComplete(String url) {

            this.url = url;
        }



        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                //$.md5(Cookies.get('motdepasse') + ticket.cle)


                String strConfirmation = getMd5Hash(strMotDePasse+strCle);
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/commande?idTicket="+strTicketID+"&confirmation="+strConfirmation+"&action=modifierUser&p1="+strId+"&p2="+MDstrCourriel+"&p3="+MDstrMotDePasse+"&p4="+MDstrAlias+"&p5="+strAvatar+"&p6=true");
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
            //confirmLogin();
            //receivedModifyResponse();
            Log.e("FinalResponse",jsonSaved);
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
