package com.example.moneymate.models;

public class Notification {
    private int id;
    private int borrower_id;

    private String full_name;

    private String amount;

    private String message;
    private String status;
    private String created_at;

    // Constructor, Getters, Setters
    public Notification(int id, int borrower_id,String full_name, String amount, String message, String status, String created_at) {
        this.id = id;
        this.borrower_id = borrower_id;
        this.full_name = full_name;
        this.amount = amount;
        this.message = message;
        this.status = status;
        this.created_at = created_at;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getAmount() {
        return amount;
    }

    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return created_at; }
}

