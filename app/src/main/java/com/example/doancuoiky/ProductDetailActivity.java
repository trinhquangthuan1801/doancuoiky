package com.example.doancuoiky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dbHelper = new DatabaseHelper(this);

        // --- NHẬN DỮ LIỆU TỪ INTENT ---
        Intent intent = getIntent();
        int productId = intent.getIntExtra("PRODUCT_ID", -1);
        String name = intent.getStringExtra("PRODUCT_NAME");
        String price = intent.getStringExtra("PRODUCT_PRICE");
        String imagePath = intent.getStringExtra("PRODUCT_IMAGE_PATH");
        String category = intent.getStringExtra("PRODUCT_CATEGORY");
        String owner = intent.getStringExtra("PRODUCT_OWNER");
        String description = intent.getStringExtra("PRODUCT_DESCRIPTION");

        // --- ÁNH XẠ VIEW ---
        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivCart = findViewById(R.id.ivCart); // Ánh xạ icon giỏ hàng
        TextView tvDetailName = findViewById(R.id.tvDetailName);
        TextView tvDetailPrice = findViewById(R.id.tvDetailPrice);
        TextView tvDetailCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDetailOwner = findViewById(R.id.tvDetailOwner);
        TextView tvDetailDescription = findViewById(R.id.tvDetailDescription);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        Button btnBuyNow = findViewById(R.id.btnBuyNow);

        // --- HIỂN THỊ DỮ LIỆU ---
        Glide.with(this).load(imagePath).placeholder(R.drawable.ic_image_placeholder).into(ivDetailImage);
        tvDetailName.setText(name != null ? name : "N/A");
        tvDetailPrice.setText(price != null ? price : "đ0");
        tvDetailCategory.setText(String.format("Danh mục: %s", category != null ? category : "N/A"));
        tvDetailOwner.setText(String.format("Người bán: %s", owner != null ? owner : "N/A"));
        tvDetailDescription.setText(description != null ? description : "Không có mô tả.");

        // --- XỬ LÝ SỰ KIỆN ---
        ivBack.setOnClickListener(v -> finish());

        // Sự kiện click cho icon giỏ hàng
        ivCart.setOnClickListener(v -> {
            Intent cartIntent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(cartIntent);
        });

        btnAddToCart.setOnClickListener(v -> {
            if (productId != -1) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                String currentUsername = sharedPreferences.getString("user_name", null);

                if (currentUsername != null) {
                    dbHelper.addToCart(productId, currentUsername);
                    Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút "Mua Ngay"
        btnBuyNow.setOnClickListener(v -> {
            Intent checkoutIntent = new Intent(ProductDetailActivity.this, CheckoutActivity.class);

            checkoutIntent.putExtra("PRODUCT_ID", productId);
            checkoutIntent.putExtra("PRODUCT_NAME", name);
            checkoutIntent.putExtra("PRODUCT_PRICE", price);
            checkoutIntent.putExtra("PRODUCT_IMAGE_PATH", imagePath);
            checkoutIntent.putExtra("PRODUCT_OWNER", owner);

            startActivity(checkoutIntent);
        });
    }
}
