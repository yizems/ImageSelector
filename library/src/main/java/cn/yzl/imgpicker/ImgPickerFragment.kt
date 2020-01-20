package cn.yzl.imgpicker

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.HandlerThread
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

/**
 * Created by YZL on 2018/1/4.
 */
class ImgPickerFragment : Fragment() {

    private lateinit var fileStorage: FileStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    private var tempUri //原图保存地址
            : Uri? = null
    private var tempPath: String? = null
    private var targetPath: Uri? = null
    /**
     * 标记是否拍照,如果是的话,后面要删除 临时文件
     */
    private var isClickCamera = false
    private lateinit var option: ImgPickerOption
    private var sucessCallBack: SuccessCallback? = null
    private var failCallBack: FailCallback? = null
    fun setSucessCallBack(sucessCallBack: SuccessCallback?) {
        this.sucessCallBack = sucessCallBack
    }

    fun setFailCallBack(failCallBack: FailCallback?) {
        this.failCallBack = failCallBack
    }

    fun setOption(option: ImgPickerOption) {
        this.option = option
        fileStorage = FileStorage(option.rootDir)
    }

    fun openCaram() {
        try {
            val file = fileStorage.createFile()

            tempPath = file.absolutePath
            tempUri =
                    FileProvider.getUriForFile(context!!, activity!!.packageName + ".fileprovider", file)
            val intent = Intent()
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE //设置Action为拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri) //将拍取的照片保存到指定URI
            val resInfoList =
                    context!!.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context!!.grantUriPermission(packageName, tempUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(intent, REQUEST_CAPTURE)
        } catch (e: Exception) {
            e.printStackTrace()
            failCallBack?.invoke(e)
        }
    }

    fun openAlum() {
        try {
            targetPath = null
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, REQUEST_PICK_IMAGE)
        } catch (e: Exception) {
            e.printStackTrace()
            failCallBack?.invoke(e)
        }
    }

    /**
     * 修剪照片
     */
    private fun cropPhotoZoom() {
        try {
            if (!option.crop) {
                callSuccess(tempUri)
                return
            }
            val file = fileStorage.createFile()
            targetPath =
                    FileProvider.getUriForFile(context!!, context!!.packageName + ".fileprovider", file)
            val intent = Intent("com.android.camera.action.CROP")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.setDataAndType(tempUri, "image/*")
            intent.putExtra("crop", "true")
            if (option.isFreeRatio) {
                intent.putExtra("aspectX", 0.1f)
                intent.putExtra("aspectY", 0.1f)
            } else {
                intent.putExtra("aspectX", option.getxRatio())
                intent.putExtra("aspectY", option.getyRatio())
            }
            intent.putExtra("scale", true)
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, targetPath)
            intent.putExtra(
                    "outputFormat",
                    Bitmap.CompressFormat.PNG.toString()
            )
            intent.putExtra("noFaceDetection", true)
            val resInfoList =
                    context!!.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                context!!.grantUriPermission(packageName, tempUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context!!.grantUriPermission(packageName, targetPath, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(intent, CROP_PTHOTO_REQUEST_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
            failCallBack?.invoke(e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        try {
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    isClickCamera = false
                    tempUri = data!!.data
                    cropPhotoZoom()
                }
                REQUEST_CAPTURE -> {
                    isClickCamera = true
                    cropPhotoZoom()
                }
                CROP_PTHOTO_REQUEST_CODE -> {
                    callSuccess(targetPath)
                    deleteCameraTempImg()
                }
            }
        } catch (e: Exception) {
            failCallBack?.invoke(e)
            e.printStackTrace()
        }
    }

    /**
     * 删除临时文件
     */
    private fun deleteCameraTempImg() {
        if (isClickCamera) {
            if (!tempPath.isNullOrBlank()) {
                val file = File(tempPath!!)
                if (file.exists()) {
                    file.delete()
                }
            }
        }
    }

    /**
     * 异步删除所有文件,包括选择的文件和根文件夹,
     * 如果是相机拍摄的照片,会在裁剪后自动删除,无需调用该方法
     */
    fun clearIamges() {
        object : HandlerThread("delimgs") {
            override fun onLooperPrepared() {
                fileStorage.clearFile()
            }
        }.start()
    }

    private fun callSuccess(uri: Uri?) {
        if (sucessCallBack != null) {
            val ret = PickerResult()
            ret.uri = uri
            val cursor = activity!!.contentResolver
                    .query(
                            uri!!, arrayOf(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            MediaStore.MediaColumns.SIZE
                    ),
                            null, null, null
                    )
            cursor!!.moveToFirst()
            ret.fileName = cursor.getString(0)
            ret.size = cursor.getLong(1)
            cursor.close()
            sucessCallBack?.invoke(ret)
        }
    }

    companion object {
        /**
         * 选择照片请求码
         */
        private const val REQUEST_CAPTURE = 900
        private const val REQUEST_PICK_IMAGE = 901
        /**
         * 剪切照片请求码
         */
        private const val CROP_PTHOTO_REQUEST_CODE = 902
    }
}