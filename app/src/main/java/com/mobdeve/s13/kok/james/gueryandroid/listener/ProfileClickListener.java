package com.mobdeve.s13.kok.james.gueryandroid.listener;

import android.content.Intent;
import android.view.View;

import com.mobdeve.s13.kok.james.gueryandroid.activity.PostDetailsActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.ViewProfileActivity;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.ContentHolder;

public class ProfileClickListener implements View.OnClickListener {

    private ContentHolder contentHolder;
    public ProfileClickListener(ContentHolder contentHolder){
        this.contentHolder = contentHolder;
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ViewProfileActivity.class);
        intent.putExtra(ViewProfileActivity.PROFILE_ID, contentHolder.getContent().getProfile().getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        v.getContext().startActivity(intent);
    }
}
