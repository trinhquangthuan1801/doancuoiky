package com.example.doancuoiky;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseHelper dbHelper;
    private ChipGroup chipGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Sử dụng lại layout activity_main.xml
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo các view và logic từ MainActivity cũ
        dbHelper = new DatabaseHelper(getContext());
        dbHelper.createDefaultDataIfEmpty();

        rvProducts = view.findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList); // Khởi tạo adapter với list rỗng
        rvProducts.setAdapter(productAdapter);

        loadProducts(); // Load tất cả sản phẩm ban đầu

        chipGroup = view.findViewById(R.id.chipGroup);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                loadProducts();
            } else {
                int checkedId = checkedIds.get(0);
                if (checkedId == R.id.chipAll) {
                    loadProducts();
                } else if (checkedId == R.id.chipElectronics) {
                    loadProductsByCategory("Electronics");
                } else if (checkedId == R.id.chipClothes) {
                    loadProductsByCategory("Personal Items");
                } else if (checkedId == R.id.chipBooks) {
                    loadProductsByCategory("Books");
                }
            }
        });
    }

    private void loadProducts() {
        List<Product> newProducts = dbHelper.getAllApprovedProducts();
        updateProductList(newProducts);
    }

    private void loadProductsByCategory(String category) {
        List<Product> newProducts = dbHelper.getApprovedProductsByCategory(category);
        updateProductList(newProducts);
    }

    private void updateProductList(List<Product> newProducts) {
        productList.clear();
        productList.addAll(newProducts);
        productAdapter.notifyDataSetChanged();
    }
}
