package com.example.moneymate.models;

public class ResetPasswordRequest {
    private String phonenumber;
    private String password;

    public ResetPasswordRequest(String phonenumber, String password) {
        this.phonenumber = phonenumber;
        this.password = password;
    }
}

