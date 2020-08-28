package com.litchi.taobaodemo.model.bean;

import androidx.annotation.Nullable;

import java.util.List;

public class GoodsDetailInfo {
    private String pictUrl;
    private String shop_title;
    private String title;
    private float finalPriceNum;
    private float originalPriceNum;
    private int volume;
    private List<String> smallImages;
    private String clickedUrl;
    private String couponUrl;

    public String getClickedUrl() {
        return clickedUrl;
    }

    public void setClickedUrl(String clickedUrl) {
        this.clickedUrl = clickedUrl;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public String getPictUrl() {
        return pictUrl;
    }

    public void setPictUrl(String pictUrl) {
        this.pictUrl = pictUrl;
    }

    public String getShop_title() {
        return shop_title;
    }

    public void setShop_title(String shop_title) {
        this.shop_title = shop_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getFinalPriceNum() {
        return finalPriceNum;
    }

    public void setFinalPriceNum(float finalPriceNum) {
        this.finalPriceNum = finalPriceNum;
    }

    public float getOriginalPriceNum() {
        return originalPriceNum;
    }

    public void setOriginalPriceNum(float originalPriceNum) {
        this.originalPriceNum = originalPriceNum;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public List<String> getSmallImages() {
        return smallImages;
    }

    public void setSmallImages(List<String> smallImages) {
        this.smallImages = smallImages;
    }

    public GoodsDetailInfo(String pictUrl, String shop_title, String title, float finalPriceNum, float originalPriceNum, int volume, List<String> smallImages, String clickedUrl, String couponUrl) {
        this.pictUrl = pictUrl;
        this.shop_title = shop_title;
        this.title = title;
        this.finalPriceNum = finalPriceNum;
        this.originalPriceNum = originalPriceNum;
        this.volume = volume;
        this.smallImages = smallImages;
        this.clickedUrl = clickedUrl;
        this.couponUrl = couponUrl;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof GoodsDetailInfo)) {
            return false;
        }
//        String shopTitleObj = ((GoodsDetailInfo) obj).getShop_title();
//        String titleObj = ((GoodsDetailInfo) obj).getTitle();
//        return (shopTitleObj != null && shopTitleObj.equals(getShop_title())) &&
//                (titleObj != null && titleObj.equals(getTitle()));
        return ((GoodsDetailInfo) obj).getPictUrl().equals(getPictUrl());
    }
}
