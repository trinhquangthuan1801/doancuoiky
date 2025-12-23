package com.example.doancuoiky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etEditName, etEditEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Ánh xạ View
        ImageView ivBack = findViewById(R.id.ivBack);
        Button btnSaveChanges = findViewById(R.id.btnSaveChanges);
        etEditName = findViewById(R.id.etEditName);
        etEditEmail = findViewById(R.id.etEditEmail);

        // Nhận dữ liệu từ ProfileActivity và điền vào ô EditText
        String currentUsername = getIntent().getStringExtra("CURRENT_USERNAME");
        String currentEmail = getIntent().getStringExtra("CURRENT_EMAIL");
        etEditName.setText(currentUsername);
        etEditEmail.setText(currentEmail);

        // Xử lý sự kiện
        ivBack.setOnClickListener(v -> finish());

        btnSaveChanges.setOnClickListener(v -> {
            String newUsername = etEditName.getText().toString();
            String newEmail = etEditEmail.getText().toString();

            // Đóng gói dữ liệu mới vào Intent để gửi về
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_USERNAME", newUsername);
            resultIntent.putExtra("NEW_EMAIL", newEmail);

            // Đặt kết quả là OK và gửi về
            setResult(Activity.RESULT_OK, resultIntent);

            Toast.makeText(this, "Lưu thay đổi thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng màn hình chỉnh sửa
        });
    }
}