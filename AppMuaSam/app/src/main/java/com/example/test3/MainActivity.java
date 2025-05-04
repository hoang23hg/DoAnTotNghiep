package com.example.test3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.test3.User.CaNhanFragment;
import com.example.test3.User.DonHangFragment;
import com.example.test3.User.GioHangFragment;
import com.example.test3.User.TrangChuFragment;
import com.example.test3.User.YeuThichFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new TrangChuFragment();
            } else if (id == R.id.nav_cart) {
                selectedFragment = new GioHangFragment();
            } else if (id == R.id.nav_favorite) {
                selectedFragment = new YeuThichFragment();
            } else if (id == R.id.nav_orders) {
                selectedFragment = new DonHangFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new CaNhanFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });


        // Load mặc định TrangChuFragment khi mở app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new TrangChuFragment())
                    .commit();
        }
    }
}
