package com.example.moneymate.models;

public class LenderTermsRequest {
    public int lender_id;
    public String term_1;
    public String term_2;
    public String term_3;
    public String term_4;
    public String term_5;

    public LenderTermsRequest(int lender_id, String term1, String term2, String term3, String term4, String term5) {
        this.lender_id = lender_id;
        this.term_1 = term1;
        this.term_2 = term2;
        this.term_3 = term3;
        this.term_4 = term4;
        this.term_5 = term5;
    }
}

