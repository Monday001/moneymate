package com.example.moneymate.models;

public class LenderIdResponse {
    private boolean success;
    private int lenderId;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public int getLenderId() {
        return lenderId;
    }

    public String getMessage() {
        return message;
    }
}
