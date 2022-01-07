package com.example.musicplayerapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;

import com.example.musicplayerapp.Database.DAO.UserDAO;
import com.example.musicplayerapp.Database.Services.CallBack.UserCallBack;
import com.example.musicplayerapp.Fragments.Music.Fragment_Play_Danh_Sach_Cac_Bai_Hat;
import com.example.musicplayerapp.Interface.ItemSendDataSong;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.UserInfor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import static com.example.musicplayerapp.SignIn.IDACCOUNT;


public class MainActivity extends AppCompatActivity  {

    //Bottom navigation
    public static SlidingUpPanelLayout slidingUpPanelLayout;
    public static BottomNavigationView bottomNavigationView ;
    UserInfor userInfor = UserInfor.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bottom navigation
        bottomNavigationView = findViewById(R.id.nav_view);

        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingUpPanel);
        // setBottom
        bottomNavigationView.setItemIconTintList(null);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        //Định cho ẩn bottom navigation khi vào các fragment con mà chưa được
        /**
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if(destination.getId() == R.id.slidingUpPanel) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
         **/
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        getUser();

    }

    // slidingUpPanelLayout
    public static void slidingUpPanelLayout() {
        // COLLAPSED: xup do
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                slideDown(bottomNavigationView,slideOffset);
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        }) ;
    }

    // slideDown bottom
    public static void slideDown(BottomNavigationView child, float slideOffset) {
        float heigh_to_animate = slideOffset * child.getHeight();
        ViewPropertyAnimator animator = child.animate();
        animator
                .translationY(heigh_to_animate)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(0)
                .start();
    }


    // onBackPressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // check user
    private void getUser() {
        final String userID ;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userInfor.setID(sharedPreferences.getString(IDACCOUNT,"123"));
        userID  = userInfor.getID() ;
        final UserDAO userDAO = new UserDAO(getApplicationContext());
        userDAO.getUser(userID, new UserCallBack() {
            @Override
            public void getCallback(ArrayList<UserInfor> userInfors) {
//                Log.d("test", userInfors.get(0).toString());
                if(userInfors.size()>0){
                    userInfor.setUsername(userInfors.get(0).getUsername());
                    Log.d("test", userInfors.get(0).getUsername());
                    userInfor.setEmail(userInfors.get(0).getEmail());
                    userInfor.setFavorites(userInfors.get(0).getFavorites());
                }
            }

        });
    }

}
