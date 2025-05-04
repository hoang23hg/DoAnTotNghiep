package com.example.test3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.test3.Interface.ApiService;
import com.example.test3.Retrofit.RetrofitProduct;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhGiaDialogFragment extends DialogFragment {
    private int productId;
    private String uid;

    public DanhGiaDialogFragment(int productId, String uid) {
        this.productId = productId;
        this.uid = uid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_danh_gia, container, false);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText commentEditText = view.findViewById(R.id.commentEditText);
        Button btnGui = view.findViewById(R.id.btnGuiDanhGia);

        btnGui.setOnClickListener(v -> {
            float ratingValue = ratingBar.getRating();
            String comment = commentEditText.getText().toString();

            if (ratingValue == 0 || comment.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitProduct.getClient().create(ApiService.class);
            apiService.submitReview(productId, uid, ratingValue, comment)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(getContext(), "Đánh giá thành công", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }
}


