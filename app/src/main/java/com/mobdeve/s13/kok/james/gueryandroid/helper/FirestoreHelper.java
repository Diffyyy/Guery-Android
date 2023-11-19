package com.mobdeve.s13.kok.james.gueryandroid.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FirestoreHelper {
    public static final String POSTS = "posts";
    public static final String users = "users";
    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirestoreHelper instance = null  ;


    public static FirestoreHelper getInstance(){
        if (instance == null){
            instance = new FirestoreHelper()    ;
        }
        return instance;
    }

    public  void addPost(Post post, Consumer<String> callback){
        Map<String, Object> map = convertPost(post);
        db.collection(POSTS)
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.accept(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("BURGER", "Error adding post", e);
                    }
                });
    }



    public static Map<String, Object> convertPost(Post post){
        Map<String, Object> map = new HashMap<>();

        map.put("game", post.getGame());
        map.put("body", post.getBody());
        map.put("profile", post.getProfile());
        map.put("title", post.getTitle());
        map.put("upvotes", post.getUpvotes());
        map.put("date", post.getCreatedAt());
        map.put("comments", post.getComments());
        return map;
    }



}
