package com.example.myapplication.Database;

public class User {
    private String name;
    private String password;
    private String account;

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return this.name;
    }

    public String getAccount() {
        return this.account;
    }

    public String getPassword() {
        return this.password;
    }
}
