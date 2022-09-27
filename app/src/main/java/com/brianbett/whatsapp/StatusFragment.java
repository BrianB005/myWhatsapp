package com.brianbett.whatsapp;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.whatsapp.retrofit.Contact;
import com.brianbett.whatsapp.retrofit.MyPreferences;
import com.brianbett.whatsapp.retrofit.RetrievedStatus;
import com.brianbett.whatsapp.retrofit.RetrofitHandler;
import com.brianbett.whatsapp.retrofit.StatusesInterface;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class StatusFragment extends Fragment {
    //    the add new status buttons
    View typeStatus,photoStatus;
    RecyclerView recyclerView;
    ArrayList<RetrievedStatus> myStatuses;
    ArrayList<String> allContacts;

    HashMap<String,ArrayList<String>> myContactsMap;

    StatusRecyclerViewAdapter statusRecyclerViewAdapter;
    ArrayList<Contact> contactsList;
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.status_action_bar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_status, container, false);

        assert getContext()!=null;

        String contacts = MyPreferences.getSavedItem(getContext(), "myContacts");
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        Gson gson = new Gson();
        contactsList = gson.fromJson(contacts, type);
        allContacts = new ArrayList<>();
       assert  contactsList!=null;
        for (Contact contact : contactsList) {
            assert contact != null;
            allContacts.add(contact.getUserId());

        }


//        ensuring that the action bar shows
        setHasOptionsMenu(true);
        myStatuses=new ArrayList<>();
//        configuring the recyclerView
        recyclerView=rootView.findViewById(R.id.status_recycler_view);
        assert getActivity()!=null;
        statusRecyclerViewAdapter=new StatusRecyclerViewAdapter(getActivity().getApplicationContext(),myStatuses,contactsList);
        recyclerView.setAdapter(statusRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        typeStatus=rootView.findViewById(R.id.open_new_typed_status);
        photoStatus=rootView.findViewById(R.id.open_camera);
        typeStatus.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(),TypeStatusActivity.class));
        });



//        picking image from camera

        ActivityResultLauncher<Intent> launcher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        Uri uri = result.getData().getData();
                        Intent intent=new Intent(getContext(),HandleSelectedImage.class);
                        intent.putExtra("photo",uri.toString());
                        startActivity(intent);
                        // Use the uri to load the image
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {

                        Log.d("Error",ImagePicker.Companion.getError(result.getData()));
                    }
                });
        photoStatus.setOnClickListener(view-> ImagePicker.Companion.with(getActivity())
                .crop()
                .provider(ImageProvider.BOTH)
                .createIntentFromDialog(new Function1() {
                    public Object invoke(Object var1) {
                        this.invoke((Intent) var1);
                        return Unit.INSTANCE;
                    }

                    public void invoke(@NotNull Intent it) {
                        Intrinsics.checkNotNullParameter(it, "it");
                        launcher.launch(it);
                    }
                })
        );

        myContactsMap=new HashMap<>();

        myContactsMap.put("contacts",allContacts);

        RetrofitHandler.getMyLastStatus(getContext(), new StatusesInterface() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void success(List<RetrievedStatus> statuses) {

                if(statuses.size()!=0) {
                    myStatuses.add(0, statuses.get(0));

                }else{
                    RetrievedStatus retrievedStatus=new RetrievedStatus();
                    retrievedStatus.setTimeCreated("Tap here to add a status update!");
                    myStatuses.add(0, retrievedStatus);

                }
                statusRecyclerViewAdapter.notifyDataSetChanged();





            }
            @Override
            public void failure(Throwable throwable) {

            }
        });
        RetrofitHandler.getFriendsStatuses(getContext(), myContactsMap, new StatusesInterface() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void success(List<RetrievedStatus> statuses) {
                myStatuses.addAll(statuses);
                statusRecyclerViewAdapter.notifyDataSetChanged();

            }
            @Override
            public void failure(Throwable throwable) {
                Log.d("Exception Oops!",throwable.getMessage());
            }
        });

        return rootView;
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent=new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.clear_call_log:
                Toast.makeText(getActivity(),"Status privacy activated",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}