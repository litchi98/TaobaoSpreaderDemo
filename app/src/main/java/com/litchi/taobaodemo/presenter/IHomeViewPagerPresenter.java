package com.litchi.taobaodemo.presenter;

import com.litchi.taobaodemo.base.IBasePresenter;
import com.litchi.taobaodemo.view.IHomeViewPagerView;

public interface IHomeViewPagerPresenter extends IBasePresenter<IHomeViewPagerView> {
    void getCategoryDetail(int categoryId);

    void loadMoreCategoryDetail(int categoryId);
}
