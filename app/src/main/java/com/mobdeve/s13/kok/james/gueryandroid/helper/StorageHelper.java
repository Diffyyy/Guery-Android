package com.mobdeve.s13.kok.james.gueryandroid.helper;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class StorageHelper {

    private static StorageHelper instance;
    public static final String POSTS_FOLDER = "posts/";
    public static final String PFP_FOLDER = "pfp/";

    public static final String URI = "gs://guery-android.appspot.com";


    public static StorageHelper getInstance(){
        if(instance== null){
            instance = new StorageHelper();
        }
        return instance;
    }
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef=  storage.getReference();

    public void retrieve(String firebasePath, Consumer<Uri> callback, Consumer<Exception> failed){
        if(firebasePath==null || firebasePath.isEmpty()){
            failed.accept(new NullPointerException("Firebase path is null"));
            return;
        }
        Log.d("BURGER", "RETRIEVING ATTACHMENT: "+firebasePath);
        StorageReference fileRef = storageRef.child(firebasePath);
        fileRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri result) {
//                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(iv.getContext());
//                        circularProgressDrawable.setCenterRadius(30);
                        callback.accept(result);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failed.accept(e);
                    }
                });

    }

    public void upload(String filename, String folder, InputStream inputStream, Consumer<String> callback){
//        Log.d("BURGER", "FILE EXISTS: "+f.exists());
        String uploadPath = folder + filename ;
        StorageReference imageRef = storageRef.child(uploadPath);
        imageRef.putStream(inputStream)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                            String downloadUrl = uri.toString();
//                            callback.accept(downloadUrl);
//                        });
                        callback.accept(uploadPath);
                    }
                });
    }
    public void uploadPostAttachment(String filename, InputStream inputStream,  Consumer<String> callback ){
        if(inputStream!=null)upload(filename, POSTS_FOLDER, inputStream, callback);
        else callback.accept(null);
    }



}
