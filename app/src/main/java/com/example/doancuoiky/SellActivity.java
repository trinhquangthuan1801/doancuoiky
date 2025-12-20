package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellActivity extends AppCompatActivity {

    private EditText etProductName, etDescription, etPrice;
    private Spinner spinnerCategory;
    private Button btnPost;
    private DatabaseHelper dbHelper;
    private String currentRole;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        // Lấy role và username
        currentRole = getIntent().getStringExtra("ROLE");
        currentUsername = getIntent().getStringExtra("USERNAME");
        
        if (currentRole == null) currentRole = "user";
        if (currentUsername == null) currentUsername = "guest"; // Mặc định nếu lỗi

        dbHelper = new DatabaseHelper(this);

        etProductName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnPost = findViewById(R.id.btnPost);

        String[] categories = {"Electronics", "Personal Items", "Books"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnPost.setOnClickListener(v -> {
            String name = etProductName.getText().toString();
            String price = etPrice.getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (name.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên và giá sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                if (!price.startsWith("$")) {
                    price = "$" + price;
                }

                String status = "pending";
                if ("admin".equals(currentRole)) {
                    status = "approved";
                }

                // Lưu sản phẩm kèm owner và quantity mặc định là 10
                Product newProduct = new Product(name, price, R.drawable.ic_image_placeholder, category, status, currentUsername, 10);
                dbHelper.addProduct(newProduct);

                if ("approved".equals(status)) {
                    Toast.makeText(this, "Đăng bán thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Sản phẩm đang chờ Admin duyệt!", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(SellActivity.this, MainActivity.class);
                intent.putExtra("ROLE", currentRole);
                intent.putExtra("USERNAME", currentUsername);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Bottom Navigation logic
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_sell);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(SellActivity.this, MainActivity.class);
                intent.putExtra("ROLE", currentRole);
                intent.putExtra("USERNAME", currentUsername);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_sell) {
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent intent = new Intent(SellActivity.this, ProfileActivity.class);
                intent.putExtra("ROLE", currentRole);
                intent.putExtra("USERNAME", currentUsername);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
    }
}