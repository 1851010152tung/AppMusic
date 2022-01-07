package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cuberto.liquid_swipe.LiquidPager;
import com.example.musicplayerapp.Fragments.OnBoarding.Adapter.OnBoardingViewPagerAdapter;

public class OnBoardingActivity extends AppCompatActivity{

    LiquidPager pager;
    OnBoardingViewPagerAdapter onBoardingViewPagerAdapter;
    Button btn_started;
    TextView tv_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        AnhXa();



        //Pager_ (liquid swipe)
        pager = findViewById(R.id.pager);
        onBoardingViewPagerAdapter = new OnBoardingViewPagerAdapter(getSupportFragmentManager(),1);
        pager.setAdapter(onBoardingViewPagerAdapter);

    }


    private void AnhXa() {
        btn_started=findViewById(R.id.btn_get_started);
        tv_skip=findViewById(R.id.tv_skip);
    }

}