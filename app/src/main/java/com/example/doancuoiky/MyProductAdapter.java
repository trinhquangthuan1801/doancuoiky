package com.example.doancuoiky;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyProductViewHolder> {

    private List<Product> productList;

    public MyProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_product, parent, false);
        return new MyProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("Giá: " + product.getPrice());
        holder.tvCategory.setText("Loại: " + product.getCategory());
        holder.tvQuantity.setText("Số lượng: " + product.getQuantity());

        // Hiển thị trạng thái và đổi màu
        String status = product.getStatus();
        if ("approved".equals(status)) {
            holder.tvStatus.setText("Đã duyệt");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Màu xanh
        } else {
            holder.tvStatus.setText("Chờ duyệt");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")); // Màu cam
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvCategory, tvStatus, tvQuantity;

        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvMyProductName);
            tvPrice = itemView.findViewById(R.id.tvMyProductPrice);
            tvCategory = itemView.findViewById(R.id.tvMyProductCategory);
            tvStatus = itemView.findViewById(R.id.tvMyProductStatus);
            tvQuantity = itemView.findViewById(R.id.tvMyProductQuantity);
        }
    }
}