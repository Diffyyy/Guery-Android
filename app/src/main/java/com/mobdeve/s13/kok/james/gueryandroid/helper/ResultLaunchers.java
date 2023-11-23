package com.mobdeve.s13.kok.james.gueryandroid.helper;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.mobdeve.s13.kok.james.gueryandroid.activity.PostDetailsActivity;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.PostItemAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

import java.util.ArrayList;

public class ResultLaunchers {
    public static ActivityResultLauncher<Intent> postClicked(ActivityResultCaller caller, PostItemAdapter adapter){
        ActivityResultLauncher<Intent> myLauncher = caller.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult o) {

                Log.d("BURGER", "");
                if(o.getResultCode()==RESULT_OK){
                    Intent intent = o.getData();
                    int index = intent.getIntExtra(PostDetailsActivity.POST_INDEX, -10);
                    Log.d("BURGER", "HEYYY: "+index);
                    Post post = intent.getParcelableExtra("post");

                    Log.d("BURGER", "GOT POST: "+post);
                    if(index!=-10){
                        adapter.getPosts().set(index, post);
                        adapter.notifyItemChanged(index );
                    }
                    Log.d("BURGER", "POST INDEX: "+intent.getIntExtra(PostDetailsActivity.POST_INDEX, -10));
                }else if(o.getResultCode()==RESULT_CANCELED){
                    Log.d("BURGER", "POST NOT CHANGED");
                }
            }
        });
        return myLauncher;
    }


}
