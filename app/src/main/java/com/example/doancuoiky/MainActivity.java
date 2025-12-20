package com.example.doancuoiky;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductCursorAdapter productAdapter;
    private AppDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new AppDatabaseHelper(this);

        // Khởi tạo RecyclerView
        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        // Lấy dữ liệu sản phẩm và hiển thị
        setupRecyclerView();

        // Xử lý Bottom Navigation
        setupBottomNavigation();
    }

    private void setupRecyclerView() {
        Cursor cursor = dbHelper.getAllActiveProducts(); // Lấy sản phẩm đã được duyệt
        productAdapter = new ProductCursorAdapter(this, cursor);
        rvProducts.setAdapter(productAdapter);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                return true; // Đã ở trang này
            } else if (itemId == R.id.navigation_my_products) {
                startActivity(new Intent(this, MyProductsActivity.class));
                return true;
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
        // Cập nhật lại danh sách sản phẩm mỗi khi quay lại MainActivity
        // để đảm bảo dữ liệu luôn mới (ví dụ: sau khi admin duyệt sản phẩm)
        Cursor newCursor = dbHelper.getAllActiveProducts();
        productAdapter.swapCursor(newCursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng cursor khi activity bị hủy để tránh rò rỉ bộ nhớ
        if (productAdapter != null && productAdapter.mCursor != null) {
            productAdapter.mCursor.close();
        }
    }
}
