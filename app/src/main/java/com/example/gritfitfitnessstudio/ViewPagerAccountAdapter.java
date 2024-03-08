package com.example.gritfitfitnessstudio;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAccountAdapter extends FragmentStateAdapter {

    public ViewPagerAccountAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @Override
    public Fragment createFragment(int position) {
        if (position==0){
            return new AccountPostFragment();
        }else{
            return new AccountProgressFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
