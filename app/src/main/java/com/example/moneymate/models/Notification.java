package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("notification_id")
    private int id;

    private String message;
    private String status;

    @SerializedName("created_at")
    private String createdAt;

    private Loan loan;
    private Lender lender;

    // Getters
    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Loan getLoan() {
        return loan;
    }

    public Lender getLender() {
        return lender;
    }

    // Inner class Loan
    public static class Loan {
        @SerializedName("loan_id")
        private int id;

        private String status;
        private double amount;

        public int getId() {
            return id;
        }

        public String getStatus() {
            return status;
        }

        public double getAmount() {
            return amount;
        }
    }

    // Inner class Lender
    public static class Lender {
        @SerializedName("lender_id")
        private int id;

        @SerializedName("companyname")
        private String companyName;

        public int getId() {
            return id;
        }

        public String getCompanyName() {
            return companyName;
        }
    }
}
