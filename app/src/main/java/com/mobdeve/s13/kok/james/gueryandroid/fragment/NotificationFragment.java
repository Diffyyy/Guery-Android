package com.mobdeve.s13.kok.james.gueryandroid.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobdeve.s13.kok.james.gueryandroid.adapter.NotificationAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.FragmentNotificationBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.NotificationsGenerator;
import com.mobdeve.s13.kok.james.gueryandroid.model.Notification;

import java.util.ArrayList;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link NotificationFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class NotificationFragment extends Fragment {
    private ArrayList<Notification> data = NotificationsGenerator.generateNotifications();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentNotificationBinding binding = FragmentNotificationBinding.inflate(inflater, container, false);
        NotificationAdapter adapter = new NotificationAdapter(data) ;
        binding.rvNotificationsList.setAdapter(adapter);
        binding.rvNotificationsList.setLayoutManager(new LinearLayoutManager(requireContext()));

        return binding.getRoot();

    }
}