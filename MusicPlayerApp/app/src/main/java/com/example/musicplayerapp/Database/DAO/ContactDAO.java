package com.example.musicplayerapp.Database.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicplayerapp.Database.Services.CallBack.ContactCallBack;
import com.example.musicplayerapp.Model.Contact;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class ContactDAO {
    Context context;
    Contact contact = new Contact();
    ArrayList<Contact> list = new ArrayList<>() ;

    public ContactDAO(Context context) {
        this.context = context;

    }
    public void getContact(String UserID, final ContactCallBack contactCallBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID);
        documentReference
                .collection("MyContact").whereEqualTo("deleted",false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("MyContact", snapshot.getId() + " => " + snapshot.getData());
                                contact = snapshot.toObject(Contact.class) ;
                                list.add(contact) ;
                            }  contactCallBack.getCallback(list);
                            Log.d("danhsachContact",list.toString()) ;
                        } else {
                            Log.w("MyContact", "Error getting documents.", task.getException());
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }
    //Tao mã id ngẫu nhiên cho contact
    public static String randomString() {
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        } String result = String.valueOf(sb.append(DATA.charAt(RANDOM.nextInt(DATA.length()))));
        return result;
    }
    //Tao Contact ca nhan moi
    public void createContact(final String UserID, String Name, String SDT, final ContactCallBack contactCallBack){
        randomString() ;
        Log.d("random",randomString()) ;
        String ID = "PL" + UserID + randomString() ;
        contact = new Contact(ID,Name,SDT,false) ;
//      danhsachPlaylist.add(playList) ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID) ;
        documentReference
                .collection("MyContact").document(ID).set(contact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MyContact", "DocumentSnapshot successfully updated!");
                        list.clear();
                        contactCallBack.getCallback(list);
                        getContact(UserID,contactCallBack);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyContact", "Error updating document", e);
                    }
                });

    }

    //Doi ten Contact ca nhan
    public void renameContact(final String UserID, String id, String Name, final ContactCallBack contactCallBack){
        contact = new Contact(id,Name,null,false) ;
        list.add(contact) ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID) ;
        documentReference
                .collection("MyContact").document(id).update("name",Name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MyContact", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("Đổi tên Contact thành công");
                        list.clear();
                        contactCallBack.getCallback(list);
                        getContact(UserID,contactCallBack);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyContact", "Error updating document", e);
                    }
                });
    }
    //Doi sdt Contact ca nhan
    public void reSDTContact(final String UserID, String id, String SDT, final ContactCallBack contactCallBack){
        contact = new Contact(id,null,SDT,false) ;
        list.add(contact) ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID) ;
        documentReference
                .collection("MyContact").document(id).update("number",SDT)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MyContact", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("Đổi SDT Contact thành công");
                        list.clear();
                        contactCallBack.getCallback(list);
                        getContact(UserID,contactCallBack);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyContact", "Error updating document", e);
                    }
                });
    }

    //Xoa contact ca nhan
    public void deleteContact(final String UserID, String id, final ContactCallBack contactCallBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID) ;
        documentReference
                .collection("MyContact").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MyPlaylist", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("Xóa Contact thành công");
                        list.clear();
                        contactCallBack.getCallback(list);
                        getContact(UserID,contactCallBack);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyPlaylist", "Error updating document", e);
                    }
                });

    }

    private void ToastAnnotation(String mesage){
        Toast.makeText(context,mesage,Toast.LENGTH_SHORT).show();
    }
}
