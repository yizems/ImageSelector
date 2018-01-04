package com.example.simple;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.logging.Logger;

import cn.yzl.imageselector.ImageSelectUtil;

/**
 * Created by YZL on 2018/1/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String s = Environment.getExternalStorageDirectory() + "/aImageSelector";
        Log.e("sss", s);
        ImageSelectUtil.init(getPackageName(), s);
    }
}
