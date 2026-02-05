package com.example.rentingsystem.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {
    // 可编辑字段
    private TextInputEditText etUsername, etPassword, etRealName, etPhone;
    // 只读字段
    private TextInputEditText etUserId, etIdentityType;
    private Button btnSubmit;
    private RentingDao rentingDao;
    private int UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Compiler EdgeToEdge = null;
        EdgeToEdge.enable();
        setContentView(R.layout.edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");  // 清空默认标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etRealName = findViewById(R.id.et_real_name);
        etPhone = findViewById(R.id.et_phone);
        etUserId = findViewById(R.id.et_user_id);
        etIdentityType = findViewById(R.id.et_identity_type);
        btnSubmit=findViewById(R.id.btn_save);
        btnSubmit.setOnClickListener(v -> {
            // 【可选】添加日志验证：确认点击事件是否触发（排查用）
            Log.d("btnClick", "保存修改按钮被点击，开始执行updateUserInfo");
            updateUserInfo();
        });
        rentingDao = new RentingDao(this);
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        UserId = sp.getInt("user_id", -1);
        loadUserData();
    }
    private void updateUserInfo() {
        User user = new User();
        user.setUserId(Integer.parseInt(etUserId.getText().toString().trim()));
        user.setUsername(etUsername.getText().toString().trim());
        user.setPassword(etPassword.getText().toString().trim());
        user.setRealName(etRealName.getText().toString().trim());
        user.setPhone(etPhone.getText().toString().trim());
        Log.d("psw", user.getPassword()); // 自动调用重写的toString()，打印所有属性
        rentingDao = new RentingDao(this);
        int result = rentingDao.updateUser(user);
        if (result > 0) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
        }
}

    private void loadUserData() {
        // 使用RentingDao获取用户信息
        User user = rentingDao.getUser(UserId);
        if (user == null) {
            Toast.makeText(this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etUserId.setText(String.valueOf(user.getUserId()));
        etUsername.setText(user.getUsername());
        etPassword.setText(user.getPassword());
        etRealName.setText(user.getRealName());
        etPhone.setText(user.getPhone());
        etIdentityType.setText(getIdentityTypeName(user.getIdentityType()));
    }

    private String getIdentityTypeName(int type) {
        switch (type) {
            case 1: return "租户";
            case 2: return "房东";
            case 3: return "管理员";
            default: return "未知";
        }
    }
}