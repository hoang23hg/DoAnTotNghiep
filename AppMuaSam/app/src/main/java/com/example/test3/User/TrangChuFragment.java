package com.example.test3.User;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.test3.Adapter.BannerAdapter;
import com.example.test3.Adapter.CategoryAdapter;
import com.example.test3.Adapter.ProductAdapter;
import com.example.test3.Interface.ApiCategory;
import com.example.test3.Interface.ApiProduct;
import com.example.test3.Interface.ApiUser;
import com.example.test3.Model.Category;
import com.example.test3.Model.Product;
import com.example.test3.Model.UserResponse;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;
import com.example.test3.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangChuFragment extends Fragment {
    private ViewPager2 viewPager;
    private Handler handler = new Handler();
    private Runnable runnable;
    private List<Integer> bannerList;
    private int currentPage = 0;

    private RecyclerView recyclerViewProducts, recyclerViewCategories;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private TextView tvUserName;
    private SharedPreferences prefs;
    private BroadcastReceiver profileUpdateReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        // Ánh xạ UI
        tvUserName = view.findViewById(R.id.tvDisplayName);
        viewPager = view.findViewById(R.id.viewPagerBanner);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        ImageView imgSearch = view.findViewById(R.id.imgSearch);

        // Khởi tạo SharedPreferences
        prefs = requireActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);

        // Bắt sự kiện click vào ô tìm kiếm
        imgSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

        // Cài đặt Banner
        bannerList = List.of(R.drawable.pt_banner5, R.drawable.pt_banner3, R.drawable.pt_banner4, R.drawable.pt_banner6);
        BannerAdapter adapter = new BannerAdapter(getContext(), bannerList);
        viewPager.setAdapter(adapter);

        // Tự động chuyển banner mỗi 3 giây
        runnable = () -> {
            if (currentPage == bannerList.size()) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
            handler.postDelayed(runnable, 3000);
        };
        handler.postDelayed(runnable, 3000);

        // Cấu hình RecyclerView
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Gọi API
        fetchProducts();
        fetchCategories();
        loadUserName();

        // Khởi tạo BroadcastReceiver
        profileUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                fetchUserInfo();
            }
        };

        return view;
    }

    private void fetchProducts() {
        ApiProduct apiProduct = RetrofitProduct.getClient().create(ApiProduct.class);
        apiProduct.getProducts().enqueue(new Callback<Map<String, List<Product>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Product>>> call, Response<Map<String, List<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body().get("products");
                    productAdapter = new ProductAdapter(products);
                    recyclerViewProducts.setAdapter(productAdapter);
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Product>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void fetchCategories() {
        ApiCategory apiCategory = RetrofitProduct.getClient().create(ApiCategory.class);
        apiCategory.getCategories().enqueue(new Callback<Map<String, List<Category>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Category>>> call, Response<Map<String, List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().get("categories");
                    if (categories != null && !categories.isEmpty()) {
                        categoryAdapter = new CategoryAdapter(getContext(), categories, recyclerViewCategories);
                        recyclerViewCategories.setAdapter(categoryAdapter);
                    } else {
                        Toast.makeText(getContext(), "Không có danh mục nào!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Category>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối API danh mục", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void loadUserName() {
        String savedName = prefs.getString("user_name", null);
        if (savedName != null) {
            tvUserName.setText(savedName);
        } else {
            fetchUserInfo();
        }
    }

    private void fetchUserInfo() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ApiUser apiUser = RetrofitProduct.getClient().create(ApiUser.class);

        apiUser.getUserInfo(userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse.User user = response.body().getData();
                    if (user.getDisplayName() != null) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("user_name", user.getDisplayName());
                        editor.apply();
                        tvUserName.setText(user.getDisplayName());
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể lấy tên người dùng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("UPDATE_PROFILE");
        ContextCompat.registerReceiver(
                requireContext(),
                profileUpdateReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
        );
    }


    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(profileUpdateReceiver);
    }
}
