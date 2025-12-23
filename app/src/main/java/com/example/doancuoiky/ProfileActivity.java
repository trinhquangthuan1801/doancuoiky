package com.example.doancuoiky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private String currentRole;
    private String currentUsername;
    private TextView tvName;
    private TextView tvEmail;

    // Trình khởi chạy để nhận kết quả từ EditProfileActivity
    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Lấy role và username
        currentRole = getIntent().getStringExtra("ROLE");
        currentUsername = getIntent().getStringExtra("USERNAME");
        
        if (currentRole == null) currentRole = "user";
        if (currentUsername == null) currentUsername = "guest";

        // Ánh xạ TextViews
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvName.setText(currentUsername);
        tvEmail.setText(String.format("%s@example.com", currentUsername));

        // Đăng ký trình lắng nghe kết quả
        editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String newUsername = data.getStringExtra("NEW_USERNAME");
                    String newEmail = data.getStringExtra("NEW_EMAIL");

                    // Cập nhật giao diện với dữ liệu mới
                    if (newUsername != null) {
                        currentUsername = newUsername; // Cập nhật biến username
                        tvName.setText(newUsername);
                    }
                    if (newEmail != null) {
                        tvEmail.setText(newEmail);
                    }
                }
            }
        );

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

        // Xử lý click "Chỉnh sửa hồ sơ"
        LinearLayout llEditProfile = findViewById(R.id.llEditProfile);
        llEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("CURRENT_USERNAME", currentUsername);
            intent.putExtra("CURRENT_EMAIL", tvEmail.getText().toString());
            // Sử dụng launcher để mở màn hình
            editProfileLauncher.launch(intent);
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
            } else if (itemId == R.id.navigation_chat) {
                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
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