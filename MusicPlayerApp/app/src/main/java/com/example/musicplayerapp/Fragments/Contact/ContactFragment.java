package com.example.musicplayerapp.Fragments.Contact;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Account.ProfileFragment;
import com.example.musicplayerapp.Database.DAO.ContactDAO;
import com.example.musicplayerapp.Database.Services.CallBack.ContactCallBack;
import com.example.musicplayerapp.Fragments.Contact.Adapter.ContactAdapter;
import com.example.musicplayerapp.Home.Adapter.ArtistAdapter;
import com.example.musicplayerapp.Home.ListArtistFragment;
import com.example.musicplayerapp.Model.Artist;
import com.example.musicplayerapp.Model.Contact;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ContactFragment extends Fragment {
    ArrayList<Contact> mylistcontacts = new ArrayList<>();
    RecyclerView rcvContact;
    ContactAdapter adapter;
    Toolbar toolbar;
    LinearLayout back;
    ImageView reset,addContact;
    EditText searchContactList;
    UserInfor userInfor = UserInfor.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_contact_list, container, false);
        init(root);
        //hien thi toolbar tim kiem
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //setHasOptionsMenu(true);

        //Search
        mylistcontacts = new ArrayList<>();
        adapter = new ContactAdapter(getContext(),mylistcontacts, ContactFragment.this);
        rcvContact.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvContact.setAdapter(adapter);
        rcvContact.setHasFixedSize(true);
        //getData("");
        searchContactList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()!=null){

                    filter(s.toString());


                }
            }
        });


        EventClick();
       // kiem tra
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        CheckContact(UserInfor.getInstance().getID());
        dialog.dismiss();


        return root;
    }


    // Filter
    private void filter (String text){
        ArrayList<Contact> filteredList = new ArrayList<>();
        for(Contact item : mylistcontacts){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }


    private void EventClick() {
        // nut reset
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.loading);
                dialog.show();
                getData(userInfor.getID());
                dialog.dismiss();
            }
        });
        //nut them contact
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
                getData(userInfor.getID());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.nav_default_pop_enter_anim,R.anim.nav_default_exit_anim);
                fr.replace(R.id.nav_host_fragment,new ProfileFragment());
                fr.commit();
            }
        });

    }

    private void init(ViewGroup root) {
        rcvContact = root.findViewById(R.id.rcvcontact);
        reset = root.findViewById(R.id.reset);
        addContact = root.findViewById(R.id.addcontact);
        searchContactList = root.findViewById(R.id.contact_search);
        back = root.findViewById(R.id.img_backContact);
        //toolbar = root.findViewById(R.id.toolbarcontact);

    }

    // Kiem tra neu chua co du lieu tren firestore thi tao moi con co roi thi hien thi
    private void CheckContact(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(id) ;
        documentReference.collection("MyContact").whereEqualTo("deleted",false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    checkpermission();
                }else{
                    getData(userInfor.getID());
                }
            }
        });
    }

    /**
// thanh tim kiem contact
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

     **/



    private void checkpermission() {
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }else{
                getContactList();
                getData(userInfor.getID());
                Toast.makeText(getActivity(), "Please press the Reset button ", Toast.LENGTH_SHORT).show();
        }
    }




// lay du lieu phone gan vao model
    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME ;
        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,sort);
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Uri uriphone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                Cursor phoneCursor = getActivity().getContentResolver().query(uriphone,null,selection,new String[]{id},null);
                if(phoneCursor.moveToNext()){
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));
                    Contact model = new Contact();
                    model.setName(name);
                    model.setNumber(number);
                    mylistcontacts.add(model);
                    CreateNew(name,number);
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
    }
    // tao moi bo suu tap add danh sach contact tu phone len firestore
    private void CreateNew(String name,String SDT) {
        UserInfor userInfor = UserInfor.getInstance();
        ContactDAO contactDAO = new ContactDAO(getContext());
        contactDAO.createContact(userInfor.getID(), name, SDT, new ContactCallBack() {
            @Override
            public void getCallback(ArrayList<Contact> contacts) {
                mylistcontacts.clear();
                mylistcontacts.addAll(contacts);
            }
        });
    }
    // lay du lieu tu firestore len recycleview
    private void getData(String UID) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        ContactDAO contactDAO = new ContactDAO(getContext());
        contactDAO.getContact(UID, new ContactCallBack() {
            @Override
            public void getCallback(ArrayList<Contact> contacts) {
                mylistcontacts = contacts;
                Log.d("MyContact",contacts.toString()) ;
                rcvContact.setLayoutManager( new LinearLayoutManager(getContext()));
                adapter = new ContactAdapter(getActivity(),contacts,ContactFragment.this);
                rcvContact.setAdapter(adapter);
                dialog.dismiss();
            }
        });
    }
    // kiem tra quyen truy cap
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getContactList();
        }else {
            Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_SHORT).show();
            checkpermission();
        }
    }
    // tạo dialog add contact
    private void showDialogAdd() {
        androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        final EditText name = new EditText(getContext());
        final EditText sdt = new EditText(getContext());
        name.setHint("Enter contact's name");
        sdt.setHint("Enter number phone");
        name.setMinEms(16);
        sdt.setMinEms(15);
        linearLayout.addView(name);
        linearLayout.addView(sdt);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        builder.setView(linearLayout);
        //button Rename
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                CreateNew(name.getText().toString(),sdt.getText().toString());
                Toast.makeText(getActivity(),"New contact created successfully",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
}
