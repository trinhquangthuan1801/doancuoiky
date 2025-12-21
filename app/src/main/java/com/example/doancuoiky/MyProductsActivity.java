package com.example.doancuoiky;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyProductsActivity extends AppCompatActivity {

    private RecyclerView rvMyProducts;
    private MyProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Product> myProductList;
    private String currentUsername;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        // --- LẤY USERNAME TỪ SHAREDPREFERENCES (cách làm đúng) ---
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUsername = prefs.getString("user_name", null);

        if (currentUsername == null) {
            Toast.makeText(this, "Lỗi: không xác thực được người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        rvMyProducts = findViewById(R.id.rvMyProducts);
        rvMyProducts.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        loadMyProducts();
    }

    private void loadMyProducts() {
        // Luôn lấy sản phẩm của người dùng đang đăng nhập
        myProductList = dbHelper.getProductsByOwner(currentUsername);
        adapter = new MyProductAdapter(myProductList);
        rvMyProducts.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại danh sách khi quay lại màn hình
        loadMyProducts();
    }
}
