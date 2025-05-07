package com.example.moneymate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;
import com.example.moneymate.models.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {

    private List<Payment> paymentList;

    // No-argument constructor
    public PaymentHistoryAdapter() {
        this.paymentList = new ArrayList<>();
    }

    // Optional: Constructor with initial list
    public PaymentHistoryAdapter(List<Payment> paymentList) {
        this.paymentList = paymentList != null ? paymentList : new ArrayList<>();
    }

    // Method to update the data
    public void setData(List<Payment> newList) {
        this.paymentList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        holder.ref.setText(payment.getTransactionId());
        holder.date.setText(payment.getDate());
        holder.amount.setText("Ksh " + payment.getAmount());
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ref, date, amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ref = itemView.findViewById(R.id.transactionId);
            date = itemView.findViewById(R.id.paymentDate);
            amount = itemView.findViewById(R.id.paymentAmount);
        }
    }
}
