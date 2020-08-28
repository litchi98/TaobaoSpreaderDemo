package com.litchi.taobaodemo.view;

import com.litchi.taobaodemo.base.IBaseView;
import com.litchi.taobaodemo.model.bean.CategoryDetail;

import java.util.List;

public interface IHomeViewPagerView extends IBaseView {
    void onCategoryDetailLoaded(List<CategoryDetail.DataBean> details);

    void onCarouselDataLoaded(List<CategoryDetail.DataBean> details);

    int getCategoryId();

    void onLoadMoreDone(List<CategoryDetail.DataBean> details);

    void loadMoreFailed();
}
