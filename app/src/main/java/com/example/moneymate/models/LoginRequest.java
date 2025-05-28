package com.example.moneymate.models;

public class LoginRequest {

    private String username;
    private String password;
    private String private_key;

    public LoginRequest(String username, String password, String private_key) {
        this.username = username;
        this.password = password;
        this.private_key = private_key;
    }
}
