package com.example.moneymate.models;

import java.util.List;

public class BorrowerHistory {
    private String companyName;
    private String loanAmount;
    private String status;
    private String date; // Represents the latest or main payment date
    private List<Payment> paymentHistory;

    public BorrowerHistory(String companyName, String loanAmount, String status, String date, List<Payment> paymentHistory) {
        this.companyName = companyName;
        this.loanAmount = loanAmount;
        this.status = status;
        this.date = date;
        this.paymentHistory = paymentHistory;
    }

    // Getters
    public String getCompanyName() {
        return companyName;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public List<Payment> getPaymentHistory() {
        return paymentHistory;
    }
}
