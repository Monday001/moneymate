package com.example.moneymate.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class P2PResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("companies")
    private List<P2PItem> companies;

    public boolean isSuccess() {
        return success;
    }

    public List<P2PItem> getCompanies() {
        return companies;
    }
}
