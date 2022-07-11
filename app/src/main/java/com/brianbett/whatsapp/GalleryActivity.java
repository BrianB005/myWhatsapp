package com.brianbett.whatsapp;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;


import android.content.pm.PackageManager;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.GridView;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    ArrayList<File> myImages;
    List<String> imageUris;
    List<Bitmap> thumbnails;

    RecyclerView recyclerView;
    ImagesRecyclerViewAdapter myAdapter;
//    GridView gridView;
//    GridViewImageAdapter gridViewImageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        recyclerView=findViewById(R.id.images_recycler_view);
//        gridView=findViewById(R.id.grid_view);

        
        imageUris=new ArrayList<>();
        thumbnails=new ArrayList<>();
        if(checkPermissions()){
            showImages();
        }


    }
    private boolean checkPermissions(){
        boolean feedback=false;
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},200);
        }
        else{
            feedback=true;
        }
        return feedback;
    }

    private ArrayList<File> getImages(File filesDirectory) {
        ArrayList<File> allImages = new ArrayList<>();
        File[] allFiles = filesDirectory.listFiles();
        for (File file : allFiles) {
            if (file.isDirectory()) {
                allImages.addAll(getImages(file));
            } else {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    allImages.add(file);
                }
            }
        }
        return allImages;
    }
    private  void showImages() {
        myImages=getImages(Environment.getExternalStorageDirectory());
        for(int i=0;i<myImages.size();i++){
            imageUris.add(String.valueOf(myImages.get(i)));

//            Bitmap compressedImage= ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(myImages.get(i).getPath()),100,160);
//            thumbnails.add(compressedImage);
        }
        myAdapter=new ImagesRecyclerViewAdapter(GalleryActivity.this,imageUris);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(GalleryActivity.this,3));

//        gridViewImageAdapter=new GridViewImageAdapter(GalleryActivity.this,imageUris);
//        gridView.setAdapter(gridViewImageAdapter);

        

    }


}













