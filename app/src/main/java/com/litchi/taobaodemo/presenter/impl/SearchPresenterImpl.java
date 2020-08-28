package com.litchi.taobaodemo.presenter.impl;

import com.google.gson.reflect.TypeToken;
import com.litchi.taobaodemo.model.Api;
import com.litchi.taobaodemo.model.bean.SearchRecommend;
import com.litchi.taobaodemo.model.bean.SearchResult;
import com.litchi.taobaodemo.presenter.ISearchPresenter;
import com.litchi.taobaodemo.ui.activity.SearchActivity;
import com.litchi.taobaodemo.utils.LogUtils;
import com.litchi.taobaodemo.utils.RetrofitUtils;
import com.litchi.taobaodemo.utils.SharedPreferencesUtils;
import com.litchi.taobaodemo.view.ISearchView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements ISearchPresenter {
    private static final String TAG = SearchPresenterImpl.class.getSimpleName();
    private static final String KEY_SEARCH_HISTORY = "searchHistory";
    private static final int DEFAULT_PAGE = 1;
    private ISearchView searchView;

    private static ISearchPresenter searchPresenter;
    private List<SearchRecommend.DataBean> searchRecommendData = new ArrayList<>();

    private int page = DEFAULT_PAGE;
    private String keyword;
    private List<String> searchHistory = new ArrayList<>();
    private TypeToken<List<String>> typeToken;

    private SearchPresenterImpl() {
        typeToken = new TypeToken<List<String>>() {
        };
    }

    public static ISearchPresenter getInstance() {
        if (searchPresenter == null) {
            searchPresenter = new SearchPresenterImpl();
        }
        return searchPresenter;
    }

    @Override
    public void getSearchHistory() {
        new Thread(this::getSearchHistoryRun).run();
    }

    private void getSearchHistoryRun() {
        List<String> searchHistories = SharedPreferencesUtils.getInstance().getObj(KEY_SEARCH_HISTORY, typeToken);
        if (searchHistories == null || searchHistories.size() == 0) {
        } else if (searchView != null) {
            ((SearchActivity) searchView).runOnUiThread(() -> searchView.onSearchHistoryLoaded(searchHistories));
        } else {
            this.searchHistory = searchHistories;
        }
    }

    @Override
    public void removeSearchHistory() {
        SharedPreferencesUtils.getInstance().delete(KEY_SEARCH_HISTORY);
    }

    private void addSearchHistory(String history) {
        SharedPreferencesUtils preferencesUtils = SharedPreferencesUtils.getInstance();
        List<String> histories = preferencesUtils.getObj(KEY_SEARCH_HISTORY, typeToken);
        if (histories == null) {
            histories = new ArrayList<>();
        }
        histories.remove(history);
        histories.add(history);
        preferencesUtils.saveObj(KEY_SEARCH_HISTORY, histories);
    }

    @Override
    public void getSearchRecommend() {
        Retrofit retrofit = RetrofitUtils.getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<SearchRecommend> call = api.getSearchRecommend();
        call.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchRecommend searchRecommend = response.body();
                    LogUtils.d(TAG, "request search recommend success, search recommend ===> " + searchRecommend);
                    if (searchView != null) {
                        searchView.onSearchRecommendLoaded(searchRecommend.getData());
                    } else {
                        searchRecommendData.addAll(searchRecommend.getData());
                    }
                } else {
                    LogUtils.d(TAG, "request search recommend failed, response code ===> " + code);
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                LogUtils.d(TAG, "request search recommend error!");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void doSearch(String keyword) {
        this.page = DEFAULT_PAGE;
        searchAction(keyword);
    }

    private void searchAction(String keyword) {
        if (searchView != null) {
            searchView.onSearchLoading();
        }
        this.keyword = keyword;
        addSearchHistory(keyword);
        Retrofit retrofit = RetrofitUtils.getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<SearchResult> call = api.doSearch(keyword, page);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchResult searchResult = response.body();
                    LogUtils.d(TAG, "request search result success, search result ===> " + searchResult);
                    if (searchView != null && searchResult != null) {
                        searchView.onSearchSuccess(searchResult.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
                        page++;
                    }
                } else {
                    LogUtils.d(TAG, "request search result failed, response code ===> " + code);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                LogUtils.d(TAG, "request search result error!");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void loadMore() {
        Retrofit retrofit = RetrofitUtils.getRetrofit();
        Api api = retrofit.create(Api.class);
        LogUtils.d(TAG, "search result load more, keyword ===> " + keyword + ", page ===> " + page);
        Call<SearchResult> call = api.doSearch(keyword, page);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchResult searchResult = response.body();
                    LogUtils.d(TAG, "load more search result success, search result ===> " + searchResult);
                    if (searchView != null && searchResult != null && searchResult.getData().getTbk_dg_material_optional_response() != null) {
                        searchView.onLoadMoreSuccess(searchResult.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
                        page++;
                    } else if (searchView != null) {
                        searchView.onLoadMoreNone();
                    }
                } else {
                    LogUtils.d(TAG, "load more result failed, response code ===> " + code);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                LogUtils.d(TAG, "load more result error!");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerView(ISearchView view) {
        this.searchView = view;
        if (searchRecommendData.size() > 0) {
            searchView.onSearchRecommendLoaded(this.searchRecommendData);
        }
        if (searchHistory.size() > 0) {
            searchView.onSearchHistoryLoaded(this.searchHistory);
        }
    }

    @Override
    public void unregisterView(ISearchView view) {
        searchView = null;
        searchRecommendData.clear();
        searchHistory.clear();
        page = 1;
    }
}
