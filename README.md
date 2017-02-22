# 相册选择工具

## 实现功能

- 主要针对单张照片做处理
- 拍照或者选择一张照片到程序中,可以裁剪
- 适配Android 7
- Android 4.4 以下也可以使用

## 使用

如果项目中也有定义fileprovider的话
需要在清单文件中加入一下代码

```xml
<application
        tools:replace="authorities"
        />

```

```java

 ImageSelectUtil imageSelectUtils = new ImageSelectUtil.Build(this)
                 //临时图片保存位置
                .setRootDir("dir")
                //是否裁剪
                .setCrop(true)
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


    //打开相机选择
    imageSelectUtils.openCaram();
    //打开相册选择
    imageSelectUtils.openAlum();


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageSelectUtils.onResult(requestCode, resultCode, data);
    }
    
    //删除产生的所有文件,包括目录和需要的文件,异步方式
    //如果只是想删除拍照产生的照片,那么无需调用本方法,在裁剪后会自动删除,如果没有裁剪,则不删除
    imageSelectUtils.clearImgs();
```
## 依赖
```gradle

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
        
        dependencies {
	        compile 'com.github.yizeliang:ImageSelector:2.2'
	}

```
