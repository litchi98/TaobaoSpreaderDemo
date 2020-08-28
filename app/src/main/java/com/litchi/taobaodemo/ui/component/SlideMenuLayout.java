package com.litchi.taobaodemo.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.utils.LogUtils;

public class SlideMenuLayout extends ViewGroup {

    private static final String TAG = SlideMenuLayout.class.getSimpleName();
    private View menuItem;
    private OnMenuItemClickListener onMenuItemClickListener;
    private View delete;
    private int firstChildLeft;
    private View firstChild;
    private int actionX;

    private Scroller scroller;
    private boolean isItemOpen = false;

    public SlideMenuLayout(Context context) {
        this(context, null);
    }

    public SlideMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setFocusable(true);
        scroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        firstChild = getChildAt(0);
        LogUtils.d(TAG, "firstChild ===>" + firstChild);
        //加载子view
        menuItem = LayoutInflater.from(getContext()).inflate(R.layout.item_slide_menu, this, false);
        this.addView(menuItem);
        delete = menuItem.findViewById(R.id.delete);
        setEvent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        //测量第一个孩子
        int firstChildHeightMeasureSpec;
        LayoutParams firstChildLayoutParams = firstChild.getLayoutParams();
        int firstChildHeight = firstChildLayoutParams.height;
        switch (firstChildHeight) {
            case LayoutParams.MATCH_PARENT:
                firstChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasureSize, MeasureSpec.EXACTLY);
                break;
            case LayoutParams.WRAP_CONTENT:
                firstChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasureSize, MeasureSpec.AT_MOST);
                break;
            default:
                firstChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(firstChildHeight, MeasureSpec.EXACTLY);
                break;
        }
        firstChild.measure(MeasureSpec.makeMeasureSpec(widthMeasureSize, MeasureSpec.EXACTLY), firstChildHeightMeasureSpec);
        int firstChildMeasuredHeight = firstChild.getMeasuredHeight();
        //测量第二个孩子
        menuItem.measure(MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthMeasureSpec) * 0.2), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(firstChildMeasuredHeight, MeasureSpec.EXACTLY));
        //测量自己
        setMeasuredDimension((int) (MeasureSpec.getSize(widthMeasureSpec) * 1.2),
                MeasureSpec.makeMeasureSpec(firstChildMeasuredHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int firstChildTop = 0;
        int firstChildRight = firstChildLeft + firstChild.getMeasuredWidth();
        int firstChildBottom = firstChild.getMeasuredHeight();
        firstChild.layout(firstChildLeft, firstChildTop, firstChildRight, firstChildBottom);
        menuItem.layout(firstChildRight, 0, firstChildRight + menuItem.getMeasuredWidth(), firstChildBottom);
    }

    private void setEvent() {
        delete.setOnClickListener(v -> {
            if (onMenuItemClickListener != null) {
                onMenuItemClickListener.onDeleteClick();
                closeQuick();
            }
        });
        firstChild.setOnClickListener(v -> {
            if (onMenuItemClickListener != null) {
                onMenuItemClickListener.onContentClick();
            }
        });
    }

    private void closeQuick() {
        scrollTo(0, 0);
        isItemOpen = false;
        invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isItemOpen && ev.getX() > firstChild.getMeasuredWidth() - delete.getMeasuredWidth()) {
            return false;
        } else return isItemOpen || ev.getAction() == MotionEvent.ACTION_MOVE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        touchEventBeta1(event);
        touchEventRelease(event);
        return false;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            scrollTo(currX, 0);
            invalidate();
        }
    }

    private void touchEventRelease(MotionEvent event) {
        int action = event.getAction();
        int dX = (int) (event.getX() - actionX);
        int scrollX = getScrollX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionX = (int) event.getX();
                if (isItemOpen) {
                    close();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                actionX = (int) event.getX();
                if (scrollX - dX <= 0) {
                    scrollTo(0, 0);
                } else if (scrollX - dX > menuItem.getMeasuredWidth()) {
                    scrollTo(menuItem.getMeasuredWidth(), 0);
                } else {
                    scrollBy(-dX, 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (scrollX < menuItem.getMeasuredWidth() / 2) {
//                    scrollTo(0, 0);
                    scroller.startScroll(scrollX, 0, -scrollX, 0, 500);
                    isItemOpen = false;
                } else {
//                    scrollTo(menuItem.getMeasuredWidth(), 0);
                    scroller.startScroll(scrollX, 0, menuItem.getMeasuredWidth() - scrollX, 0, 500);
                    isItemOpen = true;
                }
                invalidate();
                break;
            default:
                break;
        }
    }

    public void close() {
        scroller.startScroll(menuItem.getMeasuredWidth(), 0, -menuItem.getMeasuredWidth(), 0, 500);
        isItemOpen = false;
        invalidate();
    }

    private void touchEventBeta1(MotionEvent event) {
        int action = event.getAction();
        int dx = (int) (event.getX() - actionX);
        actionX = (int) event.getX();
        int measuredWidth = menuItem.getMeasuredWidth();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                firstChildLeft += dx;
                if (-firstChildLeft < measuredWidth / 2) {
                    firstChildLeft = 0;
                } else {
                    firstChildLeft = -measuredWidth;
                }
                requestLayout();
                break;
            case MotionEvent.ACTION_MOVE:
                firstChildLeft += dx;
                if (firstChildLeft < -measuredWidth) {
                    firstChildLeft = -measuredWidth;
                } else if (firstChildLeft >= 0) {
                    firstChildLeft = 0;
                }
                if (dx > 0 && -firstChildLeft < measuredWidth - 40) {
                    firstChildLeft = 0;
                }
                requestLayout();
                break;
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public interface OnMenuItemClickListener {
        void onDeleteClick();

        void onContentClick();
    }
}
