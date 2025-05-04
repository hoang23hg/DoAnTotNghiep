package com.example.test3.Details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test3.Interface.ApiService;
import com.example.test3.Model.DescriptionResponse;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoTaFragment extends Fragment {
    private int productId;
    private TextView tvDescription;

    public static MoTaFragment newInstance(int productId) {
        MoTaFragment fragment = new MoTaFragment();
        Bundle args = new Bundle();
        args.putInt("productId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mo_ta, container, false);
        tvDescription = view.findViewById(R.id.descriptionTxt);

        if (getArguments() != null) {
            productId = getArguments().getInt("productId");
            fetchDescription(productId);
        }

        return view;
    }

    private void fetchDescription(int productId) {
        ApiService apiService = RetrofitProduct.getClient().create(ApiService.class);
        Call<DescriptionResponse> call = apiService.getDescription(productId);

        call.enqueue(new Callback<DescriptionResponse>() {
            @Override
            public void onResponse(Call<DescriptionResponse> call, Response<DescriptionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvDescription.setText(response.body().getDescription());
                } else {
                    tvDescription.setText("Không có mô tả.");
                }
            }

            @Override
            public void onFailure(Call<DescriptionResponse> call, Throwable t) {
                tvDescription.setText("Lỗi khi tải mô tả.");
                Log.e("MoTaFragment", "onFailure: ", t);
            }
        });
    }
}

