package com.example.doancuoiky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyProductsActivity extends AppCompatActivity {

    private RecyclerView rvMyProducts;
    private MyProductCursorAdapter adapter;
    private AppDatabaseHelper dbHelper;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        dbHelper = new AppDatabaseHelper(this);

        // Lấy ID người dùng từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Nút Back
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        // Cấu hình RecyclerView
        rvMyProducts = findViewById(R.id.rvMyProducts);
        rvMyProducts.setLayoutManager(new LinearLayoutManager(this));

        setupRecyclerView();
        setupBottomNavigation();
    }

    private void setupRecyclerView() {
        Cursor cursor = dbHelper.getProductsByOwner(currentUserId);
        adapter = new MyProductCursorAdapter(this, cursor);
        rvMyProducts.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_my_products);
        bottomNav.setOnItemSelectedListener(item -> {
             int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_my_products) {
                return true; // Đã ở trang này
            } else if (itemId == R.id.navigation_sell) {
                startActivity(new Intent(this, SellActivity.class));
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại dữ liệu khi quay lại màn hình
        Cursor newCursor = dbHelper.getProductsByOwner(currentUserId);
        if (adapter != null) {
            adapter.swapCursor(newCursor);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.mCursor != null) {
            adapter.mCursor.close();
        }
    }
}
