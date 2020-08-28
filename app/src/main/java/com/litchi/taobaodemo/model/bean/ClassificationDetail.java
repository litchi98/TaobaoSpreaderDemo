package com.litchi.taobaodemo.model.bean;

public class ClassificationDetail {
    private int imageResourceId;
    private String description;

    public ClassificationDetail(int imageResourceId, String description) {
        this.imageResourceId = imageResourceId;
        this.description = description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
