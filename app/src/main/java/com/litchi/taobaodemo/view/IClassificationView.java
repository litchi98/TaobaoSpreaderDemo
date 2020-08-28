package com.litchi.taobaodemo.view;

import com.litchi.taobaodemo.base.IBaseView;
import com.litchi.taobaodemo.model.bean.Classification;

import java.util.List;

public interface IClassificationView extends IBaseView {
    void onClassificationsLoaded(List<Classification> classifications);
}
