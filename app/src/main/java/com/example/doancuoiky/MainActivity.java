package com.example.doancuoiky;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String role = getIntent().getStringExtra("ROLE");
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        
        if (role != null) {
            if (role.equals("admin")) {
                tvWelcome.setText("Welcome Admin!");
            } else if (role.equals("user")) {
                tvWelcome.setText("Welcome User!");
            }
        }
    }
}