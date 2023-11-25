package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemVidBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.StorageHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PostVideoHolder extends PostItemHolder {
    private PlayerView videoView;
    private MediaItem video;
    private ExoPlayer player;


    @OptIn(markerClass = UnstableApi.class)
    public PostVideoHolder(@NonNull View itemView) {
        super(itemView);
        PostItemVidBinding binding = PostItemVidBinding.bind(itemView);
        videoView = binding.postVideoInclude.vvVideo;
        player = new ExoPlayer.Builder(itemView.getContext()).build();

        videoView.setPlayer(player);
        videoView.setControllerShowTimeoutMs(1000);

    }
    public void bind(Post post){
        super.bind(post);
        bind(post,videoView, player);
    }
    public static void initializePlayer(ExoPlayer player, MediaItem item){

        player.setMediaItem(item);
        player.prepare();
        player.seekTo(0);
        player.pause();

    }
    public static void bind(String attached, PlayerView videoView, ExoPlayer player){

        MediaItem mediaItem;
        if(attached == null){
//            initializePlayer(player, MediaItem.EMPTY);
            //Log.d("BURGER", "ERRO ATTACHMENT VIDEO NOT FOUND");
            return;
        }
        if(attached.contains("content")){
            mediaItem = MediaItem.fromUri(attached);
            initializePlayer(player, mediaItem);
            return;
        }
        StorageHelper.getInstance().retrieve(attached, new Consumer<Uri>() {
            @Override
            public void accept(Uri uri) {
                //Log.d("BURGER", "GOT VIDEO URI: "+uri.toString());
                MediaItem item = MediaItem.fromUri(uri);
                //Log.d("BURGER", "MEDIA ITEM: "+item.mediaMetadata.mediaType);
                initializePlayer(player,item);
            }
        }, new Consumer<Exception>() {
            @Override
            public void accept(Exception e) {
                //Log.d("BURGER", "FAILED TO RETRIEVE VIDEO FRMOM FIREBASE");
            }
        });
    }

    public static void bind(Post post, PlayerView videoView, ExoPlayer player){
        bind(post.getAttached(), videoView, player);
    }

    public Post getPost() {
        return post;
    }
}
