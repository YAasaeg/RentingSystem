package com.example.rentingsystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etRealName, etPhone;
    private RadioGroup rgIdentity;
    private Button btnRegister;
    private RentingDao rentingDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rentingDao = new RentingDao(this);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etRealName = findViewById(R.id.et_real_name);
        etPhone = findViewById(R.id.et_phone);
        rgIdentity = findViewById(R.id.rg_identity);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String realName = etRealName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            int identityType = rgIdentity.getCheckedRadioButtonId() == R.id.rb_tenant ? 1 : 2;

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || 
                TextUtils.isEmpty(realName) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // In real app, hash this!
            user.setRealName(realName);
            user.setPhone(phone);
            user.setIdentityType(identityType);

            long result = rentingDao.registerUser(user);
            if (result > 0) {
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "注册失败，用户名或手机号可能已存在", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
