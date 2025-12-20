package com.example.doancuoiky;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvChats;
    private ChatAdapter adapter;
    private List<ChatMessage> chatList;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvChats = findViewById(R.id.rvChats);
        rvChats.setLayoutManager(new LinearLayoutManager(this));

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish()); // Thêm sự kiện quay lại

        loadChatMessages();
    }

    private void loadChatMessages() {
        chatList = new ArrayList<>();
        chatList.add(new ChatMessage("Shop Áo Thun", "Chào bạn, sản phẩm này còn hàng không?", "10:30 AM", R.drawable.ic_person));
        chatList.add(new ChatMessage("Người bán MacBook", "Bạn muốn trả giá bao nhiêu?", "Hôm qua", R.drawable.ic_person));
        chatList.add(new ChatMessage("Hỗ trợ kỹ thuật", "Cảm ơn bạn đã liên hệ, chúng tôi sẽ phản hồi sớm.", "Thứ 2", R.drawable.ic_person));
        chatList.add(new ChatMessage("Bạn bè", "Hẹn gặp ở quán cà phê nhé!", "20/04/2024", R.drawable.ic_person));
        chatList.add(new ChatMessage("Giao hàng nhanh", "Đơn hàng của bạn đang được vận chuyển.", "18/04/2024", R.drawable.ic_person));
        chatList.add(new ChatMessage("Nhóm học tập", "Tối nay có ai học bài không?", "17/04/2024", R.drawable.ic_person));
        chatList.add(new ChatMessage("Admin", "Tài khoản của bạn đã được xác minh.", "15/04/2024", R.drawable.ic_person));
        
        adapter = new ChatAdapter(chatList);
        rvChats.setAdapter(adapter);
    }
}