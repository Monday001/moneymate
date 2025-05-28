package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    private boolean success;
    private String message;

    @SerializedName("lender_id")
    private String lenderId;

    @SerializedName("borrower_id") // Add this for borrower ID
    private String borrowerId;

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

    public String getBorrowerId() {  // Getter for borrowerId
        return borrowerId;
    }

    public String getUserType() {
        return userType;
    }

    public String getUsername() {
        return username;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLenderId(String lenderId) {
        this.lenderId = lenderId;
    }

    public void setBorrowerId(String borrowerId) {  // Setter for borrowerId
        this.borrowerId = borrowerId;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
