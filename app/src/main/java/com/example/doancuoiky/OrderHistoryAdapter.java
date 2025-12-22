package com.example.doancuoiky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;
    private DatabaseHelper dbHelper;
    private String mode; // Chế độ: "purchase" hoặc "sale"

    public OrderHistoryAdapter(List<Order> orderList, Context context, String mode, DatabaseHelper dbHelper) {
        this.orderList = orderList;
        this.context = context;
        this.mode = mode;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        Product product = dbHelper.getProductById(order.getProductId());

        if (product != null) {
            holder.tvProductName.setText(product.getName());
            Glide.with(context)
                    .load(product.getImagePath())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(holder.ivProductImage);
        } else {
            holder.tvProductName.setText("Sản phẩm không còn tồn tại");
            holder.ivProductImage.setImageResource(R.drawable.ic_image_placeholder);
        }

        holder.tvOrderDate.setText("Ngày: " + order.getOrderDate());
        holder.tvPrice.setText(order.getPurchasePrice());

        if ("purchase".equals(mode)) {
            holder.tvAdditionalInfo.setText("Người bán: " + order.getSellerUsername());
        } else if ("sale".equals(mode)) {
            holder.tvAdditionalInfo.setText("Người mua: " + order.getBuyerUsername());
        }

        // HIỂN THỊ THÔNG TIN GIAO HÀNG
        holder.tvShippingAddress.setText("Địa chỉ: " + order.getShippingAddress());
        holder.tvContactPhone.setText("SĐT: " + order.getContactPhone());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvOrderDate, tvPrice, tvAdditionalInfo, tvShippingAddress, tvContactPhone;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAdditionalInfo = itemView.findViewById(R.id.tvAdditionalInfo);
            tvShippingAddress = itemView.findViewById(R.id.tvShippingAddress); // Ánh xạ TextView mới
            tvContactPhone = itemView.findViewById(R.id.tvContactPhone);     // Ánh xạ TextView mới
        }
    }
}
