package com.example.test3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.Adapter.SearchAdapter;
import com.example.test3.Interface.ApiProduct;
import com.example.test3.Model.Product;
import com.example.test3.Retrofit.RetrofitProduct;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private ImageButton btnBack, btnScan;
    private RecyclerView recyclerSearch;
    private SearchAdapter searchAdapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtSearch = findViewById(R.id.edtSearch);
        btnBack = findViewById(R.id.btnBack);
        btnScan = findViewById(R.id.btnScan);
        recyclerSearch = findViewById(R.id.recyclerSearch);

        recyclerSearch.setLayoutManager(new GridLayoutManager(this, 2));
        searchAdapter = new SearchAdapter(this, productList);
        recyclerSearch.setAdapter(searchAdapter);

        btnBack.setOnClickListener(v -> finish());
        btnScan.setOnClickListener(v -> {
            String query = edtSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                searchProducts(query);
            } else {
                Toast.makeText(SearchActivity.this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchProducts(String query) {
        if (query.isEmpty()) {
            productList.clear();
            searchAdapter.notifyDataSetChanged();
            return;
        }

        ApiProduct apiProduct = RetrofitProduct.getClient().create(ApiProduct.class);
        Call<List<Product>> call = apiProduct.searchProducts(query);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
