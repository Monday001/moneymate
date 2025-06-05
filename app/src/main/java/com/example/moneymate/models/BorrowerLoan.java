package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

public class BorrowerLoan {

    @SerializedName("application_id")
    private int applicationId;

    @SerializedName("amount")
    private String loanAmount;

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("status")
    private String status;

    @SerializedName("loan_id")
    private int loanId;

    @SerializedName("company_name")  // <-- adjust this key based on your JSON response
    private String companyName;

    // Getters
    public int getApplicationId() {
        return applicationId;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getStatus() {
        return status;
    }

    public int getLoanId() {
        return loanId;
    }

    public String getCompanyName() {
        return companyName;
    }

    // Setters (optional, add if needed)
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
