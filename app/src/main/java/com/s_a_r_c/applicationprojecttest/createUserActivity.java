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

public class createUserActivity extends AppCompatActivity {

String jsonSaved = "";
    String strTicketID = "";
    String strAlias = "";
    String strPassword = "";
    String strEmail = "";
    String strCaptcha = "";
    String strSuccess = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

    public void confirmSuccesffulLoginAttempt()
    {
        Log.e("confirmSuccesffulLogin",jsonSaved+"Message");
        try {
            JSONObject lireJSON     = new JSONObject(jsonSaved);
            strSuccess =lireJSON.get("success").toString();
            if(strSuccess.equals("true"))
            {
                finish();
            }
            else
            {
                TextView mTextView = (TextView) findViewById(R.id.textView11);
                mTextView.setText("Captcha: (Le captcha est erroné !)");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }

    }

    public void sendCreateUserRequest(View view)
    {
        EditText editText1 = (EditText)findViewById(R.id.editText7);
        EditText editText2 = (EditText)findViewById(R.id.editText8);
        EditText editText3 = (EditText)findViewById(R.id.editText9);
        strAlias = editText1.getText().toString();
        strEmail= editText2.getText().toString();
        strPassword = editText3.getText().toString();
        new DownloadJsonCreatenAttempt(null).execute("Useless");
    }
    public void confirmRequest()
    {
        Log.e("CreateUserRequest",jsonSaved+"message");
        try {

            JSONObject lireJSON     = new JSONObject(jsonSaved);
            //JSONObject jsonMovie = new JSONObject();
            String strCaptcha =lireJSON.get("captcha").toString();
            strTicketID =lireJSON.get("idTicket").toString();
            byte[] decodedString = Base64.decode(strCaptcha, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView img= (ImageView) findViewById(R.id.imageView3);
            img.setImageBitmap(decodedByte);
            img.requestLayout();
            img.getLayoutParams().height = 200;
            img.getLayoutParams().width = 200;
            img.requestLayout();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
    }

    public void confirmUserCreation(View view)
    {
        EditText editText4 = (EditText)findViewById(R.id.editText10);
        strCaptcha = editText4.getText().toString();
        new DownloadJsonCaptchaAttempt(null).execute("Useless");
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

    private class DownloadJsonCaptchaAttempt extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonCaptchaAttempt(String url) {

            this.url = url;
        }



        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {

                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/verifCaptcha?idTicket="+strTicketID+"&captcha="+strCaptcha);
                Log.e("strCaptchaAttempt","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/verifCaptcha?idTicket="+strTicketID+"&captcha="+strCaptcha);
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
            Log.e("onPostExecute",result+"Message");
            jsonSaved = result;
            confirmSuccesffulLoginAttempt();
        }
    }

    private class DownloadJsonCreatenAttempt extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonCreatenAttempt(String url) {

            this.url = url;
        }



        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/creer?alias="+strAlias+"&courriel="+strEmail+"&mdp="+getMd5Hash(strPassword)+"&actif=true&date=12/04/2016%2017:19:11&idAvatar=1");
              //  URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/creer?alias=Alias&courriel=Courriel@courriel.com&mdp=5f4dcc3b5aa765d61d8327deb882cf99&actif=true&date=12/04/2016%2016:19:11&idAvatar=1");
                Log.e("SendingJson","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/creer?alias="+strAlias+"&courriel="+strEmail+"&mdp="+getMd5Hash(strPassword)+"&actif=true&date=12/04/2016 17:19:11&idAvatar=1");
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
            confirmRequest();
        }
    }
}
