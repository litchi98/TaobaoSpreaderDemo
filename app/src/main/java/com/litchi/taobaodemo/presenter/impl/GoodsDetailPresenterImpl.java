package com.litchi.taobaodemo.presenter.impl;

import android.text.TextUtils;

import com.litchi.taobaodemo.model.Api;
import com.litchi.taobaodemo.model.bean.CategoryDetail;
import com.litchi.taobaodemo.model.bean.CouponCode;
import com.litchi.taobaodemo.model.bean.GoodsDetailInfo;
import com.litchi.taobaodemo.model.bean.RequestCouponParam;
import com.litchi.taobaodemo.model.bean.SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean;
import com.litchi.taobaodemo.presenter.IGoodsDetailPresenter;
import com.litchi.taobaodemo.utils.LogUtils;
import com.litchi.taobaodemo.utils.RetrofitUtils;
import com.litchi.taobaodemo.view.IGoodsDetailView;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoodsDetailPresenterImpl implements IGoodsDetailPresenter {
    private static final String TAG = GoodsDetailPresenterImpl.class.getSimpleName();
    private static final int STATE_LOADING = 0;
    private static final int STATE_NET_ERR = 1;
    private IGoodsDetailView goodsDetailView;

    private static IGoodsDetailPresenter goodsDetailPresenter;
    private GoodsDetailInfo goodsDetailInfo;
    private String couponText;

    private GoodsDetailPresenterImpl() {
    }

    public static IGoodsDetailPresenter getInstance() {
        if (goodsDetailPresenter == null) {
            goodsDetailPresenter = new GoodsDetailPresenterImpl();
        }
        return goodsDetailPresenter;
    }

    @Override
    public void getCouponCode(CategoryDetail.DataBean dataBean) {
        float originalPriceNum = Float.parseFloat(dataBean.getZk_final_price());
        int discountNum = dataBean.getCoupon_amount();
        float finalPriceNum = originalPriceNum - discountNum;
        CategoryDetail.DataBean.SmallImagesBean small_images = dataBean.getSmall_images();
        List<String> smallImageUrls = null;
        if (small_images != null) {
            smallImageUrls = small_images.getString();
        }
        this.goodsDetailInfo = new GoodsDetailInfo("https:" + dataBean.getPict_url(), dataBean.getShop_title(),
                dataBean.getTitle(), finalPriceNum, originalPriceNum, dataBean.getVolume(),
                smallImageUrls, "https:" + dataBean.getClick_url(), "https:" + dataBean.getCoupon_click_url());
        getCouponCode(goodsDetailInfo);
    }

    @Override
    public void getCouponCode(MapDataBean mapDataBean) {
        float originalPriceNum = Float.parseFloat(mapDataBean.getZk_final_price());
        int discountNum = Integer.valueOf(mapDataBean.getCoupon_amount());
        float finalPriceNum = originalPriceNum - discountNum;
        MapDataBean.SmallImagesBean small_images = mapDataBean.getSmall_images();
        List<String> smallImageUrls = null;
        if (small_images != null) {
            smallImageUrls = small_images.getString();
        }
        this.goodsDetailInfo = new GoodsDetailInfo(mapDataBean.getPict_url(), mapDataBean.getShop_title(),
                mapDataBean.getTitle(), finalPriceNum, originalPriceNum, mapDataBean.getVolume(),
                smallImageUrls, "https:" + mapDataBean.getUrl(), "https:" +mapDataBean.getCoupon_share_url());
        getCouponCode(goodsDetailInfo);
    }

    @Override
    public void getCouponCode(GoodsDetailInfo goodsDetailInfo) {
        this.goodsDetailInfo = goodsDetailInfo;
        String clickedUrl = goodsDetailInfo.getClickedUrl();
        String couponUrl = goodsDetailInfo.getCouponUrl();
        if (TextUtils.isEmpty(couponUrl)) {
            getCouponCode(clickedUrl);
        } else {
            getCouponCode(couponUrl);
        }
    }

    private void getCouponCode(String url) {
        Retrofit retrofit = RetrofitUtils.getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<CouponCode> call = api.getCouponCode(new RequestCouponParam(url, null));
        call.enqueue(new Callback<CouponCode>() {
            @Override
            public void onResponse(Call<CouponCode> call, Response<CouponCode> response) {
                updateViewState(STATE_LOADING);
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    CouponCode couponCode = response.body();
                    String couponText = couponCode.getData().getTbk_tpwd_create_response().getData().getModel();
                    if (goodsDetailView != null && goodsDetailInfo != null) {
                        goodsDetailView.onCouponCodeLoaded(couponText, goodsDetailInfo);
                    } else {
                        GoodsDetailPresenterImpl.this.couponText = couponText;
                    }
                    LogUtils.d(TAG, "request coupon code success, coupon code ===> " + couponCode);
                } else {
                    LogUtils.d(TAG, "request coupon code failed, response code ===> " + code);
                    updateViewState(STATE_NET_ERR);
                }
            }

            @Override
            public void onFailure(Call<CouponCode> call, Throwable t) {
                LogUtils.d(TAG, "request coupon code error!");
                t.printStackTrace();
                updateViewState(STATE_NET_ERR);
            }
        });
    }

    @Override
    public void registerView(IGoodsDetailView view) {
        this.goodsDetailView = view;
        if (couponText != null && goodsDetailInfo != null) {
            view.onCouponCodeLoaded(couponText, goodsDetailInfo);
        }
    }

    @Override
    public void unregisterView(IGoodsDetailView view) {
        goodsDetailView = null;
        couponText = null;
        goodsDetailInfo = null;
    }

    private void updateViewState(int state) {
        if (goodsDetailView == null) {
            return;
        }
        switch (state) {
            case STATE_LOADING:
                goodsDetailView.onLoading();
                break;
            case STATE_NET_ERR:
                goodsDetailView.onNetErr();
                break;
        }
    }
}
