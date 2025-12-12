package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_sell); // Đánh dấu item Sell là đang chọn

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(SellActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Đóng SellActivity
                return true;
            } else if (itemId == R.id.navigation_sell) {
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent intent = new Intent(SellActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish(); // Đóng SellActivity
                return true;
            }
            // Các item khác (Chat) có thể thêm logic sau
            return false;
        });
    }
}