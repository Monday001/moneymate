package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Loan {
    @SerializedName("loan_id")
    private int id;
    private int borrower_id;
    private String full_name;
    private String phone_number;
    private String email_address;
    private String amount;
    private String purpose;
    private String application_status;
    private String date_submitted;
    private String id_front;
    private String id_back;
    private String username;
    private List<Payment> paymentHistory;

    private String disbursement_status;

    private String disbursed_amount;

    private String disbursed_date;

    // Getters for all fields
    public int getId() { return id; }
    public int getBorrower_id() { return borrower_id; }
    public String getFull_name() { return full_name; }
    public String getPhone_number() { return phone_number; }
    public String getEmail_address() { return email_address; }
    public String getAmount() { return amount; }
    public String getPurpose() { return purpose; }
    public String getApplication_status() { return application_status; }
    public String getDate_submitted() { return date_submitted; }
    public String getId_front() { return id_front; }
    public String getId_back() { return id_back; }
    public String getUsername() { return username; }
    public List<Payment> getPaymentHistory() { return paymentHistory; }

    public String getDisbursement_status() { return disbursement_status; }

    public String getDisbursed_amount() { return disbursed_amount; }

    public String getDisbursed_date() {
        return disbursed_date;
    }

}
