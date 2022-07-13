package com.brianbett.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brianbett.whatsapp.retrofit.Contact;
import com.brianbett.whatsapp.retrofit.MyPreferences;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.TypedStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TypeStatusActivity extends AppCompatActivity {
    TextView openEmojis, changeFont, changeBackground;
    View parentView;
    EditText statusInput;
    ArrayList<String> allContacts;

    static int backgroundChangeCount = 0;
    static int fontCount = 0;
    int[] backgroundColors;
    int[] fonts;


    ProgressBar progressBar;
    FloatingActionButton sendStatusBtn;

    ArrayList<Contact> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_status);


//        retrieving the list of contacts from shared preferences

        String contacts = MyPreferences.getSavedItem(getApplicationContext(), "myContacts");
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        Gson gson = new Gson();
        contactsList = gson.fromJson(contacts, type);


        allContacts = new ArrayList<>();
        if (contactsList == null) {
            return;
        } else {
            for (Contact contact : contactsList) {
                assert contact != null;
                allContacts.add(contact.getUserId());

            }
        }

        parentView = findViewById(R.id.type_status_page);
//        creating background colors array
        backgroundColors = new int[]{Color.BLUE, Color.CYAN, Color.MAGENTA, Color.GRAY, Color.LTGRAY, Color.GREEN, Color.YELLOW, Color.LTGRAY, Color.rgb(200, 214, 99), Color.rgb(108, 222, 68)};

//        creating the fonts array
        fonts = new int[]{R.font.anton, R.font.baumans, R.font.chicle, R.font.droid_sans_bold, R.font.duru_sans, R.font.elsie, R.font.fauna_one, R.font.gudea_italic};


        openEmojis = findViewById(R.id.open_emojis);
        changeBackground = findViewById(R.id.change_background);
        changeFont = findViewById(R.id.change_font);
        statusInput = findViewById(R.id.status_input);

        progressBar = findViewById(R.id.progress_bar);
        sendStatusBtn = findViewById(R.id.send_status);


        openEmojis.setOnClickListener(view -> {
            Toast.makeText(this, "Stay tuned .Emoji page is still a work in progress", Toast.LENGTH_SHORT).show();
        });
        changeBackground.setOnClickListener(view -> {
            changeBackground();
        });
        changeFont.setOnClickListener(view -> {
            changeInputFont();
        });

        statusInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (statusInput.getText().toString().equals("")) {
                    sendStatusBtn.setVisibility(View.GONE);

                } else {
                    sendStatusBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        sendStatusBtn.setOnClickListener(view -> sendStatus());
    }

    private void changeInputFont() {
        if (fontCount == fonts.length) {
            fontCount = 0;
        } else {
            Typeface typeface = ResourcesCompat.getFont(this, fonts[fontCount]);
            statusInput.setTypeface(typeface);
            fontCount++;
        }

    }

    public void changeBackground() {
        if (backgroundChangeCount == backgroundColors.length) {
            backgroundChangeCount = 0;
        } else {
            parentView.setBackgroundColor(backgroundColors[backgroundChangeCount]);
            backgroundChangeCount++;
        }

    }

    public void sendStatus() {


        String title = statusInput.getText().toString();
        int selectedFont = 0;
        if (fontCount > 0)   selectedFont = fontCount - 1;

        int selectedBackgroundColor = 0;
        if (backgroundChangeCount > 0)  selectedBackgroundColor = backgroundChangeCount - 1;

        String backgroundColor = String.valueOf(backgroundColors[selectedBackgroundColor]);
        String font = String.valueOf(fonts[selectedFont]);


//        String backgroundColor=backgroundChangeCount==1?String.valueOf(backgroundColors[backgroundChangeCount]):String.valueOf(backgroundColors[backgroundChangeCount-1]);
////        String font=fontCount==1?String.valueOf(fonts[fontCount]):String.valueOf(fonts[fontCount-1]);
            TypedStatus typedStatus = new TypedStatus(allContacts, title, font, backgroundColor);

            RetrofitHandler.createTypedStatus(getApplicationContext(), typedStatus, progressBar, statusInput);

    }
}