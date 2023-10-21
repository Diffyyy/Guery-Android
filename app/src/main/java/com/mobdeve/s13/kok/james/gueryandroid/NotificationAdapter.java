package com.mobdeve.s13.kok.james.gueryandroid;

import android.view.*;
import java.util.*;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.NotificationItemBinding;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewholder>{

    private ArrayList<Notification> notifications;

    public NotificationAdapter(ArrayList<Notification> notifications){
        this.notifications = notifications;
    }

    @Override
    public NotificationViewholder onCreateViewHolder(ViewGroup parent, int viewType){
        NotificationViewholder viewHolder = new NotificationViewholder(NotificationItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(NotificationViewholder holder, int position) {
        Notification data = notifications.get(position);
        holder.bind(data);
    }
    public int getItemCount() {
        return notifications.size();
    }

}
