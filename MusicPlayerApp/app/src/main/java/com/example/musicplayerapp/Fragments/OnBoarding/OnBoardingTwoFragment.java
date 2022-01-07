package com.example.musicplayerapp.Fragments.OnBoarding;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musicplayerapp.ChooseSignInOrSignUp;
import com.example.musicplayerapp.R;


public class OnBoardingTwoFragment extends Fragment {


    TextView tv_skip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_on_boarding_two, container, false);
        tv_skip = root.findViewById(R.id.tv_skip);
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChooseSignInOrSignUp.class);
                getActivity().startActivity(intent);
            }
        });
        return root;
    }
}