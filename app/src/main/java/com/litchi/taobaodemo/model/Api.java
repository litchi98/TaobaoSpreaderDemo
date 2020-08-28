package com.litchi.taobaodemo.model;

import com.litchi.taobaodemo.model.bean.Categories;
import com.litchi.taobaodemo.model.bean.CategoryDetail;
import com.litchi.taobaodemo.model.bean.CouponCode;
import com.litchi.taobaodemo.model.bean.RequestCouponParam;
import com.litchi.taobaodemo.model.bean.SearchRecommend;
import com.litchi.taobaodemo.model.bean.SearchResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET("discovery/{materialId}/{page}")
    Call<CategoryDetail> getCategoryDetail(@Path("materialId")int materialId, @Path("page")int page);

    @POST("tpwd")
    Call<CouponCode> getCouponCode(@Body RequestCouponParam requestCouponParam);

    @GET("search/recommend")
    Call<SearchRecommend> getSearchRecommend();

    @GET("search")
    Call<SearchResult> doSearch(@Query("keyword") String keyword, @Query("page") int page);
}
