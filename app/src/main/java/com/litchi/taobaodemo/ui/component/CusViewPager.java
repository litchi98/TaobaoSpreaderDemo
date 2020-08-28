package com.litchi.taobaodemo.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CusViewPager extends ViewPager {

    private static final String TAG = CusViewPager.class.getSimpleName();

    Runnable mTask = new Runnable() {


        @Override
        public void run() {
            int currentItem = CusViewPager.this.getCurrentItem();
            currentItem++;
            CusViewPager.this.setCurrentItem(currentItem);
            postDelayed(this, 3000);
        }
    };
    private float x;
    private float y;

    public CusViewPager(@NonNull Context context) {
        this(context, null);
    }

    public CusViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void stopLooper() {
        removeCallbacks(mTask);
    }

    public void startLooper() {
        stopLooper();
        postDelayed(mTask, 4000);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            startLooper();
        } else {
            stopLooper();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startLooper();
        } else if (visibility == GONE) {
            stopLooper();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.x = ev.getX();
                this.y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = ev.getX();
                float newY = ev.getY();
                float dx = Math.abs(newX - x);
                float dy = Math.abs(newY - y);
               if (dy > dx && dy > 50) {
                    requestDisallowInterceptTouchEvent(false);
                } else {
                   requestDisallowInterceptTouchEvent(true);
               }

        }
        return super.dispatchTouchEvent(ev);
    }
}
