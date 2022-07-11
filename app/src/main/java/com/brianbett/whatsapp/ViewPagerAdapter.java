package com.brianbett.whatsapp;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;

public class ViewPagerAdapter extends FragmentStateAdapter {
    TabLayout tabLayout;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity,TabLayout layoutTab) {
        super(fragmentActivity);
        tabLayout=layoutTab;
    }


    @SuppressLint("NonConstantResourceId")
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new CameraFragment();
            case 2:
                return new StatusFragment();
            case 3:
                return new CallsFragment();

            default:
                return new MessagesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return tabLayout.getTabCount();
    }
}
