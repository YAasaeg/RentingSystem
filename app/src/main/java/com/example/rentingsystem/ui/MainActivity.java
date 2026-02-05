package com.example.rentingsystem.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.rentingsystem.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private int userId;
    private int identityType; // 1=Tenant, 2=Landlord

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get User Info
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = sp.getInt("user_id", -1);
        identityType = sp.getInt("identity_type", 1);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(getHomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                fragment = getHomeFragment();
            } else if (itemId == R.id.nav_message) {
                if (identityType == 2) {
                    fragment = new LandlordMessageFragment();
                } else {
                    fragment = new TenantMessageFragment();
                }
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment(); // Shared or specific
            }

            return loadFragment(fragment);
        });
        
        // Adjust menu items based on role if needed (e.g. Landlord has "Business" instead of "Message" or different icon/title)
        if (identityType == 2) {
             bottomNav.getMenu().findItem(R.id.nav_home).setTitle("房源管理");
             bottomNav.getMenu().findItem(R.id.nav_message).setTitle("业务处理");
        }
    }

    private Fragment getHomeFragment() {
        if (identityType == 2) {
            return new LandlordHomeFragment();
        } else {
            return new TenantHomeFragment();
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
            return true;
        }
        return false;
    }
}
