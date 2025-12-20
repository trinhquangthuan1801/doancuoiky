package com.example.doancuoiky;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SellActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etProductName, etDescription, etPrice;
    private Spinner spinnerCategory;
    private SeekBar sbCondition;
    private TextView tvConditionPercentage;
    private ImageView ivImagePreview;
    private Button btnSelectImage, btnPost;

    private AppDatabaseHelper dbHelper;
    private Uri finalImageUri; // Sẽ lưu URI của file ảnh đã copy
    private HashMap<String, Integer> categoryMap = new HashMap<>();
    private int selectedCategoryId = -1;
    private int currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        dbHelper = new AppDatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        etProductName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        sbCondition = findViewById(R.id.sbCondition);
        tvConditionPercentage = findViewById(R.id.tvConditionPercentage);
        ivImagePreview = findViewById(R.id.ivImagePreview);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnPost = findViewById(R.id.btnPost);

        setupConditionSeekBar();
        setupCategorySpinner();
        setupImageSelection();
        setupPostButton();
        setupBottomNavigation();
    }

    private void setupImageSelection() {
        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    private void setupPostButton() {
        btnPost.setOnClickListener(v -> {
            // ... (code kiểm tra dữ liệu như cũ)
            String name = etProductName.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            int condition = sbCondition.getProgress();

            if (name.isEmpty() || priceStr.isEmpty() || selectedCategoryId == -1 || finalImageUri == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn ảnh!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            try {
                double price = Double.parseDouble(priceStr);
                long productId = dbHelper.addProduct(name, price, desc, condition, selectedCategoryId, currentUserId);

                if (productId != -1) {
                    // Lưu URI của ảnh đã copy vào DB
                    dbHelper.addImageForProduct(productId, finalImageUri.toString());

                    Toast.makeText(this, "Đăng bán thành công! Sản phẩm đang chờ duyệt.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SellActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Lỗi khi đăng bán sản phẩm.", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá sản phẩm không hợp lệ.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri sourceUri = data.getData();
            // Sao chép ảnh vào bộ nhớ trong của app
            finalImageUri = copyImageToInternalStorage(sourceUri, "product_image_" + System.currentTimeMillis());

            if (finalImageUri != null) {
                ivImagePreview.setImageURI(finalImageUri);
                ivImagePreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                Toast.makeText(this, "Lỗi khi lưu ảnh.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri copyImageToInternalStorage(Uri uri, String newFileName) {
        try {
            ContentResolver resolver = getContentResolver();
            ParcelFileDescriptor parcelFileDescriptor = resolver.openFileDescriptor(uri, "r", null);
            if (parcelFileDescriptor == null) return null;

            FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            File file = new File(getDir("images", Context.MODE_PRIVATE), newFileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }

            inputStream.close();
            outputStream.close();
            parcelFileDescriptor.close();

            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // --- Các hàm setup khác không thay đổi ---

    private void setupConditionSeekBar() {
        sbCondition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvConditionPercentage.setText(String.format("%d%%", progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void setupCategorySpinner() {
        ArrayList<String> categoryNames = new ArrayList<>();
        try (Cursor cursor = dbHelper.getAllCategories()) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    categoryNames.add(name);
                    categoryMap.put(name, id);
                } while (cursor.moveToNext());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = parent.getItemAtPosition(position).toString();
                selectedCategoryId = categoryMap.get(categoryName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = -1;
            }
        });
    }
    
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_sell);
        bottomNav.setOnItemSelectedListener(item -> {
             int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_my_products) {
                startActivity(new Intent(this, MyProductsActivity.class));
                return true;
            } else if (itemId == R.id.navigation_sell) {
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}
