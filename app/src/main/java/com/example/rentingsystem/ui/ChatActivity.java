package com.example.rentingsystem.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.Message;
import com.example.rentingsystem.model.User;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_TARGET_USER_ID = "extra_target_user_id";

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private EditText etMessage;
    private Button btnSend;
    
    private RentingDao rentingDao;
    private int currentUserId;
    private int targetUserId;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        rentingDao = new RentingDao(this);
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sp.getInt("user_id", -1);
        
        targetUserId = getIntent().getIntExtra(EXTRA_TARGET_USER_ID, -1);
        if (targetUserId == -1) {
            Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initView();
        loadMessages();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        
        TextView tvTitle = findViewById(R.id.tv_title);
        User targetUser = rentingDao.getUser(targetUserId);
        if (targetUser != null) {
            tvTitle.setText(targetUser.getRealName());
        } else {
            tvTitle.setText("聊天");
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        messageList = rentingDao.getChatHistory(currentUserId, targetUserId);
        adapter = new ChatAdapter(messageList, currentUserId);
        recyclerView.setAdapter(adapter);
        if (messageList.size() > 0) {
            recyclerView.scrollToPosition(messageList.size() - 1);
        }
    }

    private void sendMessage() {
        String content = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        
        rentingDao.createMessage(currentUserId, targetUserId, content);
        etMessage.setText("");
        loadMessages();
    }
}
