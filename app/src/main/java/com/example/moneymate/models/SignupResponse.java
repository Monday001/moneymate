package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {
    private boolean success;
    private String message;

    @SerializedName("lender_id")
    private int lenderId;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getLenderId() {
        return lenderId;
    }
}
