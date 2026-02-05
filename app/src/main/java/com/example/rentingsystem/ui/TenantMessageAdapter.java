package com.example.rentingsystem.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TenantMessageAdapter extends FragmentStateAdapter {

    public TenantMessageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new NoticeListFragment();
        }
        return new ChatListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
