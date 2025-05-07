package com.example.moneymate.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;
import com.example.moneymate.RepaymentDetailsActivity;
import com.example.moneymate.models.Repayment;

import java.util.List;

public class RepaymentAdapter extends RecyclerView.Adapter<RepaymentAdapter.ViewHolder> {
    private List<Repayment> repaymentList;

    public RepaymentAdapter(List<Repayment> repaymentList) {
        this.repaymentList = repaymentList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, amount, paidOn;
        Button viewButton;

        public ViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.borrower_history_companyName);
            amount = itemView.findViewById(R.id.borrower_history_loanAmount);
            viewButton = itemView.findViewById(R.id.borrower_view_button);
        }
    }

    @Override
    public RepaymentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrower_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepaymentAdapter.ViewHolder holder, int position) {
        Repayment repayment = repaymentList.get(position);

        // Set the text for company name and amount
        holder.companyName.setText(repayment.getCompanyName());
        holder.amount.setText("Amount: Ksh " + repayment.getAmount());

        // Set click listener for the VIEW button to open the repayment details activity
        holder.viewButton.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, RepaymentDetailsActivity.class);

            // Pass necessary data to the next activity
            intent.putExtra("loan_id", repayment.getLoanId());  // Passing loan_id
            intent.putExtra("amount", repayment.getAmount());  // Passing amount
            intent.putExtra("company_name", repayment.getCompanyName());  // Passing company name

            // Start the activity
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return repaymentList.size();
    }
}
