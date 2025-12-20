package com.example.doancuoiky;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerViewChatList;
    private ChatListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liên kết layout fragment_chat_list.xml với Fragment này
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ các view từ layout
        recyclerViewChatList = view.findViewById(R.id.recycler_view_chat_list);

        // Thiết lập cho RecyclerView
        recyclerViewChatList.setLayoutManager(new LinearLayoutManager(getContext()));

        // 1. Tạo dữ liệu mẫu (để kiểm tra giao diện)
        List<String> sampleConversations = Arrays.asList(
                "Shop Thời Trang ABC",
                "Nguyễn Văn An",
                "Đồ điện tử XYZ",
                "Admin Hỗ Trợ",
                "Trần Thị Bích"
        );

        // 2. Khởi tạo Adapter với dữ liệu mẫu
        adapter = new ChatListAdapter(sampleConversations);

        // 3. Gán adapter cho RecyclerView
        recyclerViewChatList.setAdapter(adapter);
    }
}
    