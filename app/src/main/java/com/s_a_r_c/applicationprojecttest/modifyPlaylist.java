package com.s_a_r_c.applicationprojecttest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.s_a_r_c.applicationprojecttest.dummy.DummyContent;
import com.s_a_r_c.applicationprojecttest.dummy.FinalContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class modifyPlaylist extends AppCompatActivity {
    String jsonSaved = "";
    String strAvatar = "";
    String strAlias = "";
    String strTicketID = "";
    String strPlaylistID = "";
    String strActive = "";
    String strPublique = "";
    String strNom = "";
    String strCle = "";
    String MDstrNom = "";
    String MDstrPublique = "";
    String MDstrActive = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_playlist);

        Intent intent = getIntent();
        String strMessage = intent.getStringExtra(playListListActivity.EXTRA_MESSAGE);
        strMessage = strMessage+"";
        fillUpActivity();

/*
        if(!strMessage.equals("null")) {
            try {

                JSONObject lireJSON = new JSONObject(strMessage);
                strMotDePasse = lireJSON.get("motdepasse").toString();
                strCourriel = lireJSON.get("courriel").toString();
                strId = lireJSON.get("Id").toString();
                strAvatar = lireJSON.get("avatar").toString();
                strAlias = lireJSON.get("alias").toString();
                strPlaylistID = lireJSON.get("action").toString();
                setTitle(strPlaylistID);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("labo7", e.toString());
            }
            Log.e("strMessage","StrMessage "+strMessage+" StrID:"+strPlaylistID);
            new DownloadPlaylistDetailsJson(null).execute("Useless");
        }*/

    }
    public void fillUpActivity()
    {


        Log.e("FINALCONTENTLISTESELECT",DummyContent.getStrPlaylistSelected()+"");
                FinalContent.PlaylistITEM playlistITEMFinal = new FinalContent.PlaylistITEM("","","","","");
                for(FinalContent.PlaylistITEM playlistITEM : FinalContent.ITEMS)
                {
                    if(playlistITEM.id.equals(DummyContent.getStrPlaylistSelected()))
                    {
                        playlistITEMFinal = playlistITEM;
                    }
                }

                strActive = playlistITEMFinal.active;
                strNom =  playlistITEMFinal.name;
                strPublique =  playlistITEMFinal.publique;
                setTitle("Modification de "+ strNom);


        EditText editText = (EditText)findViewById(R.id.editText14);
        editText.setText(strNom);
        EditText editText2 = (EditText)findViewById(R.id.etPlaylistDuplicate);
        editText2.setText(strNom+"(DUP)");
        if(strActive.equals("true"))
        {
            CheckBox checkBoxPublique = (CheckBox)findViewById(R.id.checkBox3);
            checkBoxPublique.setChecked(true);
        }
        if(strPublique.equals("true"))
        {
            CheckBox checkBoxActive = (CheckBox)findViewById(R.id.checkBox4);
            checkBoxActive.setChecked(true);
        }
    }
    private class DownloadPlaylistDetailsJson extends AsyncTask<String, Void, String> {
        String url;

        public DownloadPlaylistDetailsJson(String url) {

            this.url = url;
        }



        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/afficher/"+strPlaylistID);
                Log.e("URL","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/afficher/"+strPlaylistID);
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
            fillUpActivity();
        }
    }
    public void startModification(View view)
    {
        EditText etEditPlaylistName = (EditText) findViewById(R.id.etPlaylistName);

        if (etEditPlaylistName.getText().toString().trim().equals("")) {
            hideKeyboardShowToast("Nouveau nom de la liste invalide");
        } else {
            new DownloadJsonModifyAttept(null).execute("Useless");
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
                        strString = "";
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString);}
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError",c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
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
            receivedModifyResponse();
        }
    }

    public void startDuplication(View view)
    {
        EditText etPlaylistDuplication = (EditText) findViewById(R.id.etPlaylistDuplicate);

        if (etPlaylistDuplication.getText().toString().trim().equals("")) {
            hideKeyboardShowToast("Nom de la duplication invalide");
        } else {
            new DownloadJsonDuplicationAttept(null).execute("Useless");
        }


    }

    private class DownloadJsonDuplicationAttept extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonDuplicationAttept(String url) {

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
            receivedDuplicationResponse();
        }
    }
    public void receivedDuplicationResponse() {
        Log.e("JSON SAVED", jsonSaved + "Message");

        try {

            JSONObject lireJSON     = new JSONObject(jsonSaved);
            strTicketID =lireJSON.get("idTicket").toString();
            strCle=lireJSON.get("cle").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("labo7",e.toString());
        }

        EditText editText = (EditText)findViewById(R.id.etPlaylistDuplicate);
        MDstrNom = editText.getText().toString();
        new DownloadJsonDuplicationComplete(null).execute("Useless");
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

        EditText editText = (EditText)findViewById(R.id.editText14);
        MDstrNom = editText.getText().toString();
        CheckBox checkBoxPublique = (CheckBox)findViewById(R.id.checkBox3);
        if(checkBoxPublique.isChecked())
        {
            MDstrPublique = "true";

        }
        else
        {
            MDstrPublique = "false";
        }
        CheckBox checkBoxActive = (CheckBox) findViewById(R.id.checkBox4);
        if(checkBoxActive.isChecked())
        {
            MDstrActive = "true";

        }
        else
        {
            MDstrActive = "false";
        }
        new DownloadJsonModifyComplete(null).execute("Useless");
    }
    public void visualsong(View view)
    {
        Intent intent = new Intent(this, visualizeSongsActivity.class);
        startActivity(intent);
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

                MDstrNom = MDstrNom.replaceAll(" ", "%20");
                String strConfirmation = getMd5Hash(DummyContent.getPassword()+strCle);
                //$.put("http://localhost:8080/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" + ticket.idTicket + "&confirmation=" + $.md5(Cookies.get('motdepasse') + ticket.cle) + "&action=afficherListeDeLecture&p1=" + id,
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=modifierListeDeLecture&p1=" + strPlaylistID + "&p2=" + DummyContent.getId() + "&p3=" + MDstrNom + "&p4=" + MDstrPublique + "&p5=" + MDstrActive + "&p6=12/06/2016%2020:05:10");
                Log.e("Error","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=modifierListeDeLecture&p1=" + strPlaylistID + "&p2=" + DummyContent.getId() + "&p3=" + MDstrNom + "&p4=" + MDstrPublique + "&p5=" + MDstrActive + "&p6=12/06/2016%2020:05:10");
                //URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/utilisateur/commande?idTicket="+strTicketID+"&confirmation="+strConfirmation+"&action=modifierUser&p1="+strId+"&p2="+MDstrCourriel+"&p3="+MDstrMotDePasse+"&p4="+MDstrAlias+"&p5="+strAvatar+"&p6=true");
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

    private class DownloadJsonDuplicationComplete extends AsyncTask<String, Void, String> {
        String url;

        public DownloadJsonDuplicationComplete(String url) {

            this.url = url;
        }



        protected String doInBackground(String... url) {

            HttpURLConnection c = null;
            try {
                //$.md5(Cookies.get('motdepasse') + ticket.cle)

                MDstrNom = MDstrNom.replaceAll(" ", "%20");
                String strConfirmation = getMd5Hash(DummyContent.getPassword()+strCle);
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=dupliquerListeDeLecture&p1=" + DummyContent.getId() + "&p2=" +  strPlaylistID+ "&p3=" + MDstrNom);
                Log.e("Error","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=dupliquerListeDeLecture&p1=" + DummyContent.getId() + "&p2=" +  strPlaylistID+ "&p3=" + MDstrNom);

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

    public void startDelete(View view)
    {
        new DownloadJsonDeleteAttept(null).execute("Useless");
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
                        strString = "";
                        InputStreamReader  inputStreamReader1 =new InputStreamReader(c.getErrorStream());
                        BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);
                        StringBuilder stringBuilder1 = new StringBuilder();
                        while ((strString = bufferedReader1.readLine()) != null){stringBuilder1.append(strString);}
                        bufferedReader1.close();
                        Log.e("JsonRetrieveError",c.getResponseMessage());
                        return "{\"success\":\"false\",\"reason\":\"" + stringBuilder1.toString() + "\"}";
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

        EditText editText = (EditText)findViewById(R.id.etPlaylistDuplicate);
        MDstrNom = editText.getText().toString();
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

                MDstrNom = MDstrNom.replaceAll(" ", "%20");
                String strConfirmation = getMd5Hash(DummyContent.getPassword()+strCle);
               // http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=145&confirmation=3f268243b7bb9a831e32b16a9a54e3ff&action=supprimerListe&p1=9&p2=20
                URL u = new URL("http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=supprimerListe&p1=" + DummyContent.getId() + "&p2=" +  strPlaylistID);
                Log.e("Error","http://424t.cgodin.qc.ca:8180/ProjetFinalServices/service/ListeDeLecture/commande?idTicket=" +strTicketID+ "&confirmation=" +strConfirmation+ "&action=dupliquerListeDeLecture&p1=" + DummyContent.getId() + "&p2=" +  strPlaylistID);

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

    private void hideKeyboardShowToast(String strMessage) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Toast toast = Toast.makeText(this, strMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}
