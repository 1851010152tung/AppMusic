package com.example.musicplayerapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;

import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Toast;

import com.example.musicplayerapp.Model.UserInfor;

import com.facebook.CallbackManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignIn extends AppCompatActivity  {
    EditText txtUserName;
    EditText txtPassword;
    CallbackManager callbackManager;
    ImageView btnSignIn,img_back;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserInfor userInfor = UserInfor.getInstance();
    public static final String IDACCOUNT = "IDACCOUNT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
//Ánh Xạ===========================================================================================================
        txtUserName = findViewById(R.id.edt_email);
        txtPassword = findViewById(R.id.edt_pass);
        img_back = findViewById(R.id.img_back);
        btnSignIn = findViewById(R.id.img_next);
        ImageView signUp_text = findViewById(R.id.btn_sign_up2);
        ImageView Forgetpass = findViewById(R.id.btn_forgotPass);
        callbackManager = CallbackManager.Factory.create();
//Thao tác với các nút==============================================================================================
//Khi Nhấn vào nút đăng ký
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
//Khi nhấn vào nút Quên Mật Khẩu
        Forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });
//Khi Nhấn vào nút đăng nhập thông thường
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        //Khi nhan vao nut quay lai
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, ChooseSignInOrSignUp.class));
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                finish();
            }
        });
    }


    //Hàm Đăng Ký=======================================================================================================
    private void signUp() {
        startActivity(new Intent(SignIn.this, SignUp.class));
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
        finish();
    }
    //Hàm Đăng Nhập Thông Thường
    private void login() {
        final String UserName = txtUserName.getText().toString();
        String Password = txtPassword.getText().toString();
        // Kiểm tra dữ liệu người dùng nhập vào
        if(UserName.isEmpty()){
            txtUserName.setError("Please Enter Username !");
            txtUserName.requestFocus();
        }
        else if(Password.isEmpty()){
            txtPassword.setError("Please Enter Password !");
            txtPassword.requestFocus();
        }
        else if (UserName.isEmpty() && Password.isEmpty()){
            Toast.makeText(SignIn.this, "You need to enter enough information !", Toast.LENGTH_SHORT).show();
        }
        // khi nhập đủ thông tin
        else if(!(UserName.isEmpty() && Password.isEmpty())){
                mAuth.signInWithEmailAndPassword(UserName,Password).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(SignIn.this, "Wrong account or password, Please Try Again !", Toast.LENGTH_SHORT).show();
                    }
                    // thành công
                    else {
                        updateUI();
                    }
                }
            });
        }
        else{
            Toast.makeText(SignIn.this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }
    //    Save data
    private void saveData(String userID) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IDACCOUNT, userID);
        editor.apply();
    }
    //Khôi Phục Mật Khẩu================================================================================================
//    Dialog
    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        //set Layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to get in dialog
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Please Enter Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,50,10,10);

        builder.setView(linearLayout);

        //button Recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                String email = emailEt.getText().toString().trim();
                beginRecover(email);
            }
        });
        //button Cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //Show Dialog
        builder.create().show();
    }
    // bắt đâu xử lí khôi phục
    private void beginRecover(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignIn.this, "Email sent ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignIn.this, "Fail .... Please Enter Again", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //get and show proper error message
                Toast.makeText(SignIn.this, "Sending email"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        } else {
            saveData("default");
        }
    }

    //Chuyển Activity khi đăng nhập thành công
    private void updateUI() {
        saveData(mAuth.getCurrentUser().getUid()) ;
        UserInfor userInfor = UserInfor.getInstance();
        userInfor.setID(mAuth.getCurrentUser().getUid());
        Log.d("demo", (mAuth.getCurrentUser().getUid())) ;
        Intent HomePage = new Intent(SignIn.this, MainActivity.class);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(HomePage);
    }


}