package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Nhận dữ liệu từ Intent
        String name = getIntent().getStringExtra("NAME");
        String price = getIntent().getStringExtra("PRICE");
        int imageRes = getIntent().getIntExtra("IMAGE", R.drawable.ic_launcher_background);
        String category = getIntent().getStringExtra("CATEGORY");
        int quantity = getIntent().getIntExtra("QUANTITY", 0);

        // Ánh xạ View
        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        ImageView ivBack = findViewById(R.id.ivBack);
        TextView tvDetailName = findViewById(R.id.tvDetailName);
        TextView tvDetailPrice = findViewById(R.id.tvDetailPrice);
        TextView tvDetailCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDetailQuantity = findViewById(R.id.tvDetailQuantity);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        Button btnBuyNow = findViewById(R.id.btnBuyNow);

        // Hiển thị dữ liệu
        ivDetailImage.setImageResource(imageRes);
        tvDetailName.setText(name != null ? name : "N/A");
        tvDetailPrice.setText(price != null ? price : "$0");
        tvDetailCategory.setText(String.format("Category: %s", category != null ? category : "N/A"));
        tvDetailQuantity.setText(String.format("Số lượng: %d", quantity));

        // Xử lý sự kiện
        ivBack.setOnClickListener(v -> finish());

        btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        // SỬA LẠI NÚT MUA NGAY
        btnBuyNow.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
            // Chúng ta sẽ truyền dữ liệu sản phẩm qua intent ở bước sau
            startActivity(intent);
        });
    }
}