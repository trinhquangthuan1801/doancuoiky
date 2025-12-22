package com.example.doancuoiky;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class SalesHistoryActivity extends AppCompatActivity {

    private RecyclerView rvSalesHistory;
    private OrderHistoryAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        dbHelper = new DatabaseHelper(this);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // RecyclerView
        rvSalesHistory = findViewById(R.id.rvSalesHistory);
        rvSalesHistory.setLayoutManager(new LinearLayoutManager(this));

        loadSalesHistory();
    }

    private void loadSalesHistory() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String currentUsername = prefs.getString("user_name", null);

        if (currentUsername != null) {
            orderList = dbHelper.getSalesHistory(currentUsername);
            // Truyền dbHelper vào adapter
            adapter = new OrderHistoryAdapter(orderList, this, "sale", dbHelper);
            rvSalesHistory.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close(); // Đóng kết nối khi Activity bị hủy
        }
    }
}
