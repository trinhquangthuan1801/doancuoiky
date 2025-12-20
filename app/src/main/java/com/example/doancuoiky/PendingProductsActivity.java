package com.example.doancuoiky;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PendingProductsActivity extends AppCompatActivity implements PendingProductAdapter.OnProductStatusChangeListener {

    private RecyclerView rvPendingProducts;
    private PendingProductAdapter adapter;
    private AppDatabaseHelper dbHelper;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_products);

        // Kiểm tra quyền Admin
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userRole = prefs.getString("user_role", "");

        if (!"admin".equals(userRole)) {
            Toast.makeText(this, "Bạn không có quyền truy cập chức năng này.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        dbHelper = new AppDatabaseHelper(this);

        // Ánh xạ views
        rvPendingProducts = findViewById(R.id.rvPendingProducts);
        tvEmpty = findViewById(R.id.tvEmpty);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        // Cấu hình RecyclerView
        rvPendingProducts.setLayoutManager(new LinearLayoutManager(this));

        // Tải dữ liệu
        loadPendingProducts();
    }

    private void loadPendingProducts() {
        Cursor cursor = dbHelper.getPendingProducts();
        if (adapter == null) {
            adapter = new PendingProductAdapter(this, cursor, this);
            rvPendingProducts.setAdapter(adapter);
        } else {
            adapter.swapCursor(cursor);
        }

        // Hiển thị thông báo nếu không có sản phẩm nào
        if (cursor == null || cursor.getCount() == 0) {
            rvPendingProducts.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvPendingProducts.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    // Đây là hàm callback từ Adapter
    @Override
    public void onProductStatusChanged() {
        Toast.makeText(this, "Đã cập nhật trạng thái sản phẩm.", Toast.LENGTH_SHORT).show();
        // Tải lại danh sách để cập nhật giao diện
        loadPendingProducts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.mCursor != null) {
            adapter.mCursor.close();
        }
    }
}
