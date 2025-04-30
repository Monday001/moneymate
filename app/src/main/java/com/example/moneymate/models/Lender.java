package com.example.moneymate.models;

public class Lender {
    private String companyname;
    private String license;
    private String email;
    private String password;

    public Lender(String companyname, String license, String email, String password) {
        this.companyname = companyname;
        this.license = license;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
