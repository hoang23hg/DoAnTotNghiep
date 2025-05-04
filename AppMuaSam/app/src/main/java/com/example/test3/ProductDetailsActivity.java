package com.example.test3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.test3.Adapter.ProductDetailViewPagerAdapter;
import com.example.test3.Interface.ApiCart;
import com.example.test3.Interface.ApiFavorite;
import com.example.test3.Interface.ApiService;
import com.example.test3.Interface.ApiSize;
import com.example.test3.Model.Size;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView backBtn, bookmarkBtn, shareBtn, itemPic;
    private TextView titleTxt, priceTxt, tvAverageRating;
    private Button addToCartBtn;
    private List<Size> sizeList; // Lưu danh sách size từ API

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        backBtn = findViewById(R.id.backBtn);
        bookmarkBtn = findViewById(R.id.imageViewFavorite); // Bookmark icon
        shareBtn = findViewById(R.id.imageView9); // Share icon
        itemPic = findViewById(R.id.itemPic);

        titleTxt = findViewById(R.id.titleTxt);
        priceTxt = findViewById(R.id.priceTxt);

        tvAverageRating = findViewById(R.id.tvAverageRating);
        addToCartBtn = findViewById(R.id.addToCardBtn);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        int productId = intent.getIntExtra("product_id", 0);
        loadSizes(productId);
        String name = intent.getStringExtra("product_name");
        double price = intent.getDoubleExtra("product_price", 0.0);
        String imageUrl = intent.getStringExtra("product_image");


        titleTxt.setText(name);
        priceTxt.setText(price + " $");



        Glide.with(this).load(imageUrl).into(itemPic);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ProductDetailViewPagerAdapter adapter = new ProductDetailViewPagerAdapter(this, productId);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Mô tả");
                    } else {
                        tab.setText("Đánh giá");
                    }
                }).attach();

        getAverageRating(productId);


        // Xử lý khi bấm nút quay lại
        backBtn.setOnClickListener(v -> finish());

        // Xử lý khi bấm bookmark (có thể cập nhật logic lưu vào database)
        bookmarkBtn.setOnClickListener(v -> {
            addToFavorites();
        });

        // Xử lý khi bấm share
        shareBtn.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Xem sản phẩm này: " + name + " - Giá: " + price + "$");
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ sản phẩm"));
        });


        addToCartBtn.setOnClickListener(v -> {
            if (selectedButton == null) {
                Toast.makeText(this, "Vui lòng chọn size!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy thông tin người dùng
            String userId = getUserId();
            if (userId == null) {
                Toast.makeText(this, "Bạn cần đăng nhập để mua hàng!", Toast.LENGTH_SHORT).show();
                return;
            }

            int addProductId = getIntent().getIntExtra("product_id", 0);
            double addPrice = getIntent().getDoubleExtra("product_price", 0.0);

            // Tìm `sizeId` theo tên size từ `selectedButton`
            String selectedSizeName = selectedButton.getText().toString();
            int selectedSizeId = getSizeIdByName(selectedSizeName);

            if (selectedSizeId == -1) {
                Toast.makeText(this, "Lỗi lấy size ID!", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = 1; // Mặc định là 1 sản phẩm
            addToCart(userId, addProductId, quantity, selectedSizeId, addPrice);
        });


    }
    private void loadSizes(int productId) {
        ApiSize Apisize = RetrofitProduct.getClient().create(ApiSize.class);
        Apisize.getSizesByProduct(productId).enqueue(new Callback<List<Size>>() {
            @Override
            public void onResponse(@NonNull Call<List<Size>> call, @NonNull Response<List<Size>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displaySizes(response.body()); // Truyền danh sách trực tiếp
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Size>> call, Throwable t) {
                Log.e("RetrofitError", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
    private Button selectedButton = null; // Lưu trạng thái nút được chọn

    private void displaySizes(List<Size> sizes) {
        this.sizeList = sizes; // Lưu danh sách size vào biến toàn cục
        LinearLayout sizeContainer = findViewById(R.id.sizeContainer);
        sizeContainer.removeAllViews();

        for (Size size : sizes) {
            Button sizeButton = new Button(this);
            sizeButton.setText(size.getSizeName());
            sizeButton.setTextSize(16);
            sizeButton.setTypeface(null, Typeface.BOLD);
            sizeButton.setAllCaps(true);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dpToPx(48), 1);
            params.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
            sizeButton.setLayoutParams(params);
            sizeButton.setBackgroundResource(R.drawable.size_button_normal);

            sizeButton.setOnClickListener(v -> {
                if (selectedButton != null) {
                    selectedButton.setBackgroundResource(R.drawable.size_button_normal);
                }
                sizeButton.setBackgroundResource(R.drawable.size_button_selected);
                selectedButton = sizeButton;
            });

            sizeContainer.addView(sizeButton);
        }
    }


    // Chuyển đổi dp -> px
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
    private void addToOrder(String userId, int productId, int quantity, String size, double price) {
        ApiService apiService = RetrofitProduct.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.addToOrder(userId, productId, quantity, size, price);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailsActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "Lỗi khi thêm!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addToCart(String userId, int productId, int quantity, int sizeId, double price) {
        ApiCart apiCart = RetrofitProduct.getClient().create(ApiCart.class);
        Call<ResponseBody> call = apiCart.addToCart(userId, productId, quantity, sizeId, price);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("DEBUG", "Response success: " + response.code());
                    Toast.makeText(ProductDetailsActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("DEBUG", "Response error: " + response.code() + " - " + response.message());
                    Toast.makeText(ProductDetailsActivity.this, "Lỗi khi thêm!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DEBUG", "Request failed: " + t.getMessage());
                Toast.makeText(ProductDetailsActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getSizeIdByName(String sizeName) {
        if (sizeList != null) {
            for (Size size : sizeList) {
                if (size.getSizeName().equals(sizeName)) {
                    return size.getSizeId();
                }
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }




    private String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid(); // Trả về UID của người dùng
        } else {
            return null; // Người dùng chưa đăng nhập
        }
    }
    private void addToFavorites() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy UID từ Firebase
        int productId = getIntent().getIntExtra("product_id", -1); // Nhận product_id từ Intent

        if (productId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/") // Thay bằng URL API của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiFavorite apiFavorite = retrofit.create(ApiFavorite.class);
        Call<ResponseBody> call = apiFavorite.addToFavorites(uid, productId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailsActivity.this, "Đã thêm vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "Lỗi khi thêm!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductDetailsActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getAverageRating(int productId) {
        ApiService apiService = RetrofitProduct.getClient().create(ApiService.class);
        Call<JsonObject> call = apiService.getAverageRating(productId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject body = response.body();

                    // Kiểm tra nếu key tồn tại và không null
                    if (body.has("average_rating") && !body.get("average_rating").isJsonNull()) {
                        float avgRating = (float) body.get("average_rating").getAsDouble();

                        // Nếu bằng 0 và bạn muốn mặc định vẫn là 5 sao
                        if (avgRating == 0.0f) {
                            tvAverageRating.setText("5.0");
                        } else {
                            tvAverageRating.setText(String.valueOf(avgRating));
                        }
                    } else {
                        // Nếu không có giá trị, hiển thị mặc định 5 sao
                        tvAverageRating.setText("5.0");
                    }
                } else {
                    tvAverageRating.setText("5.0"); // fallback
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                tvAverageRating.setText("5.0"); // fallback khi bị lỗi kết nối
            }
        });
    }





}




