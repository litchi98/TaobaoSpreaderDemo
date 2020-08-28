package com.litchi.taobaodemo.presenter;

import com.litchi.taobaodemo.base.IBasePresenter;
import com.litchi.taobaodemo.view.ISearchView;

public interface ISearchPresenter extends IBasePresenter<ISearchView> {
    void getSearchHistory();

    void removeSearchHistory();

    void getSearchRecommend();

    void doSearch(String keyword);

    void loadMore();
}
