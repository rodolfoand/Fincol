package com.fatec.fincol.model;

public class AccountVersion2 {

    private String id;
    private String name;
    private String accountImage;
    private Double balance;

    public AccountVersion2() {
    }

    public AccountVersion2(String name) {
        this.name = name;
    }

    public AccountVersion2(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public AccountVersion2(String id, String name, String accountImage, Double balance) {
        this.id = id;
        this.name = name;
        this.accountImage = accountImage;
        this.balance = balance;
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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
