package com.example.simple;

import android.app.Application;
import android.os.Environment;

import cn.yzl.imageselector.ImageSelectUtil;

/**
 * Created by YZL on 2018/1/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageSelectUtil.init(getPackageName(), Environment.getExternalStorageDirectory()+"/aImageSelector");
    }
}
