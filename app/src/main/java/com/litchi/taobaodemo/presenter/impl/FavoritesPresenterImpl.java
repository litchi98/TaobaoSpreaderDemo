package com.litchi.taobaodemo.presenter.impl;

import com.google.gson.reflect.TypeToken;
import com.litchi.taobaodemo.model.bean.GoodsDetailInfo;
import com.litchi.taobaodemo.presenter.IFavoritesPresenter;
import com.litchi.taobaodemo.utils.LogUtils;
import com.litchi.taobaodemo.utils.SharedPreferencesUtils;
import com.litchi.taobaodemo.view.IFavoritesView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesPresenterImpl implements IFavoritesPresenter {
    private static final String TAG = FavoritesPresenterImpl.class.getSimpleName();
    private static final String KEY_FAVORITE_SP = "favorites";
    private IFavoritesView favoritesView;

    private static FavoritesPresenterImpl favoritesPresenter;
    private final SharedPreferencesUtils preferencesUtils;

    private FavoritesPresenterImpl() {
        preferencesUtils = SharedPreferencesUtils.getInstance();
    }

    public static FavoritesPresenterImpl getInstance() {
        if (favoritesPresenter == null) {
            favoritesPresenter = new FavoritesPresenterImpl();
        }
        return favoritesPresenter;
    }

    @Override
    public boolean addOrDeleteFavorite(GoodsDetailInfo goodsDetailInfo) {
        List<GoodsDetailInfo> favorites = preferencesUtils.getObj(KEY_FAVORITE_SP, new TypeToken<List<GoodsDetailInfo>>() {
        });
        boolean isFavorite;
        if (favorites == null || favorites.size() == 0) {
            favorites = new ArrayList<>();
            favorites.add(goodsDetailInfo);
            preferencesUtils.saveObj(KEY_FAVORITE_SP, favorites);
            isFavorite = true;
        } else {
            boolean isContains = favorites.contains(goodsDetailInfo);
            if (isContains) {
                favorites.remove(goodsDetailInfo);
                isFavorite = false;
            } else {
                favorites.add(goodsDetailInfo);
                isFavorite = true;
            }
        }
        preferencesUtils.saveObj(KEY_FAVORITE_SP, favorites);
        LogUtils.d(TAG, (isFavorite?"add favorite success, title ===> ":"remove favorite success, title ===> ") + goodsDetailInfo.getTitle());
        return isFavorite;
    }

    @Override
    public void removeFavorite(GoodsDetailInfo goodsDetailInfo) {
        List<GoodsDetailInfo> favorites = preferencesUtils.getObj(KEY_FAVORITE_SP, new TypeToken<List<GoodsDetailInfo>>() {
        });
        if (favorites == null || favorites.size() == 0) {
            return;
        }
        favorites.remove(goodsDetailInfo);
        preferencesUtils.saveObj(KEY_FAVORITE_SP, favorites);
        LogUtils.d(TAG, "remove favorite success, title ===> " + goodsDetailInfo.getTitle());
    }

    @Override
    public void getFavorites() {
        List<GoodsDetailInfo> favorites = preferencesUtils.getObj(KEY_FAVORITE_SP, new TypeToken<List<GoodsDetailInfo>>() {
        });
        if (favoritesView != null) {
            favoritesView.onFavoritesLoaded(favorites);
        }
        LogUtils.d(TAG, "get favorites success, favorites ===> " + favorites);
    }

    @Override
    public boolean isFavorite(GoodsDetailInfo goodsDetailInfo) {
        List<GoodsDetailInfo> favorites = preferencesUtils.getObj(KEY_FAVORITE_SP, new TypeToken<List<GoodsDetailInfo>>() {
        });
        if (favorites == null || favorites.size() == 0){
            return false;
        }
        return favorites.contains(goodsDetailInfo);
    }

    @Override
    public void registerView(IFavoritesView view) {
        this.favoritesView = view;
    }

    @Override
    public void unregisterView(IFavoritesView view) {
        this.favoritesView = null;
    }
}
