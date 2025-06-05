package com.example.moneymate.models;

import java.util.List;

public class LenderHistory {
    private int loanId;
    private String username;
    private String amount;
    private String status;
    private String date;
    private String phone;
    private String email;
    private String idFront;
    private String idBack;

    private String purpose;
    private List<Payment> paymentHistory;

    public LenderHistory(int loanId, String username, String amount, String status, String date,
                         String phone, String email, String idFront, String idBack, String purpose,
                         List<Payment> paymentHistory) {
        this.loanId = loanId;
        this.username = username;
        this.amount = amount;
        this.status = status;
        this.date = date;
        this.phone = phone;
        this.email = email;
        this.idFront = idFront;
        this.idBack = idBack;
        this.purpose = purpose;
        this.paymentHistory = paymentHistory;
    }

    // Getters and Setters
    public int getLoanId() {
        return loanId;
    }
    public String getName() {
        return username;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getIdFront() {
        return idFront;
    }

    public String getIdBack() {
        return idBack;
    }

    public String getPurpose() {
        return purpose;
    }

    public List<Payment> getPaymentHistory() {
        return paymentHistory;
    }
}
