package com.example.rentingsystem.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.model.House;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    private Context context;
    private List<House> houseList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(House house);
    }

    public HouseAdapter(Context context, List<House> houseList) {
        this.context = context;
        this.houseList = houseList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    public void updateData(List<House> newList) {
        this.houseList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_house, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {
        House house = houseList.get(position);
        holder.tvTitle.setText(house.getTitle());
        holder.tvRent.setText("¥ " + house.getRent() + "/月");
        holder.tvAddress.setText(house.getAddress() + " | " + house.getHouseType() + " | " + house.getArea() + "㎡");
        
        // Status and Color
        String statusStr = "未知";
        int colorRes = R.color.status_off_shelf;
        switch (house.getStatus()) {
            case 1: 
                statusStr = "可租"; 
                colorRes = R.color.status_rentable;
                break;
            case 2: 
                statusStr = "已租"; 
                colorRes = R.color.status_rented;
                break;
            case 3: 
                statusStr = "已下架"; 
                colorRes = R.color.status_off_shelf;
                break;
        }
        holder.tvStatus.setText(statusStr);
        
        // Update Badge Background Color Dynamically
        GradientDrawable bg = (GradientDrawable) holder.tvStatus.getBackground();
        bg.setColor(ContextCompat.getColor(context, colorRes));

        // Image
        if (!TextUtils.isEmpty(house.getImagePath())) {
            int resId = context.getResources().getIdentifier(house.getImagePath(), "drawable", context.getPackageName());
            if (resId != 0) {
                holder.ivImage.setImageResource(resId);
            } else {
                holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(house);
            }
        });
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    static class HouseViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvRent, tvAddress, tvStatus;

        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_house_image);
            tvTitle = itemView.findViewById(R.id.tv_house_title);
            tvRent = itemView.findViewById(R.id.tv_house_rent);
            tvAddress = itemView.findViewById(R.id.tv_house_address);
            tvStatus = itemView.findViewById(R.id.tv_house_status);
        }
    }
}
