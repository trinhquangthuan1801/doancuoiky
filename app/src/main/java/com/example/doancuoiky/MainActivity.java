package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo RecyclerView
        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        // Tạo dữ liệu mẫu
        productList = new ArrayList<>();
        productList.add(new Product("iPhone 14 Pro Max", "$1750", R.drawable.ic_launcher_background));
        productList.add(new Product("MacBook Pro 16", "$3400", R.drawable.ic_launcher_background));
        productList.add(new Product("Áo thun", "$20", R.drawable.ic_launcher_background));
        productList.add(new Product("Sách Lập trình", "$30", R.drawable.ic_launcher_background));
        productList.add(new Product("Giày Nike", "$150", R.drawable.ic_launcher_background));
        productList.add(new Product("Tai nghe Sony", "$250", R.drawable.ic_launcher_background));

        // Thiết lập Adapter
        productAdapter = new ProductAdapter(productList);
        rvProducts.setAdapter(productAdapter);

        // Xử lý Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_home); // Đánh dấu item Home là đang chọn

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_sell) {
                Intent intent = new Intent(MainActivity.this, SellActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_home) {
                return true;
            }
            // Các item khác (Chat, Profile) xử lý tương tự
            return false;
        });
    }
}