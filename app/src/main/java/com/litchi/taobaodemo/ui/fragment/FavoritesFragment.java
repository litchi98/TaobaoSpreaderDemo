package com.litchi.taobaodemo.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.lcodecore.tkrefreshlayout.CusNestedScrollView;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.base.BaseFragment;
import com.litchi.taobaodemo.model.bean.GoodsDetailInfo;
import com.litchi.taobaodemo.presenter.IFavoritesPresenter;
import com.litchi.taobaodemo.presenter.impl.FavoritesPresenterImpl;
import com.litchi.taobaodemo.presenter.impl.GoodsDetailPresenterImpl;
import com.litchi.taobaodemo.ui.activity.GoodsDetailActivity;
import com.litchi.taobaodemo.ui.activity.MainActivity;
import com.litchi.taobaodemo.ui.activity.SearchActivity;
import com.litchi.taobaodemo.ui.adapter.FavoritesAdapter;
import com.litchi.taobaodemo.ui.component.CusTextView;
import com.litchi.taobaodemo.utils.LogUtils;
import com.litchi.taobaodemo.utils.SizeUtils;
import com.litchi.taobaodemo.view.IFavoritesView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

import static com.litchi.taobaodemo.ui.activity.SearchActivity.KEY_SEARCH_INTENT;

public class FavoritesFragment extends BaseFragment implements IFavoritesView {

    private static final String TAG = FavoritesFragment.class.getSimpleName();
    @BindView(R.id.favorites_tab_layout)
    public TabLayout tabLayout;

    @BindView(R.id.favorites_recycler)
    public RecyclerView favoritesRecycler;

    @BindView(R.id.favorites_parent)
    public View parentView;

    @BindView(R.id.favorites_no_data)
    public View noDataView;

    @BindView(R.id.favorites_go_shopping)
    public View goShopping;

    @BindView(R.id.favorites_nested_scroll)
    public CusNestedScrollView nestedScrollView;

    @BindView(R.id.favorites_header)
    public CusTextView header;

    @BindView(R.id.favorites_num)
    public TextView favoritesNum;

    private FavoritesAdapter favoritesAdapter;
    private IFavoritesPresenter favoritesPresenter;

    @Override
    protected int getNormalLayoutResourceId() {
        return R.layout.fragment_favorites;
    }

    @Override
    protected void initView(View view) {
        favoritesAdapter = new FavoritesAdapter();
        favoritesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritesRecycler.setAdapter(favoritesAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("默认").setTag(1));
        tabLayout.addTab(tabLayout.newTab().setText("升序").setTag(2));
        tabLayout.addTab(tabLayout.newTab().setText("降序").setTag(3));
        setCurrState(STATE_NORMAL);
        nestedScrollView.setHeaderHeight(header.getMeasuredHeight());
    }

    @Override
    protected void initEvent(View view) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Integer tag = (Integer) tab.getTag();
                switch (tag){
                    case 1:
                        favoritesAdapter.normalSort();
                        favoritesRecycler.scrollToPosition(0);
                        break;
                    case 2:
                        favoritesAdapter.ascSort();
                        favoritesRecycler.scrollToPosition(0);
                        break;
                    case 3:
                        favoritesAdapter.descSort();
                        favoritesRecycler.scrollToPosition(0);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 获取父组件高度
                if (parentView == null) {
                    return;
                }
                int parentLayoutMeasuredHeight = parentView.getMeasuredHeight();
                // 第一次布局时高度为0，后续为正常高度
                ViewGroup.LayoutParams layoutParams = favoritesRecycler.getLayoutParams();
                layoutParams.height = parentLayoutMeasuredHeight - SizeUtils.dip2px(Objects.requireNonNull(getContext()), 70);
                favoritesRecycler.setLayoutParams(layoutParams);
                // 设置完值后，解除监听避免重复调用该方法
                if (parentLayoutMeasuredHeight != 0) {
                    parentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        goShopping.setOnClickListener(v -> ((MainActivity) Objects.requireNonNull(getActivity())).switchToHomeFragment());
        favoritesAdapter.setOnClickListener(new FavoritesAdapter.OnClickListener() {
            @Override
            public void onFindSimilarClicked(String title) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra(KEY_SEARCH_INTENT, title);
                startActivity(intent);
            }

            @Override
            public void onItemClicked(GoodsDetailInfo goodsDetailInfo) {
                GoodsDetailPresenterImpl.getInstance().getCouponCode(goodsDetailInfo);
                Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onDeleteClicked(GoodsDetailInfo goodsDetailInfo) {
                FavoritesPresenterImpl.getInstance().removeFavorite(goodsDetailInfo);
                favoritesAdapter.removeItem(goodsDetailInfo);
                updateFavoriteCount();
            }
        });
        header.setOnMeasureListener(measureHeight -> {
            if (measureHeight == 0) {
                return;
            }
            nestedScrollView.setHeaderHeight(measureHeight);
            header.removeOnMeasureListener();
        });
        nestedScrollView.setOnVerticalScroll(isScrollBottom -> {
            if (isScrollBottom) {
                updateFavoriteCount();
                favoritesNum.setVisibility(View.VISIBLE);
            } else {
                favoritesNum.setVisibility(View.GONE);
            }
        });
    }

    private void updateFavoriteCount() {
        favoritesNum.setText(String.format(getContext().getString(R.string.favorites_num_format), favoritesAdapter.getItemCount()));
    }

    @Override
    protected void initPresenter() {
        favoritesPresenter = FavoritesPresenterImpl.getInstance();
        favoritesPresenter.registerView(this);
    }

    @Override
    protected void fetchData() {
        LogUtils.d(TAG, "favorites fragment fetchData()...");
        favoritesPresenter.getFavorites();
    }

    @Override
    protected void release() {
        favoritesPresenter.unregisterView(this);
    }

    @Override
    public void onFavoritesLoaded(List<GoodsDetailInfo> goodDetailInfos) {
        if (goodDetailInfos == null || goodDetailInfos.size() == 0){
            noDataView.setVisibility(View.VISIBLE);
        } else {
            noDataView.setVisibility(View.GONE);
            Collections.reverse(goodDetailInfos);
            favoritesAdapter.setData(goodDetailInfos);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            favoritesPresenter.getFavorites();
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onNetErr() {

    }
}
