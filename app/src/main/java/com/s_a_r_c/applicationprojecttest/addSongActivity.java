package com.s_a_r_c.applicationprojecttest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        EditText editText = (EditText)findViewById(R.id.etURLNewMusic);
        editText.setText("https://www.youtube.com/watch?v=Pw-0pbY9JeU");
    }

    public void createSong(View view)
    {
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
            new DownloadJsonDeleteAttept(null).execute("Useless");
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

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/getTicket/"+DummyContent.getCourriel());
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
                        strString = "";
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString);}
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError",c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
                    case 500:
                        return "{\"success\":\"false\",\"reason\":\"" + "Une erreur est survenue !" + "\"}";
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

        EditText editText = (EditText)findViewById(R.id.etTitleNewMusic);
        strTitre = editText.getText().toString();
        EditText editText8 = (EditText)findViewById(R.id.etArtistNewMusic);
        strArtiste = editText8.getText().toString();
        EditText editText9 = (EditText)findViewById(R.id.etURLNewMusic);
        strUrl = editText9.getText().toString().replace(" ", "%20");
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox7);
        CheckBox checkBox2 = (CheckBox)findViewById(R.id.checkBox8);
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

                String strConfirmation = DummyContent.encryptMD5(DummyContent.getPassword()+strCle);
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=184&confirmation=99fee649e6b6e7680a094d49cec71961&action=nouvelleMusique&p1=9&p2=testestTitre&p3=testtestArtiste&p4=testTestURL&p5=true&p6=true
                //http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=181&confirmation=cc8e336a813dff682d64349fa4374724&action=nouvelleMusique&p1=9&p2=TestTitre&p3=TestArtiste&p4=https://www.youtube.com/watch?v=Pw-0pbY9JeU&p5=true&p6=true
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=nouvelleMusique&p1="+DummyContent.getId()+"&p2="+strTitre+"&p3="+strArtiste+"&p4="+strUrl.replace(" ", "%20")+"&p5="+strPublique+"&p6="+strActive);
                Log.e("Error","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/Musique/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=nouvelleMusique&p1="+DummyContent.getId()+"&p2="+strTitre+"&p3="+strArtiste+"&p4="+strUrl.replace(" ", "%20")+"&p5="+strPublique+"&p6="+strActive);

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
                        strString = "";
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString);}
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError",c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
                    case 500:
                        return "{\"success\":\"false\",\"reason\":\"" + "Une erreur est survenue !" + "\"}";
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
            if (result != null || jsonSaved != null) {
                finalResponse();
            }

        }
    }
    public void finalResponse()
    {
        try {
            JSONObject lireJSON     = new JSONObject(jsonSaved);
            String strSuccess =lireJSON.get("success").toString();
            if(strSuccess.equals("true"))
            {
                hideKeyboardShowToast(lireJSON.get("reason").toString());
                finish();
            }
            else
            {
                hideKeyboardShowToast(lireJSON.get("reason").toString());
                Log.e("Final Response","Failure");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
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

}
