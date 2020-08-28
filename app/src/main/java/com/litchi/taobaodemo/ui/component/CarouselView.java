package com.litchi.taobaodemo.ui.component;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.model.bean.CategoryDetail;

import java.util.ArrayList;
import java.util.List;


public class CarouselView extends RelativeLayout {

    private static final String TAG = "CarouselView";

    private CusViewPager viewPager;
    private MyAdapter myAdapter;
    private ViewGroup pointContainer;
    private int realPosition;
    private List<CategoryDetail.DataBean> categoryDetails = new ArrayList<>();
    private OnCarouselViewMeasureListener onCarouselViewMeasureListener;
    private OnItemClickedListener onItemClickedListener;

    public CarouselView(Context context) {
        this(context, null);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initEvent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (onCarouselViewMeasureListener != null) {
            int measuredHeight = getMeasuredHeight();
            MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
            int bottomMargin = layoutParams.bottomMargin;
            onCarouselViewMeasureListener.onCarouselViewMeasure(measuredHeight + bottomMargin);
        }
    }

    private void initEvent() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (categoryDetails.size() == 0) {
                    return;
                }
                realPosition = position % categoryDetails.size();
                for (int i = 0; i < categoryDetails.size(); i++) {
                    if (i == realPosition) {
                        pointContainer.getChildAt(i).setBackgroundColor(Color.parseColor("#ff0000"));
                    } else {
                        pointContainer.getChildAt(i).setBackgroundColor(Color.parseColor("#4D000000"));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnTouchListener(new OnTouchListener() {
            private float x;
            private float y;
            private long downTime;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        viewPager.stopLooper();
                        downTime = System.currentTimeMillis();
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (onItemClickedListener != null && System.currentTimeMillis() - downTime < 500
                                && Math.abs(x - event.getX()) < 5 && Math.abs(y - event.getY()) < 5){
                            onItemClickedListener.onItemClicked(categoryDetails.get(realPosition));
                        }
                        viewPager.startLooper();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_carousel, this, true);
        viewPager = view.findViewById(R.id.view_pager);
        pointContainer = view.findViewById(R.id.point_container);
        viewPager.setOffscreenPageLimit(2);
        myAdapter = new MyAdapter();
        viewPager.setAdapter(myAdapter);
    }

    private void setPoint() {
        pointContainer.removeAllViews();
        for (int i = 0; i < categoryDetails.size(); i++) {
            View point = new View(getContext());
            if (i == 0) {
                point.setBackgroundColor(Color.parseColor("#ff0000"));
            } else {
                point.setBackgroundColor(Color.parseColor("#4D000000"));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getContext().getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getContext().getResources().getDisplayMetrics()));
            point.setLayoutParams(layoutParams);
            pointContainer.addView(point);
        }
    }

    public void setData(List<CategoryDetail.DataBean> categoryDetails) {
        this.categoryDetails.clear();
        this.categoryDetails.addAll(categoryDetails);
        setPoint();
        myAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 + 2);
    }

    public interface OnItemClickedListener {
        void onItemClicked(CategoryDetail.DataBean dataBean);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public interface OnCarouselViewMeasureListener {
        void onCarouselViewMeasure(int measureHeight);
    }

    public void setOnCarouselViewMeasureListener(OnCarouselViewMeasureListener onCarouselViewMeasureListener) {
        this.onCarouselViewMeasureListener = onCarouselViewMeasureListener;
    }

    public void removeOnCarouselViewMeasureListener() {
        this.onCarouselViewMeasureListener = null;
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_carousel, container, false);
            ImageView imageView = view.findViewById(R.id.cover);
            TextView title = view.findViewById(R.id.title);
            int realPosition = position % categoryDetails.size();
            CategoryDetail.DataBean categoryDetail = categoryDetails.get(realPosition);
            String imageUrl = new StringBuilder().append("https:").append(categoryDetail.getPict_url()).append("_400x400").toString();
            Glide.with(imageView).load(imageUrl).into(imageView);
            String description = categoryDetail.getItem_description();
            if (TextUtils.isEmpty(description)) {
                title.setBackgroundResource(R.color.titleNoneBg);
            } else {
                title.setText(description.replace("，", "\u0020"));
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
