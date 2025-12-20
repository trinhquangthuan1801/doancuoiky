package com.example.doancuoiky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ConversationViewHolder> {

    private final List<String> conversationList;

    public ChatListAdapter(List<String> conversationList) {
        this.conversationList = conversationList;
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView username;
        TextView lastMessage;
        TextView timestamp;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.iv_avatar);
            username = itemView.findViewById(R.id.tv_username);
            lastMessage = itemView.findViewById(R.id.tv_last_message);
            timestamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        String currentItem = conversationList.get(position);

        holder.username.setText(currentItem);
        holder.lastMessage.setText("Bạn: Chào bạn, món hàng này còn không?");
        holder.timestamp.setText("10:30");
        holder.avatar.setImageResource(R.drawable.ic_default_avatar); // Gán ảnh mặc định

        holder.itemView.setOnClickListener(v -> {
            // Xử lý khi nhấn vào item
        });
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }
}
