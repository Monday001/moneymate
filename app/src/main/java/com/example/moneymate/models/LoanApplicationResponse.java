package com.example.moneymate.models;

public class LoanApplicationResponse {
    private boolean success;
    private LoanApplication application;

    public boolean isSuccess() {
        return success;
    }

    public LoanApplication getApplication() {
        return application;
    }
}

