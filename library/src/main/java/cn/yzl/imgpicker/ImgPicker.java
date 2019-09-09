package cn.yzl.imgpicker;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by YZL on 2018/1/4.
 */
public class ImgPicker {

    static final String TAG = "RX_IMG_PICKER_YZL";

    static FailCallBack defaultFailCallBack;

    public static ImgPickerOption DEFAULT_OPTION = new ImgPickerOption()
            .setCrop(true)
            .setFreeRatio(false)
            .setRootDir(FileStorage.getDefultDir())
            .setRatio(1, 1);

    ImgPickerFragment mRxImgPickerFragment;

    public ImgPicker(FragmentActivity activity) {
        if (DEFAULT_OPTION == null) {
            throw new NullPointerException("默认配置不能为null");
        }
        init(activity, DEFAULT_OPTION);
    }

    public ImgPicker(FragmentActivity activity, ImgPickerOption option) {
        init(activity, option);
    }

    private void init(FragmentActivity activity, ImgPickerOption option) {
        mRxImgPickerFragment = getRxPermissionsFragment(activity);
        mRxImgPickerFragment.setOption(option);
        mRxImgPickerFragment.setFailCallBack(defaultFailCallBack);
    }

    private ImgPickerFragment getRxPermissionsFragment(FragmentActivity activity) {
        ImgPickerFragment rxImgPickerFragment = findRxImgPickerFragment(activity);
        boolean isNewInstance = rxImgPickerFragment == null;
        if (isNewInstance) {
            rxImgPickerFragment = new ImgPickerFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(rxImgPickerFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return rxImgPickerFragment;
    }

    private ImgPickerFragment findRxImgPickerFragment(FragmentActivity activity) {
        return (ImgPickerFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    public static void setDefaultOption(ImgPickerOption defaultOption) {
        DEFAULT_OPTION = defaultOption;
    }

    public void openAlbum() {
        mRxImgPickerFragment.openAlum();
    }

    public void openCamera() {
        mRxImgPickerFragment.openCaram();
    }

    public ImgPicker success(SucessCallBack sucessCallback) {
        mRxImgPickerFragment.setSucessCallBack(sucessCallback);
        return this;
    }

    public ImgPicker fail(FailCallBack failCallBack) {
        mRxImgPickerFragment.setFailCallBack(failCallBack);
        return this;
    }

    public static void setDefaultFailCallBack(FailCallBack failCallBack) {
        defaultFailCallBack = failCallBack;
    }

    public interface SucessCallBack {
        void sucess(PickerResult ret);
    }

    public interface FailCallBack {
        void error(Exception e);
    }
}
