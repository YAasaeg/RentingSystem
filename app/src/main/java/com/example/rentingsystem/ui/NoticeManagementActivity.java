package com.example.rentingsystem.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.Notice;
import com.example.rentingsystem.model.User;

import java.util.List;

public class NoticeManagementActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Button btnPublish;
    private RentingDao rentingDao;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_management);

        rentingDao = new RentingDao(this);
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sp.getInt("user_id", -1);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");  // 清空默认标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        btnPublish = findViewById(R.id.btn_publish);

        btnPublish.setOnClickListener(v -> publishNotice());
    }

    private void publishNotice() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请填写完整公告信息", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get all tenants linked to this landlord
        List<User> tenants = rentingDao.getLandlordTenants(currentUserId);
        
        if (tenants.isEmpty()) {
            Toast.makeText(this, "暂无租客可发送公告", Toast.LENGTH_SHORT).show();
            return;
        }

        int count = 0;
        for (User tenant : tenants) {
            Notice notice = new Notice();
            notice.setPublisherId(currentUserId);
            notice.setTitle(title);
            notice.setContent(content);
            notice.setReceiverId(tenant.getUserId());
            
            rentingDao.createNotice(notice);
            count++;
        }

        Toast.makeText(this, "已成功发送给 " + count + " 位租客", Toast.LENGTH_SHORT).show();
        finish();
    }
}
