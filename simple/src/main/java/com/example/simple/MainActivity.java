package com.example.simple;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.yzl.imgpicker.ImgPicker;
import cn.yzl.imgpicker.PickerResult;

public class MainActivity extends AppCompatActivity {

    ImgPicker rxImagePicker;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        rxImagePicker = new ImgPicker(this,ImgPicker.DEFAULT_OPTION.clone().setCrop(true))
                .success(new ImgPicker.SucessCallBack() {

                    @Override
                    public void sucess(PickerResult ret) {
                        Glide.with(MainActivity.this)
                                .load(ret.uri)
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
