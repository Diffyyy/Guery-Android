package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

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
    @OptIn(markerClass = UnstableApi.class) public PostVideoHolder(@NonNull View itemView) {
        super(itemView);
        PostItemVidBinding binding = PostItemVidBinding.bind(itemView);
        videoView = binding.postVideoInclude.videoVv;
        player = new ExoPlayer.Builder(itemView.getContext()).build();
        videoView.setPlayer(player);
//        videoView.setControllerAutoShow(false);
        videoView.setControllerShowTimeoutMs(500);
    }
    public void bind(Post post){
        super.bind(post);
        bind(post,videoView, player);
    }

    public static void bind(Post post, PlayerView videoView, ExoPlayer player){
        MediaItem mediaItem;
        if(post.getAttached().contains("content")){
            mediaItem = MediaItem.fromUri(post.getAttached());
            player.addMediaItem(mediaItem);
            player.prepare();
            return;
        }
        StorageHelper.getInstance().retrieve(post.getAttached(), new Consumer<Uri>() {
            @Override
            public void accept(Uri uri) {
                Log.d("BURGER", "GOT VIDEO URI: "+uri.toString());
                MediaItem item = MediaItem.fromUri(uri);
                Log.d("BURGER", "MEDIA ITEM: "+item.mediaMetadata.mediaType);
                player.addMediaItem(MediaItem.fromUri(uri));
                player.prepare();
                player.seekTo(0);
                player.pause();


            }
        }, new Consumer<Exception>() {
            @Override
            public void accept(Exception e) {
                Log.d("BURGER", "FAILED TO RETRIEVE VIDEO FRMOM FIREBASE");
            }
        });
    }

    public Post getPost() {
        return post;
    }
}
