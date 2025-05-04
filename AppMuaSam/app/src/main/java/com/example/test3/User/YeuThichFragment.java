package com.example.test3.User;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.test3.Adapter.FavoriteAdapter;
import com.example.test3.Interface.ApiFavorite;
import com.example.test3.Interface.FavoriteListener;
import com.example.test3.Model.Favorite;
import com.example.test3.R;
import com.example.test3.Retrofit.RetrofitProduct;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YeuThichFragment extends Fragment implements FavoriteListener {
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<Favorite> favoriteList;

    @Override
    public void onRemoveFavorite(String uid, int productId) {
        deleteFavorite(uid, productId);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yeu_thich, container, false);

        recyclerView = view.findViewById(R.id.recyclerOrders);

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        loadFavoriteProducts();

        return view;
    }

    private void loadFavoriteProducts() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (uid == null) {
            Toast.makeText(getContext(), "Lỗi: Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiFavorite apiFavorite = RetrofitProduct.getClient().create(ApiFavorite.class);
        apiFavorite.getFavoriteList(uid).enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoriteList = response.body();
                    favoriteAdapter = new FavoriteAdapter(getContext(), favoriteList, YeuThichFragment.this);
                    recyclerView.setAdapter(favoriteAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {
                Log.e("Favorite Error", "Lỗi khi lấy danh sách yêu thích: " + t.getMessage());
            }
        });
    }

    public void deleteFavorite(String uid, int productId) {
        ApiFavorite apiFavorite = RetrofitProduct.getClient().create(ApiFavorite.class);
        apiFavorite.removeFavorite(uid, productId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã xóa sản phẩm khỏi yêu thích", Toast.LENGTH_SHORT).show();

                    // Xóa sản phẩm khỏi danh sách và cập nhật Adapter
                    for (int i = 0; i < favoriteList.size(); i++) {
                        if (favoriteList.get(i).getProductId() == productId) {
                            favoriteList.remove(i);
                            favoriteAdapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
