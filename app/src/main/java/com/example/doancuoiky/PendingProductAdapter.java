package com.example.doancuoiky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PendingProductAdapter extends RecyclerView.Adapter<PendingProductAdapter.PendingViewHolder> {
    private List<Product> productList;
    private OnProductActionListener listener;
    private Context context;

    public interface OnProductActionListener {
        void onApprove(int productId);
        void onReject(int productId);
    }

    public PendingProductAdapter(List<Product> productList, OnProductActionListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_pending_product, parent, false);
        return new PendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("Giá: " + product.getPrice());
        holder.tvCategory.setText("Loại: " + product.getCategory());
        holder.tvOwner.setText("Người đăng: " + product.getOwner()); // Hiển thị người đăng

        // Dùng Glide để hiển thị ảnh
        Glide.with(context)
                .load(product.getImagePath())
                .placeholder(R.drawable.ic_image_placeholder)
                .into(holder.ivProduct);

        holder.btnApprove.setOnClickListener(v -> listener.onApprove(product.getId()));
        holder.btnReject.setOnClickListener(v -> listener.onReject(product.getId()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class PendingViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvCategory, tvOwner;
        ImageView ivProduct;
        Button btnApprove, btnReject;

        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPendingName);
            tvPrice = itemView.findViewById(R.id.tvPendingPrice);
            tvCategory = itemView.findViewById(R.id.tvPendingCategory);
            tvOwner = itemView.findViewById(R.id.tvPendingOwner);
            ivProduct = itemView.findViewById(R.id.ivPendingImage);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}