package com.example.moneymate.models;

public class LenderLoginRequest {
    private String private_key;
    private String password;

    public LenderLoginRequest(String private_key, String password) {
        this.private_key = private_key;
        this.password = password;
    }

    // Getters and Setters
    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

