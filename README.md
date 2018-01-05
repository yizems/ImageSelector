# 相册选择工具

## 1 实现功能

- 主要针对单张照片做处理
- 拍照或者选择一张照片到程序中,可以裁剪
- 适配Android 7




## 1.5 更新日志

### 4.0 版本 [![](https://jitpack.io/v/yizeliang/ImageSelector.svg)](https://jitpack.io/#yizeliang/ImageSelector)

使用看simple


参考

https://github.com/tbruyelle/RxPermissions



## 2 使用


### 2.1 在项目中添加fileprovider

```xml
 <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="包名.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/filepaths" />
        </provider>

```

### 2.2 Application中初始化

` ImageSelectUtil.init(BuildConfig.APPLICATION_ID); `

### 2.3 构建对象

````java

 ImageSelectUtil imageSelectUtils = new ImageSelectUtil.Build(this)
                 //临时图片保存位置
                .setRootDir("dir")
                //是否裁剪
                .setCrop(true)//.setCrop(true,1,2) 裁剪比例
		.freeRatio() //自由比例,上面设置的比例会失效
                //结果接收
                .setCallBack(new ImageSelectCallBack() {
                    @Override
                    public void sucess(String s) {
                        Logger.e(s);
                        LoadLocalImageUtil.displayFromSDCard(s, ivUser);
                    }

                    @Override
                    public void error(Exception e) {
                        e.printStackTrace();
                    }
                }).build();
````

### 2.4 使用
````java
    //打开相机选择
    imageSelectUtils.openCaram();
    //打开相册选择
    imageSelectUtils.openAlum();

    //覆盖方法,由ImageSelectUtils处理照片选择结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageSelectUtils.onResult(requestCode, resultCode, data);
    }
    
    //删除产生的所有文件,包括目录和需要的文件,异步方式
    //如果只是想删除拍照产生的照片,那么无需调用本方法,在裁剪后会自动删除,如果没有裁剪,则不删除
    imageSelectUtils.clearImgs();
````
## 依赖
```gradle

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
        
        dependencies {
	        compile 'com.github.yizeliang:ImageSelector:3.2'
	}

```
