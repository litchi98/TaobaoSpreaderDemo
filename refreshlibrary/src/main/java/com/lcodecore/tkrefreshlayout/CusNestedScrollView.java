package com.lcodecore.tkrefreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class CusNestedScrollView extends NestedScrollView {

    private static final String TAG = CusNestedScrollView.class.getSimpleName();
    private int verticalScrollDistance;
    private int headerHeight;
    private RecyclerView childRecycle;
    private OnVerticalScrollListener onVerticalScrollListener;

    public CusNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public CusNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CusNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof RecyclerView) {
            this.childRecycle = (RecyclerView) target;
        }
        if (verticalScrollDistance < headerHeight) {
            scrollBy(dx, dy);
            consumed[0] = dx;
            consumed[1] = dy;
            if (onVerticalScrollListener != null) {
                onVerticalScrollListener.onVerticalScroll(false);
            }
        } else if (onVerticalScrollListener != null) {
            onVerticalScrollListener.onVerticalScroll(true);
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.verticalScrollDistance = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public int getVerticalScrollDistance() {
        return verticalScrollDistance;
    }

    public boolean isViewToBottom() {
        if (childRecycle != null) {
            boolean isBottom = childRecycle.canScrollVertically(1);
            return !isBottom;
        }
        return false;
    }

    public void setOnVerticalScroll(OnVerticalScrollListener onVerticalScrollListener) {
        this.onVerticalScrollListener = onVerticalScrollListener;
    }

    public interface OnVerticalScrollListener {
        void onVerticalScroll(boolean isScrollBottom);
    }
}
