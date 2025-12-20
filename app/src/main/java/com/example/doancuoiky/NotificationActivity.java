package com.example.doancuoiky;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        loadNotifications();
    }

    private void loadNotifications() {
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationItem("Khuyến mãi đặc biệt", "Giảm giá 50% cho tất cả sản phẩm điện tử trong hôm nay.", "2 giờ trước"));
        notificationList.add(new NotificationItem("Cập nhật hệ thống", "Hệ thống sẽ được bảo trì từ 2h-3h sáng mai.", "1 ngày trước"));
        notificationList.add(new NotificationItem("Đơn hàng đã được duyệt", "Sản phẩm 'Áo thun' bạn đăng đã được duyệt và hiển thị trên trang chủ.", "3 ngày trước"));
        notificationList.add(new NotificationItem("Chào mừng bạn mới", "Cảm ơn bạn đã tham gia cộng đồng mua sắm của chúng tôi!", "1 tuần trước"));

        adapter = new NotificationAdapter(notificationList);
        rvNotifications.setAdapter(adapter);
    }
}