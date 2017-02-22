package cn.yzl.imageselector;

import android.content.Context;

/**
 * Created by YZL on 2017/2/22.
 */

public class SelectConfig {
    private String rootDir;
    private boolean crop;
    private Object object;


    public SelectConfig(String rootDir, boolean crop, Object object, ImageSelectCallBack callBack) {
        this.rootDir = rootDir;
        this.crop = crop;
        this.object = object;
        this.callBack = callBack;
    }

    private ImageSelectCallBack callBack;


    public ImageSelectCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(ImageSelectCallBack callBack) {
        this.callBack = callBack;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
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
}
