package com.s_a_r_c.applicationprojecttest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;


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

                UserDatabase userDb = new UserDatabase(getApplicationContext());
                Log.d("xxxxxxxxxxxxxxxxx", jsonSaved);
                userDb.logInUser(Integer.valueOf(lireJSON.get("Id").toString()),
                        lireJSON.get("alias").toString(),
                        lireJSON.get("courriel").toString(),
                        Integer.valueOf(lireJSON.get("avatar").toString()),
                        "",
                        lireJSON.get("motdepasse").toString());

                //show his username
                Map<String, String> userInfos = userDb.retournerInfosUser();
                Log.d("alias of the dude", userInfos.get("alias"));


                Intent intent = new Intent();
                intent.putExtra("jsonSavedTransfer",jsonSaved);
                setResult(RESULT_OK, intent);
                finish();
            }
            else
            {
                TextView mTextView = (TextView) findViewById(R.id.textView4);
                mTextView.setText(lireJSON.get("reason").toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }

    }
    /** Called when the user clicks the Login button */
    public void sendConfirmCaptcha(View view)
    {
        hideKeyboard();

        TextView tvCaptcha = (TextView) findViewById(R.id.tvCaptcha);

        if (tvCaptcha != null && tvCaptcha.getText().toString().trim().length() != 6) {
            Snackbar.make(findViewById(android.R.id.content),"Veuillez entrer un captcha complet", Snackbar.LENGTH_SHORT).show();
        } else {
            Log.e("Captcha Attempt","confirm");

            strCaptcha = findViewById(R.id.textView4).toString();
            EditText mTextView = (EditText)findViewById(R.id.tvCaptcha);
            strCaptcha = mTextView.getText().toString();
            new DownloadJsonCaptchaAttempt(null).execute("Useless");
        }

    }
    public void sendLoginAttempt(View view) {
        hideKeyboard();

        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvPassword = (TextView) findViewById(R.id.tvPassword);

        if(tvUsername != null && tvUsername.getText().toString().trim().equals(""))
        {
            Snackbar.make(findViewById(android.R.id.content),"Veuillez entrer un nom d'utilisateur !", Snackbar.LENGTH_SHORT).show();
        } else if (tvPassword != null && tvPassword.getText().toString().trim().equals("")) {
            Snackbar.make(findViewById(android.R.id.content),"Veuillez entrer un mot de passe", Snackbar.LENGTH_SHORT).show();
        } else {
            Log.e("Login Attempt","Username");
            new DownloadJsonLoginAttempt(null).execute("Useless");
        }
    }
    public void confirmLogin()
    {
        try {
            JSONObject lireJSON     = new JSONObject(jsonSaved);

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
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString+"\n");}
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError",c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":" + stringBuilder1.toString() + "}";
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

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
