package com.litchi.taobaodemo.ui.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.litchi.taobaodemo.R;

public class LoadingView extends View {
    private Bitmap bitmap;
    private Paint paint;
    private Rect srcRect;
    private Rect dstRect;
    private Runnable task;
    private int degrees = 0;
    private boolean isLoadedSuccess = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.loading);
        paint = new Paint();
        createTask();
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

    private void initRect() {
        srcRect = new Rect();
        srcRect.bottom = bitmap.getHeight();
        srcRect.right = bitmap.getWidth();
        dstRect = new Rect();
        dstRect.top = (getMeasuredHeight() - bitmap.getHeight()) / 2;
        dstRect.left = (getMeasuredWidth() - bitmap.getWidth()) / 2;
        dstRect.bottom = dstRect.top + bitmap.getHeight();
        dstRect.right = dstRect.left + bitmap.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
        initRect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(degrees, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint);
        canvas.restore();
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

    private void startLooper() {
        if (!isLoadedSuccess) {
            removeCallbacks(task);
            post(task);
        }
    }

    private void stopLooper() {
        removeCallbacks(task);
    }

    public void isLoaded(boolean isLoadedSuccess){
        this.isLoadedSuccess = isLoadedSuccess;
        this.degrees = 0;
        stopLooper();
    }
}
