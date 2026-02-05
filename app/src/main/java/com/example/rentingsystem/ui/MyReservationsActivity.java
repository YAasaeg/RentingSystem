package com.example.rentingsystem.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.Reservation;

import java.util.List;

public class MyReservationsActivity extends AppCompatActivity {

    private RecyclerView rvList;
    private TextView tvEmpty;
    private RentingDao rentingDao;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        rentingDao = new RentingDao(this);
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);

        rvList = findViewById(R.id.rv_list);
        tvEmpty = findViewById(R.id.tv_empty);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {
        List<Reservation> list = rentingDao.getTenantReservations(userId);
        if (list == null || list.isEmpty()) {
            rvList.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvList.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            ReservationAdapter adapter = new ReservationAdapter(this, list, false);
            rvList.setAdapter(adapter);
        }
    }
}
