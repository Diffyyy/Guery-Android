package com.mobdeve.s13.kok.james.gueryandroid.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.PostDetailsActivity;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

import java.time.LocalDateTime;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DialogHelper {

    public static AlertDialog.Builder deleteDialog(Context context, int index, Post post, BiConsumer<Integer, Post> callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete post");
        builder.setMessage("Are you sure you want to delete this post?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.accept(index, post);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    public static AlertDialog.Builder getNotLoggedInDialog(Context context, String message, Consumer<Void> cancel){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Not logged in");
        builder.setMessage(message  );
        builder.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent  = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(cancel!=null) cancel.accept(null);
            }
        });
        return builder;
    }

    public static AlertDialog.Builder getCommentDialog(Context context, String username, BiConsumer<DialogInterface, String> callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
        builder.setTitle("Reply to: " + username);

        final EditText input = new EditText(context);
        input.setSingleLine(false);  //add this
        input.setLines(5);
        input.setMaxLines(7);
        input.setGravity(Gravity.LEFT | Gravity.TOP);
        input.setHorizontalScrollBarEnabled(false); //this
//                        input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setHint("Write your reply here...");

        input.setTextColor(Color.WHITE);
        input.setHintTextColor(Color.WHITE);

        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reply = input.getText().toString();
                callback.accept(dialog, reply);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder;
    }
}
