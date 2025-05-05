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
import android.widget.TextView;

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
    TextView tvNoReview;

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
        tvNoReview = view.findViewById(R.id.tvNoReview);

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

                    if (reviewList == null || reviewList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        tvNoReview.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        tvNoReview.setVisibility(View.GONE);
                        reviewAdapter = new ReviewAdapter(reviewList);
                        recyclerView.setAdapter(reviewAdapter);
                        reviewAdapter.notifyDataSetChanged();
                    }

                } else {
                    Log.d("API", "Response không thành công hoặc body null");
                    recyclerView.setVisibility(View.GONE);
                    tvNoReview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
                recyclerView.setVisibility(View.GONE);
                tvNoReview.setVisibility(View.VISIBLE);
            }
        });
    }

}

