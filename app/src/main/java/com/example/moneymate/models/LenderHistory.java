package com.example.moneymate.models;

public class LenderHistory {
    private String username;
    private String amount;

    public LenderHistory(String username, String amount) {
        this.username = username;
        this.amount = amount;
    }

    public String getName() {
        return username;
    }

    public String getAmount() {
        return amount;
    }
}
