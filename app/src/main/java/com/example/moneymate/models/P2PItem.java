package com.example.moneymate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class P2PItem implements Parcelable {

    @SerializedName("id")
    private int lenderId;

    @SerializedName("name")
    private String name;

    @SerializedName("amount")
    private String amount;

    @SerializedName("overview")
    private String overview;

    @SerializedName("terms")
    private String terms;

    public P2PItem(int lenderId, String name, String amount, String overview, String terms) {
        this.lenderId = lenderId;
        this.name = name;
        this.amount = amount;
        this.overview = overview;
        this.terms = terms;
    }

    protected P2PItem(Parcel in) {
        lenderId = in.readInt();
        name = in.readString();
        amount = in.readString();
        overview = in.readString();
        terms = in.readString();
    }

    public static final Creator<P2PItem> CREATOR = new Creator<P2PItem>() {
        @Override
        public P2PItem createFromParcel(Parcel in) {
            return new P2PItem(in);
        }

        @Override
        public P2PItem[] newArray(int size) {
            return new P2PItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lenderId);
        dest.writeString(name);
        dest.writeString(amount);
        dest.writeString(overview);
        dest.writeString(terms);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public int getLenderId() {
        return lenderId;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getOverview() {
        return overview;
    }

    public String getTerms() {
        return terms;
    }

    // Setters
    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}
