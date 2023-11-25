package com.mobdeve.s13.kok.james.gueryandroid.helper;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.squareup.picasso.Picasso;

import java.util.function.Consumer;

public class ImageLoaderHelper {

//    public static void placeHolderImage(ImageView imageView, int radius){
//        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(imageView.getContext());
//        circularProgressDrawable.setCenterRadius(radius);
////        imageView.setImageDrawable(circularProgressDrawable);
////        imageView.setImageResource(R.drawable.loading);
//        circularProgressDrawable.setBounds(50,50,50,50);
//
////        Picasso.get()
////                .placeholder(circularProgressDrawable)
////                .into(imageView );
//    }

    public static void loadImage(Uri uri, ImageView imageView){
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.progress)
                .into(imageView );
    }

    public static void loadPfp(String pfp, ImageView imageView){
        if(pfp==null) imageView.setImageResource(R.drawable.placeholder);
        else if(pfp.contains("content")) imageView.setImageURI(Uri.parse(pfp));
        else {
            StorageHelper.getInstance().retrieve(pfp, new Consumer<Uri>() {
                @Override

                public void accept(Uri uri) {
                    ImageLoaderHelper.loadImage(uri, imageView);
                }
            }, new Consumer<Exception>() {
                @Override
                public void accept(Exception e) {
                    Log.d("BURGER", "FAILED TO LOAD PROFILE IMAGE: "+e.getMessage());
                }
            });

        }
    }

//    public static void loadImage(Uri uri, ImageView imageView){
//        loadImage(uri, imageView, 30);
//    }
}
