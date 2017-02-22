package cn.yzl.imageselector;

/**
 * Created by YZL on 2017/2/22.
 */

public interface ImageSelectCallBack {
    void sucess(String path);
    void error(Exception e);
}
