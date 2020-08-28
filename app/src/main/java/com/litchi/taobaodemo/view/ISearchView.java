package com.litchi.taobaodemo.view;

import com.litchi.taobaodemo.model.bean.SearchRecommend;
import com.litchi.taobaodemo.model.bean.SearchResult;

import java.util.List;

public interface ISearchView {
    void onSearchHistoryLoaded(List<String> searchHistories);

    void onSearchRecommendLoaded(List<SearchRecommend.DataBean> data);

    void onSearchSuccess(List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> mapDataBeans);

    void onLoadMoreSuccess(List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> map_data);

    void onSearchLoading();

    void onLoadMoreNone();
}
