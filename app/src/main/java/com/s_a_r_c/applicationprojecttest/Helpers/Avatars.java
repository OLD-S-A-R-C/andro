package com.s_a_r_c.applicationprojecttest.Helpers;

import com.s_a_r_c.applicationprojecttest.dummy.AvatarContent;

import java.util.HashMap;

/**
 * Created by Computer on 12/9/2016.
 */

public class Avatars {
    private static Avatars mInstance = null;

    private HashMap<String, AvatarContent> lstAvatars;
    private HashMap<Integer, AvatarContent> lstAvatarsById;

    private Avatars(){
        lstAvatars = new HashMap<String, AvatarContent>();
        lstAvatarsById = new HashMap<Integer, AvatarContent>();
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
        lstAvatarsById.put(avatar.getId(), avatar);
    }

    public HashMap<String, AvatarContent> getListAvatars() {
        return lstAvatars;
    }

    public HashMap<Integer, AvatarContent> getListAvatarsById() {
        return lstAvatarsById;
    }
}