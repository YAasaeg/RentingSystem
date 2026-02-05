package com.example.rentingsystem.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.Contract;
import com.example.rentingsystem.model.House;
import com.example.rentingsystem.model.RentApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApplicationManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ApplicationAdapter adapter;
    private RentingDao rentingDao;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_management);

        rentingDao = new RentingDao(this);
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);

        initView();
        loadData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        List<RentApplication> list = rentingDao.getLandlordApplications(userId);
        adapter = new ApplicationAdapter(list, new ApplicationAdapter.OnActionListener() {
            @Override
            public void onApprove(RentApplication app) {
                showApproveDialog(app);
            }

            @Override
            public void onReject(RentApplication app) {
                showRejectDialog(app);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void showApproveDialog(RentApplication app) {
        new AlertDialog.Builder(this)
                .setTitle("确认同意")
                .setMessage("同意申请将自动生成租赁合同（" + app.getStartDate() + " 至 " + app.getEndDate() + "），是否继续？")
                .setPositiveButton("同意", (dialog, which) -> {
                    approveApplication(app);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void approveApplication(RentApplication app) {
        // 1. Update Application Status
        rentingDao.updateApplicationStatus(app.getApplyId(), 2, null);

        // 2. Generate Contract
        House house = rentingDao.getHouse(app.getHouseId());
        if (house != null) {
            Contract contract = new Contract();
            contract.setTenantId(app.getTenantId());
            contract.setLandlordId(userId);
            contract.setHouseId(app.getHouseId());
            contract.setRent(house.getRent());
            contract.setStatus(1); // 1=Signing/Active

            // Date: From Application
            contract.setStartDate(app.getStartDate());
            contract.setEndDate(app.getEndDate());

            long contractId = rentingDao.createContract(contract);
            
            // 3. Update House Status (Optional: Maybe set to Rented? Or keep Rentable until signed?)
            // Requirement says "Generate Contract". Let's update House status to Rented (2)
            // And link contract ID
            rentingDao.updateHouseStatus(house.getHouseId(), 2, (int) contractId);

            Toast.makeText(this, "已同意并生成合同", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "房源信息异常，合同生成失败", Toast.LENGTH_SHORT).show();
        }

        loadData();
    }

    private void showRejectDialog(RentApplication app) {
        new AlertDialog.Builder(this)
                .setTitle("确认拒绝")
                .setMessage("是否拒绝该租房申请？")
                .setPositiveButton("拒绝", (dialog, which) -> {
                    rejectApplication(app);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void rejectApplication(RentApplication app) {
        rentingDao.updateApplicationStatus(app.getApplyId(), 3, "房东拒绝");
        Toast.makeText(this, "已拒绝申请", Toast.LENGTH_SHORT).show();
        loadData();
    }
}
