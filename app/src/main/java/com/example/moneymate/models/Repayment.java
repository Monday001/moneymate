package com.example.moneymate.models;

public class Repayment {
    private String loanId;
    private String amount;
    private String companyName;
    private String referenceCode;

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
}
