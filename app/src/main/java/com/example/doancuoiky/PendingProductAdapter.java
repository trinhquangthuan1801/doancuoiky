package com.example.doancuoiky;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Locale;

public class PendingProductAdapter extends RecyclerView.Adapter<PendingProductAdapter.PendingViewHolder> {

    private Context mContext;
    public Cursor mCursor;
    private OnProductStatusChangeListener mListener;

    public interface OnProductStatusChangeListener {
        void onProductStatusChanged();
    }

    public PendingProductAdapter(Context context, Cursor cursor, OnProductStatusChangeListener listener) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
    }

    public static class PendingViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productSeller, productCondition;
        Button btnApprove, btnReject;

        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_pending_product_image);
            productName = itemView.findViewById(R.id.tv_pending_product_name);
            productPrice = itemView.findViewById(R.id.tv_pending_product_price);
            productSeller = itemView.findViewById(R.id.tv_pending_product_seller);
            productCondition = itemView.findViewById(R.id.tv_pending_product_condition);
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnReject = itemView.findViewById(R.id.btn_reject);
        }
    }

    @NonNull
    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pending_product, parent, false);
        return new PendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingViewHolder holder, int position) {
        if (mCursor == null || !mCursor.moveToPosition(position)) {
            return;
        }

        long productId = mCursor.getLong(mCursor.getColumnIndexOrThrow("id"));
        String name = mCursor.getString(mCursor.getColumnIndexOrThrow("name"));
        double price = mCursor.getDouble(mCursor.getColumnIndexOrThrow("price"));
        String seller = mCursor.getString(mCursor.getColumnIndexOrThrow("seller_name"));
        int condition = mCursor.getInt(mCursor.getColumnIndexOrThrow("condition_percentage"));
        String imageUriString = mCursor.getString(mCursor.getColumnIndexOrThrow("image_uri"));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.productName.setText(name);
        holder.productPrice.setText(currencyFormat.format(price));
        holder.productSeller.setText("Người bán: " + seller);
        holder.productCondition.setText("Tình trạng: " + condition + "%");

        if (imageUriString != null && !imageUriString.isEmpty()) {
            holder.productImage.setImageURI(Uri.parse(imageUriString));
            holder.productImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_image_placeholder);
        }

        holder.btnApprove.setOnClickListener(v -> {
            AppDatabaseHelper dbHelper = new AppDatabaseHelper(mContext); // Chỉ tạo khi cần
            dbHelper.updateProductStatus(productId, 1);
            if (mListener != null) {
                mListener.onProductStatusChanged();
            }
        });

        holder.btnReject.setOnClickListener(v -> {
            AppDatabaseHelper dbHelper = new AppDatabaseHelper(mContext); // Chỉ tạo khi cần
            dbHelper.updateProductStatus(productId, 2);
            if (mListener != null) {
                mListener.onProductStatusChanged();
            }
        });
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
