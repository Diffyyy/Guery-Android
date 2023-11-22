package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

public class PostImageHolder extends PostItemHolder {

    private ImageView image;
    public PostImageHolder(@NonNull View itemView) {
        super(itemView);
        PostItemImgBinding binding = PostItemImgBinding.bind(itemView);
        image = binding.imageIv ;

    }
    public void bind(Post post){
        super.bind(post);
//        image.setImageURI(Uri.parse(post.getAttached()));
//        image.setImageURI(Uri.parse(post.getAttached()));
        image.setImageResource(R.drawable.kirby);
    }

}
