package com.example.doancuoiky;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductCursorAdapter extends RecyclerView.Adapter<ProductCursorAdapter.ProductViewHolder> {

    private Context mContext;
    public Cursor mCursor;

    public ProductCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public TextView productSeller;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_product_image);
            productName = itemView.findViewById(R.id.tv_product_name);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            productSeller = itemView.findViewById(R.id.tv_seller_name);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }

        // Lấy dữ liệu trực tiếp từ Cursor (đã được tối ưu)
        String name = mCursor.getString(mCursor.getColumnIndexOrThrow("name"));
        double price = mCursor.getDouble(mCursor.getColumnIndexOrThrow("price"));
        String seller = mCursor.getString(mCursor.getColumnIndexOrThrow("seller_name"));
        String imageUriString = mCursor.getString(mCursor.getColumnIndexOrThrow("image_uri"));

        // Định dạng giá tiền
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Gán dữ liệu
        holder.productName.setText(name);
        holder.productPrice.setText(currencyFormat.format(price));
        holder.productSeller.setText("Người bán: " + seller);

        // Xử lý ảnh
        if (imageUriString != null && !imageUriString.isEmpty()) {
            holder.productImage.setImageURI(Uri.parse(imageUriString));
            holder.productImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_image_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}