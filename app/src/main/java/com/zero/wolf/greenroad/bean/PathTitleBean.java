package com.zero.wolf.greenroad.bean;

/**
 * Created by Administrator on 2017/8/23.
 */

public class PathTitleBean {
    String path;
    String title;

    public PathTitleBean(String path, String title) {
        this.path = path;
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PathTitleBean{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
