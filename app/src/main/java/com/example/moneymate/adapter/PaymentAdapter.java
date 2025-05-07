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

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<Payment> paymentList = new ArrayList<>();

    public void setPaymentList(List<Payment> list) {
        this.paymentList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_history, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = paymentList.get(position);
        holder.transactionId.setText(payment.getTransactionId());
        holder.date.setText(payment.getDate());
        holder.amount.setText(payment.getAmount());
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView transactionId, date, amount;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            transactionId = itemView.findViewById(R.id.transactionId);
            date = itemView.findViewById(R.id.paymentDate);
            amount = itemView.findViewById(R.id.paymentAmount);
        }
    }
}

