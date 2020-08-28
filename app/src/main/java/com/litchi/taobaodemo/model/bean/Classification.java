package com.litchi.taobaodemo.model.bean;

import java.util.List;

public class Classification {
    private String title;
    private List<ClassificationDetail> classificationDetailList;

    public Classification(String title, List<ClassificationDetail> classificationDetailList) {
        this.title = title;
        this.classificationDetailList = classificationDetailList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ClassificationDetail> getClassificationDetailList() {
        return classificationDetailList;
    }

    public void setClassificationDetailList(List<ClassificationDetail> classificationDetailList) {
        this.classificationDetailList = classificationDetailList;
    }
}
