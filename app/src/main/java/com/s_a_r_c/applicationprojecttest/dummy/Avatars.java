package com.s_a_r_c.applicationprojecttest.dummy;

import java.util.HashMap;

/**
 * Created by Computer on 12/9/2016.
 */

public class Avatars {
    private static Avatars mInstance = null;

    private HashMap<String, AvatarContent> lstAvatars;

    private Avatars(){
        lstAvatars = new HashMap<String, AvatarContent>();
    }

    public static Avatars getInstance(){
        if(mInstance == null)
        {
            mInstance = new Avatars();
        }
        return mInstance;
    }

    public void addAvatar(AvatarContent avatar) {
        lstAvatars.put(avatar.getNom(), avatar);
    }

    public HashMap<String, AvatarContent> getListAvatars() {
        return lstAvatars;
    }
}