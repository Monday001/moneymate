package com.example.moneymate.models;

public class LatestLoanResponse {
    private boolean success;
    private Loan loan;

    public boolean isSuccess() {
        return success;
    }

    public Loan getLoan() {
        return loan;
    }
}

