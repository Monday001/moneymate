package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BorrowerLoanResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("loans")
    private List<BorrowerLoan> loans;

    // Getters
    public List<BorrowerLoan> getLoans() {
        return loans;
    }

    public boolean isStatus() {
        return status;
    }
}

