package com.example.doancuoiky;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {

    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;
    private List<DatabaseHelper.CartItem> cartItems;
    private DatabaseHelper dbHelper;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Giỏ hàng");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvCartItems = findViewById(R.id.rv_cart_items);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartItems = new ArrayList<>();

        // SỬA LỖI: Sử dụng đúng key "user_name" để lấy tên người dùng
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("user_name", null);

        if (currentUsername == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadCartItems();

        cartAdapter = new CartAdapter(this, cartItems, this);
        rvCartItems.setAdapter(cartAdapter);

        btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng thanh toán đang được phát triển!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadCartItems() {
        if (currentUsername != null) {
            cartItems.clear();
            cartItems.addAll(dbHelper.getCartItems(currentUsername));
            if (cartAdapter != null) {
                cartAdapter.updateCartItems(cartItems);
            }
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        double total = 0;
        for (DatabaseHelper.CartItem item : cartItems) {
            try {
                // Bỏ ký tự không phải số và chuyển đổi an toàn
                String priceString = item.getProduct().getPrice().replaceAll("[^\\d]", "");
                if (!priceString.isEmpty()) {
                    double price = Double.parseDouble(priceString);
                    total += price * item.getQuantity();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Log lỗi để dễ dàng gỡ rối
            }
        }

        // Định dạng tiền tệ cho Việt Nam
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(currencyFormat.format(total));
    }

    @Override
    public void onItemQuantityChanged(int cartItemId, int newQuantity) {
        dbHelper.updateCartItemQuantity(cartItemId, newQuantity);
        loadCartItems(); // Tải lại để đảm bảo dữ liệu nhất quán
    }

    @Override
    public void onItemRemoved(int cartItemId) {
        dbHelper.deleteCartItem(cartItemId);
        loadCartItems(); // Tải lại để cập nhật RecyclerView và tổng giá
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại dữ liệu khi quay lại màn hình này để cập nhật thay đổi
        loadCartItems();
    }
}
