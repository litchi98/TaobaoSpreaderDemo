package com.litchi.taobaodemo.presenter.impl;

import com.litchi.taobaodemo.model.Api;
import com.litchi.taobaodemo.model.bean.CategoryDetail;
import com.litchi.taobaodemo.presenter.IHomeViewPagerPresenter;
import com.litchi.taobaodemo.utils.LogUtils;
import com.litchi.taobaodemo.utils.RetrofitUtils;
import com.litchi.taobaodemo.view.IHomeViewPagerView;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeViewPagerPresenterImpl implements IHomeViewPagerPresenter {

    private static final String TAG = HomeViewPagerPresenterImpl.class.getSimpleName();
    private static final int DEFAULT_PAGE = 1;

    private Map<Integer, Integer> currCategoryPage = new HashMap<>();
    private Map<Integer, IHomeViewPagerView> viewMap = new HashMap<>();

    @Override
    public void getCategoryDetail(int categoryId) {
        final IHomeViewPagerView view = viewMap.get(categoryId);
        if (view != null) {
            view.onLoading();
        }
        Retrofit retrofit = RetrofitUtils.getRetrofit();
        Api api = retrofit.create(Api.class);
        Integer page = currCategoryPage.get(categoryId);
        if (page == null){
            page = DEFAULT_PAGE;
            currCategoryPage.put(categoryId, page);
        }
        Call<CategoryDetail> call = api.getCategoryDetail(categoryId, page);
        call.enqueue(new Callback<CategoryDetail>() {
            @Override
            public void onResponse(Call<CategoryDetail> call, Response<CategoryDetail> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    CategoryDetail categoryDetail = response.body();
                    List<CategoryDetail.DataBean> details = categoryDetail.getData();
                    LogUtils.d(TAG, "request category detail success, category detail ===> " + categoryDetail);
                    if (view != null) {
                        view.onCategoryDetailLoaded(details);
                    }
                    if (view != null) {
                        view.onCarouselDataLoaded(details.subList(details.size() - 5, details.size()));
                    }
                } else {
                    LogUtils.d(TAG, "request category detail failed, response code ===> " + code);
                    if (view != null) {
                        view.onNetErr();
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryDetail> call, Throwable t) {
                LogUtils.d(TAG, "request category detail error...");
                t.printStackTrace();
                if (view != null) {
                    view.onNetErr();
                }
            }
        });
    }

    @Override
    public void loadMoreCategoryDetail(int categoryId) {
        IHomeViewPagerView view = viewMap.get(categoryId);
        Retrofit retrofit = RetrofitUtils.getRetrofit();
        Api api = retrofit.create(Api.class);
        Integer page = currCategoryPage.get(categoryId);
        if (page == null) {
            page = DEFAULT_PAGE;
        }
        currCategoryPage.put(categoryId, page + 1);
        Call<CategoryDetail> call = api.getCategoryDetail(categoryId, page);
        call.enqueue(new Callback<CategoryDetail>() {
            @Override
            public void onResponse(Call<CategoryDetail> call, Response<CategoryDetail> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    CategoryDetail categoryDetail = response.body();
                    LogUtils.d(TAG, "request load more category detail success, category detail ===> " + categoryDetail);
                    if (view != null) {
                        view.onLoadMoreDone(categoryDetail.getData());
                    }
                } else {
                    LogUtils.d(TAG, "request load more category detail failed, response code ===> " + code);
                    if (view != null) {
                        view.loadMoreFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryDetail> call, Throwable t) {
                LogUtils.d(TAG, "request load more category detail error!");
                t.printStackTrace();
                if (view != null) {
                    view.loadMoreFailed();
                }
            }
        });
    }

    @Override
    public void registerView(IHomeViewPagerView view) {
        int categoryId = view.getCategoryId();
        viewMap.put(categoryId, view);
    }

    @Override
    public void unregisterView(IHomeViewPagerView view) {
        int categoryId = view.getCategoryId();
        viewMap.remove(categoryId);
    }
}
