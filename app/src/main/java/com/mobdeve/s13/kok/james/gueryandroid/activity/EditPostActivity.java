package com.mobdeve.s13.kok.james.gueryandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityEditPostBinding;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.CreatepostFragment;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

import java.util.Map;

public class EditPostActivity extends AppCompatActivity {
    private CreatepostFragment editFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditPostBinding binding = ActivityEditPostBinding.inflate(getLayoutInflater());
        Post post = getIntent().getParcelableExtra(CreatepostFragment.POST);
        editFragment = new CreatepostFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CreatepostFragment.POST, post);
        bundle.putBoolean(CreatepostFragment.IS_ADD, false );
        editFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.edit_fragment, editFragment).commit();
        setContentView(binding.getRoot());
    }

    public void finishEdit(){
        Intent intent = new Intent();
        intent.putExtra(PostDetailsActivity.POST_INDEX, getIntent().getIntExtra(PostDetailsActivity.POST_INDEX, -10));
        Log.d("BURGER", "FINISHED EDITING: "+editFragment.getArguments().getParcelable(CreatepostFragment.POST));
        Post newPost = editFragment.getArguments().getParcelable(CreatepostFragment.POST);
        intent.putExtra(CreatepostFragment.POST, newPost    );
        Log.d("BURGER", "POST INDEX: "+intent.getIntExtra(PostDetailsActivity.POST_INDEX, -10));
        setResult(RESULT_OK, intent);
        finish();


    }
}
