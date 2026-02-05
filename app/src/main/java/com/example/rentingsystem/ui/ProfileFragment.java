package com.example.rentingsystem.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rentingsystem.R;

import android.view.ContextThemeWrapper;
import android.widget.LinearLayout;

import static android.content.Context.MODE_PRIVATE;
// ... existing imports

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button btnLogout = view.findViewById(R.id.btn_logout);
        LinearLayout containerActions = view.findViewById(R.id.container_actions);

        SharedPreferences sp = getActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);
        int identityType = sp.getInt("identity_type", 1); // 1=Tenant, 2=Landlord
        // Add "My Applications" button
        ContextThemeWrapper newContext1 = new ContextThemeWrapper(getContext(), com.google.android.material.R.style.Widget_MaterialComponents_Button_OutlinedButton);
        Button btnMyProfile = new Button(newContext1);
        btnMyProfile.setText("编辑个人信息");
        btnMyProfile.setTextSize(17);
        btnMyProfile.setTextColor(Color.BLACK);
        btnMyProfile.setBackgroundColor(Color.parseColor("#E3F2FD"));
        btnMyProfile.setAllCaps(false);
        btnMyProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),EditProfileActivity.class);
            startActivity(intent);
        });

        containerActions.addView(btnMyProfile);
        if (identityType == 1) {
            // Create "My Reservations" button programmatically
            ContextThemeWrapper newContext = new ContextThemeWrapper(getContext(), com.google.android.material.R.style.Widget_MaterialComponents_Button_OutlinedButton);
            Button btnMyReservations = new Button(newContext);
            btnMyReservations.setText("我的预约");
            btnMyReservations.setTextSize(17);
            btnMyReservations.setTextColor(Color.BLACK);
            btnMyReservations.setBackgroundColor(Color.parseColor("#E3F2FD"));
            btnMyReservations.setAllCaps(false);

            // Set Layout Params (match_parent width)
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            btnMyReservations.setLayoutParams(params);

            btnMyReservations.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MyReservationsActivity.class);
                startActivity(intent);
            });

            containerActions.addView(btnMyReservations);

            // Add "My Applications" button
            Button btnMyApplications = new Button(newContext);
            btnMyApplications.setText("我的申请");
            btnMyApplications.setTextSize(17);
            btnMyApplications.setTextColor(Color.BLACK);
            btnMyApplications.setBackgroundColor(Color.parseColor("#E3F2FD"));
            btnMyApplications.setAllCaps(false);
            btnMyApplications.setLayoutParams(params);
            btnMyApplications.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MyApplicationsActivity.class);
                startActivity(intent);
            });
            containerActions.addView(btnMyApplications);

            // Add "My Contracts" button
            Button btnMyContracts = new Button(newContext);
            btnMyContracts.setText("我的合同");
            btnMyContracts.setTextSize(17);
            btnMyContracts.setTextColor(Color.BLACK);
            btnMyContracts.setBackgroundColor(Color.parseColor("#E3F2FD"));
            btnMyContracts.setAllCaps(false);
            btnMyContracts.setLayoutParams(params);
            btnMyContracts.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MyContractsActivity.class);
                startActivity(intent);
            });
            containerActions.addView(btnMyContracts);
        }

        btnLogout.setOnClickListener(v -> {
            sp.edit().clear().apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}
