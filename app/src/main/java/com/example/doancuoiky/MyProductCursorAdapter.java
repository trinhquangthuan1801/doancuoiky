package com.example.doancuoiky;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Locale;

public class MyProductCursorAdapter extends RecyclerView.Adapter<MyProductCursorAdapter.MyProductViewHolder> {

    private Context mContext;
    public Cursor mCursor;

    public MyProductCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public static class MyProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public TextView productStatus;

        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_my_product_image);
            productName = itemView.findViewById(R.id.tv_my_product_name);
            productPrice = itemView.findViewById(R.id.tv_my_product_price);
            productStatus = itemView.findViewById(R.id.tv_my_product_status);
        }
    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_my_product, parent, false);
        return new MyProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }

        // Lấy dữ liệu trực tiếp từ Cursor (đã được tối ưu)
        String name = mCursor.getString(mCursor.getColumnIndexOrThrow("name"));
        double price = mCursor.getDouble(mCursor.getColumnIndexOrThrow("price"));
        int status = mCursor.getInt(mCursor.getColumnIndexOrThrow("status"));
        String imageUriString = mCursor.getString(mCursor.getColumnIndexOrThrow("image_uri"));

        // Định dạng giá tiền
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Gán dữ liệu
        holder.productName.setText(name);
        holder.productPrice.setText(currencyFormat.format(price));

        // Xử lý trạng thái
        switch (status) {
            case 0:
                holder.productStatus.setText("Đang chờ duyệt");
                holder.productStatus.setTextColor(Color.parseColor("#FFA500"));
                break;
            case 1:
                holder.productStatus.setText("Đang bán");
                holder.productStatus.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_green_dark));
                break;
            case 2:
                holder.productStatus.setText("Bị từ chối");
                holder.productStatus.setTextColor(Color.RED);
                break;
            default:
                holder.productStatus.setText("Không rõ");
                holder.productStatus.setTextColor(Color.BLACK);
                break;
        }

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