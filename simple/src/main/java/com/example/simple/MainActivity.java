package com.example.simple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.yzl.imageselector.ImageSelectCallBack;
import cn.yzl.imageselector.ImageSelectUtil;

public class MainActivity extends AppCompatActivity {

    ImageSelectUtil imageSelectUtil;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        imageSelectUtil = new ImageSelectUtil.Build(this)
                .setCallBack(new ImageSelectCallBack() {
                    @Override
                    public void sucess(String path) {
                        Log.e("ssss", path);
                        File file = new File(path);
                        if (file.exists()) {
                            Glide.with(MainActivity.this)
                                    .load(file)
                                    .into(iv);
                        } else {
                            Log.e("eee", "文件不存在");
                        }
                    }

                    @Override
                    public void error(Exception e) {

                    }
                })
                .setCrop(false)
                .build();
        findViewById(R.id.bt_camera)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageSelectUtil.openCaram();
                    }
                });

        findViewById(R.id.bt_album)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageSelectUtil.openAlum();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSelectUtil.onResult(requestCode, resultCode, data);
    }
}
