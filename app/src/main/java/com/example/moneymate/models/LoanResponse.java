package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LoanResponse {
    private boolean success;

    @SerializedName("applications") // This must match the JSON key from your API response
    private List<Loan> applications;

    public boolean isSuccess() {
        return success;
    }

    public List<Loan> getApplications() {
        return applications;
    }
}
