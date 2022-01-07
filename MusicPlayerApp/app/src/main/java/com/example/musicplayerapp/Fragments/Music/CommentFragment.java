package com.example.musicplayerapp.Fragments.Music;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.musicplayerapp.Database.DAO.SongsDAO;
import com.example.musicplayerapp.Database.Services.CallBack.CommentCallBack;

import com.example.musicplayerapp.Fragments.Music.Adapter.CommentAdapter;

import com.example.musicplayerapp.Model.Comment;

import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



public class CommentFragment extends BottomSheetDialogFragment {
    ArrayList<Comment> listComment;
    public static String idbaihat;
    public static String tenbaihat;
    //public static int dem;
    RecyclerView rcvCmt;
    CommentAdapter commentAdapter;
    TextView tv_post,tv_namesong, countNumberComment;
    EditText edt_content;
    ImageView avatar;
    UserInfor userInfor = UserInfor.getInstance();
    Song song = Song.getInstance();
    private String TAG = "Lifecycle" ;
    public static int position = 0 ;
    String imageuser;
    ArrayList<String> commentArrayList = new ArrayList<String>() ;
    int tamp =0;



    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
    View root = inflater.inflate(R.layout.fragment_bottom_write_comment,container,false);
    init(root);
    GetImageUser();
    Log.d(TAG,"getDataFromIntent") ;
    Bundle bundle = getArguments();
    //Nhận dữ liệu từ các fragment truyền qua
    idbaihat = bundle.getString("Songs");
    tenbaihat = bundle.getString("NameSongs");

    // get position hiện tại của bài hát
    if (bundle.getInt("position") != 0) {
        position = bundle.getInt("position");
    } else {
        position = 0;
    }
    tv_namesong.setText(tenbaihat);



    getData(idbaihat);

    tv_post.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String cmt = edt_content.getText().toString();
            if(cmt.matches("")){
                Toast.makeText(getContext(),"You need to write something !",Toast.LENGTH_SHORT).show();
            }else{
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.loading);
                dialog.show();
                CreateNew(cmt);
                getData(idbaihat);
                dialog.dismiss();
            }
        }
    });



    return root;
}
    private void GetImageUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference Ref = db.collection("Users").document(userInfor.getID());
        Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfor user = documentSnapshot.toObject(UserInfor.class);
                imageuser = user.getImage();
                    Picasso.get().load(imageuser).into(avatar);
            }
        });
    }

    private void init(View root) {
        rcvCmt = root.findViewById(R.id.rcvComment);
        tv_post = root.findViewById(R.id.tv_pos);
        tv_namesong= root.findViewById(R.id.namesong);
        edt_content = (EditText)root.findViewById(R.id.edt_content);
        avatar = root.findViewById(R.id.avatatar);
        //countNumberComment = root.findViewById(R.id.tv_numberComment);
    }


    private void getData(String id) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        SongsDAO songDAO = new SongsDAO(getContext());
        songDAO.getSongComment(id, new CommentCallBack() {
            @Override
            public void getCallBack(ArrayList<Comment> comments) {
                listComment = comments;
                Log.d("SongComment",comments.toString()) ;
                rcvCmt.setLayoutManager(new LinearLayoutManager(getActivity()));
                Log.d("CommentTest", comments.toString());
                commentAdapter = new CommentAdapter(getContext(),listComment, CommentFragment.this);
                rcvCmt.setAdapter(commentAdapter);
                dialog.dismiss();
            }
        });
    }
    // Tạo mới
    private void CreateNew(String content) {
        SongsDAO songsDAO = new SongsDAO(getContext());
        songsDAO.createComment(idbaihat, userInfor.getUsername(), getDate(), content, imageuser, new CommentCallBack() {
            @Override
            public void getCallBack(ArrayList<Comment> comments) {
                listComment.clear();
                listComment.addAll(comments);
                commentAdapter.notifyDataSetChanged();
                edt_content.getText().clear();
            }
        });
    }
    private String getDate(){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }



    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction =this.getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
