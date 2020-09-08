package com.fatec.fincol.model;

public class AccountVersion2 {

    private String id;
    private String name;
    private String accountImage;

    public AccountVersion2(String name) {
        this.name = name;
    }

    public AccountVersion2(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public AccountVersion2(String id, String name, String accountImage) {
        this.id = id;
        this.name = name;
        this.accountImage = accountImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountImage() {
        return accountImage;
    }

    public void setAccountImage(String accountImage) {
        this.accountImage = accountImage;
    }

    @Override
    public String toString() {
        return "AccountVersion2{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", accountImage='" + accountImage + '\'' +
                '}';
    }
}
