package com.example.doancuoiky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatList;

    public ChatAdapter(List<ChatMessage> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatList.get(position);
        holder.senderName.setText(message.getSenderName());
        holder.lastMessage.setText(message.getLastMessage());
        holder.timestamp.setText(message.getTimestamp());
        holder.avatar.setImageResource(message.getAvatarResource());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView senderName, lastMessage, timestamp;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.ivAvatar);
            senderName = itemView.findViewById(R.id.tvSenderName);
            lastMessage = itemView.findViewById(R.id.tvLastMessage);
            timestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}