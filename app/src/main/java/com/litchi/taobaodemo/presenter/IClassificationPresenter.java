package com.litchi.taobaodemo.presenter;

import com.litchi.taobaodemo.base.IBasePresenter;
import com.litchi.taobaodemo.view.IClassificationView;

public interface IClassificationPresenter extends IBasePresenter<IClassificationView> {
    void getClassifications();
}
