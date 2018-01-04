package cn.yzl.imgpicker;

/**
 * Created by YZL on 2017/2/22.
 */

public class ImgPickerOption implements Cloneable{
    private String rootDir;
    private boolean crop;
    private int xRatio;
    private int yRatio;

    private boolean freeRatio;

    public ImgPickerOption() {

    }

    public ImgPickerOption(String rootDir, boolean crop, int xRatio, int yRatio, boolean freeRatio) {
        this.rootDir = rootDir;
        this.crop = crop;
        this.xRatio = xRatio;
        this.yRatio = yRatio;
        this.freeRatio = freeRatio;
    }

    public String getRootDir() {
        return rootDir;
    }

    public ImgPickerOption setRootDir(String rootDir) {
        this.rootDir = rootDir;
        return this;
    }

    public boolean getCrop() {
        return crop;
    }

    public ImgPickerOption setCrop(boolean crop) {
        this.crop = crop;
        return this;
    }


    public int getxRatio() {
        return xRatio;
    }

    public ImgPickerOption setxRatio(int xRatio) {
        this.xRatio = xRatio;
        return this;
    }

    public int getyRatio() {
        return yRatio;
    }

    public ImgPickerOption setyRatio(int yRatio) {
        this.yRatio = yRatio;
        return this;
    }

    public ImgPickerOption setRatio(int xRatio, int yRatio) {
        this.xRatio = xRatio;
        this.yRatio = yRatio;
        return this;
    }

    public boolean isFreeRatio() {
        return freeRatio;
    }

    public ImgPickerOption setFreeRatio(boolean freeRatio) {
        this.freeRatio = freeRatio;
        return this;
    }

    @Override
    public ImgPickerOption clone(){
        return new ImgPickerOption(rootDir,crop,xRatio,yRatio,freeRatio);
    }
}
