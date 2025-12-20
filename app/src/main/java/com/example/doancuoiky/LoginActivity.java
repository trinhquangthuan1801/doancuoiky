package com.example.doancuoiky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private AppDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new AppDatabaseHelper(this);

        etEmail = findViewById(R.id.etUsername); // ID trong XML vẫn là etUsername, chúng ta dùng lại nó
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sử dụng AppDatabaseHelper để xác thực người dùng
            Cursor cursor = dbHelper.getUser(email, password);

            if (cursor != null && cursor.moveToFirst()) {
                // Đăng nhập thành công, lấy thông tin từ Cursor
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String userRole = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                String userFullName = cursor.getString(cursor.getColumnIndexOrThrow("full_name"));

                cursor.close();
                loginSuccess(userId, userRole, userFullName);

            } else {
                // Đăng nhập thất bại
                Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) {
                cursor.close();
            }
        });

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void loginSuccess(int userId, String role, String fullName) {
        // Lưu thông tin người dùng vào SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_id", userId);
        editor.putString("user_role", role);
        editor.putString("user_fullname", fullName);
        editor.apply(); // Lưu lại

        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

        // Chuyển sang MainActivity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // Xóa các activity cũ khỏi stack để người dùng không thể back lại trang login
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}