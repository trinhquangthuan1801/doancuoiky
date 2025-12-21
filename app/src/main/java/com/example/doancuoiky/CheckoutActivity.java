package com.example.doancuoiky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String productName, productPrice, productImagePath, productOwner;
    private int productId;
    private EditText etAddress, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        dbHelper = new DatabaseHelper(this);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Ánh xạ View
        ImageView ivProductImage = findViewById(R.id.iv_product_image);
        TextView tvProductName = findViewById(R.id.tv_product_name);
        TextView tvProductPrice = findViewById(R.id.tv_product_price);
        TextView tvTotalPrice = findViewById(R.id.tv_total_price);
        etAddress = findViewById(R.id.et_address); // Ô nhập địa chỉ
        etPhone = findViewById(R.id.et_phone);     // Ô nhập SĐT
        Button btnOrder = findViewById(R.id.btn_order); // Nút Đặt hàng

        // Nhận dữ liệu sản phẩm từ Intent (cần có cả ID và Owner)
        Intent intent = getIntent();
        productId = intent.getIntExtra("PRODUCT_ID", -1);
        productName = intent.getStringExtra("PRODUCT_NAME");
        productPrice = intent.getStringExtra("PRODUCT_PRICE");
        productImagePath = intent.getStringExtra("PRODUCT_IMAGE_PATH");
        productOwner = intent.getStringExtra("PRODUCT_OWNER");

        // Kiểm tra dữ liệu sản phẩm
        if (productId == -1 || productOwner == null) {
            Toast.makeText(this, "Lỗi: Dữ liệu sản phẩm không hợp lệ.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Hiển thị thông tin sản phẩm
        tvProductName.setText(productName);
        tvProductPrice.setText(productPrice);
        tvTotalPrice.setText(String.format("Tổng cộng: %s", productPrice));
        Glide.with(this).load(productImagePath).placeholder(R.drawable.ic_image_placeholder).into(ivProductImage);

        // Xử lý sự kiện nhấn nút "Đặt hàng"
        btnOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Kiểm tra thông tin nhập vào
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ địa chỉ và số điện thoại.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thông tin người mua từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String buyerUsername = prefs.getString("user_name", null);

        if (buyerUsername == null) {
            Toast.makeText(this, "Lỗi: Không thể xác định người mua. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            return;
        }

        // Không cho phép tự mua sản phẩm của chính mình
        if (buyerUsername.equals(productOwner)) {
            Toast.makeText(this, "Bạn không thể mua sản phẩm của chính mình.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy ngày giờ hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        // Thực hiện 2 hành động với Database
        // 1. Thêm đơn hàng vào bảng `orders`
        dbHelper.addOrder(productId, buyerUsername, productOwner, productPrice, currentDateAndTime, address, phone);

        // 2. Cập nhật trạng thái sản phẩm thành `sold`
        dbHelper.markProductAsSold(productId);

        // Thông báo và quay về trang chủ
        Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();

        Intent mainIntent = new Intent(CheckoutActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish(); // Đóng màn hình thanh toán
    }
}
