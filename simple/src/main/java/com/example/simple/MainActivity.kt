package com.example.simple

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import cn.yzl.imgpicker.ImgPicker
import cn.yzl.imgpicker.PickerResult
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    lateinit var rxImagePicker: ImgPicker
    var iv: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iv = findViewById(R.id.iv)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 100
            )
        }
        rxImagePicker = ImgPicker(
                this,
                crop = true,
                rootDir = this.cacheDir.absolutePath)
                .success {
                    Glide.with(this@MainActivity)
                            .load(it!!.uri)
                            .into(iv)
                }
        findViewById<View>(R.id.bt_camera)
                .setOnClickListener { rxImagePicker.openCamera() }
        findViewById<View>(R.id.bt_album)
                .setOnClickListener { rxImagePicker.openAlbum() }
    }
}