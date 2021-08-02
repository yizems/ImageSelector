package cn.yzl.imgpicker

import androidx.fragment.app.FragmentActivity

/**
 * Created by YZL on 2018/1/4.
 */
@Suppress("ConvertSecondaryConstructorToPrimary")
class ImgPicker {
    var mRxImgPickerFragment: ImgPickerFragment? = null

    constructor(
            activity: FragmentActivity,
            rootDir: String = DEFAULT_OPTION.rootDir,
            crop: Boolean = DEFAULT_OPTION.crop,
            xRatio: Int = DEFAULT_OPTION.getxRatio(),
            yRatio: Int = DEFAULT_OPTION.getyRatio(),
            freeRatio: Boolean = DEFAULT_OPTION.isFreeRatio

    ) {
        init(activity, ImgPickerOption(rootDir, crop, xRatio, yRatio, freeRatio))
    }

    private fun init(activity: FragmentActivity, option: ImgPickerOption) {
        mRxImgPickerFragment = getRxPermissionsFragment(activity)
        mRxImgPickerFragment!!.setOption(option)
        mRxImgPickerFragment!!.setFailCallBack(defaultFailCallBack)
    }

    private fun getRxPermissionsFragment(activity: FragmentActivity): ImgPickerFragment? {
        var rxImgPickerFragment = findRxImgPickerFragment(activity)
        val isNewInstance = rxImgPickerFragment == null
        if (isNewInstance) {
            rxImgPickerFragment = ImgPickerFragment()
            val fragmentManager =
                    activity.supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .add(rxImgPickerFragment, TAG)
                    .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return rxImgPickerFragment
    }

    private fun findRxImgPickerFragment(activity: FragmentActivity): ImgPickerFragment? {
        return activity.supportFragmentManager.findFragmentByTag(TAG) as ImgPickerFragment?
    }

    fun openAlbum() {
        mRxImgPickerFragment!!.openAlum()
    }

    fun openCamera() {
        mRxImgPickerFragment!!.openCaram()
    }

    fun success(sucessCallback: SuccessCallback?): ImgPicker {
        mRxImgPickerFragment!!.setSucessCallBack(sucessCallback)
        return this
    }

    fun fail(failCallBack: FailCallback?): ImgPicker {
        mRxImgPickerFragment!!.setFailCallBack(failCallBack)
        return this
    }

    companion object {

        internal const val TAG = "RX_IMG_PICKER_YZL"

        @JvmStatic
        var defaultFailCallBack: FailCallback? = null

        @JvmStatic
        var DEFAULT_OPTION: ImgPickerOption = ImgPickerOption()
                .setCrop(true)
                .setFreeRatio(false)
                .setRootDir(FileStorage.getDefultDir())
                .setRatio(1, 1)
    }
}

typealias FailCallback = ((e: Exception?) -> Unit)

typealias SuccessCallback = ((ret: PickerResult) -> Unit)