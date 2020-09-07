package com.fatec.fincol.model;

public class Collaborator {

    private String user;
    private String account;

    public Collaborator(String user, String account) {
        this.user = user;
        this.account = account;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
