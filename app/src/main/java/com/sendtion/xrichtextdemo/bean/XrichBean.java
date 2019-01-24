package com.sendtion.xrichtextdemo.bean;

public class XrichBean {

    private String text;
    private String imgUrl;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "XrichBean{" +
                "text='" + text + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
