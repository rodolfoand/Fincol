package com.fatec.fincol.model;

public class User {
    private String uid;
    private String name;
    private String email;
    private String profileImage;

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public User(String uid, String name, String email, String profileImage) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
