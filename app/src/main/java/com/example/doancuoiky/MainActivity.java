package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;
    private ChipGroup chipGroup;
    private String currentRole; 
    private String currentUsername;
    private ImageView ivNotification; // Thêm icon chuông

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lấy role và username từ Intent
        currentRole = getIntent().getStringExtra("ROLE");
        currentUsername = getIntent().getStringExtra("USERNAME");

        if (currentRole == null) {
            currentRole = "user";
        }
        if (currentUsername == null) {
            currentUsername = "guest";
        }

        // Ánh xạ icon chuông và cài đặt sự kiện click
        ivNotification = findViewById(R.id.ivNotification);
        ivNotification.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);
        
        // Tạo dữ liệu mẫu nếu database rỗng (chạy lần đầu)
        dbHelper.createDefaultDataIfEmpty();

        // Khởi tạo RecyclerView
        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        // Lấy dữ liệu mặc định (tất cả sản phẩm đã duyệt)
        loadProducts();

        // Xử lý ChipGroup (Categories)
        chipGroup = findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                loadProducts();
            } else {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chipAll) {
                    loadProducts();
                } else if (checkedId == R.id.chipElectronics) {
                    loadProductsByCategory("Electronics");
                } else if (checkedId == R.id.chipClothes) {
                    loadProductsByCategory("Personal Items");
                } else if (checkedId == R.id.chipBooks) {
                    loadProductsByCategory("Books");
                }
            }
        });

        // Xử lý Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_sell) {
                Intent intent = new Intent(MainActivity.this, SellActivity.class);
                intent.putExtra("ROLE", currentRole); 
                intent.putExtra("USERNAME", currentUsername);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("ROLE", currentRole); 
                intent.putExtra("USERNAME", currentUsername); 
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Integer> checkedIds = chipGroup.getCheckedChipIds();
        if (checkedIds.isEmpty()) {
             loadProducts();
        } else {
            int checkedId = checkedIds.get(0);
             if (checkedId == R.id.chipAll) loadProducts();
             else if (checkedId == R.id.chipElectronics) loadProductsByCategory("Electronics");
             else if (checkedId == R.id.chipClothes) loadProductsByCategory("Personal Items");
             else if (checkedId == R.id.chipBooks) loadProductsByCategory("Books");
        }
    }

    private void loadProducts() {
        productList = dbHelper.getAllApprovedProducts();
        updateAdapter();
    }

    private void loadProductsByCategory(String category) {
        productList = dbHelper.getApprovedProductsByCategory(category);
        updateAdapter();
    }

    private void updateAdapter() {
        productAdapter = new ProductAdapter(productList);
        rvProducts.setAdapter(productAdapter);
    }
}