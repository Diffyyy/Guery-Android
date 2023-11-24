package com.mobdeve.s13.kok.james.gueryandroid.helper;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.squareup.picasso.Picasso;

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

//    public static void loadImage(Uri uri, ImageView imageView){
//        loadImage(uri, imageView, 30);
//    }
}
