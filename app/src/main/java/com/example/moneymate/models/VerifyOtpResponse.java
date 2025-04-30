package com.example.moneymate.models;

public class VerifyOtpResponse {
    private boolean success;
    private String message;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
