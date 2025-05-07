package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class P2PResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("companies")
    private List<CompanyItem> companies;

    public boolean isSuccess() {
        return success;
    }

    public List<CompanyItem> getCompanies() {
        return companies;
    }
}
