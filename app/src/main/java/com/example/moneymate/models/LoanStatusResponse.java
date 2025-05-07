package com.example.moneymate.models;

import java.util.List;

public class LoanStatusResponse {
    private boolean success;
    private int user_count;
    private LoanStatus loan_status;
    private List<Loan> loans;

    // Getter and Setter for success
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter and Setter for user_count
    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    // Getter and Setter for loan_status
    public LoanStatus getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(LoanStatus loan_status) {
        this.loan_status = loan_status;
    }

    // Getter and Setter for loans
    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public static class LoanStatus {
        private int approved_count;
        private int denied_count;
        private int pending_count;

        public int getApproved_count() {
            return approved_count;
        }

        public void setApproved_count(int approved_count) {
            this.approved_count = approved_count;
        }

        public int getDenied_count() {
            return denied_count;
        }

        public void setDenied_count(int denied_count) {
            this.denied_count = denied_count;
        }

        public int getPending_count() {
            return pending_count;
        }

        public void setPending_count(int pending_count) {
            this.pending_count = pending_count;
        }
    }

    public static class Loan {
        private int loan_id;
        private int borrower_id;
        private double amount;
        private String status;
        private String date_submitted;
        private String id_front;
        private String id_back;
        private List<Payment> payment_history;

        public int getLoan_id() {
            return loan_id;
        }

        public void setLoan_id(int loan_id) {
            this.loan_id = loan_id;
        }

        public int getBorrower_id() {
            return borrower_id;
        }

        public void setBorrower_id(int borrower_id) {
            this.borrower_id = borrower_id;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate_submitted() {
            return date_submitted;
        }

        public void setDate_submitted(String date_submitted) {
            this.date_submitted = date_submitted;
        }

        public String getId_front() {
            return id_front;
        }

        public void setId_front(String id_front) {
            this.id_front = id_front;
        }

        public String getId_back() {
            return id_back;
        }

        public void setId_back(String id_back) {
            this.id_back = id_back;
        }

        public List<Payment> getPayment_history() {
            return payment_history;
        }

        public void setPayment_history(List<Payment> payment_history) {
            this.payment_history = payment_history;
        }
    }

    public static class Payment {
        private String payment_date;
        private double amount_paid;

        public String getPayment_date() {
            return payment_date;
        }

        public void setPayment_date(String payment_date) {
            this.payment_date = payment_date;
        }

        public double getAmount_paid() {
            return amount_paid;
        }

        public void setAmount_paid(double amount_paid) {
            this.amount_paid = amount_paid;
        }
    }
}
