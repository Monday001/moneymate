package com.example.moneymate.adapter;

import android.content.Context;
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

public class LoanHistoryAdapter extends RecyclerView.Adapter<LoanHistoryAdapter.ViewHolder> {

    private Context context;
    private List<LenderHistory> loanApplications;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onViewClick(LenderHistory application);
    }

    public LoanHistoryAdapter(Context context, List<LenderHistory> loanApplications, OnItemClickListener listener) {
        this.context = context;
        this.loanApplications = loanApplications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lender_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LenderHistory application = loanApplications.get(position);

        holder.txtUsername.setText(application.getName());
        holder.txtAmount.setText(application.getAmount());

        holder.btnView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewClick(application);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loanApplications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername, txtAmount;
        Button btnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.lender_history_username);
            txtAmount = itemView.findViewById(R.id.lender_history_amount);
            btnView = itemView.findViewById(R.id.lender_view_button);
        }
    }
}
