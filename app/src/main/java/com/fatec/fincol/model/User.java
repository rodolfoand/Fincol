package com.fatec.fincol.model;

public class User {
    private String uid;
    private String Name;

    public User(String uid, String name) {
        this.uid = uid;
        Name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
