package com.example.musicplayerapp.Account;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.musicplayerapp.Abstract.AbstractAppBarLayout;
import com.example.musicplayerapp.ChooseSignInOrSignUp;
import com.example.musicplayerapp.Fragments.Contact.ContactFragment;
import com.example.musicplayerapp.Fragments.ListOfUser.LikedSongsFragment;
import com.example.musicplayerapp.Fragments.SongFromPhone.SongsFromPhoneFragment;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.example.musicplayerapp.SignIn;
import com.example.musicplayerapp.UserPlayList.MyPlaylistFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rhexgomez.typer.roboto.TyperRoboto;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class ProfileFragment extends Fragment {

    TextView tvUsername,tvUserEmail,tvPhone,tvSignOut;
    ArrayList<String> list;
    UserInfor userInfor = UserInfor.getInstance();
    LinearLayout Favorites;
    LinearLayout Playlist;
    LinearLayout setAccount,Report, Support;
    LinearLayout btn_Contact,btn_songinphone;
    LinearLayout btn_Favorites;
    LinearLayout btn_AboutUs;
    SwitchCompat swface;
    Bitmap bitmap;
    Uri filepath;
    ImageView img_user;
    Button btnsave;
    LinearLayout btnSignOut;
    LinearLayout lnNumberPhone;
    Toolbar toolbarProfile;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Dialog dialog;

  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        init(root) ;
        appBarLayout.addOnOffsetChangedListener(new AbstractAppBarLayout() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED)
                {
                    collapsingToolbarLayout.setTitle("");

                }else if(state==State.COLLAPSED){
                    collapsingToolbarLayout.setTitle("My Profile");
                    collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getContext(),R.color.brown_black));
                    collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
                    collapsingToolbarLayout.setCollapsedTitleTypeface(TyperRoboto.ROBOTO_BOLD());
                    //toolbarProfile.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                    //toolbar_home;
                }else {
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });
        GetUser();
        onClick() ;



        return root;    }

    private void onClick() {

        //Khi nhấn vào bài hát yêu thích
        btn_Favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfor.setisFavorites(true);
                userInfor.setisPlayList(true);
                changeFragment(new LikedSongsFragment());
            }
        });

        //Khi nhấn vào nút chọn ảnh từ bộ sưu tập
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Choose an image file"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        //Khi Nhấn Vào PlayList
        Playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putBoolean("AddMusic",false);
                Fragment fragment = new MyPlaylistFragment();
                fragment.setArguments(bundle);
                changeFragment(fragment);
            }
        });
        btn_songinphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SongsFromPhoneFragment();
                changeFragment(fragment);
            }
        });
        btn_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.loading);
                dialog.show();
                Fragment fragment = new ContactFragment();
                changeFragment(fragment);
                dialog.dismiss();
            }
        });
        //Khi nhấn vào nút lưu ảnh
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.loading);
                dialog.show();
                UploadImage();
                dialog.dismiss();
            }
        });

        //Khi nhấn vào nút Đăng Xuất
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ReportFragment();
                changeFragment(fragment);
            }
        });

        //Support

        dialog = new Dialog(getContext());
        Support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });


        btn_AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                Intent intent = new Intent(getContext(), TikTokVideoActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                 **/

                ShowDialogAboutUs();


            }
        });

    }


    private void ShowDialog ()
    {
        dialog.setContentView(R.layout.dialog_support);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnOk = dialog.findViewById(R.id.btn_okeh);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void ShowDialogAboutUs()
    {
        dialog.setContentView(R.layout.dialog_about_us);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnYeah = dialog.findViewById(R.id.btn_yeah);
        btnYeah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    /**Test
    private void ShowDialog()
    {
        dialog.setContentView(R.layout.dialog_emoji);
        new AestheticDialog.Builder(getActivity(), DialogStyle.EMOJI, DialogType.SUCCESS)
                .setTitle("Success")
                .setMessage("I say a b c d e")
                .show();
        //dialog.setTitle("Success");
        //dialog.show();

    }
     **/



    //tai anh len firebase store
    private void UploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference("image1" + new Random().nextInt(50));
        uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference Ref = db.collection("Users").document(userInfor.getID());
                        Ref.update("image",uri.toString());
                        userInfor.setImage(uri.toString());
                        Toast.makeText(getActivity().getApplicationContext(),"Saving Success!",Toast.LENGTH_SHORT).show();
                        btnsave.setEnabled(false);
                    }
                });
            }
        });
    }

    //set anh vao Imageview
    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        if(requestCode == 1 ){
            filepath = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img_user.setImageBitmap(bitmap);
                btnsave.setEnabled(true);
            }catch (Exception ex){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Check user
    @SuppressLint("SetTextI18n")
    private void GetUser() {
        UserInfor userInfor = UserInfor.getInstance();
        if(userInfor.getUsername()!=null){
            tvUsername.setText( userInfor.getUsername());
            tvUserEmail.setText(userInfor.getEmail());
            // neu moi tao tai khoan khong có ảnh thì set ảnh mặc định
            if(userInfor.getImage() == null){
                img_user.setImageResource(R.drawable.ic_account_circle_black_24dp);
            }
            list = userInfor.getFavorites();
            btnsave.setEnabled(false);

            //Truy cap vao firestore lay chuoi hinh anh ra gan vao imageview
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference Ref = db.collection("Users").document(userInfor.getID());
            Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserInfor user = documentSnapshot.toObject(UserInfor.class);
                    Picasso.get().load(user.getImage()).into(img_user);
                    if(user.getPhone()!=null)
                    {
                    tvPhone.setText(user.getPhone());
                    }else lnNumberPhone.setVisibility(View.GONE);
                }
            });
        }else{
            // không có tài khoản thì tắt chức năng
            tvSignOut.setText("Sign In");
            Favorites.setEnabled(false);
            Playlist.setEnabled(false);
            btnsave.setEnabled(false);
            swface.setChecked(false);
            btn_Contact.setEnabled(false);
            img_user.setImageResource(R.drawable.ic_account_box_black_24dp);
        }
    }


    private void init(View root) {
        tvUsername = root.findViewById(R.id.tvUserName);
        tvUserEmail = root.findViewById(R.id.tvUserEmail);
        //swface =root.findViewById(R.id.swface);
        img_user = root.findViewById(R.id.img_user);
        tvPhone = root.findViewById(R.id.tvPhonenumber);
        btn_Contact = root.findViewById(R.id.ln_contact);
        btn_Favorites = root.findViewById(R.id.Favorites);
        btn_songinphone = root.findViewById(R.id.ln_songinphone);
        btnsave = root.findViewById(R.id.btnsave);
        Playlist = root.findViewById(R.id.PlayList);
        btnSignOut = root.findViewById(R.id.btnSignOut);
        Report = root.findViewById(R.id.ln_report);
        lnNumberPhone = root.findViewById(R.id.ln_numberPhone);
        Support = root.findViewById(R.id.ln_support);
        btn_AboutUs = root.findViewById(R.id.ln_introduction);
        //toolbarProfile = root.findViewById(R.id.toolbar_profile);
        collapsingToolbarLayout = root.findViewById(R.id.collapsingToolBarProfile);
        appBarLayout = root.findViewById(R.id.appBarLayout);
    }

    // Đăng xuất
    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), ChooseSignInOrSignUp.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
    }

    // Thay đổi Fragment
    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction =this.getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}