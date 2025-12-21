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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<DatabaseHelper.CartItem> cartItems;
    private OnCartItemChangeListener listener;

    // Interface để lắng nghe sự kiện thay đổi trong giỏ hàng
    public interface OnCartItemChangeListener {
        void onItemQuantityChanged(int cartItemId, int newQuantity);
        void onItemRemoved(int cartItemId);
    }

    public CartAdapter(Context context, List<DatabaseHelper.CartItem> cartItems, OnCartItemChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        DatabaseHelper.CartItem cartItem = cartItems.get(position);
        Product product = cartItem.getProduct();

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(product.getPrice());
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        Glide.with(context)
             .load(product.getImagePath())
             .placeholder(R.drawable.image_placeholder_background)
             .into(holder.ivProductImage);

        // Xử lý sự kiện nút tăng
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() + 1;
            cartItem.setQuantity(newQuantity);
            holder.tvQuantity.setText(String.valueOf(newQuantity));
            if (listener != null) {
                listener.onItemQuantityChanged(cartItem.getId(), newQuantity);
            }
        });

        // Xử lý sự kiện nút giảm
        holder.btnDecrease.setOnClickListener(v -> {
            int currentQuantity = cartItem.getQuantity();
            if (currentQuantity > 1) {
                int newQuantity = currentQuantity - 1;
                cartItem.setQuantity(newQuantity);
                holder.tvQuantity.setText(String.valueOf(newQuantity));
                if (listener != null) {
                    listener.onItemQuantityChanged(cartItem.getId(), newQuantity);
                }
            } else {
                // Nếu số lượng là 1 và người dùng nhấn giảm, hãy xóa mặt hàng đó
                if (listener != null) {
                    listener.onItemRemoved(cartItem.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void removeItem(int position) {
        cartItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartItems.size());
    }

    public void updateCartItems(List<DatabaseHelper.CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged(); 
    }


    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice, tvQuantity;
        Button btnDecrease, btnIncrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
        }
    }
}
