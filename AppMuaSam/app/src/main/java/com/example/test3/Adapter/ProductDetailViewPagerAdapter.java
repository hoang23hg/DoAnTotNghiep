package com.example.test3.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.test3.Details.DanhGiaFragment;
import com.example.test3.Details.MoTaFragment;

public class ProductDetailViewPagerAdapter extends FragmentStateAdapter {
    private int productId;

    public ProductDetailViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int productId) {
        super(fragmentActivity);
        this.productId = productId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return MoTaFragment.newInstance(productId);
        else return DanhGiaFragment.newInstance(productId);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

