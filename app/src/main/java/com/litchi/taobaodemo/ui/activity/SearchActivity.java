package com.litchi.taobaodemo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.model.bean.SearchRecommend;
import com.litchi.taobaodemo.model.bean.SearchResult;
import com.litchi.taobaodemo.presenter.ISearchPresenter;
import com.litchi.taobaodemo.presenter.impl.GoodsDetailPresenterImpl;
import com.litchi.taobaodemo.presenter.impl.SearchPresenterImpl;
import com.litchi.taobaodemo.ui.adapter.SearchResultAdapter;
import com.litchi.taobaodemo.ui.component.FlowLayout;
import com.litchi.taobaodemo.ui.component.LoadingView;
import com.litchi.taobaodemo.utils.LogUtils;
import com.litchi.taobaodemo.view.ISearchView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchActivity extends AppCompatActivity implements ISearchView {

    private static final String TAG = SearchActivity.class.getSimpleName();
    public static final String KEY_SEARCH_INTENT = "keyword";
    private Unbinder bind;

    @BindView(R.id.search_edit_text)
    public EditText searchEdit;

    @BindView(R.id.search_back)
    public View back;

    @BindView(R.id.search_history)
    public FlowLayout searchHistoryFlow;

    @BindView(R.id.search_recommend)
    public FlowLayout searchRecommendFlow;

    @BindView(R.id.search_loading)
    public LoadingView loadingView;

    @BindView(R.id.search_result)
    public RecyclerView searchResult;

    @BindView(R.id.search_main)
    public View searchMain;

    @BindView(R.id.search_result_refresh)
    public TwinklingRefreshLayout searchResultRefresh;

    @BindView(R.id.search_clear_text)
    public View clearText;

    @BindView(R.id.search_delete_history)
    public View deleteHistory;

    private ISearchPresenter searchPresenter;
    private SearchResultAdapter searchResultAdapter;

    private boolean isInMainView = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.activity_search);
        bind = ButterKnife.bind(this);
        initPresenter();
        initView();
        checkIntentData();
        initEvent();
    }

    private void checkIntentData() {
        Intent intent = getIntent();
        String keyword = intent.getStringExtra(KEY_SEARCH_INTENT);
        if (keyword != null) {
            searchEdit.setText(keyword);
            SearchPresenterImpl.getInstance().doSearch(keyword);
            putAwayKeyboard();
        }
    }

    private void initEvent() {
        back.setOnClickListener(v -> {
            if (isInMainView) {
                finish();
            } else {
                cancelSearch();
            }
        });
        // 设置软键盘监听事件，监听搜索action，点击搜索后收起软键盘，消费事件
        searchEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = searchEdit.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    // 如果关键词为空，则搜索hint内容
                    keyword = searchEdit.getHint().toString();
                    searchEdit.setText(keyword);
                }
                SearchPresenterImpl.getInstance().doSearch(keyword);
                // 收起软键盘,去掉搜索框焦点，隐藏光标
                putAwayKeyboard();
            }
            return true;
        });
        // 设置recyclerView中item的点击事件，跳转至goodsDetail页面
        searchResultAdapter.setOnItemClickedListener(mapDataBean -> {
            GoodsDetailPresenterImpl.getInstance().getCouponCode(mapDataBean);
            Intent intent = new Intent(SearchActivity.this, GoodsDetailActivity.class);
            startActivity(intent);
        });
        // 设置flowLayout中item的点击事件
        searchRecommendFlow.setOnItemClickListener(this::flowLayoutItemAction);
        searchHistoryFlow.setOnItemClickListener(this::flowLayoutItemAction);
        // 设置refreshLayout加载更多事件
        searchResultRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                searchPresenter.loadMore();
            }
        });
        // 清楚搜索框，拉起键盘
        clearText.setOnClickListener(v -> clearText());
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    clearText.setVisibility(View.GONE);
                } else {
                    clearText.setVisibility(View.VISIBLE);
                }
            }
        });
        deleteHistory.setOnClickListener(v -> {
            searchPresenter.removeSearchHistory();
            deleteHistory.setVisibility(View.GONE);
            searchHistoryFlow.clear();
        });
        searchResult.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                putAwayKeyboard();
            }
            return false;
        });
    }

    private void flowLayoutItemAction(String keyword) {
        // 调用搜索
        SearchPresenterImpl.getInstance().doSearch(keyword);
        // 收起软键盘,去掉搜索框焦点，隐藏光标
        putAwayKeyboard();
        // 设置搜索框内容
        searchEdit.setText(keyword);
    }

    private void putAwayKeyboard() {
        searchResult.requestFocus();
        InputMethodManager manager = ((InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null)
            manager.hideSoftInputFromWindow(searchEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initView() {
        searchEdit.setFocusable(true);
        searchEdit.setFocusableInTouchMode(true);
        searchEdit.requestFocus();
        searchResultAdapter = new SearchResultAdapter();
        searchResult.setAdapter(searchResultAdapter);
        searchResult.setLayoutManager(new LinearLayoutManager(this));
        searchResultRefresh.setEnableRefresh(false);
        clearText();
    }

    private void initPresenter() {
        searchPresenter = SearchPresenterImpl.getInstance();
        searchPresenter.registerView(this);
    }

    @Override
    protected void onDestroy() {
        if (bind != null) {
            bind.unbind();
        }
        if (searchPresenter != null) {
            searchPresenter.unregisterView(this);
        }
        super.onDestroy();
    }

    @Override
    public void onSearchHistoryLoaded(List<String> searchHistories) {
        Collections.reverse(searchHistories);
        searchHistoryFlow.setDataByString(searchHistories);
        deleteHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchRecommendLoaded(List<SearchRecommend.DataBean> data) {
        searchRecommendFlow.setData(data);
    }

    @Override
    public void onSearchSuccess(List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> mapDataBeans) {
        loadingView.setVisibility(View.GONE);
        searchResultRefresh.setVisibility(View.VISIBLE);
        searchResultAdapter.setData(mapDataBeans);
        searchResult.scrollToPosition(0);
        isInMainView = false;
    }

    @Override
    public void onLoadMoreSuccess(List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> mapDataBeans) {
        LogUtils.d(TAG, "mapDataBeans.size ===> " + mapDataBeans.size());
        searchResultAdapter.updateData(mapDataBeans);
        searchResultRefresh.finishLoadmore();
    }

    @Override
    public void onSearchLoading() {
        searchMain.setVisibility(View.GONE);
        searchResultRefresh.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        isInMainView = false;
    }

    public void cancelSearch() {
        searchMain.setVisibility(View.VISIBLE);
        searchPresenter.getSearchHistory();
        searchResultRefresh.setVisibility(View.GONE);
        clearText();
        isInMainView = true;
    }

    private void clearText() {
        searchEdit.setText("");
        searchEdit.requestFocus();
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(manager).showSoftInput(searchEdit, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onLoadMoreNone() {
        Toast.makeText(this, "没有更多了...", Toast.LENGTH_SHORT).show();
        searchResultRefresh.finishLoadmore();
    }
}
