package cn.yzl.imageselector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;


/**
 * Created by YZL on 2017/2/22.
 */
public class IamgeSelectUtils {

    /**
     * 选择照片请求码
     */
    private static final int REQUEST_CAPTURE = 900;

    private static final int REQUEST_PICK_IMAGE = 901;
    /**
     * 剪切照片请求码
     */
    private static final int CROP_PTHOTO_REQUEST_CODE = 902;

    private final Context context;

    private Object obj;
    private Uri tempUri;//原图保存地址
    private String tempPath;
    private String targetPath;
    /**
     * 标记是否拍照,如果是的话,后面要删除 临时文件
     */
    private boolean isClickCamera;

    public IamgeSelectUtils(Context context, Activity obj) {
        this.obj = obj;
        this.context = context;
    }

    public IamgeSelectUtils(Context context, Fragment obj) {
        this.obj = obj;
        this.context = context;
    }


    public void openCaram() {
        targetPath = null;
        File file = new FileStorage().createFile();
        tempPath = file.getAbsolutePath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            tempUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } else {
            tempUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);//将拍取的照片保存到指定URI
        startIntent(intent, REQUEST_CAPTURE);
    }

    public void openAlum() {
        targetPath = null;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startIntent(intent, REQUEST_PICK_IMAGE);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        tempPath = null;
        tempUri = data.getData();
        if (DocumentsContract.isDocumentUri(context, tempUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(tempUri);
            if ("com.android.providers.media.documents".equals(tempUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                tempPath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(tempUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                tempPath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(tempUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            tempPath = getImagePath(tempUri, null);
        } else if ("file".equalsIgnoreCase(tempUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            tempPath = tempUri.getPath();
        }
        cropPhotoZoom();
    }

    /**
     * 根据URI 获取文件绝对路径
     *
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void startIntent(Intent intent, int code) {
        if (obj instanceof Fragment) {
            ((Fragment) obj).startActivityForResult(intent, code);
        }
        if (obj instanceof Activity) {
            ((Activity) obj).startActivityForResult(intent, code);
        }
    }

    /**
     * 修剪照片
     */
    public void cropPhotoZoom() {

        File file = new FileStorage().createFile();
        targetPath = file.getAbsolutePath();
        Uri outputUri = null;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //通过FileProvider创建一个content类型的Uri
//            outputUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
//        } else {
        outputUri = Uri.fromFile(file);
//        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(tempUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        startIntent(intent, CROP_PTHOTO_REQUEST_CODE);
    }


    private void handleImageBeforeKitKat(Intent intent) {
        tempUri = intent.getData();
        tempPath = getImagePath(tempUri, null);
        cropPhotoZoom();
    }


    public String onResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE://从相册选择
                    isClickCamera = false;
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                    break;
                case REQUEST_CAPTURE://拍照
                    isClickCamera = true;
                    if (resultCode == Activity.RESULT_OK) {
                        cropPhotoZoom();
                    }
                    break;
                case CROP_PTHOTO_REQUEST_CODE://裁剪完成
                    deleteCameraTempImg();
                    return targetPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 删除临时文件
     */
    private void deleteCameraTempImg() {
        if (isClickCamera) {
            if (!TextUtils.isEmpty(tempPath)) {
                File file = new File(tempPath);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }
}
