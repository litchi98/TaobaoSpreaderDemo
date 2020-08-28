package com.litchi.taobaodemo.view;

import com.litchi.taobaodemo.base.IBaseView;
import com.litchi.taobaodemo.model.bean.GoodsDetailInfo;

import java.util.List;

public interface IFavoritesView extends IBaseView {
    void onFavoritesLoaded(List<GoodsDetailInfo> goodDetailInfos);
}
