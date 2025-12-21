package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Tìm và cài đặt Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Xử lý nút quay lại
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        // **BƯỚC 5.2: NHẬN VÀ HIỂN THỊ DỮ LIỆU**

        // Ánh xạ các View
        ImageView ivProductImage = findViewById(R.id.iv_product_image);
        TextView tvProductName = findViewById(R.id.tv_product_name);
        TextView tvProductPrice = findViewById(R.id.tv_product_price);
        TextView tvTotalPrice = findViewById(R.id.tv_total_price); // Thêm TextView cho tổng tiền

        // Nhận Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("PRODUCT_NAME");
        String productPrice = intent.getStringExtra("PRODUCT_PRICE");
        int productImageRes = intent.getIntExtra("PRODUCT_IMAGE", R.drawable.ic_image_placeholder);

        // Gán dữ liệu lên View
        ivProductImage.setImageResource(productImageRes);
        tvProductName.setText(productName);
        tvProductPrice.setText(productPrice);
        
        // Hiển thị tổng tiền ở thanh dưới cùng
        tvTotalPrice.setText(String.format("Tổng cộng: %s", productPrice));
    }
}
