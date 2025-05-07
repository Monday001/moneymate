package com.example.moneymate.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Payment implements Parcelable {
    private String transactionId;
    private String date;
    private String amount;

    public Payment(String transactionId, String date, String amount) {
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    // Parcelable implementation
    protected Payment(Parcel in) {
        transactionId = in.readString();
        date = in.readString();
        amount = in.readString();
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(transactionId);
        dest.writeString(date);
        dest.writeString(amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
