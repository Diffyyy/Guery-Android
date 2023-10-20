package com.mobdeve.s13.kok.james.gueryandroid;

import android.view.*;
import java.util.*;
import androidx.recyclerview.widget.RecyclerView;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewholder>{

    private ArrayList<Notification> notifications;

    public NotificationAdapter(ArrayList<Notification> notifications){
        this.notifications = notifications;
    }

    @Override
    public NotificationViewholder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications, parent, false);
        return new NotificationViewholder(view);
    }


    @Override
    public void onBindViewHolder(NotificationViewholder holder, int position) {
        Notification data = notifications.get(position);
        holder.getImage().setImageResource(data.getImageId());
        holder.getUsername().setText("@" + data.getUser());
        holder.getContent().setText(data.getContent() );
        holder.getAction().setText(data.getAction());
        holder.getDate().setText(data.getReceivedOn().toStringNoYear());
    }
    public int getItemCount() {
        return notifications.size();
    }

}
