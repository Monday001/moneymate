package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

public class Repayment {

    @SerializedName("loan_id")
    private String loanId;

    private String amount;

    @SerializedName("company_name")
    private String companyName;

    @SerializedName("reference_code")
    private String referenceCode;

    @SerializedName("created_at")
    private String date;

    // ✅ No-arg constructor (needed by Gson)
    public Repayment() {}

    // ✅ Custom constructor (for creating a request manually)
    public Repayment(String loanId, String amount, String companyName, String referenceCode) {
        this.loanId = loanId;
        this.amount = amount;
        this.companyName = companyName;
        this.referenceCode = referenceCode;
    }

    // Getters
    public String getLoanId() { return loanId; }
    public String getAmount() { return amount; }
    public String getCompanyName() { return companyName; }
    public String getReferenceCode() { return referenceCode; }
    public String getDate() { return date; }
}
