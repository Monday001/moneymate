package com.example.moneymate.models;

public class User {
    private String username;
    private String phonenumber;
    private String password;
    private String created_at;

    public User(String username, String phonenumber, String password) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.password = password;
        // 'created_at' will typically be set server-side
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public String getCreated_at() {
        return created_at;
    }

    // Setters (Optional, useful for Gson deserialization or if updating values)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
