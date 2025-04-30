package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    private boolean success;
    private String message;

    @SerializedName("lender_id")
    private String lenderId;

    @SerializedName("user_type")
    private String userType;

    private String username;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getLenderId() {
        return lenderId;
    }

    public String getUserType() {
        return userType;
    }

    public String getUsername() {
        return username;
    }
}
