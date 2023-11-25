package com.mobdeve.s13.kok.james.gueryandroid.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.activity.EditPostActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.HomeActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.FragmentCreatepostBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.ImageLoaderHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.StorageHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostImageHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostVideoHolder;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;


public class CreatepostFragment extends Fragment {
    public static final String IS_ADD = "isAdd";
    public static final String POST = "post";
    private ActivityResultLauncher<Intent> mediaPickerLauncher;
    private String attachment = null;
    private FragmentCreatepostBinding viewBinding;

    private PlayerView playerView;
    private ImageView imageView;

    private ExoPlayer player;
    public static final String ATTACHMENT_PLACEHOLDER = "PLACEHOLDERSECRET";

    public CreatepostFragment(){

    }

    private boolean isAdd(){
        return getArguments().getBoolean(IS_ADD);
    }

    private Post getPost(){
        return getArguments().getParcelable(POST);
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentCreatepostBinding.inflate(inflater, container,false);

        imageView = viewBinding.ivPreview;
        playerView = viewBinding.vvPreview;
        player = new ExoPlayer.Builder(playerView.getContext()).build();
        playerView.setControllerShowTimeoutMs(1000);
        playerView.setPlayer(player);

        viewBinding.btnRemoveAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachment = null;
                hideImageView();
                hideVideoView();
                viewBinding.btnRemoveAttach.setVisibility(View.INVISIBLE);
                Log.d("BURGER", "ATTACHMENT SET TO NULL ON CLICK");
            }
        });
        ImageLoaderHelper.loadPfp(AuthHelper.getInstance().getProfile().getPfp(), viewBinding.ivCreatePfp);

        if(!isAdd()){
            Post post =getPost()    ;
            viewBinding.etCreatePosttitle.setText(post.getTitle());
            viewBinding.etCreateCommunity.setText(post.getGame());
            viewBinding.etCreateContent.setText(post.getBody());
            //viewBinding.btnCreatePost.setText("Save");
            if(post.getType()==Post.PostType.VIDEO.value){
                toggleVideoView(post.getAttached());
                attachment = ATTACHMENT_PLACEHOLDER;
                Log.d("BURGER", "ATTACHMENT IS PLACEHOLDER");
            }else if(post.getType()==Post.PostType.IMAGE.value){
                toggleImageView(post.getAttached());
                attachment = ATTACHMENT_PLACEHOLDER;
                Log.d("BURGER", "ATTACHMENT IS PLACEHOLDER");
            }
        }
        mediaPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if (resultCode == RESULT_OK) {
                            if (data.getData() != null) {
                                if (Build.VERSION.SDK_INT < 19) {
                                    attachment = data.getData().toString();
                                    //We can set this to Filename
                                    viewBinding.tvMedia.setText(attachment);

                                } else {
                                    attachment = data.getData().toString();
                                    try {
                                        getActivity().getContentResolver().takePersistableUriPermission(Uri.parse(attachment), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    } catch (SecurityException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(attachment.contains("image")) {
                                    toggleImageView(attachment);
                                }
                                else if(attachment.contains("video")) {
                                    toggleVideoView(attachment);
                                }
                            }
                        }
                        if (result.getResultCode() == RESULT_OK){
                            try {
                                if(result.getData() != null) {

                                }
                            } catch(Exception exception){
                                Log.d("TAG",""+exception.getLocalizedMessage());
                            }
                        }
                    }
                }
        );
        viewBinding.ibAddImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("*/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                mediaPickerLauncher.launch(Intent.createChooser(i, "Select attachment"));
            }
        });

        viewBinding.btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new profile
                //add post to home
                String postTitle = viewBinding.etCreatePosttitle.getText().toString();
                String postContent = viewBinding.etCreateContent.getText().toString();
                String postCommunity = viewBinding.etCreateCommunity.getText().toString();
                if(!postTitle.isEmpty() && !postContent.isEmpty() && !postCommunity.isEmpty()){
                    Post post;
                    Log.d("BURGER", "MADE IT HERE?");
                    if(isAdd()){
                        post  = new Post(postCommunity, AuthHelper.getInstance().getProfile(), LocalDateTime.now(), postTitle, postContent);;
                    }else{
                        post = getPost()    ;
                        post.setTitle(postTitle);
                        post.setBody(postContent);
                        post.setGame(postCommunity);
                        post.setCreatedAt(LocalDateTime.now());
                    }

                    if(attachment==null) post.setType(Post.PostType.TEXT.value);
                    else if(attachment.toString().contains("image")) {
                        Log.d("BURGER", "IMAGE ATTACHED");
                        post.setType(Post.PostType.IMAGE.value);
                    }else if(attachment.toString().contains("video")){
                        post.setType(Post.PostType.VIDEO.value);
                        Log.d("BURGER", "VIDEO ATTACHED");
                    }else if(!attachment.equals(ATTACHMENT_PLACEHOLDER)){
                        viewBinding.btnRemoveAttach.callOnClick();
                        Toast.makeText(getContext(), "Only images and videos allowed", Toast.LENGTH_SHORT);
                    }
                    viewBinding.etCreatePosttitle.setText(null);
                    viewBinding.etCreateContent.setText(null);
                    viewBinding.etCreateCommunity.setText(null);

                    if(isAdd())FirestoreHelper.getInstance().addPost(post, new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            AuthHelper.getInstance().getProfile().incrementPosts();
                            Log.d("BURGER", "AM I IN HERE?");
                            post.setId(s);
                            post.setAttachment(attachment);
                            InputStream inputStream = null;
                            try {
                                inputStream = (attachment==null)?null: getActivity().getContentResolver().openInputStream(Uri.parse(attachment));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            StorageHelper.getInstance().uploadPostAttachment(s, inputStream, new Consumer<String>() {
                                @Override
                                public void accept(String s) {
//                                        post.setAttachment(s);
                                    ((HomeActivity)getActivity()).setItemSelected(R.id.nav_home);
                                    ((HomeActivity)getActivity()).addPost(post);
                                }
                            });
                        }
                    });
                    else{
                        Log.d("BURGER", "ATTACHMENT: "+attachment);
                        boolean changed = !Objects.equals(attachment, ATTACHMENT_PLACEHOLDER);
                        FirestoreHelper.getInstance().editPost(post, new Consumer<Void>() {
                            @Override
                            public void accept(Void unused) {
                                Log.d("BURGER", "WHAT IS WRONG HERE");

                                if(changed){

                                    post.setAttachment(attachment);
                                    Log.d("BURGER", "POST ATTACHMENT CHANGED: "+attachment);
                                }
                                Consumer<String> callback = new Consumer<String>() {
                                    @Override
                                    public void accept(String s) {
                                        Bundle bundle = new Bundle();
                                        Log.d("BURGER", "SENDING RESULTS BACK: "+post);
                                        bundle.putParcelable(POST, post);
                                        setArguments(bundle);
                                        ((EditPostActivity)getActivity()).finishEdit();
                                    }
                                };
                                InputStream inputStream;
                                if(changed){
                                    try {
                                        inputStream = (attachment==null )?null: getActivity().getContentResolver().openInputStream(Uri.parse(attachment));
                                    } catch (FileNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                    StorageHelper.getInstance().uploadPostAttachment(post.getId(), inputStream, callback);
                                }
                                else{
                                    callback.accept(null);

                                }
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(container.getContext(), "Make sure all fields are not blank", Toast.LENGTH_LONG).show();
                }
            }
        });

        viewBinding.btnCreateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return back to home
                if(isAdd()) ((HomeActivity)getActivity()).setItemSelected(R.id.nav_home);
                else{
                    ((EditPostActivity)getActivity()).finishEdit();
                }
            }
        });


        return viewBinding.getRoot();
    }
    private void hideImageView(){
        imageView.setVisibility(View.INVISIBLE);

    }
    private void hideVideoView(){
        player.stop();
        playerView.setVisibility(View.INVISIBLE);

    }
    private void showImageView(){
        imageView.setVisibility(View.VISIBLE);
    }
    private void showVideoView(){
        playerView.setVisibility(View.VISIBLE);
    }

    private void toggleImageView(String attached){
        viewBinding.btnRemoveAttach.setVisibility(View.VISIBLE);
        showImageView();
        hideVideoView();
        PostImageHolder.bind(attached, imageView);
    }

    private void toggleVideoView(String attached)   {
        viewBinding.btnRemoveAttach.setVisibility(View.VISIBLE);
        hideImageView();
        showVideoView();
        PostVideoHolder.bind(attached, playerView, player);

    }


}