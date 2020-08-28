package com.litchi.taobaodemo.presenter;

import com.litchi.taobaodemo.base.IBasePresenter;
import com.litchi.taobaodemo.model.bean.CategoryDetail.DataBean;
import com.litchi.taobaodemo.model.bean.GoodsDetailInfo;
import com.litchi.taobaodemo.view.IGoodsDetailView;
import com.litchi.taobaodemo.model.bean.SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean;

public interface IGoodsDetailPresenter extends IBasePresenter<IGoodsDetailView> {
    void getCouponCode(DataBean dataBean);
    void getCouponCode(MapDataBean mapDataBean);

    void getCouponCode(GoodsDetailInfo goodsDetailInfo);
}
