package com.brianbett.whatsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brianbett.whatsapp.retrofit.RetrofitHandler;

import java.util.ArrayList;

public class StatusFragment extends Fragment {
    //    the add new status buttons
    View typeStatus,photoStatus;
    RecyclerView recyclerView;
    ArrayList<SingleStatus> statuses;
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



//        ensuring that the action bar shows
        setHasOptionsMenu(true);
        statuses=new ArrayList<>();
//        configuring the recyclerView
        recyclerView=rootView.findViewById(R.id.status_recycler_view);
        StatusRecyclerViewAdapter statusRecyclerViewAdapter=new StatusRecyclerViewAdapter(getContext(),statuses);
        recyclerView.setAdapter(statusRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        typeStatus=rootView.findViewById(R.id.open_new_typed_status);
        photoStatus=rootView.findViewById(R.id.open_camera);
        typeStatus.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(),TypeStatusActivity.class));
        });
        photoStatus.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(),CameraActivity.class));
        });


//        initializing the statuses  array
        SingleStatus singleStatus =new SingleStatus(R.drawable.image7,"My status",724084603,"Tap to add a new status");
        singleStatus.setMine(true);
        SingleStatus singleStatus1 =new SingleStatus(R.drawable.image2,"Audrey",724084603,"Yesterday 11:24");
        SingleStatus singleStatus2 =new SingleStatus(R.drawable.image3,"Ses Kush",724084603,"Yesterday 21:35");
        SingleStatus singleStatus3 =new SingleStatus(R.drawable.image4,"Brian Bett",724084603,"Today 07:09");
        SingleStatus singleStatus4 =new SingleStatus(R.drawable.image5,"Ivy Cherop",724084603,"56 minutes ago");
        SingleStatus singleStatus5 =new SingleStatus(R.drawable.image4,"Stella",724084603,"Today 12:35");
        SingleStatus singleStatus6 =new SingleStatus(R.drawable.image6,"Stacy",724084603,"Today 02:35");
        SingleStatus singleStatus7 =new SingleStatus(R.drawable.image7,"Drake",724084603,"Yesterday 06:04");
        SingleStatus singleStatus8 =new SingleStatus(R.drawable.image1,"Kim Yun",724084603,"Yesterday 21:30");
        SingleStatus singleStatus9 =new SingleStatus(R.drawable.image3,"Bernice",724049005,"43 minutes ago");

        statuses.add(singleStatus);statuses.add(singleStatus1);statuses.add(singleStatus2);statuses.add(singleStatus3);
        statuses.add(singleStatus4);statuses.add(singleStatus5);statuses.add(singleStatus6);statuses.add(singleStatus7);
        statuses.add(singleStatus8);statuses.add(singleStatus9);statuses.add(singleStatus2);statuses.add(singleStatus3);


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