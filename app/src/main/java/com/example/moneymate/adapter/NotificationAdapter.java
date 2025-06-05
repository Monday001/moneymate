package com.example.moneymate.adapter;

import com.example.moneymate.NotificationViewActivity;
import com.example.moneymate.models.Notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notificationList;
    private Context context;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullname, tvAmount;

        public ViewHolder(View view) {
            super(view);
            tvFullname = view.findViewById(R.id.tvFullname);
            tvAmount = view.findViewById(R.id.tvAmount);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification n = notificationList.get(position);
        holder.tvFullname.setText(n.getFull_name());
        holder.tvAmount.setText("Ksh " + n.getAmount());

        // Optional: add click listener to open detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotificationViewActivity.class);
            intent.putExtra("full_name", n.getFull_name());
            intent.putExtra("amount", n.getAmount());
            intent.putExtra("status", n.getStatus());
            intent.putExtra("message", n.getMessage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
