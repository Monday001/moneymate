package com.example.moneymate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.BorrowerDetailsActivity;
import com.example.moneymate.R;
import com.example.moneymate.models.BorrowerHistory;

import java.util.ArrayList;
import java.util.List;

public class BorrowerHistoryAdapter extends RecyclerView.Adapter<BorrowerHistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<BorrowerHistory> borrowerHistoryList;

    public BorrowerHistoryAdapter(Context context, List<BorrowerHistory> borrowerHistoryList) {
        this.context = context;
        this.borrowerHistoryList = borrowerHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_borrower_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowerHistory item = borrowerHistoryList.get(position);

        holder.companyNameText.setText(item.getCompanyName());
        holder.loanAmountText.setText(item.getLoanAmount());

        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, BorrowerDetailsActivity.class);
            intent.putExtra("companyName", item.getCompanyName());
            intent.putExtra("loanAmount", item.getLoanAmount());
            intent.putExtra("status", item.getStatus());
            intent.putExtra("paymentHistory", new ArrayList<>(item.getPaymentHistory()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return borrowerHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyNameText, loanAmountText;
        Button viewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyNameText = itemView.findViewById(R.id.borrower_history_companyName);
            loanAmountText = itemView.findViewById(R.id.borrower_history_loanAmount);
            viewButton = itemView.findViewById(R.id.borrower_view_button);
        }
    }
}
