package com.example.doancuoiky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvFullName, tvEmail;
    private Button btnApproveProducts, btnMyProducts, btnLogout;

    private AppDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new AppDatabaseHelper(this);

        // Ánh xạ views
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        btnApproveProducts = findViewById(R.id.btnApproveProducts);
        btnMyProducts = findViewById(R.id.btnMyProducts);
        btnLogout = findViewById(R.id.btnLogout);

        // Tải thông tin người dùng và cài đặt giao diện
        loadUserProfile();

        // Cài đặt các listener
        setupListeners();

        // Cài đặt bottom navigation
        setupBottomNavigation();
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String fullName = prefs.getString("user_fullname", "Khách");
        String role = prefs.getString("user_role", "");
        // Lấy email từ DB vì chưa lưu trong SharedPreferences
        int userId = prefs.getInt("user_id", -1);
        String email = dbHelper.getUserEmailById(userId);

        // Hiển thị thông tin
        tvFullName.setText(fullName);
        tvEmail.setText(email);

        // Ẩn/hiện nút duyệt sản phẩm dựa trên vai trò
        if ("admin".equals(role)) {
            btnApproveProducts.setVisibility(View.VISIBLE);
        } else {
            btnApproveProducts.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        btnApproveProducts.setOnClickListener(v -> {
            startActivity(new Intent(this, PendingProductsActivity.class));
        });

        btnMyProducts.setOnClickListener(v -> {
            startActivity(new Intent(this, MyProductsActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            // Xóa thông tin đã lưu trong SharedPreferences
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // Chuyển về màn hình Login
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_profile);

        bottomNav.setOnItemSelectedListener(item -> {
             int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_my_products) {
                 startActivity(new Intent(this, MyProductsActivity.class));
                return true;
            } else if (itemId == R.id.navigation_sell) {
                startActivity(new Intent(this, SellActivity.class));
                return true;
            } else if (itemId == R.id.navigation_profile) {
                 return true; // Đã ở trang này
            }
            return false;
        });
    }
}
