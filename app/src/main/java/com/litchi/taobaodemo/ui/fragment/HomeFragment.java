package com.litchi.taobaodemo.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.base.BaseFragment;
import com.litchi.taobaodemo.model.bean.Categories;
import com.litchi.taobaodemo.presenter.ISearchPresenter;
import com.litchi.taobaodemo.presenter.impl.HomePresenterImpl;
import com.litchi.taobaodemo.presenter.impl.SearchPresenterImpl;
import com.litchi.taobaodemo.ui.activity.SearchActivity;
import com.litchi.taobaodemo.ui.adapter.HomeViewPagerAdapter;
import com.litchi.taobaodemo.utils.LogUtils;
import com.litchi.taobaodemo.view.IHomeView;
import com.vondear.rxfeature.activity.ActivityScanerCode;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeView {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomePresenterImpl homePresenter;

    @BindView(R.id.home_tab_layout)
    public TabLayout homeTabLayout;

    @BindView(R.id.home_view_pager2)
    public ViewPager2 homeViewPager;

    @BindView(R.id.home_search_edit)
    public EditText search;

    @BindView(R.id.home_scan)
    public View scanBtn;

    private HomeViewPagerAdapter homeViewPagerAdapter;
    private Categories categories;

    @Override
    protected int getNormalLayoutResourceId() {
        return R.layout.fragment_home;
    }

    @Override
    protected int getBaseLayoutResourceId() {
        return R.layout.fragment_base_home;
    }

    @Override
    protected void initPresenter() {
        homePresenter = new HomePresenterImpl();
        homePresenter.registerView(this);
    }

    @Override
    protected void initView(View view) {
        homeViewPagerAdapter = new HomeViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        homeViewPager.setAdapter(homeViewPagerAdapter);
        homeViewPager.setSaveEnabled(false);
        // 去除viewpager2边界阴影
        View childView = homeViewPager.getChildAt(0);
        if (childView instanceof RecyclerView) {
            childView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        // 绑定tabView 和 viewPager
        new TabLayoutMediator(homeTabLayout, homeViewPager, (tab, position) -> {
            String title = categories.getData().get(position).getTitle();
            tab.setText(title);
        }).attach();
        // 禁用edit长按和文本选择
        search.setLongClickable(false);
        search.setTextIsSelectable(false);
    }

    @Override
    protected void initEvent(View view) {
        search.setOnClickListener(v -> {
            ISearchPresenter searchPresenter = SearchPresenterImpl.getInstance();
            searchPresenter.getSearchRecommend();
            searchPresenter.getSearchHistory();
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });
        scanBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ActivityScanerCode.class);
            startActivity(intent);
        });
    }

    @Override
    protected void fetchData() {
        homePresenter.getCategories();
    }

    @Override
    protected void release() {
        homePresenter.unregisterView(this);
    }

    @Override
    protected void doNetErrRetry() {
        LogUtils.d(TAG, "do net err retry...");
        fetchData();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        setCurrState(STATE_NORMAL);
        this.categories = categories;
        homeViewPagerAdapter.setCategories(categories);
    }

    @Override
    public void onLoading() {
        setCurrState(STATE_LOADING);
    }

    @Override
    public void onNetErr() {
        setCurrState(STATE_NET_ERR);
        Toast.makeText(getContext(), "请求超时", Toast.LENGTH_SHORT).show();
    }
}
