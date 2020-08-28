package com.litchi.taobaodemo.model.bean;

public class RequestCouponParam {
    private String url;
    private String title;

    public RequestCouponParam(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
