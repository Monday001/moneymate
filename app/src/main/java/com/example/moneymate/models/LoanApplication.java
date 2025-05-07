package com.example.moneymate.models;

import java.util.List;

public class LoanApplication {
    private String fullName;
    private String amount;
    private String applicationStatus;
    private String dateSubmitted;
    private String phoneNumber;
    private String emailAddress;
    private String idFront;
    private String idBack;
    private String purpose;
    private List<Payment> paymentHistory;

    // Getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }

    public String getDateSubmitted() { return dateSubmitted; }
    public void setDateSubmitted(String dateSubmitted) { this.dateSubmitted = dateSubmitted; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getIdFront() { return idFront; }
    public void setIdFront(String idFront) { this.idFront = idFront; }

    public String getIdBack() { return idBack; }
    public void setIdBack(String idBack) { this.idBack = idBack; }

    public String getStatus() {
        return applicationStatus;
    }

    public String getDateApplied() {
        return dateSubmitted;
    }


    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public List<Payment> getPaymentHistory() { return paymentHistory; }
    public void setPaymentHistory(List<Payment> paymentHistory) { this.paymentHistory = paymentHistory; }
}
