package com.occultation.www.dazong;

import com.alibaba.fastjson.JSON;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-09-17 15:08
 */
public class FileInfo {

    private String url;

    private String path;

    private int width;

    private int height;

    private String signature;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
