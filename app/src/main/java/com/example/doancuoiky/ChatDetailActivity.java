package com.example.doancuoiky;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private Toolbar toolbar;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        toolbar = findViewById(R.id.toolbar);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(messageAdapter);

        loadDummyMessages();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sẽ xử lý sau
            }
        });
    }

    private void loadDummyMessages() {
        messageList.add(new Message("Chào bạn, sản phẩm này còn hàng không?", false));
        messageList.add(new Message("Chào bạn, vẫn còn nhé.", true));
        messageList.add(new Message("Giá của nó là bao nhiêu vậy shop?", false));
        messageList.add(new Message("Giá sản phẩm là 250.000đ bạn nhé. Bạn có muốn xem thêm ảnh thật không?", true));
        messageList.add(new Message("Có, bạn gửi cho mình xem nhé.", false));
        messageList.add(new Message("Đoạn chat này có đoạn dài có đoạn chỉ vài câu tùy phù hợp vào tin nhắn bạn thấy ở mỗi đoạn chat.", true));
        messageList.add(new Message("OK bạn!", false));

        messageAdapter.notifyDataSetChanged();
    }
}
