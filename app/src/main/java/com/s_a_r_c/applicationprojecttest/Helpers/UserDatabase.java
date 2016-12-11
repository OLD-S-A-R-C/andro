

package com.s_a_r_c.applicationprojecttest.Helpers;

import android.content.ContentValues;
import android.content.Context;
import java.util.HashMap;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.s_a_r_c.applicationprojecttest.dummy.AvatarContent;

public class UserDatabase extends SQLiteOpenHelper {

    private static UserDatabase mInstance = null;

    public static final String USER_ID = "id";
    public static final String USER_ALIAS = "alias";
    public static final String USER_EMAIL = "courriel";
    public static final String USER_AVATAR_ID = "avatar_id";
    public static final String USER_AVATAR_B64 = "avatar_b64";
    public static final String USER_MD5_PASSWORD = "motdepasse";

    private UserDatabase(Context context) {
        super(context, "projet_final", null, 1);
        this.reset();
    }

    public static UserDatabase getInstance(Context context){
        if(mInstance == null)
        {
            mInstance = new UserDatabase(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlite_requete_table_login = "CREATE TABLE USER_INFOS ("
                + USER_ID + " INTEGER PRIMARY KEY,"
                + USER_ALIAS + " TEXT,"
                + USER_EMAIL + " TEXT UNIQUE,"
                + USER_AVATAR_ID + " INTEGER,"
                + USER_AVATAR_B64 + " TEXT,"
                + USER_MD5_PASSWORD + " TEXT" + ")";
        db.execSQL(sqlite_requete_table_login);
        //db.close();
    }

    public void logInUser(int idUser, String alias, String email, int idAvatar, String avatarB64, String motdepasse) {
        this.reset();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, idUser);
        values.put(USER_ALIAS, alias);
        values.put(USER_EMAIL, email);
        values.put(USER_AVATAR_ID, idAvatar);
        values.put(USER_AVATAR_B64, avatarB64);
        values.put(USER_MD5_PASSWORD, motdepasse);

        db.insert("USER_INFOS", null, values);
        //db.close();
    }

    public HashMap retournerInfosUser(){
        HashMap utilisateur = new HashMap();
        String requete = "SELECT * FROM USER_INFOS";

        SQLiteDatabase bd = this.getReadableDatabase();
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();

        if(cursor.getCount() > 0){
            utilisateur.put(USER_ID, cursor.getString(0));
            utilisateur.put(USER_ALIAS, cursor.getString(1));
            utilisateur.put(USER_EMAIL, cursor.getString(2));
            utilisateur.put(USER_AVATAR_ID, cursor.getString(3));
            utilisateur.put(USER_AVATAR_B64, cursor.getString(4));
            utilisateur.put(USER_MD5_PASSWORD, cursor.getString(5));
        }
        cursor.close();
        //bd.close();

        return utilisateur;
    }

    public String retournerID()
    {
        return USER_ID;
    }

    public int emptyOrNot() {
        String countQuery = "SELECT  * FROM USER_INFOS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();

        cursor.close();
        //db.close();

        return rowCount;
    }

    public Boolean loggedIn() {
        if (emptyOrNot() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void reset(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("USER_INFOS", null, null);
        //db.close();
    }

    public void logOut() {
        reset();
    }

    public void updateAvatar(String avatarB64) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_AVATAR_B64, avatarB64);

        db.update("USER_INFOS", values, USER_ID + "=" + this.retournerInfosUser().get(USER_ID), null);
        //db.close();
    }

    public void updateEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, email);

        db.update("USER_INFOS", values, USER_ID + "=" + this.retournerInfosUser().get(USER_ID), null);
        //db.close();
    }

    public void updateAfterEdit(String alias, String email, String password, String avatarID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ALIAS, alias);
        values.put(USER_EMAIL, email);
        values.put(USER_MD5_PASSWORD, email);
        values.put(USER_AVATAR_ID, email);

        db.update("USER_INFOS", values, USER_ID + "=" + this.retournerInfosUser().get(USER_ID), null);
        //db.close();
    }

    //obligatoire sinon crash
    @Override
    public void onUpgrade(SQLiteDatabase db, int intOld, int intNew) {
        db.execSQL("DROP TABLE IF EXISTS USER_INFOS");
        onCreate(db);
    }

}

