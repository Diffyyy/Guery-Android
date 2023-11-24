package com.mobdeve.s13.kok.james.gueryandroid.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ScrollView;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.CommentAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityPostDetailsBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostFooterLayoutBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostHeaderLayoutBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemVidBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostVideoLayoutBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.listener.ProfileClickListener;
import com.mobdeve.s13.kok.james.gueryandroid.listener.ReplyListener;
import com.mobdeve.s13.kok.james.gueryandroid.listener.SwipeUpListener;
import com.mobdeve.s13.kok.james.gueryandroid.listener.VoteListener;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.model.Content;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Vote;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.ContentHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostImageHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostItemHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostVideoHolder;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.function.Consumer;

public class    PostDetailsActivity extends AppCompatActivity implements ContentHolder, SwipeRefreshLayout.OnRefreshListener {
    public final static String POST_KEY = "Post";
    public static final String POST_INDEX  = "postIndex";
    private View loadingSpinner;
//    private View postView;
    ActivityPostDetailsBinding binding;
    private PostItemBinding postBinding;
    private String postId;
    private ScrollView scrollView;
    private Post post;
    private Vote prevVote;

    private CommentAdapter adapter;
    private ArrayList<Comment> comments;

    private PostFooterLayoutBinding footerBinding;
    private PostHeaderLayoutBinding headerBinding;
    private SwipeRefreshLayout refreshLayout;
    private ViewStub  postStub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding   = ActivityPostDetailsBinding.inflate(getLayoutInflater());


        Intent intent = getIntent();
        postId = intent.getStringExtra(POST_KEY);
//        Log.println(Log.ASSERT, "BURGER", ""+post.createdAt );


        loadingSpinner = binding.loading.getRoot();
        scrollView = binding.scrollView;
        refreshLayout = binding.refreshLayout;
        refreshLayout.setOnRefreshListener(this);


        comments = new ArrayList<>();
        adapter = new CommentAdapter(  );
        adapter.setReplies(comments);

        binding.commentsRv.setLayoutManager(new LinearLayoutManager(PostDetailsActivity.this));
        binding.commentsRv.setAdapter(adapter);
        postBinding = binding.postBinding;
        initializeListeners();


        setContentView(binding.getRoot());

        retrievePost();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                if(post.isVoting()) return;

                Intent intent = getIntent();
                post.getComments().clear();
                if(post.getUserVote().equals(prevVote)){
                    setResult(RESULT_CANCELED);
                }else{
                    intent.putExtra("post", post);
                    setResult(RESULT_OK,intent );
                }

                Log.d("BURGER", "BACK PRESSED:  "+post);
                finish();
            }
        });


    }
    private void initializeListeners(){
        headerBinding = postBinding.postHeaderInclude;
        footerBinding = postBinding.postFooterInclude;
        footerBinding.postEngagementBar.postUpvoteBtn.setOnClickListener(new VoteListener(this, this,footerBinding.postEngagementBar.postDownvoteBtn, Vote.UP,  footerBinding.postEngagementBar.upvoteTv ));
        footerBinding.postEngagementBar.postDownvoteBtn.setOnClickListener(new VoteListener(this, this,footerBinding.postEngagementBar.postUpvoteBtn, Vote.DOWN,  footerBinding.postEngagementBar.upvoteTv ));
        footerBinding.postEngagementBar.postReplyBtn.setOnClickListener(new ReplyListener(this, adapter, 1));

        ProfileClickListener clickListener = new ProfileClickListener(this);
        headerBinding.usernameTv.setOnClickListener(clickListener);
        headerBinding.pfpIv.setOnClickListener(clickListener);
    }
    private void retrievePost(){

        loadStart();
        FirestoreHelper.getInstance().retrievePost(postId, new Consumer<Post>() {
            @Override
            public void accept(Post post) {
                if(post==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PostDetailsActivity.this, R.style.myDialog));
                    builder.setTitle("Error occurred");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    builder.setMessage("Post details could not be retrieved (post may have been deleted)");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                Log.d("BURGER", "POST DETAILS: "+post);
                loadStop();
                PostDetailsActivity.this.post = post;
                bind(postBinding, post);
                prevVote = post.getUserVote();

                Log.d("BURGER", "ADAPTER: "+adapter);
                adapter.setReplies(post.getComments());
                adapter.retrieveComments();



                binding.backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       getOnBackPressedDispatcher().onBackPressed();
//                        finish();
                    }
                });

            }
        });
    }
    @OptIn(markerClass = UnstableApi.class) private void bind(PostItemBinding binding, Post post){
        PostItemHolder.bind(post, binding, this);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        postBinding.placeholderCl.removeAllViews();
        if(post.getType() == Post.PostType.IMAGE.value){
            View view = getLayoutInflater().inflate(R.layout.post_image_layout, postBinding.getRoot(),false );
            postBinding.placeholderCl.addView(view,0, layoutParams );
            PostImageHolder.bind(post, view.findViewById(R.id.image_iv) );
        }else if(post.getType()==Post.PostType.VIDEO.value){

            View view = getLayoutInflater().inflate(R.layout.post_video_layout, postBinding.getRoot(),false );
            PlayerView playerView = view.findViewById(R.id.video_vv);
            ExoPlayer player = new ExoPlayer.Builder(playerView.getContext()).build();

            playerView.setControllerShowTimeoutMs(1000);
            playerView.setPlayer(player);
            PostVideoHolder.bind(post, playerView, player );
            postBinding.placeholderCl.addView(view,0, layoutParams );
//            PostVideoHolder.bind(post, view.findViewById(R.id.video_vv) );
        }

    }

    public void loadStart(){
        refreshLayout.setRefreshing(true);
        postBinding.getRoot().setVisibility(View.INVISIBLE);
        binding.commentsRv.setVisibility(View.INVISIBLE);
//        loadingSpinner.setVisibility(View.VISIBLE);
////        int viewheight = loading.getHeight();
//
//        ObjectAnimator animator = ObjectAnimator.ofFloat(loadingSpinner,"translationY", -20f, 20f);
//        animator.setDuration(100);
//        animator.start();
    }
    public void loadStop(){
        refreshLayout.setRefreshing(false);
//        loadingSpinner.setVisibility(View.GONE);
        postBinding.getRoot().setVisibility(View.VISIBLE);
        binding.commentsRv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("Burger","POST DETAILS DESTORYED");
    }

    @Override
    public Content getContent() {
        return post;
    }

    @Override
    public void onRefresh() {
        retrievePost();
    }
}