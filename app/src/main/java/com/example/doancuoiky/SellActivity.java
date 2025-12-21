package com.example.doancuoiky;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SellActivity extends AppCompatActivity {

    private EditText etProductName, etDescription, etPrice;
    private Spinner spinnerCategory;
    private Button btnPost, btnSelectImage;
    private ImageView ivImagePreview;
    private DatabaseHelper dbHelper;
    private String currentRole;
    private String currentUsername;
    private ImageView ivBack;

    private Uri selectedImageUri; // Biến lưu URI của ảnh được chọn

    // Launcher mới để xử lý kết quả chọn ảnh
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    // Hiển thị ảnh đã chọn bằng Glide
                    Glide.with(this)
                            .load(selectedImageUri)
                            .into(ivImagePreview);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        // Lấy role và username từ SharedPreferences thay vì Intent để ổn định hơn
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentRole = prefs.getString("user_role", "user");
        currentUsername = prefs.getString("user_name", "guest");

        dbHelper = new DatabaseHelper(this);

        // Ánh xạ Views
        ivBack = findViewById(R.id.ivBack);
        etProductName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnPost = findViewById(R.id.btnPost);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivImagePreview = findViewById(R.id.ivImagePreview);

        // Cài đặt Spinner
        String[] categories = {"Electronics", "Personal Items", "Books"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Xử lý sự kiện
        ivBack.setOnClickListener(v -> finish());
        setupImageSelection();
        setupPostButton();
        setupBottomNavigation();
    }

    private void setupImageSelection() {
        View.OnClickListener selectImageListener = v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        };
        btnSelectImage.setOnClickListener(selectImageListener);
        ivImagePreview.setOnClickListener(selectImageListener); // Cho phép nhấn vào ảnh để chọn
    }

    private void setupPostButton() {
        btnPost.setOnClickListener(v -> {
            String name = etProductName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            // --- VALIDATION --- (Kiểm tra dữ liệu đầu vào)
            if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn ảnh!", Toast.LENGTH_SHORT).show();
                return; // Dừng lại nếu thiếu thông tin
            }

            // --- LƯU ẢNH --- (Sao chép ảnh vào bộ nhớ trong của app)
            String imagePath = copyImageToInternalStorage(selectedImageUri);
            if (imagePath == null) {
                Toast.makeText(this, "Lỗi khi lưu ảnh!", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- TẠO SẢN PHẨM ---
            if (!priceStr.startsWith("$")) {
                priceStr = "$" + priceStr;
            }

            String status = "admin".equals(currentRole) ? "approved" : "pending";

            Product newProduct = new Product(name, priceStr, imagePath, category, status, currentUsername, description);
            dbHelper.addProduct(newProduct);

            // --- THÔNG BÁO VÀ CHUYỂN HƯỚNG ---
            String message = "admin".equals(currentRole) ? "Đăng bán thành công!" : "Sản phẩm đang chờ Admin duyệt!";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(SellActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Hàm quan trọng: Sao chép file ảnh từ Uri vào bộ nhớ trong của ứng dụng
    private String copyImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            // Tạo tên file duy nhất dựa trên thời gian
            String fileExtension = getFileExtension(uri);
            String fileName = "product_" + System.currentTimeMillis() + "." + fileExtension;

            // Lấy thư mục lưu trữ riêng của app
            File directory = getExternalFilesDir(null); // Hoặc getFilesDir();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, fileName);

            // Sao chép dữ liệu
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            // Trả về đường dẫn tuyệt đối của file đã lưu
            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper để lấy đuôi file (jpg, png...)
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.navigation_sell);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                Intent intent = new Intent(SellActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // finish để không quay lại được
                return true;
            } else if (itemId == R.id.navigation_sell) {
                return true; // Đang ở màn hình này rồi
            } else if (itemId == R.id.navigation_chat) {
                Intent intent = new Intent(SellActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent intent = new Intent(SellActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
    }
}
