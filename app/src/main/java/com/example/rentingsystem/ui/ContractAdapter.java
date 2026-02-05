package com.example.rentingsystem.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.model.Contract;

import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder> {

    private List<Contract> list;
    private boolean isLandlord;

    public ContractAdapter(List<Contract> list, boolean isLandlord) {
        this.list = list;
        this.isLandlord = isLandlord;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contract, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contract contract = list.get(position);
        
        holder.tvHouseTitle.setText(contract.getHouseTitle());
        if (isLandlord) {
            holder.tvTenantName.setText("租客: " + contract.getTenantName());
            holder.tvTenantName.setVisibility(View.VISIBLE);
        } else {
            holder.tvTenantName.setVisibility(View.GONE);
        }
        
        holder.tvPeriod.setText("租期: " + contract.getStartDate() + " 至 " + contract.getEndDate());
        holder.tvRent.setText("租金: ¥ " + contract.getRent() + " / 月");
        holder.tvTime.setText("创建时间: " + contract.getCreateTime());

        // Status: 1=Active, 2=Expired, 3=Terminated
        switch (contract.getStatus()) {
            case 1:
                holder.tvStatus.setText("生效中");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
                break;
            case 2:
                holder.tvStatus.setText("已过期");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#9E9E9E")); // Grey
                break;
            case 3:
                holder.tvStatus.setText("已终止");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#F44336")); // Red
                break;
            default:
                holder.tvStatus.setText("未知");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#9E9E9E"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHouseTitle, tvStatus, tvTenantName, tvPeriod, tvRent, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHouseTitle = itemView.findViewById(R.id.tv_house_title);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTenantName = itemView.findViewById(R.id.tv_tenant_name);
            tvPeriod = itemView.findViewById(R.id.tv_period);
            tvRent = itemView.findViewById(R.id.tv_rent);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
