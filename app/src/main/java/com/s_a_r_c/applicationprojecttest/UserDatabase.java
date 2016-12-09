

    package com.s_a_r_c.applicationprojecttest;

    import android.content.ContentValues;
    import android.content.Context;
    import java.util.HashMap;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    public class UserDatabase extends SQLiteOpenHelper {

        public static final String USER_ID = "id";
        public static final String USER_ALIAS = "alias";
        public static final String USER_EMAIL = "courriel";
        public static final String USER_AVATAR_ID = "avatar_id";
        public static final String USER_AVATAR_B64 = "avatar_b64";
        public static final String USER_MD5_PASSWORD = "motdepasse";

        public UserDatabase(Context context) {
            super(context, "projet_final", null, 1);
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
            db.close();
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
            bd.close();

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
            db.close();
            cursor.close();

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
            db.close();
        }

        public void logOut() {
            reset();
        }

        //obligatoire sinon crash
        @Override
        public void onUpgrade(SQLiteDatabase db, int intOld, int intNew) {
            db.execSQL("DROP TABLE IF EXISTS USER_INFOS");
            onCreate(db);
        }

    }

