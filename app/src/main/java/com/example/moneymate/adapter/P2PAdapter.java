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
import com.example.moneymate.models.P2PItem;

import java.util.List;

public class P2PAdapter extends RecyclerView.Adapter<P2PAdapter.P2PViewHolder> {

    private List<P2PItem> p2pList;
    private Context context;

    public P2PAdapter(Context context, List<P2PItem> p2pList) {
        this.context = context;
        this.p2pList = p2pList;
    }

    @NonNull
    @Override
    public P2PViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_p2p, parent, false);
        return new P2PViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull P2PViewHolder holder, int position) {
        P2PItem item = p2pList.get(position);
        holder.companyName.setText(item.getName());
        holder.minAmount.setText("From " + item.getAmount());

        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CompanyTermsActivity.class);
            intent.putExtra("company_name", item.getName());
            intent.putExtra("overview", item.getOverview());
            intent.putExtra("terms", item.getTerms());
            intent.putExtra("lender_id", item.getLenderId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return p2pList.size();
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
