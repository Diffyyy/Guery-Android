package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Notification;

public class NotificationViewholder extends RecyclerView.ViewHolder{
    private ImageView image;
    private TextView username;
    private TextView content;
    private TextView date;
    private TextView action;
    private ImageView isUnread;

    public NotificationViewholder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.iv_notification_item_pfp);
        Log.println(Log.ASSERT, "BURGER", String.valueOf(image==null));
        username = itemView.findViewById(R.id.tv_notification_item_user);
        content = itemView.findViewById(R.id.tv_notification_item_preview);
        action = itemView.findViewById(R.id.tv_notification_item_action);
        date = itemView.findViewById(R.id.tv_notification_item_time);
        isUnread = itemView.findViewById(R.id.iv_notification_item_unread);
    }
    public void bind(Notification notification){
        image.setImageResource(notification.getImageId());
        username.setText(notification.getUser());
        content.setText(notification.getContent());
        action.setText(notification.getAction());
        date.setText(DateHelper.formatDate(notification.getReceivedOn()));

    }

    public TextView getDate() {
        return date;
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getUsername() {
        return username;
    }

    public ImageView getIsUnread() {
        return isUnread;
    }

    public TextView getAction() {
        return action;
    }

    public TextView getContent() {
        return content;
    }

}
