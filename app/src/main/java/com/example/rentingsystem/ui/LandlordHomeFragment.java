package com.example.rentingsystem.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.House;

import java.util.List;

public class LandlordHomeFragment extends Fragment {

    private RecyclerView rvHouseList;
    private TextView tvEmpty;
    private EditText etSearch;
    private HouseAdapter adapter;
    private RentingDao rentingDao;
    private int landlordId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landlord_home, container, false);
        
        Button btnAddHouse = view.findViewById(R.id.btn_add_house);
        rvHouseList = view.findViewById(R.id.rv_house_list);
        tvEmpty = view.findViewById(R.id.tv_empty);
        etSearch = view.findViewById(R.id.et_search);
        
        rentingDao = new RentingDao(getContext());
        SharedPreferences sp = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        landlordId = sp.getInt("user_id", -1);

        btnAddHouse.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddHouseActivity.class);
            startActivity(intent);
        });

        rvHouseList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadData("");
        
        return view;
    }

    private void loadData(String keyword) {
        if (landlordId == -1) return;

        List<House> houseList;
        if (keyword == null || keyword.isEmpty()) {
            houseList = rentingDao.getHousesByLandlord(landlordId);
        } else {
            houseList = rentingDao.searchLandlordHouses(landlordId, keyword);
        }

        if (houseList == null || houseList.isEmpty()) {
            rvHouseList.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvHouseList.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            
            adapter = new HouseAdapter(getContext(), houseList);
            adapter.setOnItemClickListener(house -> {
                Intent intent = new Intent(getContext(), HouseDetailActivity.class);
                intent.putExtra(HouseDetailActivity.EXTRA_HOUSE_ID, house.getHouseId());
                startActivity(intent);
            });
            rvHouseList.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (etSearch != null) {
            loadData(etSearch.getText().toString());
        } else {
            loadData("");
        }
    }
}
