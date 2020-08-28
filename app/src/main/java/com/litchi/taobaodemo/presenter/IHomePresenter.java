package com.litchi.taobaodemo.presenter;

import com.litchi.taobaodemo.base.IBasePresenter;
import com.litchi.taobaodemo.view.IHomeView;

public interface IHomePresenter extends IBasePresenter<IHomeView> {
    void getCategories();
}