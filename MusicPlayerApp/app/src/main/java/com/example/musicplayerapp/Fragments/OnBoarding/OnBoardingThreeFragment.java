package com.example.musicplayerapp.Fragments.OnBoarding;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.example.musicplayerapp.ChooseSignInOrSignUp;
import com.example.musicplayerapp.MainActivity;
import com.example.musicplayerapp.R;

public class OnBoardingThreeFragment extends Fragment {

    Button btn_get_started;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_on_boarding_three, container, false);
        btn_get_started = root.findViewById(R.id.btn_get_started);
        btn_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return root;
    }
}