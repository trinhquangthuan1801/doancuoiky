package com.example.doancuoiky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
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
    private ImageView ivNotification;
    private ImageView ivCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- QUẢN LÝ PHIÊN LÀM VIỆC BẰNG SHAREDPREFERENCES ---
        Intent intent = getIntent();
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        if (intent.hasExtra("ROLE") && intent.hasExtra("USERNAME")) {
            currentRole = intent.getStringExtra("ROLE");
            currentUsername = intent.getStringExtra("USERNAME");

            // Lưu ngay vào SharedPreferences để dùng cho toàn bộ phiên làm việc
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_role", currentRole);
            editor.putString("user_name", currentUsername);
            editor.apply();
        } else {
            // Nếu không có intent (vd: quay lại từ màn hình khác), đọc từ SharedPreferences
            currentRole = prefs.getString("user_role", "user");
            currentUsername = prefs.getString("user_name", "guest");
        }

        // Ánh xạ icon chuông và giỏ hàng
        ivNotification = findViewById(R.id.ivNotification);
        ivCart = findViewById(R.id.ivCart);

        ivNotification.setOnClickListener(v -> {
            Intent notificationIntent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(notificationIntent);
        });

        ivCart.setOnClickListener(v -> {
            Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(cartIntent);
        });

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

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
                Intent sellIntent = new Intent(MainActivity.this, SellActivity.class);
                startActivity(sellIntent);
                return true;
            } else if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_chat) {
                Intent chatIntent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(chatIntent);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProductsBasedOnChipSelection();
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

    private void loadProductsBasedOnChipSelection(){
        List<Integer> checkedIds = chipGroup.getCheckedChipIds();
        if (checkedIds.isEmpty() || checkedIds.get(0) == R.id.chipAll) {
            loadProducts();
        } else {
            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chipElectronics) {
                loadProductsByCategory("Electronics");
            } else if (checkedId == R.id.chipClothes) {
                loadProductsByCategory("Personal Items");
            } else if (checkedId == R.id.chipBooks) {
                loadProductsByCategory("Books");
            }
        }
    }
}
