package com.example.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.yzl.rx.imgpicker.ImgPicker;

public class MainActivity extends AppCompatActivity {

    ImgPicker rxImagePicker;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        rxImagePicker = new ImgPicker(this)
                .success(new ImgPicker.SucessCallBack() {
                    @Override
                    public void sucess(String path) {
                        Glide.with(MainActivity.this)
                                .load(new File(path))
                                .into(iv);
                    }
                });

        findViewById(R.id.bt_camera)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxImagePicker.openCamera();
                    }
                });

        findViewById(R.id.bt_album)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxImagePicker.openAlbum();
                    }
                });
    }

}
