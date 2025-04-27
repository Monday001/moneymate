package com.example.moneymate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;
import com.example.moneymate.models.LenderHistory;

import java.util.List;

public class LenderHistoryAdapter extends RecyclerView.Adapter<LenderHistoryAdapter.LenderHistoryViewHolder> {

    private List<LenderHistory> lenderHistoryList;

    public LenderHistoryAdapter(List<LenderHistory> lenderHistoryList) {
        this.lenderHistoryList = lenderHistoryList;
    }

    @NonNull
    @Override
    public LenderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lender_history, parent, false);
        return new LenderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LenderHistoryViewHolder holder, int position) {
        LenderHistory item = lenderHistoryList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.amountTextView.setText(item.getAmount());

        // Optional: Set a click listener for the view button
        holder.viewButton.setOnClickListener(v -> {
            // Handle view button click here
        });
    }

    @Override
    public int getItemCount() {
        return lenderHistoryList.size();
    }

    static class LenderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, amountTextView;
        Button viewButton;

        public LenderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.lender_history_username);
            amountTextView = itemView.findViewById(R.id.lender_history_amount);
            viewButton = itemView.findViewById(R.id.lender_view_button);
        }
    }
}

