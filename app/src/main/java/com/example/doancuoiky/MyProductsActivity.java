package com.example.doancuoiky;

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

        // Lấy username từ Intent
        currentUsername = getIntent().getStringExtra("USERNAME");
        if (currentUsername == null) {
            Toast.makeText(this, "Lỗi xác thực người dùng", Toast.LENGTH_SHORT).show();
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
        myProductList = dbHelper.getProductsByOwner(currentUsername);
        adapter = new MyProductAdapter(myProductList);
        rvMyProducts.setAdapter(adapter);
    }
}