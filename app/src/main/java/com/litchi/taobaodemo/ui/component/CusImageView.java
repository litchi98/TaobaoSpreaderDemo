package com.litchi.taobaodemo.ui.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.litchi.taobaodemo.R;

public class CusImageView extends AppCompatImageView {

    private int degrees = 0;
    private Runnable task;
    private boolean isLoadedSuccess = false;

    public CusImageView(@NonNull Context context) {
        this(context, null);
    }

    public CusImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
        setScaleType(ScaleType.CENTER);
        createTask();
        startLooper();
    }

    private void createTask() {
        task = () -> {
            degrees += 8;
            if (degrees >= 360) {
                degrees = degrees % 360;
            }
            invalidate();
            postDelayed(task, 42);
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(degrees, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        super.onDraw(canvas);
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

    private void stopLooper() {
        removeCallbacks(task);
    }

    private void startLooper() {
        if (!isLoadedSuccess) {
            removeCallbacks(task);
            post(task);
        }
    }

    public void isLoaded(boolean isLoadedSuccess){
        this.isLoadedSuccess = isLoadedSuccess;
        this.degrees = 0;
        stopLooper();
    }
}
