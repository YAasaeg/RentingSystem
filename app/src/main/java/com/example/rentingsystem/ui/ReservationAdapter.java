package com.example.rentingsystem.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.model.Reservation;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private Context context;
    private List<Reservation> list;
    private boolean isLandlord;
    private OnActionListener listener;

    public interface OnActionListener {
        void onApprove(Reservation res);
        void onReject(Reservation res);
    }

    public ReservationAdapter(Context context, List<Reservation> list, boolean isLandlord) {
        this.context = context;
        this.list = list;
        this.isLandlord = isLandlord;
    }

    public void setOnActionListener(OnActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation res = list.get(position);
        holder.tvTitle.setText(res.getHouseTitle());
        holder.tvTime.setText("预约时间: " + res.getReserveTime());
        
        String statusStr = "";
        int color = 0;
        switch (res.getStatus()) {
            case 0: statusStr = "待处理"; color = context.getResources().getColor(R.color.secondary); break;
            case 1: statusStr = "已同意"; color = context.getResources().getColor(R.color.status_rentable); break;
            case 2: statusStr = "已拒绝"; color = context.getResources().getColor(R.color.error); break;
        }
        holder.tvStatus.setText(statusStr);
        holder.tvStatus.setTextColor(color);

        if (isLandlord) {
            holder.tvTenantInfo.setVisibility(View.VISIBLE);
            holder.tvTenantInfo.setText("租客: " + res.getTenantName() + " (" + res.getTenantPhone() + ")");
            
            if (res.getStatus() == 0) {
                holder.layoutActions.setVisibility(View.VISIBLE);
                holder.btnApprove.setOnClickListener(v -> {
                    if (listener != null) listener.onApprove(res);
                });
                holder.btnReject.setOnClickListener(v -> {
                    if (listener != null) listener.onReject(res);
                });
            } else {
                holder.layoutActions.setVisibility(View.GONE);
            }
        } else {
            holder.tvTenantInfo.setVisibility(View.GONE);
            holder.layoutActions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvStatus, tvTime, tvTenantInfo;
        LinearLayout layoutActions;
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_house_title);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTenantInfo = itemView.findViewById(R.id.tv_tenant_info);
            layoutActions = itemView.findViewById(R.id.layout_actions);
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnReject = itemView.findViewById(R.id.btn_reject);
        }
    }
}
