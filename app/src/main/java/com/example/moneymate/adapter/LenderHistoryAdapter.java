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

import com.example.moneymate.R;
import com.example.moneymate.TransactionDetailActivity;
import com.example.moneymate.models.LenderHistory;

import java.util.ArrayList;
import java.util.List;

public class LenderHistoryAdapter extends RecyclerView.Adapter<LenderHistoryAdapter.ViewHolder> {

    private final Context context;
    private List<LenderHistory> lenderHistoryList;

    public LenderHistoryAdapter(Context context, List<LenderHistory> lenderHistoryList) {
        this.context = context;
        this.lenderHistoryList = lenderHistoryList;
    }

    // Method to update the lender history list
    public void setHistoryList(List<LenderHistory> newList) {
        this.lenderHistoryList = newList;
        notifyDataSetChanged();  // Notify adapter that data has changed
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lender_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LenderHistory item = lenderHistoryList.get(position);

        holder.nameText.setText(item.getName());
        holder.amountText.setText(item.getAmount());

        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, TransactionDetailActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("amount", item.getAmount());
            intent.putExtra("status", item.getStatus());
            intent.putExtra("date", item.getDate());
            intent.putExtra("phone", item.getPhone());
            intent.putExtra("email", item.getEmail());
            intent.putExtra("idFront", item.getIdFront());
            intent.putExtra("idBack", item.getIdBack());
            intent.putParcelableArrayListExtra("history", new ArrayList<>(item.getPaymentHistory()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lenderHistoryList != null ? lenderHistoryList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, amountText;
        Button viewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.lender_history_username);
            amountText = itemView.findViewById(R.id.lender_history_amount);
            viewButton = itemView.findViewById(R.id.lender_view_button);
        }
    }
}
