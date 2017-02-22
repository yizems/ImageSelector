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
                .setRootDir("attend")
                .setCanCrop(true)
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



                    imageSelectUtils.openCaram();
                    imageSelectUtils.openAlum();


@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageSelectUtils.onResult(requestCode, resultCode, data);
    }

```

