package com.mobdeve.s13.kok.james.gueryandroid.helper;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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

    private void retrieve(String firebasePath, Context context, Consumer<File> callback ){
        StorageReference fileRef = storageRef.child(firebasePath);
        File localFile = new File(context.getCacheDir(), firebasePath   );
        fileRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        callback.accept(localFile.getAbsoluteFile());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private  void upload(String filename, String folder, String path, Consumer<String> callback){
        Uri file = Uri.fromFile(new File(path));
        String uploadPath = folder + filename + "." +getExtension(path) ;
        StorageReference imageRef = storageRef.child(uploadPath);
        imageRef.putFile(file)
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
    public void uploadPostAttachment(String filename, String path, Consumer<String> callback ){
        upload(filename, POSTS_FOLDER, path, callback);
    }
    public void uploadPfp(String filename, String path, Consumer<String> callback){
        upload(filename, PFP_FOLDER, path, callback);
    }

    private String getExtension(String path){
//        String last = path.getLastPathSegment();
        return path.substring(path.indexOf('.')+1);
    }
}
