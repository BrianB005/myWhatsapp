package com.brianbett.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;

import com.brianbett.whatsapp.retrofit.Contact;
import com.brianbett.whatsapp.retrofit.ContactsInterface;
import com.brianbett.whatsapp.retrofit.MyPreferences;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ContactsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ContactsRecyclerViewAdapter recyclerViewAdapter;

    ArrayList<Contact> myContacts;

    ArrayList<PhoneContact> phoneContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        myContacts=new ArrayList<>();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(" Select Contacts");

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        RetrofitHandler.getDBContacts(ContactsActivity.this, new ContactsInterface() {
            @Override
            public void success(List<Contact> contacts) {
                contacts.forEach(contact -> {
                    //        showing only contacts that are saved on th phone and are in the database

                    for(PhoneContact phoneContact:phoneContacts){
                        if(phoneContact.getPhoneNumber().equals(contact.getPhoneNumber())){
                            Contact newContact=new Contact(contact.getPhoneNumber(),contact.getAbout(),contact.getUserId());
                            newContact.setName(phoneContact.getName());
                            myContacts.add(newContact);
                        }
                    }


                });
                recyclerView=findViewById(R.id.recycler_view);
    //        setting up the recycler view
                actionBar.setSubtitle(myContacts.size()+" contacts");
                recyclerViewAdapter=new ContactsRecyclerViewAdapter(ContactsActivity.this,myContacts,ContactsActivity.this);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ContactsActivity.this));

                Gson gson=new Gson();
                String contactsString=gson.toJson(myContacts);

                MyPreferences.saveItemToSP(getApplicationContext(),"myContacts",contactsString);

            }

            @Override
            public void failure(Throwable throwable) {
                Log.d("Exception",throwable.getMessage());
            }
        });
        if (ContextCompat.checkSelfPermission(
                ContactsActivity.this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) {
              phoneContacts=getAllContacts();

        } else if (shouldShowRequestPermissionRationale("")) {
        } else {
            //    permission request code
            int PERMISSION_REQUEST_CODE = 200;
            ActivityCompat.requestPermissions(ContactsActivity.this,new String []{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);

        }


//        setting up the action bar



    }

//    showing the action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public ArrayList<PhoneContact> getAllContacts(){
        //        retrieving contacts using contentResolver;
        ContentResolver contentResolver=getContentResolver();
        String orderOfContacts = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
//        columnProjections=new String []{
//                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
//                ContactsContract.Contacts.HAS_PHONE_NUMBER,
//                ContactsContract.Contacts.CONTACT_STATUS,};
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        ArrayList<PhoneContact> retrievedContacts;
        try (Cursor cursor = contentResolver.query(uri, null, null, null, orderOfContacts)) {


//        initializing the phone contacts arraylist;
            retrievedContacts = new ArrayList<>();
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    removing the whitespaces in a contact
                    phoneNumber=phoneNumber.replaceAll("\\s","");
//                    getting the last 9 bits of th number eg from+254792653663 to 792653663
                    if(phoneNumber.length()>9) {
                        phoneNumber = phoneNumber.substring(phoneNumber.length() - 9);
//                        Log.d("phoneNumber", phoneNumber);
                        retrievedContacts.add(new PhoneContact(name, phoneNumber));
                    }else{
                        retrievedContacts.add(new PhoneContact(name,phoneNumber));
                    }
                }
            }
        }
        Gson gson=new Gson();
        String contactsString=gson.toJson(retrievedContacts);
        MyPreferences.saveItemToSP(getApplicationContext(),"contacts",contactsString);

        return retrievedContacts;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            phoneContacts=getAllContacts();
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}