package com.example.rentingsystem.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.model.RentApplication;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private List<RentApplication> list;
    private OnActionListener listener;

    public interface OnActionListener {
        void onApprove(RentApplication app);
        void onReject(RentApplication app);
    }

    public ApplicationAdapter(List<RentApplication> list, OnActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentApplication app = list.get(position);
        
        holder.tvHouseTitle.setText(app.getHouseTitle());
        holder.tvTenantName.setText("租客: " + app.getTenantName() + " (" + app.getTenantPhone() + ")");
        holder.tvRemark.setText("理由: " + app.getApplyReason() + "\n时间: " + app.getStartDate() + " 至 " + app.getEndDate());
        holder.tvTime.setText(app.getCreateTime());

        // Status: 1=Pending, 2=Approved, 3=Rejected
        switch (app.getStatus()) {
            case 1:
                holder.tvStatus.setText("待处理");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
                holder.layoutActions.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.tvStatus.setText("已签约");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
                holder.layoutActions.setVisibility(View.GONE);
                break;
            case 3:
                holder.tvStatus.setText("已拒绝");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#F44336")); // Red
                holder.layoutActions.setVisibility(View.GONE);
                break;
        }

        holder.btnApprove.setOnClickListener(v -> {
            if (listener != null) listener.onApprove(app);
        });

        holder.btnReject.setOnClickListener(v -> {
            if (listener != null) listener.onReject(app);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHouseTitle, tvStatus, tvTenantName, tvRemark, tvTime;
        LinearLayout layoutActions;
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHouseTitle = itemView.findViewById(R.id.tv_house_title);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTenantName = itemView.findViewById(R.id.tv_tenant_name);
            tvRemark = itemView.findViewById(R.id.tv_remark);
            tvTime = itemView.findViewById(R.id.tv_time);
            layoutActions = itemView.findViewById(R.id.layout_actions);
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnReject = itemView.findViewById(R.id.btn_reject);
        }
    }
}
