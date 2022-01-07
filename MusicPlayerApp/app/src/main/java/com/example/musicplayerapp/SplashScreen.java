package com.example.musicplayerapp;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;



public class SplashScreen extends AppCompatActivity {



    //Variables
    Animation topAnim;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FullScreen
        //Objects.requireNonNull(getSupportActionBar()).hide();
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.animation);

        //Hooks
        imageView = findViewById(R.id.logo_splashscreen);

        imageView.setAnimation(topAnim);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, OnBoardingActivity.class);
                startActivity(intent);
            }
        }, 5000);

    }
}