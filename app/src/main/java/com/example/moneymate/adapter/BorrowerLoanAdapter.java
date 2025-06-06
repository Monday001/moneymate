package com.example.moneymate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;
import com.example.moneymate.models.BorrowerLoan;

import java.util.ArrayList;
import java.util.List;

public class BorrowerLoanAdapter extends RecyclerView.Adapter<BorrowerLoanAdapter.ViewHolder> {

    private List<BorrowerLoan> loanList = new ArrayList<>();

    public BorrowerLoanAdapter(List<BorrowerLoan> loanList) {
        if (loanList != null) {
            this.loanList = loanList;
        }
    }

    public void updateData(List<BorrowerLoan> newLoanList) {
        loanList.clear();
        if (newLoanList != null) {
            loanList.addAll(newLoanList);
        }
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView loanAmountText, companyNameText;

        public ViewHolder(View itemView) {
            super(itemView);
            loanAmountText = itemView.findViewById(R.id.tvAmount);
            companyNameText = itemView.findViewById(R.id.tvFullname);
        }
    }

    @NonNull
    @Override
    public BorrowerLoanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowerLoan loan = loanList.get(position);
        holder.loanAmountText.setText(loan.getLoanAmount());
        holder.companyNameText.setText(loan.getCompanyName());
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }
}
