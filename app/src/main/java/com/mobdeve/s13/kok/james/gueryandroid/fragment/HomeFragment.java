package com.mobdeve.s13.kok.james.gueryandroid.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.activity.HomeActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.PostDetailsActivity;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.PostItemAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.FragmentHomeBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.listener.SwipeUpListener;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.PostItemViewModel;

import java.util.ArrayList;
import java.util.function.Consumer;


public class HomeFragment extends Fragment {

    private PostItemAdapter adapter;

    private FragmentHomeBinding binding;
    private View loading;
    private String query;
    private HomeActivity homeActivity;

    private PostItemViewModel postModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postModel = new ViewModelProvider(getActivity()).get(PostItemViewModel.class);
        if(!postModel.getFragmentData().isInitialized()){
            postModel.setFragmentData(new ArrayList<>());
            Log.d("BURGER", "HEY NOT INITAILZIED");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Log.d("BURGER", "QUERY SAVED: "+query    );
        binding.searchView.setQuery(query, false);
        Log.d("BURGER", "SAVED INSTANCE STATE: "+savedInstanceState);
        ActivityResultLauncher<Intent> myLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<>() {
            @Override
            public void onActivityResult(ActivityResult o) {


                if(o.getResultCode()==RESULT_OK){
                    Intent intent = o.getData();
                    int index = intent.getIntExtra(PostDetailsActivity.POST_INDEX, -10);
                    Log.d("BURGER", "HEYYY: "+index);
                    Post post = intent.getParcelableExtra("post");

                    Log.d("BURGER", "GOT POST: "+post);
                    if(index!=-10){
                        getData().set(index, post);
                        adapter.notifyItemChanged(index );
                    }
                    Log.d("BURGER", "POST INDEX: "+intent.getIntExtra(PostDetailsActivity.POST_INDEX, -10));
                }else if(o.getResultCode()==RESULT_CANCELED){
                    Log.d("BURGER", "POST NOT CHANGED");
                }
            }
        });
        adapter = new PostItemAdapter(getData());
        adapter.setLauncher(myLauncher);

        binding.postsRv.setAdapter(adapter);
        binding.postsRv.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.postsRv.setOnTouchListener(new SwipeUpListener(container.getContext(), binding.postsRv, new Consumer<Void>()  {
            @Override
            public void accept(Void unused) {
                if(!binding.postsRv.canScrollVertically(-1)){
                    search();
                }
            }
        }));
        loading = binding.loading.getRoot();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d("BURGER", "SEARCH CLICKED");
                search();
                homeActivity.hideFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("BURGER","QUERY CHANGED?: "+newText);
                query = newText;
                if(query.isEmpty()){
                    Log.d("BURGER", "SEARCHING AGAIN? ");
                    search();
                }
                return true;
            }
        });


        if(getData().isEmpty())search();
//        if(posts.isEmpty()){
//            loadStart();
//        }

        return binding.getRoot();

    }

    public HomeFragment(){

    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Log.d("BURGER", "SAVED INSTANCE STATE CALLED");
//        outState.putParcelableArrayList("posts", posts);
//    }

    public void addPostLast(Post post){
        getData().add(post);
        adapter.notifyItemInserted(getData().size());
    }
    public void addPostFirst(Post post){
        getData().add(0, post   );
        adapter.notifyItemInserted(0);
        binding.postsRv.smoothScrollToPosition(0);
    }

    private ArrayList<Post> getData(){
        return postModel.getFragmentData().getValue();
    }
    public void search(){
        getData().clear();
        adapter.notifyDataSetChanged();

        boolean added = false;

        loadStart();
        FirestoreHelper.getInstance().searchPosts(query, new Consumer<Post>() {

            @Override
            public void accept(Post post) {
                if (!added) {
                    loadStop();
                }
                addPostLast(post);
            }
        }, new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                loadStop();
            }
        });
    }
    public void loadStart(){
        loading.setVisibility(View.VISIBLE);
//        int viewheight = loading.getHeight();

        ObjectAnimator animator = ObjectAnimator.ofFloat(loading,"translationY", -20f, 20f);
        animator.setDuration(100);
        animator.start();
    }


    public void loadStop(){
        loading.setVisibility(View.GONE);
    }

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


}