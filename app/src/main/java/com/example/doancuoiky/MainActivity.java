package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private String currentRole;
    private String currentUsername;
    private BottomNavigationView bottomNav;

    final Fragment homeFragment = new HomeFragment();
    final Fragment chatFragment = new ChatListFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment_container);

        currentRole = getIntent().getStringExtra("ROLE");
        currentUsername = getIntent().getStringExtra("USERNAME");

        bottomNav = findViewById(R.id.bottomNavigation);

        // --- BẮT ĐẦU THAY ĐỔI QUAN TRỌNG ---

        // TÁCH RIÊNG 2 LISTENER: 1 cho việc chọn, 1 cho việc CHỌN LẠI
        bottomNav.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNav.setOnItemReselectedListener(mOnNavigationItemReselectedListener);

        // --- KẾT THÚC THAY ĐỔI QUAN TRỌNG ---


        // Chỉ thêm fragments vào lần đầu tiên để tránh tạo lại
        if (savedInstanceState == null) {
            fm.beginTransaction().add(R.id.fragment_container, chatFragment, "2").hide(chatFragment).commit();
            fm.beginTransaction().add(R.id.fragment_container, homeFragment, "1").commit();
            active = homeFragment;
        }
    }

    // Listener cho việc CHỌN MỘT TAB MỚI
    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_home) {
            fm.beginTransaction().hide(active).show(homeFragment).commit();
            active = homeFragment;
            return true;
        } else if (itemId == R.id.navigation_chat) {
            fm.beginTransaction().hide(active).show(chatFragment).commit();
            active = chatFragment;
            return true;
        } else if (itemId == R.id.navigation_sell) {
            launchActivity(SellActivity.class);
            return false; // Quan trọng
        } else if (itemId == R.id.navigation_profile) {
            launchActivity(ProfileActivity.class);
            return false; // Quan trọng
        }
        return false;
    };

    // Listener cho việc NHẤN LẠI VÀO TAB ĐANG ĐƯỢC CHỌN
    // Khi bạn quay lại từ Sell/Profile và tab Chat vẫn đang sáng,
    // lần nhấn tiếp theo vào tab Home sẽ được coi là một sự kiện "chọn" (select).
    // Nhưng nếu bạn quay lại và tab Chat vẫn sáng, rồi bạn nhấn lại vào chính tab Chat,
    // đó sẽ là sự kiện "chọn lại" (reselect). Listener này sẽ không làm gì cả,
    // giúp tránh các hành vi không mong muốn.
    private final NavigationBarView.OnItemReselectedListener mOnNavigationItemReselectedListener
            = item -> {
        // Không làm gì cả khi người dùng nhấn lại vào tab đang hiển thị
        // Điều này ngăn chặn các hành vi không mong muốn và giúp "reset" listener.
    };


    private void launchActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        intent.putExtra("ROLE", currentRole);
        intent.putExtra("USERNAME", currentUsername);
        startActivity(intent);
    }
}
