package com.example.rentingsystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rentingsystem.R;

public class LandlordMessageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landlord_message, container, false);
        
        Button btnManageReservation = view.findViewById(R.id.btn_manage_reservation);
        Button btnManageApplication = view.findViewById(R.id.btn_manage_application);

        Button btnManageContract = view.findViewById(R.id.btn_manage_contract);
        Button btnMessageCenter = view.findViewById(R.id.btn_message_center);
        Button btnNoticeManage = view.findViewById(R.id.btn_manage_notice);

        btnManageReservation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReservationManagementActivity.class);
            startActivity(intent);
        });

        btnManageApplication.setEnabled(true);
        btnManageApplication.setText("申请管理");
        btnManageApplication.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ApplicationManagementActivity.class);
            startActivity(intent);
        });

        btnManageContract.setEnabled(true);
        btnManageContract.setText("合同管理");
        btnManageContract.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ContractManagementActivity.class);
            startActivity(intent);
        });

        btnMessageCenter.setEnabled(true);
        btnMessageCenter.setText("消息中心");
        btnMessageCenter.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MessageCenterActivity.class);
            startActivity(intent);
        });

        btnNoticeManage.setEnabled(true);
        btnNoticeManage.setText("公告管理");
        btnNoticeManage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NoticeManagementActivity.class);
            startActivity(intent);
        });
        
        return view;
    }
}
