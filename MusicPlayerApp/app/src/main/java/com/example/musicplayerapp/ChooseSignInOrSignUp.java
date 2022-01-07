package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChooseSignInOrSignUp extends AppCompatActivity {

    ImageView imgDK, imgDN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sign_in_or_sign_up);
        Anhxa();

        imgDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseSignInOrSignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

        imgDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseSignInOrSignUp.this, SignUp
                        .class);
                startActivity(intent);
            }
        });
    }

    private void Anhxa(){
        imgDK = findViewById(R.id.btn_sign_up);
        imgDN = findViewById(R.id.btn_sign_in);
    }
}