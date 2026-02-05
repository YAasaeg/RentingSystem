package com.example.rentingsystem.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.House;
import com.example.rentingsystem.model.Reservation;
import com.example.rentingsystem.model.RentApplication;
import com.example.rentingsystem.model.User;

import android.app.AlertDialog;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HouseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_HOUSE_ID = "extra_house_id";
    private EditText etReserveTime;
    private ImageView ivImage;
    private TextView tvTitle, tvRent, tvType, tvArea, tvAddress, tvStatus;
    private LinearLayout layoutTenantActions, layoutLandlordActions;
    private Button btnReserve, btnApply, btnBack,btnContact;
    
    private RentingDao rentingDao;
    private int houseId;
    private int userId;
    private int identityType; // 1=Tenant, 2=Landlord

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        rentingDao = new RentingDao(this);
        
        // Get Intent Data
        houseId = getIntent().getIntExtra(EXTRA_HOUSE_ID, -1);
        
        // Get User Info
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);
        identityType = sp.getInt("identity_type", 1);

        initView();
        loadData();
    }

    private void initView() {
        ivImage = findViewById(R.id.iv_house_image);
        tvTitle = findViewById(R.id.tv_house_title);
        tvRent = findViewById(R.id.tv_house_rent);
        tvType = findViewById(R.id.tv_house_type);
        tvArea = findViewById(R.id.tv_house_area);
        tvAddress = findViewById(R.id.tv_house_address);
        tvStatus = findViewById(R.id.tv_house_status);
        
        layoutTenantActions = findViewById(R.id.layout_tenant_actions);
        layoutLandlordActions = findViewById(R.id.layout_landlord_actions);
        
        btnReserve = findViewById(R.id.btn_reserve);
        btnApply = findViewById(R.id.btn_apply);
        btnBack = findViewById(R.id.btn_back);
        btnContact=findViewById(R.id.btn_contact);
        // Role-based visibility
        if (identityType == 1) { // Tenant
            layoutTenantActions.setVisibility(View.VISIBLE);
            layoutLandlordActions.setVisibility(View.GONE);
        } else { // Landlord
            layoutTenantActions.setVisibility(View.GONE);
            layoutLandlordActions.setVisibility(View.VISIBLE);
        }

        // Button Actions
        btnContact.setOnClickListener(v->{
            House house = rentingDao.getHouse(houseId);
            if (house != null) {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra(ChatActivity.EXTRA_TARGET_USER_ID, house.getLandlordId());
                startActivity(intent);
            }
        });
        btnReserve.setOnClickListener(v -> showReserveDialog());

        btnApply.setOnClickListener(v -> {
            showApplyDialog();
        });
        
        btnBack.setOnClickListener(v -> finish());
    }



    private void showReserveDialog() {
        // 创建 Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("申请预约");
        View view = getLayoutInflater().inflate(R.layout.dialog_reservation, null);
        EditText Date = view.findViewById(R.id.date);
        // Date Picker Setup
        Date.setOnClickListener(v -> showDatePicker(Date));

        // 提交预约

        builder.setView(view);

        builder.setPositiveButton("提交预约", (dialog, which) -> {
            Reservation res = new Reservation();
            res.setTenantId(userId);
            res.setHouseId(houseId);
            res.setReserveTime(Date.getText().toString());
            long result = rentingDao.addReservation(res);
            if (result > 0) {
                Toast.makeText(this, "预约申请已提交", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // 关闭对话框
            } else {
                Toast.makeText(this, "预约提交失败", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void showApplyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("申请租房");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_apply_rent, null);
        TextView tvHouseInfo = view.findViewById(R.id.tv_house_info);
        TextView tvUserInfo = view.findViewById(R.id.tv_user_info);
        EditText etRemark = view.findViewById(R.id.et_remark);
        EditText etStartDate = view.findViewById(R.id.et_start_date);
        EditText etEndDate = view.findViewById(R.id.et_end_date);

        // Date Picker Setup
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        // Fetch Data
        House house = rentingDao.getHouse(houseId);
        User user = rentingDao.getUser(userId);

        if (house != null) {
            tvHouseInfo.setText("标题: " + house.getTitle() + "\n地址: " + house.getAddress() + "\n租金: " + house.getRent());
        }
        if (user != null) {
            tvUserInfo.setText("姓名: " + user.getRealName() + "\n电话: " + user.getPhone());
        }

        builder.setView(view);

        builder.setPositiveButton("提交申请", (dialog, which) -> {
            String remark = etRemark.getText().toString();
            String startDate = etStartDate.getText().toString();
            String endDate = etEndDate.getText().toString();

            if (TextUtils.isEmpty(remark) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
                Toast.makeText(this, "请填写完整申请信息", Toast.LENGTH_SHORT).show();
                return;
            }
            submitApplication(remark, startDate, endDate);
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            editText.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void submitApplication(String remark, String startDate, String endDate) {
        RentApplication app = new RentApplication();
        app.setTenantId(userId);
        app.setHouseId(houseId);
        app.setApplyReason(remark);
        app.setStartDate(startDate);
        app.setEndDate(endDate);

        long result = rentingDao.addApplication(app);
        if (result > 0) {
            Toast.makeText(this, "申请已提交，等待房东审核", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "申请提交失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        if (houseId == -1) {
            Toast.makeText(this, "房源不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        House house = rentingDao.getHouse(houseId);
        if (house == null) {
            Toast.makeText(this, "房源信息获取失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle.setText(house.getTitle());
        tvRent.setText("¥ " + house.getRent() + " / 月");
        tvType.setText(house.getHouseType());
        tvArea.setText(house.getArea() + "㎡");
        tvAddress.setText(house.getAddress());

        // Status
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
        tvStatus.setText(statusStr);
        GradientDrawable bg = (GradientDrawable) tvStatus.getBackground();
        bg.setColor(ContextCompat.getColor(this, colorRes));

        // Image
        if (!TextUtils.isEmpty(house.getImagePath())) {
            int resId = getResources().getIdentifier(house.getImagePath(), "drawable", getPackageName());
            if (resId != 0) {
                ivImage.setImageResource(resId);
            } else {
                ivImage.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            ivImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
