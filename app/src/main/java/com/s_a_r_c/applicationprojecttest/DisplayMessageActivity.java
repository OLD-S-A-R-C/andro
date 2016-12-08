package com.s_a_r_c.applicationprojecttest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;




import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DisplayMessageActivity extends AppCompatActivity {

    String jsonSaved = "Message Not Received";
    String strCaptcha = "";
    String strTicketID = "";
    String strSuccess = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
    }
public void confirmSuccesffulLoginAttempt()
    {
Log.e("confirmSuccesffulLogin",jsonSaved+"Message");
        try {
            JSONObject lireJSON     = new JSONObject(jsonSaved);
            strSuccess =lireJSON.get("success").toString();
            if(strSuccess.equals("true"))
            {
                DummyContent dummyContent = new DummyContent();

                dummyContent.connectUser(lireJSON.get("alias").toString(), lireJSON.get("courriel").toString(), lireJSON.get("motdepasse").toString(), lireJSON.get("Id").toString() );
/*
                UserDatabase userDb = new UserDatabase(getApplicationContext());
                Log.d("xxxxxxxxxxxxxxxxx", jsonSaved);
                userDb.ajouterUtilisateur(Integer.valueOf(lireJSON.get("Id").toString()),
                        lireJSON.get("alias").toString(),
                        lireJSON.get("courriel").toString(),
                        Integer.valueOf(lireJSON.get("avatar").toString()),
                        "",
                        lireJSON.get("motdepasse").toString());
*/

                Intent intent = new Intent();
                intent.putExtra("jsonSavedTransfer",jsonSaved);
                setResult(RESULT_OK, intent);
                finish();
            }
            else
            {
                TextView mTextView = (TextView) findViewById(R.id.textView4);
                mTextView.setText("Captcha: (Le captcha est erroné !)");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }

    }
    /** Called when the user clicks the Login button */
    public void sendConfirmCaptcha(View view)
    {
        Log.e("Captcha Attempt","confirm");

        strCaptcha = findViewById(R.id.textView4).toString();
        EditText mTextView = (EditText)findViewById(R.id.editText3);
        strCaptcha = mTextView.getText().toString();
        new DownloadJsonCaptchaAttempt(null).execute("Useless");

    }
    public void sendLoginAttempt(View view) {
        // Do something in response to button
        Log.e("Login Attempt","Username");
        new DownloadJsonLoginAttempt(null).execute("Useless");
    }
    public void confirmLogin()
    {
        try {

            JSONObject lireJSON     = new JSONObject(jsonSaved);
            //JSONObject jsonMovie = new JSONObject();
            String strCaptcha =lireJSON.get("captcha").toString();
            strTicketID =lireJSON.get("idTicket").toString();
            byte[] decodedString = Base64.decode(strCaptcha, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView img= (ImageView) findViewById(R.id.imageView2);
            img.setImageBitmap(decodedByte);
            img.requestLayout();
            img.getLayoutParams().height = 200;
            img.getLayoutParams().width = 200;
            img.requestLayout();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }
        ////////////////////////////////////////////////////////////////////////
    }


    private class DownloadJsonLoginAttempt extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonLoginAttempt(String url) {

            this.url = url;
        }



        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                  URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/connexion?courriel=groy@groy.com&mdp=0fe9a1b70ea556dc15ee1d152e424ee8");
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
            confirmLogin();
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
}
