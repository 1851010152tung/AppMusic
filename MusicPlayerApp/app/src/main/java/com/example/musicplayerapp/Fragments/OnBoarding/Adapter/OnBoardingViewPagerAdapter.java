package com.example.musicplayerapp.Fragments.OnBoarding.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.musicplayerapp.Fragments.OnBoarding.OnBoardingOneFragment;
import com.example.musicplayerapp.Fragments.OnBoarding.OnBoardingThreeFragment;
import com.example.musicplayerapp.Fragments.OnBoarding.OnBoardingTwoFragment;

import org.jetbrains.annotations.NotNull;

public class OnBoardingViewPagerAdapter extends FragmentPagerAdapter {
    public OnBoardingViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new OnBoardingOneFragment();
            case 1: return new OnBoardingTwoFragment();
            case 2: return new OnBoardingThreeFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
