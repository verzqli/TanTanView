package com.example.vezqli.tantan;

/**
 * Created by vezqli on 2017/1/8.
 */

public class SwipeBean {
    private int imageUrl;
    private String name;

    public SwipeBean(int imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
