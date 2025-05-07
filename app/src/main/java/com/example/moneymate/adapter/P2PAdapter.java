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

import com.example.moneymate.CompanyTermsActivity;
import com.example.moneymate.R;
import com.example.moneymate.models.CompanyItem;

import java.util.List;

public class P2PAdapter extends RecyclerView.Adapter<P2PAdapter.P2PViewHolder> {

    private List<CompanyItem> companyList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onViewClick(CompanyItem item);
    }

    public P2PAdapter(Context context, List<CompanyItem> companyList, OnItemClickListener listener) {
        this.context = context;
        this.companyList = companyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public P2PViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_p2p, parent, false);
        return new P2PViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull P2PViewHolder holder, int position) {
        CompanyItem item = companyList.get(position);
        holder.companyName.setText(item.getName());
        holder.minAmount.setText("From " + item.getAmount());

        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CompanyTermsActivity.class);
            intent.putExtra("company_name", item.getName());
            intent.putExtra("overview", "Offering Loans from " + item.getAmount() + " @ 1.8%");
            intent.putExtra("terms", "1. Offering Loans from 10,000 @ 1.8%\n2. 2 months grace period\n3. 1.3% interest on loans above 50,000\n4. Flexible repayment plan\n5. Grow your limit on every complete transaction.");
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public static class P2PViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, minAmount;
        Button viewButton;

        public P2PViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.p2p_companyName);
            minAmount = itemView.findViewById(R.id.p2p_minAmount);
            viewButton = itemView.findViewById(R.id.terms_view_button);
        }
    }
}

