package com.example.moneymate.models;

import java.util.List;

public class RepaymentResponse {
    private boolean success;
    private List<Repayment> repayments;

    public boolean isSuccess() { return success; }
    public List<Repayment> getRepayments() { return repayments; }

    public void setRepayments(List<Repayment> repayments) {
        this.repayments = repayments;
    }
}

