package com.example.musicplayerapp.Account;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.musicplayerapp.Database.DAO.ReportDAO;

import com.example.musicplayerapp.Database.Services.CallBack.ReportCallBack;
import com.example.musicplayerapp.Model.Contact;
import com.example.musicplayerapp.Model.Report;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReportFragment  extends Fragment {
    EditText txtreport;
    EditText txtnd;
    ImageView back,next;
    Contact contact = new Contact();
    ArrayList<Contact> list = new ArrayList<>() ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserInfor userInfor = UserInfor.getInstance();
    BottomNavigationView navView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_report, container, false);
        txtreport = root.findViewById(R.id.edt_report);
        txtnd = root.findViewById(R.id.edt_nd);
        back = root.findViewById(R.id.img_back);
        next = root.findViewById(R.id.btn_next);
        navView = root.findViewById(R.id.nav_view);
        //navView.setVisibility(View.INVISIBLE);

        //navView.animate().translationY(container.getHeight()).setDuration(200);

        EventClick();
        return root;
    }

    private void EventClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.nav_default_pop_enter_anim,R.anim.nav_default_exit_anim);
                fr.replace(R.id.nav_host_fragment,new ProfileFragment());
                fr.commit();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String vd = txtreport.getText().toString().trim();
                final String nd = txtnd.getText().toString().trim();
                if (TextUtils.isEmpty(vd)|| TextUtils.isEmpty(nd)) {
                    Toast.makeText(getContext(), "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                } else {
                    CreateReport(txtreport.getText().toString(),txtnd.getText().toString());
                }
            }
        });
    }
    // tao moi bo suu tap add danh sach contact tu phone len firestore
    private void CreateReport(String report,String nd) {
        UserInfor userInfor = UserInfor.getInstance();
        ReportDAO reportDAO = new ReportDAO(getContext());
        reportDAO.createReport(userInfor.getID(), userInfor.getUsername(), userInfor.getEmail(), txtreport.getText().toString(), txtnd.getText().toString(), new ReportCallBack() {
            @Override
            public void getCallback(ArrayList<Report> contacts) {
                Toast.makeText(getContext(), "Reporting success!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
