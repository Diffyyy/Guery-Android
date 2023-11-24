package com.mobdeve.s13.kok.james.gueryandroid.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.mobdeve.s13.kok.james.gueryandroid.activity.HomeActivity;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.PostItemAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.FragmentHomeBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.ResultLaunchers;
import com.mobdeve.s13.kok.james.gueryandroid.listener.SwipeUpListener;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.PostItemViewModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private PostItemAdapter adapter;

    private FragmentHomeBinding binding;
//    private View loadingSpinner;
    private String query;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private PostItemViewModel postModel;
    private SwipeRefreshLayout refreshLayout;
    private boolean isLoading = false;
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
        binding.searchView.setQuery(query, false);
        refreshLayout = binding.refreshLayout;
        refreshLayout.setDistanceToTriggerSync(200);
        refreshLayout.setSlingshotDistance(100);
        Log.d("BURGER","CREATING VIEW");
        adapter = new PostItemAdapter(getData());
        adapter.setLauncher(ResultLaunchers.postClicked(this, adapter));

        binding.postsRv.setAdapter(adapter);
        binding.postsRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        refreshLayout.setOnRefreshListener(this);
//        loadingSpinner = binding.loading.getRoot();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d("BURGER", "SEARCH CLICKED");
                search();
                ((HomeActivity)getActivity()).hideFocus();
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
        if(getData().isEmpty())initializeData();
        return binding.getRoot();

    }

    public HomeFragment(){

    }

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

    public void initializeData(){
        refreshLayout.setRefreshing(true);
        getData().clear();
        if(query==null || query.isEmpty()){
            adapter.setPosts(getData());
            adapter.notifyDataSetChanged();
            FirestoreHelper.getInstance().retrievePosts( new Consumer<Post>() {
                boolean added = false;
                @Override
                public void accept(Post post) {
                    Log.d("BURGER", "GOT POST: "+post);
                    if (!added) {
                        refreshLayout.setRefreshing(false);
                        added = true;
                    }
                    addPostLast(post);
                }
            }, new Consumer<Integer>() {
                @Override
                public void accept(Integer unused) {
                    refreshLayout.setRefreshing(false);
                }
            });
        }else{
            adapter.setPosts(new ArrayList<>());
            adapter.notifyDataSetChanged();
            final Integer[] size = new Integer[1];
            FirestoreHelper.getInstance().retrievePosts(new Consumer<Post>() {
                @Override
                public void accept(Post post) {
                    getData().add(post);
                    if(getData().size()==size[0])search();

                }
            }, new Consumer<Integer>() {
                @Override
                public void accept(Integer unused) {
                    Log.d("BURGER", "SEARCHING NOW");
                    size[0] = unused;
                }
            });
        }


    }
    public void search(){
        final boolean[] added = {false};
        if(query!=null && !query.isEmpty()) {
            adapter.setPosts(new ArrayList<>());
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(true);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    for(Post post: getData()) {
                        if (post.getTitle().toLowerCase().contains(query.toLowerCase())
                                || post.getBody().toLowerCase().contains(query.toLowerCase())
                                || post.getGame().toLowerCase().contains(query.toLowerCase())) {
                            if(!added[0]) {
                                refreshLayout.setRefreshing(false);
                                added[0] = true;
                            }

                            added[0] = true;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.getPosts().add(post);
                                    adapter.notifyItemInserted(adapter.getPosts().size()-1);
                                }
                            });
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            });
        }else {
            adapter.setPosts(getData());
            adapter.notifyDataSetChanged();
        }


//        });
    }
//    public void loadStart(){
//        if(isLoading) return;
//        isLoading = true;
//
////        loadingSpinner.setVisibility(View.VISIBLE);
////        int viewheight = loading.getHeight();
//
//        ObjectAnimator animator = ObjectAnimator.ofFloat(loadingSpinner,"translationY", -20f, 20f);
//        animator.setDuration(100);
//        animator.start();
//    }


//    public void loadStop(){
//        if(!isLoading) return;
//        isLoading = false;
//        loadingSpinner.setVisibility(View.GONE);
//    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("BURGER", "FRAGMENT HOME STARTED");
    }

    @Override
    public void onRefresh() {

//        if(!binding.postsRv.canScrollVertically(-1)){
            initializeData();

    }
}