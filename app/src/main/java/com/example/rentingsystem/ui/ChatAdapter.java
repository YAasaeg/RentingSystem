package com.example.rentingsystem.ui;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.model.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> list;
    private int currentUserId;

    public ChatAdapter(List<Message> list, int currentUserId) {
        this.list = list;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message msg = list.get(position);
        
        holder.tvTime.setText(msg.getCreateTime());
        holder.tvContent.setText(msg.getContent());

        if (msg.getSenderId() == currentUserId) {
            // Sent by me (Right)
            holder.layoutContainer.setGravity(Gravity.END);
            holder.ivAvatarLeft.setVisibility(View.INVISIBLE); // Keep layout structure
            holder.ivAvatarRight.setVisibility(View.VISIBLE);
            holder.tvContent.setBackgroundColor(Color.parseColor("#BBDEFB")); // Light Blue
        } else {
            // Received (Left)
            holder.layoutContainer.setGravity(Gravity.START);
            holder.ivAvatarLeft.setVisibility(View.VISIBLE);
            holder.ivAvatarRight.setVisibility(View.INVISIBLE);
            holder.tvContent.setBackgroundColor(Color.parseColor("#F5F5F5")); // Light Grey
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvContent;
        ImageView ivAvatarLeft, ivAvatarRight;
        LinearLayout layoutContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivAvatarLeft = itemView.findViewById(R.id.iv_avatar_left);
            ivAvatarRight = itemView.findViewById(R.id.iv_avatar_right);
            layoutContainer = itemView.findViewById(R.id.layout_msg_container);
        }
    }
}
