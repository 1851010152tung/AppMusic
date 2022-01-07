package com.example.musicplayerapp.Fragments.Contact.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Database.DAO.ContactDAO;

import com.example.musicplayerapp.Database.Services.CallBack.ContactCallBack;

import com.example.musicplayerapp.Fragments.Contact.ContactFragment;

import com.example.musicplayerapp.Model.Artist;
import com.example.musicplayerapp.Model.Contact;

import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements Filterable {
    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1;
    private static final String LOG_TAG = "AndroidExample";
    Context context;
    List<Contact> arraylist;
    List<Contact> contactlistfull;
    ContactFragment contactFragment;

    public ContactAdapter(Context context, List<Contact> contacts, ContactFragment contactFragment) {
        this.context = context;
        this.arraylist = contacts;
        this.contactlistfull = new ArrayList<>(arraylist);
        this.contactFragment = contactFragment;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.item_contact,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ContactAdapter.ViewHolder holder, int position) {
        Contact contact = arraylist.get(position);
        holder.tv_Name.setText(contact.getName());
        holder.tv_Number.setText(contact.getNumber());
        holder.ic_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tao menu popup chon
                PopupMenu popupMenu = new PopupMenu(context, holder.ic_choose);
                popupMenu.inflate(R.menu.options_menu_contact);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu1:
                                ShowDialogName(position);
                                break;
                            case R.id.menu2:
                                ShowDialogSDT(position);
                                break;
                            case R.id.menu3:
                                DoDelete(position);
                                break;
                            case R.id.menu4:
                                if(checkPermission(Manifest.permission.SEND_SMS)){
                                }else {
                                    ActivityCompat.requestPermissions(contactFragment.getActivity(),new String[]{Manifest.permission.SEND_SMS},MY_PERMISSION_REQUEST_CODE_SEND_SMS);
                                }
                                ShowDialogInviteFr(position);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    // Dung Filter tim kiem cho toolbar
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private  Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> contacts = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                contacts.addAll(contactlistfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Contact item : contactlistfull) {
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getNumber().contains(filterPattern)) {
                        contacts.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = contacts;
            return  results;
        }
// ham tra ket qua tim kiem
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arraylist.clear();
            arraylist.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



    //Filter cho EditText
    public void filterList (ArrayList<Contact> filteredList){
        arraylist = filteredList;
        notifyDataSetChanged();
    }


// anh xa
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tv_Name, tv_Number;
        ImageView ic_choose;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_name);
            tv_Number = itemView.findViewById(R.id.tv_number);
            ic_choose = itemView.findViewById(R.id.ic_choose);
        }

    }
    //Hàm xóa contact
    private void DoDelete(final int position){
        UserInfor userInfor = UserInfor.getInstance();
        String UserID = userInfor.getID();
        final Contact contact = arraylist.get(position);
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context)
                .setMessage("Do you want to delete?")
                .setTitle("Notification ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.loading);
                        dialog.show();
                        ContactDAO contactDAO = new ContactDAO(context);
                        contactDAO.deleteContact(UserID, arraylist.get(position).getId(), new ContactCallBack() {
                            @Override
                            public void getCallback(ArrayList<Contact> contacts) {
                                arraylist.remove(contact);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00ACC1"));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00ACC1"));
    }
    private void ShowDialogInviteFr(final int position){
        androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        final EditText nd = new EditText(context);
        nd.setText("The app allows you to stream music online. Check it out!");
        nd.setHint("your message");
        nd.setMinEms(16);
        linearLayout.addView(nd);
        linearLayout.setPadding(10,50,10,10);
        builder.setView(linearLayout);
        //button Rename
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    onSend(position,nd.getText().toString());
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

    private boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(context,permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    //tao tao edit text nhap ten
    private void ShowDialogName(final int position) {
        androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        final EditText name = new EditText(context);
        name.setHint("Your contact's name");
        name.setMinEms(16);
        linearLayout.addView(name);
        linearLayout.setPadding(10,50,10,10);
        builder.setView(linearLayout);
        //button Rename
        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                DoRename(position, name.getText().toString());
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
//sua ten
    private void DoRename(int position, String name) {
        ContactDAO contactDAO = new ContactDAO(context);
//        UserInfor UserInfor = UserInfor.getInstance();
        UserInfor userInfor = UserInfor.getInstance();
        contactDAO.renameContact(userInfor.getID(), arraylist.get(position).getId(), name, new ContactCallBack() {
            @Override
            public void getCallback(ArrayList<Contact> contacts) {
                arraylist.clear();
                arraylist.addAll(contacts);
                notifyDataSetChanged();
            }
        });
    }
    //tao edit text nhap so dien thoai
    private void ShowDialogSDT(final int position) {
        androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        final EditText sdt = new EditText(context);
        sdt.setHint("Phone number");
        sdt.setMinEms(16);
        linearLayout.addView(sdt);
        linearLayout.setPadding(10,50,10,10);
        builder.setView(linearLayout);
        //button Rename
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                DoResdt(position, sdt.getText().toString());
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
// sua sdt tren firestore
    private void DoResdt(int position, String sdt) {
        ContactDAO contactDAO = new ContactDAO(context);
//        UserInfor UserInfor = UserInfor.getInstance();
        UserInfor userInfor = UserInfor.getInstance();
        contactDAO.reSDTContact(userInfor.getID(), arraylist.get(position).getId(), sdt, new ContactCallBack() {
            @Override
            public void getCallback(ArrayList<Contact> contacts) {
                arraylist.clear();
                arraylist.addAll(contacts);
                notifyDataSetChanged();
            }
        });
    }
    public void onSend(final int position,String nd){
        String phoneNumber = arraylist.get(position).getNumber();
        if(phoneNumber == null || phoneNumber.length()==0 || nd == null || nd.length() == 0){
            return;
        }
        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,null,nd,null,null);
            Toast.makeText(context,"Sending the message was successful",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }

}
