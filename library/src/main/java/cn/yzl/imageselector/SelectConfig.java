package cn.yzl.imageselector;

import android.content.Context;

/**
 * Created by YZL on 2017/2/22.
 */

public class SelectConfig {
    private String rootDir;
    private boolean crop;
    private Object object;
    private int xRatio;
    private int yRatio;


    private ImageSelectCallBack callBack;

    public SelectConfig(String rootDir, boolean crop, int xRatio, int yRatio, Object object, ImageSelectCallBack callBack) {
        this.rootDir = rootDir;
        this.crop = crop;
        this.object = object;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
        this.callBack = callBack;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public boolean getCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getxRatio() {
        return xRatio;
    }

    public void setxRatio(int xRatio) {
        this.xRatio = xRatio;
    }

    public int getyRatio() {
        return yRatio;
    }

    public void setyRatio(int yRatio) {
        this.yRatio = yRatio;
    }

    public ImageSelectCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(ImageSelectCallBack callBack) {
        this.callBack = callBack;
    }
}
