package com.litchi.taobaodemo.base;

public interface IBasePresenter<T> {
    void registerView(T view);
    void unregisterView(T view);
}
