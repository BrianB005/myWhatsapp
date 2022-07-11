package com.brianbett.whatsapp;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class GridViewImageAdapter extends BaseAdapter {
    Context context;
    List<String> imagesUris;
    public GridViewImageAdapter(Context context, List<String> imagesUris){
        this.context=context;
        this.imagesUris=imagesUris;
    }
    @Override
    public int getCount() {
        return imagesUris.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView;

        if (view == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(16, 16, 16, 16);
        } else {
            imageView = (ImageView) view;
        }
        imageView.setImageURI(Uri.parse(imagesUris.get(position)));
        return imageView;
    }
}
