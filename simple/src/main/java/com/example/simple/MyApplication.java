package com.example.simple;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import cn.yzl.imgpicker.ImgPicker;
import cn.yzl.imgpicker.ImgPickerOption;

/**
 * Created by YZL on 2018/1/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String s = Environment.getExternalStorageDirectory() + "/aImageSelector";
        Log.e("sss", s);
        ImgPicker.setDefaultOption(new ImgPickerOption()
                .setCrop(true).setRootDir(s));
        ImgPicker.setDefaultFailCallBack(new ImgPicker.FailCallBack() {
            @Override
            public void error(Exception e) {
                e.printStackTrace();
                Toast.makeText(MyApplication.this, "出错", Toast.LENGTH_LONG).show();
            }
        });
    }
}
