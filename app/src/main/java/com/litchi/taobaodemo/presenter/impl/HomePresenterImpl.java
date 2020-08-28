package com.litchi.taobaodemo.presenter.impl;

import com.litchi.taobaodemo.model.Api;
import com.litchi.taobaodemo.model.bean.Categories;
import com.litchi.taobaodemo.presenter.IHomePresenter;
import com.litchi.taobaodemo.utils.LogUtils;
import com.litchi.taobaodemo.utils.RetrofitUtils;
import com.litchi.taobaodemo.view.IHomeView;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {

    private static final String TAG = HomePresenterImpl.class.getSimpleName();
    private IHomeView homeView;

    @Override
    public void getCategories() {
        homeView.onLoading();
        Retrofit retrofit = RetrofitUtils.getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> call = api.getCategories();
        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                int responseCode = response.code();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 请求成功
                    Categories categories = response.body();
                    LogUtils.d(TAG, "request categories success, categories ===> " + categories);
                    if (homeView != null) {
                        homeView.onCategoriesLoaded(categories);
                    }
                } else {
                    // 请求失败
                    LogUtils.i(TAG, "request categories failed, response code ===> " + response.code());
                    if (homeView != null) {
                        homeView.onNetErr();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                // 请求错误
                LogUtils.e(TAG, "request categories error...");
                t.printStackTrace();
                if (homeView != null) {
                    homeView.onNetErr();
                }
            }
        });
    }

    @Override
    public void registerView(IHomeView homeView) {
        this.homeView = homeView;
    }

    @Override
    public void unregisterView(IHomeView homeView) {
        if (this.homeView != null) {
            this.homeView = null;
        }
    }
}
