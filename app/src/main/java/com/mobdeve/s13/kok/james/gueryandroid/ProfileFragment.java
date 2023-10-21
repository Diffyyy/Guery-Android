package com.mobdeve.s13.kok.james.gueryandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.FragmentProfileBinding;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ProfileFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ProfileFragment extends Fragment {

;
    private HomeActivity activity;
    public ProfileFragment(HomeActivity activity) {
        this.activity = activity;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentProfileBinding binding =  FragmentProfileBinding.inflate(inflater, container, false);
        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return binding.getRoot();
    }
}