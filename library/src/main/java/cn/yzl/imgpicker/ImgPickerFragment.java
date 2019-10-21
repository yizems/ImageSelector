package cn.yzl.imgpicker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.List;

/**
 * Created by YZL on 2018/1/4.
 */
public class ImgPickerFragment extends Fragment {
    /**
     * 选择照片请求码
     */
    private static final int REQUEST_CAPTURE = 900;

    private static final int REQUEST_PICK_IMAGE = 901;
    /**
     * 剪切照片请求码
     */
    private static final int CROP_PTHOTO_REQUEST_CODE = 902;

    private FileStorage fileStorage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private Uri tempUri;//原图保存地址
    private String tempPath;
    private Uri targetPath;
    /**
     * 标记是否拍照,如果是的话,后面要删除 临时文件
     */
    private boolean isClickCamera;

    private ImgPickerOption option;

    private ImgPicker.SucessCallBack sucessCallBack;

    private ImgPicker.FailCallBack failCallBack;

    public void setSucessCallBack(ImgPicker.SucessCallBack sucessCallBack) {
        this.sucessCallBack = sucessCallBack;
    }

    public void setFailCallBack(ImgPicker.FailCallBack failCallBack) {
        this.failCallBack = failCallBack;
    }

    public void setOption(ImgPickerOption option) {
        this.option = option;
        fileStorage = new FileStorage(option.getRootDir());
    }

    public void openCaram() {
        try {
            File file = fileStorage.createFile();
            tempPath = file.getAbsolutePath();
            tempUri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".fileprovider", file);
            Intent intent = new Intent();

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);//将拍取的照片保存到指定URI
            List<ResolveInfo> resInfoList = getContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getContext().grantUriPermission(packageName, tempUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivityForResult(intent, REQUEST_CAPTURE);
        } catch (Exception e) {
            e.printStackTrace();
            if (failCallBack != null) {
                failCallBack.error(e);
            }
        }
    }

    public void openAlum() {
        try {
            targetPath = null;
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
            if (failCallBack != null) {
                failCallBack.error(e);
            }
        }
    }

    /**
     * 修剪照片
     */
    public void cropPhotoZoom() {
        try {
            if (!option.getCrop()) {
                callSuccess(tempUri);
                return;
            }

            File file = fileStorage.createFile();
            targetPath = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", file);


            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(tempUri, "image/*");
            intent.putExtra("crop", "true");
            if (option.isFreeRatio()) {
                intent.putExtra("aspectX", 0.1f);
                intent.putExtra("aspectY", 0.1f);
            } else {
                intent.putExtra("aspectX", option.getxRatio());
                intent.putExtra("aspectY", option.getyRatio());
            }
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, targetPath);
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.PNG.toString());
            intent.putExtra("noFaceDetection", true);

            List<ResolveInfo> resInfoList = getContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getContext().grantUriPermission(packageName, tempUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getContext().grantUriPermission(packageName, targetPath, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            startActivityForResult(intent, CROP_PTHOTO_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            if (failCallBack != null) {
                failCallBack.error(e);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        try {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE://从相册选择
                    isClickCamera = false;
                    tempUri = data.getData();
                    cropPhotoZoom();
                    break;
                case REQUEST_CAPTURE://拍照
                    isClickCamera = true;
                    cropPhotoZoom();
                    break;
                case CROP_PTHOTO_REQUEST_CODE://裁剪完成
                    callSuccess(targetPath);
                    deleteCameraTempImg();
            }
        } catch (Exception e) {
            if (failCallBack != null) {
                failCallBack.error(e);
            } else {
                e.printStackTrace();
            }
        }
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

    /**
     * 异步删除所有文件,包括选择的文件和根文件夹,
     * 如果是相机拍摄的照片,会在裁剪后自动删除,无需调用该方法
     */
    public void clearIamges() {
        new HandlerThread("delimgs") {
            @Override
            protected void onLooperPrepared() {
                fileStorage.clearFile();
            }
        }.start();
    }


    private void callSuccess(Uri uri) {
        if (sucessCallBack != null) {
            PickerResult ret = new PickerResult();
            ret.uri = uri;
            Cursor cursor = getActivity().getContentResolver()
                    .query(uri,
                            new String[]{MediaStore.MediaColumns.DISPLAY_NAME,
                                    MediaStore.MediaColumns.SIZE},
                            null, null, null
                    );
            cursor.moveToFirst();

            ret.fileName = cursor.getString(0);
            ret.size = cursor.getLong(1);
            cursor.close();
            sucessCallBack.sucess(ret);
        }
    }

}
