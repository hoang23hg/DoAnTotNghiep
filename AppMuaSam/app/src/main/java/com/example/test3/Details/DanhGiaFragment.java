package com.example.test3.Details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.test3.Adapter.ReviewAdapter;
import com.example.test3.Interface.ApiService;
import com.example.test3.Model.Review;
import com.example.test3.Model.ReviewResponse;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhGiaFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private int productId;

    public static DanhGiaFragment newInstance(int productId) {
        DanhGiaFragment fragment = new DanhGiaFragment();
        Bundle args = new Bundle();
        args.putInt("productId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt("productId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_gia, container, false);
        recyclerView = view.findViewById(R.id.recyclerReview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        if (getArguments() != null) {
            productId = getArguments().getInt("productId");
            loadReviews(productId);
        }

        return view;
    }

    private void loadReviews(int productId) {
        ApiService apiService = RetrofitProduct.getClient().create(ApiService.class);
        Call<ReviewResponse> call = apiService.getReviews(productId);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> reviewList = response.body().getReviews();

                    // ✅ Log số lượng review để kiểm tra dữ liệu nhận được từ API
                    Log.d("API", "Review size: " + reviewList.size());

                    reviewAdapter = new ReviewAdapter(reviewList);
                    recyclerView.setAdapter(reviewAdapter);
                    reviewAdapter.notifyDataSetChanged();
                } else {
                    Log.d("API", "Response không thành công hoặc body null");
                }
            }


            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                Log.e("API", "Error: " + t.getMessage());

            }
        });
    }
}

