package com.example.moneymate.models;

public class VerifyOtpRequest {
    private String phonenumber;
    private String otp;

    public VerifyOtpRequest(String phonenumber, String otp) {
        this.phonenumber = phonenumber;
        this.otp = otp;
    }

    // Getters
    public String getPhonenumber() {
        return phonenumber;
    }

    public String getOtp() {
        return otp;
    }

    // Setters
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
