package com.litchi.taobaodemo.presenter;

import com.litchi.taobaodemo.base.IBasePresenter;
import com.litchi.taobaodemo.model.bean.GoodsDetailInfo;
import com.litchi.taobaodemo.view.IFavoritesView;

public interface IFavoritesPresenter extends IBasePresenter<IFavoritesView> {
    boolean addOrDeleteFavorite(GoodsDetailInfo goodsDetailInfo);

    void removeFavorite(GoodsDetailInfo goodsDetailInfo);

    void getFavorites();

    boolean isFavorite(GoodsDetailInfo goodsDetailInfo);
}
