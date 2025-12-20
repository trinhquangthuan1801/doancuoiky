package com.example.doancuoiky;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private ConversationAdapter adapter;
    private List<SingleMessage> messageList;
    private EditText etMessageInput;
    private ImageView ivSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // Lấy tên người gửi từ Intent
        String senderName = getIntent().getStringExtra("SENDER_NAME");

        // Ánh xạ View
        rvMessages = findViewById(R.id.rvMessages);
        etMessageInput = findViewById(R.id.etMessageInput);
        ivSend = findViewById(R.id.ivSend);
        TextView tvTitle = findViewById(R.id.tvConversationTitle);
        ImageView ivBack = findViewById(R.id.ivBack);

        tvTitle.setText(senderName != null ? senderName : "Chat");
        ivBack.setOnClickListener(v -> finish());

        // Cài đặt RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(layoutManager);

        // Tải tin nhắn mẫu
        loadSampleMessages();

        // Xử lý gửi tin nhắn
        ivSend.setOnClickListener(v -> {
            String messageContent = etMessageInput.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                sendMessage(messageContent);
            }
        });
    }

    private void loadSampleMessages() {
        messageList = new ArrayList<>();
        messageList.add(new SingleMessage("Chào bạn, sản phẩm này còn hàng không?", SingleMessage.TYPE_RECEIVED));
        messageList.add(new SingleMessage("Chào bạn, sản phẩm này luôn có sẵn nhé!", SingleMessage.TYPE_SENT));
        messageList.add(new SingleMessage("Ok bạn, mình đặt 1 cái nhé.", SingleMessage.TYPE_RECEIVED));
        
        adapter = new ConversationAdapter(messageList);
        rvMessages.setAdapter(adapter);
    }

    private void sendMessage(String content) {
        // Thêm tin nhắn mới vào danh sách
        messageList.add(new SingleMessage(content, SingleMessage.TYPE_SENT));
        adapter.notifyItemInserted(messageList.size() - 1);
        
        // Cuộn xuống tin nhắn cuối cùng
        rvMessages.scrollToPosition(messageList.size() - 1);

        // Xóa nội dung trong ô nhập
        etMessageInput.setText("");
    }
}