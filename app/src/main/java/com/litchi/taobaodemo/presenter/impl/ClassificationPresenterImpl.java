package com.litchi.taobaodemo.presenter.impl;

import android.os.Handler;

import com.litchi.taobaodemo.R;
import com.litchi.taobaodemo.model.bean.Classification;
import com.litchi.taobaodemo.model.bean.ClassificationDetail;
import com.litchi.taobaodemo.presenter.IClassificationPresenter;
import com.litchi.taobaodemo.view.IClassificationView;

import java.util.ArrayList;
import java.util.List;

public class ClassificationPresenterImpl implements IClassificationPresenter {
    private static final String TAG = ClassificationPresenterImpl.class.getSimpleName();
    private IClassificationView classificationView;

    private static final String KEY_CLASSIFICATIONS = "classifications";

    @Override
    public void getClassifications() {
        new Thread(() -> {
//            List<Classification> classifications = SharedPreferencesUtils.getInstance().getObj(KEY_CLASSIFICATIONS, new TypeToken<List<Classification>>() {});
            //没有数据只能直接造了 （o´・ェ・｀o）
            List<Classification> classifications = getData();
            if (classificationView != null) {
                new Handler().post(() -> classificationView.onClassificationsLoaded(classifications));
            }
        }).run();
    }

    private List<Classification> getData() {
        ArrayList<Classification> classifications = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            classifications.add(new Classification("手机数码 " + i, getClassificationDetail("手机数码")));
            classifications.add(new Classification("电脑办公 " + i, getClassificationDetail("电脑办公")));
            classifications.add(new Classification("食品 " + i, getClassificationDetail("食品")));
        }
        return classifications;
    }

    private List<ClassificationDetail> getClassificationDetail(String title) {
        ArrayList<ClassificationDetail> classificationDetails = new ArrayList<>();
        if ("手机数码".equals(title)){
            getPhone(classificationDetails);
        }
        if ("电脑办公".equals(title)) {
            getComputer(classificationDetails);
        }
        if ("食品".equals(title)) {
            getFood(classificationDetails);
        }
        return classificationDetails;
    }

    private void getFood(ArrayList<ClassificationDetail> classificationDetails) {
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "零食大礼包"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "休闲零食"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "膨化食品"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "坚果炒货"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "肉干"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "蜜饯果干"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "巧克力"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "饼干蛋糕"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "月饼"));
    }

    private void getComputer(ArrayList<ClassificationDetail> classificationDetails) {
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "设计师电脑"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "游戏本"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "轻薄本"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "游戏台式机"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "机械键盘"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "移动硬盘"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "曲屏显示器"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "组装电脑"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "显卡"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "打印机"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "投影仪"));
    }

    private void getPhone(ArrayList<ClassificationDetail> classificationDetails) {
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "苹果手机"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "华为手机"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "荣耀手机"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "小米手机"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "vivo手机"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "三星手机"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "oppo手机"));
        classificationDetails.add(new ClassificationDetail(R.mipmap.placeholder, "一加手机"));
    }

    @Override
    public void registerView(IClassificationView view) {
        this.classificationView = view;
    }

    @Override
    public void unregisterView(IClassificationView view) {
        this.classificationView = null;
    }
}
