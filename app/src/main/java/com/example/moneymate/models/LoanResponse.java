package com.example.moneymate.models;

import java.util.List;

public class LoanResponse {
    private boolean success;
    private List<LoanApplication> applications;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<LoanApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<LoanApplication> applications) {
        this.applications = applications;
    }
}
