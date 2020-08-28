package com.litchi.taobaodemo.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.model.bean.SearchRecommend;
import com.litchi.taobaodemo.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private static final String TAG = FlowLayout.class.getSimpleName();
    private List<String> strings = new ArrayList<>();
    private List<List<TextView>> lines = new ArrayList<>();

    private int horizontalMargin = SizeUtils.dip2px(getContext(), 5f);
    private int verticalMargin = SizeUtils.dip2px(getContext(), 5f);

    private OnItemClickListener onItemClickListener;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (strings.size() == 0) {
            return;
        }
        int left = horizontalMargin;
        int top = verticalMargin;
        int right = 0;
        int bottom = 0;
        for (List<TextView> line : lines) {
            bottom += getChildAt(0).getMeasuredHeight() + verticalMargin;
            for (TextView textView : line) {
                right += textView.getMeasuredWidth() + horizontalMargin;
                textView.layout(left, top, right, bottom);
                left = right + horizontalMargin;
            }
            right = 0;
            left = horizontalMargin;
            top += getChildAt(0).getMeasuredHeight() + verticalMargin;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (strings.size() == 0) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 0);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量孩子
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec)
                , MeasureSpec.AT_MOST);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec)
                , MeasureSpec.AT_MOST);
        measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

        //测量自己

        // 测量高度
        lines.clear();
        List<TextView> line = new ArrayList<>();
        lines.add(line);
        for (int i = 0; i < childCount; i++) {
            if (line.size()==0) {
                line.add((TextView) getChildAt(i));
            } else if(isAddToLineAllowed(line, getChildAt(i), MeasureSpec.getSize(widthMeasureSpec))) {
                line.add((TextView) getChildAt(i));
            } else {
                line = new ArrayList<>();
                line.add((TextView) getChildAt(i));
                lines.add(line);
            }
        }
        int childMeasureHeight = getChildAt(0).getMeasuredHeight();
        int size = lines.size();
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), childMeasureHeight * size + verticalMargin * (size + 1));
    }

    private boolean isAddToLineAllowed(List<TextView> line, View child, int parentMeasureWidth) {
        int childMeasuredWidth = child.getMeasuredWidth() + 2 * horizontalMargin;
        int totalWidth = 0;
        for (TextView textView : line) {
            totalWidth += (textView.getMeasuredWidth() + horizontalMargin);
        }
        return totalWidth + childMeasuredWidth <= parentMeasureWidth;
    }

    public void setDataByString(List<String> data){
        strings.clear();
        strings.addAll(data);
        addChildView();
    }

    public void setData(List<SearchRecommend.DataBean> data) {
        this.strings.clear();
        for (SearchRecommend.DataBean dataBean : data) {
            this.strings.add(dataBean.getKeyword());
        }
        addChildView();
    }

    private void addChildView() {
        this.removeAllViews();
        for (int i = 0; i < strings.size(); i++) {
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_layout, this, false);
            textView.setText(strings.get(i));
            final int position = i;
            textView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(strings.get(position));
                }
            });
            this.addView(textView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void clear() {
        this.strings.clear();
        this.removeAllViews();
    }

    public interface OnItemClickListener{
        void onItemClick(String keyword);
    }
}
