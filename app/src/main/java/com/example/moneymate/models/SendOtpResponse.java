package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

public class SendOtpResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("otp")
    private String otp; // optional, if your API sends back the OTP (for dev testing)

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getOtp() {
        return otp;
    }
}

