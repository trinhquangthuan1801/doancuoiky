package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private String currentRole;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentRole = getIntent().getStringExtra("ROLE");
        currentUsername = getIntent().getStringExtra("USERNAME");
        if (currentRole == null) currentRole = "user";
        if (currentUsername == null) currentUsername = "admin";
        // Cập nhật tên hiển thị trên Profile
        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(currentUsername);
        // Xử lý hiển thị menu Admin
        LinearLayout llApproveProducts = findViewById(R.id.llApproveProducts);
        if ("admin".equals(currentRole)) {
            llApproveProducts.setVisibility(View.VISIBLE);
            llApproveProducts.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, PendingProductsActivity.class);
                startActivity(intent);
            });
        } else {
            llApproveProducts.setVisibility(View.GONE);
        }
        // Xử lý click "Sản phẩm của tôi"
        LinearLayout llMyProducts = findViewById(R.id.llMyProducts);
        llMyProducts.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyProductsActivity.class);
            intent.putExtra("USERNAME", currentUsername);
            startActivity(intent);
        });
        // Bottom Navigation logic
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.putExtra("ROLE", currentRole);
                intent.putExtra("USERNAME", currentUsername);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_sell) {
                Intent intent = new Intent(ProfileActivity.this, SellActivity.class);
                intent.putExtra("ROLE", currentRole);
                intent.putExtra("USERNAME", currentUsername);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                return true;
            }
            return false;
        });
        // Logout logic
        LinearLayout llLogout = findViewById(R.id.llLogout);
        llLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}