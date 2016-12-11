package com.s_a_r_c.applicationprojecttest.dummy;

/**
 * Created by Computer on 12/9/2016.
 */

public class AvatarContent {
    private static AvatarContent mInstance = null;

    private int id;
    private String avatarB64;
    private String nom;

    public AvatarContent(){

    }

    public int getId() {
        return id;
    }

    public String getAvatarB64() {
        return avatarB64;
    }

    public String getNom() {
        return nom;
    }

    public void setAvatar(int id, String nom, String avatarB64){
        this.id = id;
        this.nom = nom;
        this.avatarB64 = avatarB64;
    }

    public String toString() {
        return nom;
    }
}