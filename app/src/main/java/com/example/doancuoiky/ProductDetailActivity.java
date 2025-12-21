package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // --- NHẬN DỮ LIỆU MỚI TỪ INTENT ---
        Intent intent = getIntent();
        String name = intent.getStringExtra("PRODUCT_NAME");
        String price = intent.getStringExtra("PRODUCT_PRICE");
        String imagePath = intent.getStringExtra("PRODUCT_IMAGE_PATH");
        String category = intent.getStringExtra("PRODUCT_CATEGORY");
        String owner = intent.getStringExtra("PRODUCT_OWNER");
        String description = intent.getStringExtra("PRODUCT_DESCRIPTION");

        // --- ÁNH XẠ VIEW MỚI ---
        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        ImageView ivBack = findViewById(R.id.ivBack);
        TextView tvDetailName = findViewById(R.id.tvDetailName);
        TextView tvDetailPrice = findViewById(R.id.tvDetailPrice);
        TextView tvDetailCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDetailOwner = findViewById(R.id.tvDetailOwner);           // View mới
        TextView tvDetailDescription = findViewById(R.id.tvDetailDescription); // View mới
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        Button btnBuyNow = findViewById(R.id.btnBuyNow);

        // --- HIỂN THỊ DỮ LIỆU MỚI ---

        // Dùng Glide để hiển thị ảnh từ đường dẫn file
        Glide.with(this)
                .load(imagePath)
                .placeholder(R.drawable.ic_image_placeholder) // Ảnh hiển thị trong lúc tải
                .into(ivDetailImage);

        tvDetailName.setText(name != null ? name : "N/A");
        tvDetailPrice.setText(price != null ? price : "$0");
        tvDetailCategory.setText(String.format("Danh mục: %s", category != null ? category : "N/A"));
        tvDetailOwner.setText(String.format("Người bán: %s", owner != null ? owner : "N/A"));
        tvDetailDescription.setText(description != null ? description : "Không có mô tả.");

        // --- XỬ LÝ SỰ KIỆN ---
        ivBack.setOnClickListener(v -> finish());

        btnAddToCart.setOnClickListener(v -> {
            // TODO: Thêm logic cho giỏ hàng ở đây sau
            Toast.makeText(this, "Chức năng giỏ hàng đang phát triển!", Toast.LENGTH_SHORT).show();
        });

        btnBuyNow.setOnClickListener(v -> {
            Intent checkoutIntent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
            
            // Gửi dữ liệu mới sang CheckoutActivity
            checkoutIntent.putExtra("PRODUCT_NAME", name);
            checkoutIntent.putExtra("PRODUCT_PRICE", price);
            checkoutIntent.putExtra("PRODUCT_IMAGE_PATH", imagePath); // Gửi đường dẫn ảnh

            startActivity(checkoutIntent);
        });
    }
}