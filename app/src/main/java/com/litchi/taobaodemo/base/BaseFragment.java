package com.litchi.taobaodemo.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.litchi.taobaodemo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private static final int STATE_NONE = -1;
    protected static final int STATE_NORMAL = 0;
    protected static final int STATE_LOADING = 1;
    protected static final int STATE_NET_ERR = 2;

    private Unbinder bind;

    private FrameLayout fragContainer;
    private View normalView;
    private View loadingView;
    private View netErrView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getBaseLayoutResourceId(), container, false);
        fragContainer = view.findViewById(R.id.base_frag_container);
        inflaterView(inflater, container);
        bind = ButterKnife.bind(this, view);
        initView(view);
        initEvent(view);
        initPresenter();
        fetchData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
        release();
    }

    @OnClick(R.id.net_err_retry)
    public void onNetErrRetry(){
        doNetErrRetry();
    }

    protected int getBaseLayoutResourceId() {
        return R.layout.fragment_base;
    }

    //    加载不同状态的页面
    private void inflaterView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        normalView = inflater.inflate(getNormalLayoutResourceId(), container, false);
        fragContainer.addView(normalView);
        loadingView = inflater.inflate(getLoadingLayoutResourceId(), container, false);
        fragContainer.addView(loadingView);
        netErrView = inflater.inflate(getNetErrLayoutResourceId(), container, false);
        fragContainer.addView(netErrView);
        setCurrState(STATE_NONE);
    }

    //    子类通过该方法修改页面状态
    public void setCurrState(int currState) {
        normalView.setVisibility(currState == STATE_NORMAL ? View.VISIBLE : View.GONE);
        loadingView.setVisibility(currState == STATE_LOADING ? View.VISIBLE : View.GONE);
        netErrView.setVisibility(currState == STATE_NET_ERR ? View.VISIBLE : View.GONE);
    }
    protected void initView(View view) {
        // 初始化控件
    }

    protected void initEvent(View view){
        // 设置事件监听
    }

    protected void release() {
        // 释放资源
    }

    protected void initPresenter() {
        // 初始化presenter
    }

    protected void fetchData() {
        // 加载数据
    }

    protected void doNetErrRetry(){
        // 网络错误，用户点击屏幕重试的操作
    }

    //    返回正常页面的布局id
    protected abstract int getNormalLayoutResourceId();

    //    返回加载中页面的布局id
    protected int getLoadingLayoutResourceId() {
        return R.layout.fragment_loading;
    }

    //    返回网络错误页面的布局id
    protected int getNetErrLayoutResourceId() {
        return R.layout.fragment_net_err;
    }
}
