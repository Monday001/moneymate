package com.example.moneymate.models;

import java.util.List;

public class LenderHistoryResponse {
    private boolean success;
    private List<LenderHistory> loans;

    public boolean isSuccess() {
        return success;
    }

    public List<LenderHistory> getLoans() {
        return loans;
    }
}
