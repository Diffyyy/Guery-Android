package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.ImageLoaderHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.StorageHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

import java.util.function.Consumer;

public class PostImageHolder extends PostItemHolder {

    private ImageView image;
    public PostImageHolder(@NonNull View itemView) {
        super(itemView);
        PostItemImgBinding binding = PostItemImgBinding.bind(itemView);
        image = binding.postImageInclude.imageIv;
    }

    public static void bind(String attached, ImageView imageView){
        if(attached==null){
            imageView.setImageResource(R.drawable.error_image);
            Log.d("BURGER", "ERROR IMAGE ATTACHMENT NOT FOUND");
            return;
        }

        if(attached.contains("content")){
            imageView.setImageURI(Uri.parse(attached));
            return;
        }

//        ImageLoaderHelper.placeHolderImage(image,1000);
        StorageHelper.getInstance().retrieve(attached, new Consumer<Uri>() {
            @Override
            public void accept(Uri uri) {
                ImageLoaderHelper.loadImage(uri, imageView);
            }
        }, new Consumer<Exception>() {
            @Override
            public void accept(Exception e) {
                Log.d("BURGER", "FAILED TO LOAD POST IMAGE: "+e.getMessage() + " FOR LOCATION: "+attached );
            }
        });
    }

    public static void bind(Post post, ImageView imageView){
        bind(post.getAttached(), imageView);
    }
    public void bind(Post post){
        super.bind(post);
        bind(post, image);
//        image.setImageResource(R.drawable.kirby);
    }

}
