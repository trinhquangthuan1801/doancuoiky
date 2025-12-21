package com.example.doancuoiky;

import android.content.Intent;
import android.content.SharedPreferences;
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

        // --- LẤY DỮ LIỆU TỪ SHAREDPREFERENCES ---
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentRole = prefs.getString("user_role", "user");
        currentUsername = prefs.getString("user_name", "guest");

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
            // MyProductsActivity giờ đã tự lấy username, không cần truyền qua Intent nữa
            Intent intent = new Intent(ProfileActivity.this, MyProductsActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation logic (đã được dọn dẹp)
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_sell) {
                startActivity(new Intent(ProfileActivity.this, SellActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_chat) {
                startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
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
            // Xóa SharedPreferences khi đăng xuất
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
