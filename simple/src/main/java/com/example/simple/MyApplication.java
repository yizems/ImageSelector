package com.example.simple;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import cn.yzl.imgpicker.ImgPicker;
import cn.yzl.imgpicker.ImgPickerOption;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by YZL on 2018/1/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String s = getCacheDir().getAbsolutePath() + "/aImageSelector";
        Log.e("sss", s);
        ImgPicker.setDEFAULT_OPTION(new ImgPickerOption()
                .setCrop(true).setRootDir(s));
        ImgPicker.setDefaultFailCallBack(new Function1<Exception, Unit>() {
            @Override
            public Unit invoke(Exception e) {
                e.printStackTrace();
                Toast.makeText(MyApplication.this, "出错", Toast.LENGTH_LONG).show();
                return null;
            }
        });
    }
}
