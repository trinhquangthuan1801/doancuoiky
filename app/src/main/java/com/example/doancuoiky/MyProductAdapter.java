package com.example.doancuoiky;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public MyProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_product, parent, false);
        return new MyProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("Giá: " + product.getPrice());
        holder.tvCategory.setText("Loại: " + product.getCategory());
        holder.tvDescription.setText(product.getDescription()); // Hiển thị mô tả

        // Dùng Glide để hiển thị ảnh
        Glide.with(context)
                .load(product.getImagePath())
                .placeholder(R.drawable.ic_image_placeholder)
                .into(holder.ivProduct);

        // Hiển thị trạng thái và đổi màu
        String status = product.getStatus();
        if ("approved".equals(status)) {
            holder.tvStatus.setText("Đã duyệt");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Màu xanh
        } else {
            holder.tvStatus.setText("Chờ duyệt");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")); // Màu cam
        }

        // Thêm sự kiện click để xem chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_NAME", product.getName());
            intent.putExtra("PRODUCT_PRICE", product.getPrice());
            intent.putExtra("PRODUCT_IMAGE_PATH", product.getImagePath());
            intent.putExtra("PRODUCT_CATEGORY", product.getCategory());
            intent.putExtra("PRODUCT_OWNER", product.getOwner());
            intent.putExtra("PRODUCT_DESCRIPTION", product.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvCategory, tvStatus, tvDescription;
        ImageView ivProduct;

        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvMyProductName);
            tvPrice = itemView.findViewById(R.id.tvMyProductPrice);
            tvCategory = itemView.findViewById(R.id.tvMyProductCategory);
            tvStatus = itemView.findViewById(R.id.tvMyProductStatus);
            tvDescription = itemView.findViewById(R.id.tvMyProductDescription);
            ivProduct = itemView.findViewById(R.id.ivMyProductImage);
        }
    }
}