package com.example.rentingsystem.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.House;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddHouseActivity extends AppCompatActivity {

    private EditText etTitle, etAddress, etRent, etArea, etHouseType;
    private TextView tvImagePath;
    private Button btnSelectImage, btnSubmit;
    private RentingDao rentingDao;
    private String selectedImagePath;
    private int landlordId;

    // List of drawable resource names to simulate house images
    private static final String[] DRAWABLE_NAMES = {
        "house_1", // Orange
        "house_2", // Green
        "house_3", // Blue
        "ic_launcher_background" // Default Android icon background
    };

    private static final String[] DRAWABLE_TITLES = {
        "温馨客厅 (house_1)",
        "舒适卧室 (house_2)",
        "现代厨房 (house_3)",
        "默认图标 (ic_launcher)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        rentingDao = new RentingDao(this);
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        landlordId = sp.getInt("user_id", -1);

        initView();
    }

    private void initView() {
        etTitle = findViewById(R.id.et_title);
        etAddress = findViewById(R.id.et_address);
        etRent = findViewById(R.id.et_rent);
        etArea = findViewById(R.id.et_area);
        etHouseType = findViewById(R.id.et_house_type);
        tvImagePath = findViewById(R.id.tv_image_path);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnSubmit = findViewById(R.id.btn_submit);
        
        btnSelectImage.setText("选择图片 (res/drawable)");

        btnSelectImage.setOnClickListener(v -> showImageSelectionDialog());

        btnSubmit.setOnClickListener(v -> submitHouse());
    }

    private void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择房源图片");
        builder.setItems(DRAWABLE_TITLES, (dialog, which) -> {
            // Store the resource name (e.g., "house_1")
            // In a real app with file upload, this would be a file path.
            // Here we store the resource identifier string.
            selectedImagePath = DRAWABLE_NAMES[which];
            tvImagePath.setText("已选择: " + DRAWABLE_TITLES[which]);
        });
        builder.show();
    }

    private void submitHouse() {
        String title = etTitle.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String rentStr = etRent.getText().toString().trim();
        String areaStr = etArea.getText().toString().trim();
        String houseType = etHouseType.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(address) || 
            TextUtils.isEmpty(rentStr) || TextUtils.isEmpty(areaStr) || 
            TextUtils.isEmpty(houseType)) {
            Toast.makeText(this, "请填写所有必填项", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Image is optional per requirement description (user says "can get image to select", implying optional or mandatory? 
        // usually mandatory if mentioned. I'll make it optional but recommended, or just proceed if empty)
        // Let's assume mandatory for "Add House Function" context
        if (TextUtils.isEmpty(selectedImagePath)) {
             Toast.makeText(this, "请选择房源图片", Toast.LENGTH_SHORT).show();
             return;
        }

        House house = new House();
        house.setLandlordId(landlordId);
        house.setTitle(title);
        house.setAddress(address);
        house.setRent(Double.parseDouble(rentStr));
        house.setArea(Double.parseDouble(areaStr));
        house.setHouseType(houseType);
        house.setImagePath(selectedImagePath);
        house.setStatus(1); // Rentable

        long result = rentingDao.addHouse(house);
        if (result > 0) {
            Toast.makeText(this, "房源发布成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "发布失败", Toast.LENGTH_SHORT).show();
        }
    }
}
