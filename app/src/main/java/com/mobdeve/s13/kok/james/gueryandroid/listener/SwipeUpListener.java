package com.mobdeve.s13.kok.james.gueryandroid.listener;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ScrollingView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Console;
import java.util.function.Consumer;

import javax.security.auth.callback.Callback;

public class SwipeUpListener implements View.OnTouchListener {

    private final GestureDetectorCompat gestureDetector;
    private final Consumer<Void>  callback;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return false;
    }


    public SwipeUpListener(Context context, final View view, Consumer<Void> callback) {
        this.callback = callback;
        gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // Check if it's a swipe-up gesture
                Log.d("BURGER", "e2 PRESSURE: "+e2.getPressure());

                if (velocityY >5) {
                    Log.d("BURGER", "SWIPED UP: "+velocityY);
                    callback.accept(null);
                }
                return false;
            }
        });
    }

}
