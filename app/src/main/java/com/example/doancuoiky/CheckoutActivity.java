package com.example.doancuoiky;

import android.os.Bundle;
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
            // Khi người dùng nhấn nút back, kết thúc Activity này và quay lại màn hình trước
            finish();
        });
    }
}
