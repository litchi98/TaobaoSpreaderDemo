package com.litchi.taobaodemo.view;

import com.litchi.taobaodemo.base.IBaseView;
import com.litchi.taobaodemo.model.bean.Categories;

public interface IHomeView extends IBaseView {

    void onCategoriesLoaded(Categories categories);

}
