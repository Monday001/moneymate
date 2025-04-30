package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

public class SendOtpRequest {

    @SerializedName("phonenumber") // must match what PHP expects
    private String phoneNumber;

    @SerializedName("email")
    private String email;

    public SendOtpRequest(String phoneNumber, String email) {
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
