package com.litchi.taobaodemo.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.CusNestedScrollView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.base.BaseFragment;
import com.litchi.taobaodemo.model.bean.CategoryDetail;
import com.litchi.taobaodemo.presenter.IGoodsDetailPresenter;
import com.litchi.taobaodemo.presenter.IHomeViewPagerPresenter;
import com.litchi.taobaodemo.presenter.impl.GoodsDetailPresenterImpl;
import com.litchi.taobaodemo.presenter.impl.HomeViewPagerPresenterImpl;
import com.litchi.taobaodemo.ui.activity.GoodsDetailActivity;
import com.litchi.taobaodemo.ui.adapter.GoodsRecyclerAdapter;
import com.litchi.taobaodemo.ui.component.CarouselView;
import com.litchi.taobaodemo.utils.LogUtils;
import com.litchi.taobaodemo.utils.SizeUtils;
import com.litchi.taobaodemo.view.IHomeViewPagerView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class HomeViewPagerFragment extends BaseFragment implements IHomeViewPagerView {

    private static final String TAG = HomeViewPagerFragment.class.getSimpleName();
    private int categoryId;
    private IHomeViewPagerPresenter homeViewPagerPresenter;

    @BindView(R.id.home_View_pager_nested_scroll_view)
    public CusNestedScrollView nestedScrollView;

    @BindView(R.id.home_view_pager_recycler_view)
    public RecyclerView goodsRecyclerView;

    @BindView(R.id.home_view_pager_carousel_view)
    public CarouselView goodsCarouselView;

    @BindView(R.id.refresh_layout)
    public TwinklingRefreshLayout refreshLayout;

    @BindView(R.id.home_view_pager_parent)
    public LinearLayout parentLayout;

    private GoodsRecyclerAdapter goodsRecyclerAdapter;

    public HomeViewPagerFragment(int categoryId) {
        super();
        // 通过构造方法保存当前页面的categoryId
        this.categoryId = categoryId;
    }

    @Override
    protected int getNormalLayoutResourceId() {
        return R.layout.fragment_home_view_pager;
    }

    @Override
    protected void initView(View view) {
        // 初始化RecyclerView
        goodsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        goodsRecyclerAdapter = new GoodsRecyclerAdapter();
        goodsRecyclerView.setAdapter(goodsRecyclerAdapter);
        // 给RecyclerView的item设置间距
        goodsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.left = SizeUtils.dip2px(Objects.requireNonNull(getContext()), 5f);
                outRect.right = SizeUtils.dip2px(getContext(), 5f);
                outRect.top = SizeUtils.dip2px(getContext(), 5f);
                outRect.bottom = SizeUtils.dip2px(getContext(), 5f);
            }
        });
        // 禁用刷新
        refreshLayout.setEnableRefresh(false);
    }

    @Override
    protected void initEvent(View view) {
        // 设置loadMore监听
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(TAG, "category detail refresh...");
                homeViewPagerPresenter.loadMoreCategoryDetail(categoryId);
            }
        });

        // 给RecyclerView动态设置高度，解决嵌套使用item不复用问题
        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获取父组件高度
                if (parentLayout == null) {
                    return;
                }
                int parentLayoutMeasuredHeight = parentLayout.getMeasuredHeight();
                // 第一次布局时高度为0，后续为正常高度
                 ViewGroup.LayoutParams layoutParams = goodsRecyclerView.getLayoutParams();
                layoutParams.height = parentLayoutMeasuredHeight;
                goodsRecyclerView.setLayoutParams(layoutParams);
                // 设置完值后，解除监听避免重复调用该方法
                if (parentLayoutMeasuredHeight != 0) {
                    parentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        goodsCarouselView.setOnCarouselViewMeasureListener(measureHeight -> {
            nestedScrollView.setHeaderHeight(measureHeight);
            goodsCarouselView.removeOnCarouselViewMeasureListener();
        });

        goodsRecyclerAdapter.setOnItemClickedListener(dataBean -> {
            LogUtils.d(TAG, "goods item clicked, titleTV ===> " + dataBean.getTitle());
            getCouponCodeClick(dataBean);
            Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
            startActivity(intent);
        });

        goodsCarouselView.setOnItemClickedListener(dataBean -> {
            LogUtils.d(TAG, "carousel item clicked, titleTV ===> " + dataBean.getTitle());
            getCouponCodeClick(dataBean);
            Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
            startActivity(intent);
        });
    }

    private void getCouponCodeClick(CategoryDetail.DataBean dataBean) {
        IGoodsDetailPresenter goodsDetailPresenter = GoodsDetailPresenterImpl.getInstance();
        goodsDetailPresenter.getCouponCode(dataBean);
    }

    @Override
    protected void initPresenter() {
        homeViewPagerPresenter = new HomeViewPagerPresenterImpl();
        homeViewPagerPresenter.registerView(this);
    }

    @Override
    protected void fetchData() {
        homeViewPagerPresenter.getCategoryDetail(categoryId);
    }

    @Override
    protected void doNetErrRetry() {
        fetchData();
    }

    @Override
    protected void release() {
        homeViewPagerPresenter.unregisterView(this);
    }

    @Override
    public void onCategoryDetailLoaded(List<CategoryDetail.DataBean> details) {
        setCurrState(STATE_NORMAL);
        goodsRecyclerAdapter.setDate(details);
    }

    @Override
    public void onCarouselDataLoaded(List<CategoryDetail.DataBean> details) {
        if (goodsCarouselView != null) {
            goodsCarouselView.setData(details);
        }
    }

    @Override
    public void onLoading() {
        setCurrState(STATE_LOADING);
    }

    @Override
    public void onNetErr() {
        setCurrState(STATE_NET_ERR);
    }

    @Override
    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public void onLoadMoreDone(List<CategoryDetail.DataBean> details) {
        goodsRecyclerAdapter.updateData(details);
        refreshLayout.finishLoadmore();
    }

    @Override
    public void loadMoreFailed() {
        Toast.makeText(getContext(), "请求超时，请检查网络或重新加载", Toast.LENGTH_SHORT).show();
        refreshLayout.finishLoadmore();
    }
}
