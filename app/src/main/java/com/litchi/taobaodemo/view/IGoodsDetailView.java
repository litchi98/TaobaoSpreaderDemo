package com.litchi.taobaodemo.view;

import com.litchi.taobaodemo.base.IBaseView;
import com.litchi.taobaodemo.model.bean.GoodsDetailInfo;

public interface IGoodsDetailView extends IBaseView {
    void onCouponCodeLoaded(String couponText, GoodsDetailInfo goodsDetailInfo);
}
