package com.mobdeve.s13.kok.james.gueryandroid.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ScrollView;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.CommentAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityPostDetailsBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DialogHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.listener.SwipeUpListener;
import com.mobdeve.s13.kok.james.gueryandroid.listener.VoteListener;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Vote;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostItemHolder;

import java.time.LocalDateTime;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PostDetailsActivity extends AppCompatActivity {
    public final static String POST_KEY = "Post";
    public static final String POST_INDEX  = "postIndex";
    private View loading;
    private View postView;
    ActivityPostDetailsBinding binding;
    private PostItemBinding postBinding;
    private String postId;
    private ScrollView scrollView;
    private Post post;
    private Vote prevVote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding   = ActivityPostDetailsBinding.inflate(getLayoutInflater());

        Intent intent = getIntent();
        postId = intent.getStringExtra(POST_KEY);
//        Log.println(Log.ASSERT, "BURGER", ""+post.createdAt );
        postBinding = binding.postBinding;
        postView = postBinding.getRoot();
        loading = binding.loading.getRoot();
        scrollView = binding.scrollView;

        scrollView.setOnTouchListener(new SwipeUpListener(this, scrollView, new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                if(!scrollView.canScrollVertically(-1)) retrievePost();
            }
        }));

        setContentView(binding.getRoot());


        retrievePost();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(post.isVoting()) return;

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
                bind(postBinding, post);
                PostDetailsActivity.this.post = post;

                prevVote = post.getUserVote();

                postBinding.postEngagementBar.postUpvoteBtn.setOnClickListener(new VoteListener(LoginActivity.p, post,postBinding.postEngagementBar.postDownvoteBtn, Vote.UP,  postBinding.postEngagementBar.upvoteTv ));
                postBinding.postEngagementBar.postDownvoteBtn.setOnClickListener(new VoteListener(LoginActivity.p, post,postBinding.postEngagementBar.postUpvoteBtn, Vote.DOWN,  postBinding.postEngagementBar.upvoteTv ));
                CommentAdapter adapter = new CommentAdapter();
                Log.d("BURGER", "ADAPTER: "+adapter);
                adapter.setReplies(post.getComments());
                adapter.retrieveComments();


                binding.commentsRv.setLayoutManager(new LinearLayoutManager(PostDetailsActivity.this));
                binding.commentsRv.setAdapter(adapter);

                binding.backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       getOnBackPressedDispatcher().onBackPressed();
//                        finish();
                    }
                });
                postBinding.postEngagementBar.postReplyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DialogHelper.getCommentDialog(PostDetailsActivity.this, post.getProfile().getUsername(), new BiConsumer<DialogInterface, String>() {

                            @Override
                            public void accept(DialogInterface dialogInterface, String s) {
                                if(!s.isBlank()){
                                    Comment comment = new Comment(LoginActivity.p, LocalDateTime.now(), s);
                                    comment.setToPost(1);
                                    FirestoreHelper.getInstance().addComment(postId, comment, new Consumer<String>() {
                                        @Override
                                        public void accept(String s) {
                                            adapter.addComment(comment);
                                        }
                                    });
                                }else dialogInterface.dismiss();

                            }
                        }).show();
                    }
                });



            }
        });
    }

    private void bind(PostItemBinding binding, Post post){
        binding.bodyTv.setText(post.getBody());
        binding.pfpIv.setImageResource(post.getProfile().getPfp());
        binding.communityTv.setText(post.getGame());
        binding.timeTv.setText(DateHelper.formatDate(post.getCreatedAt()));
        binding.titleTv.setText(post.getTitle());
        binding.postEngagementBar.upvoteTv.setText(String.valueOf(post.getUpvotes()));
        binding.usernameTv.setText(post.getProfile().getUsername());

        PostItemHolder.updateVoteBtns(post, binding.postEngagementBar.postUpvoteBtn, binding.postEngagementBar.postDownvoteBtn);


    }

    public void loadStart(){
        postView.setVisibility(View.INVISIBLE);
        binding.commentsRv.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
//        int viewheight = loading.getHeight();

        ObjectAnimator animator = ObjectAnimator.ofFloat(loading,"translationY", -20f, 20f);
        animator.setDuration(100);
        animator.start();
    }
    public void loadStop(){
        loading.setVisibility(View.GONE);
        postView.setVisibility(View.VISIBLE);
        binding.commentsRv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("Burger","POST DETAILS DESTORYED");
    }


}