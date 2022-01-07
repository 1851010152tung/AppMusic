package com.example.musicplayerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;

import com.example.musicplayerapp.Database.DAO.UserDAO;
import com.example.musicplayerapp.Database.Services.CallBack.SucessCallBack;
import com.example.musicplayerapp.Model.UserInfor;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class SignUp extends AppCompatActivity {
    EditText et_name, et_email, et_pass,et_phone;
    ImageView bt_register,img_back;
    ImageView tv_login;
    boolean ktphone= true;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);
            et_name = findViewById(R.id.edt_name);
            et_phone = findViewById(R.id.edt_numberphone);
            et_email = findViewById(R.id.edt_email);
            et_pass = findViewById(R.id.edt_pass);
            bt_register = findViewById(R.id.btn_next);
            tv_login = findViewById(R.id.btn_backsignin);
            img_back = findViewById(R.id.img_back);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            bt_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String name = et_name.getText().toString().trim();
                    final String phone = et_phone.getText().toString().trim();
                    final String email = et_email.getText().toString().trim();
                    final String password = et_pass.getText().toString().trim();
                    final String image = "https://firebasestorage.googleapis.com/v0/b/appmusic-aab05.appspot.com/o/icon.png?alt=media&token=83e280df-1b59-422b-b0ab-95e1f0dfe364";
                    if(phone.startsWith("0")){
                        ktphone = true;
                    }else {
                        ktphone = false;
                    }
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(getApplicationContext(), "Enter your name!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter your email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(getApplicationContext(), "Enter your number phone!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter your password!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (phone.length() < 9 || ktphone == false) {
                        Toast.makeText(getApplicationContext(), "Number phone wrong!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Thêm user vào firebase Authentication
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUp.this, "Email Wrong ", Toast.LENGTH_SHORT).show();
                                        Log.e("login_error",task.getException().toString());
                                    } else {
                                        final String ID = firebaseAuth.getCurrentUser().getUid();
                                        final UserDAO userDAO = new UserDAO(SignUp.this);
                                        // Gọi lại hàm Thêm user vào Firestore
                                        userDAO.signUp(ID, name, password, email,image,phone, new SucessCallBack() {
                                            @Override
                                            public void getCallBack(Boolean isSucees) {
                                                if(isSucees){
                                                    UserInfor userInfor = UserInfor.getInstance();
                                                    userInfor.setID(ID);
                                                    Toast.makeText(SignUp.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                                    Log.e("login_error","Sign Up Success");
                                                    startActivity(new Intent(SignUp.this, SignIn.class));
                                                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                                                    finish();
                                                }else{
                                                    Toast.makeText(SignUp.this, "Sign Up failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            });
            tv_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignUp.this, SignIn.class));
                    finish();
                }
            });
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignUp.this, ChooseSignInOrSignUp.class));
                    finish();
                }
            });
        }
}
