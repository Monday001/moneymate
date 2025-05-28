package com.example.moneymate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;
import com.example.moneymate.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

    private final List<Loan> loanList;

    public LoanAdapter() {
        this.loanList = new ArrayList<>();
    }

    public void setLoanList(List<Loan> list) {
        loanList.clear();               // Clear previous data
        if (list != null) {
            loanList.addAll(list);     // Add new data
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrower_history, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        Loan loan = loanList.get(position);
        holder.title.setText(loan.purpose != null ? loan.purpose : "No purpose");
        holder.amount.setText(String.valueOf(loan.amount));
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    static class LoanViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount;

        public LoanViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.borrower_history_companyName);
            amount = itemView.findViewById(R.id.borrower_history_loanAmount);
        }
    }
}
