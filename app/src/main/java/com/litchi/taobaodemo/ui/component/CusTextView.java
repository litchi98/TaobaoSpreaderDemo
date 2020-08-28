package com.litchi.taobaodemo.ui.component;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class CusTextView extends AppCompatTextView {

    private OnMeasureListener onMeasureListener;

    public CusTextView(@NonNull Context context) {
        super(context);
    }

    public CusTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CusTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.onMeasureListener != null) {
            onMeasureListener.onMeasure(getMeasuredHeight());
        }
    }

    public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
        this.onMeasureListener = onMeasureListener;
    }

    public void removeOnMeasureListener() {
        this.onMeasureListener = null;
    }

    public interface OnMeasureListener{
        void onMeasure(int measureHeight);
    }

}
