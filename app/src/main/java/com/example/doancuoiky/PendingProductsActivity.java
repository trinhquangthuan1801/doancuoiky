package com.example.doancuoiky;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PendingProductsActivity extends AppCompatActivity {

    private RecyclerView rvPendingProducts;
    private PendingProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Product> pendingList;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_products);

        rvPendingProducts = findViewById(R.id.rvPendingProducts);
        rvPendingProducts.setLayoutManager(new LinearLayoutManager(this));
        
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish()); // Đóng activity để quay lại

        dbHelper = new DatabaseHelper(this);
        loadPendingProducts();
    }

    private void loadPendingProducts() {
        pendingList = dbHelper.getPendingProducts();
        adapter = new PendingProductAdapter(pendingList, new PendingProductAdapter.OnProductActionListener() {
            @Override
            public void onApprove(int productId) {
                dbHelper.approveProduct(productId);
                Toast.makeText(PendingProductsActivity.this, "Đã duyệt sản phẩm!", Toast.LENGTH_SHORT).show();
                loadPendingProducts(); // Reload lại danh sách
            }

            @Override
            public void onReject(int productId) {
                dbHelper.deleteProduct(productId);
                Toast.makeText(PendingProductsActivity.this, "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();
                loadPendingProducts(); // Reload lại danh sách
            }
        });
        rvPendingProducts.setAdapter(adapter);
    }
}