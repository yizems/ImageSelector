package cn.yzl.rx.imgpicker;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.UUID;

/**
 * Created by Fsh on 2016/12/28.
 */
class FileStorage {

    public static String rootDir;

    public FileStorage(String dir) {
        rootDir = dir;
        createRootDir();
    }


    public void createRootDir() {
        if (rootDir != null) {
            File rootDirFile = new File(rootDir);
            if (!rootDirFile.exists()) {
                rootDirFile.mkdirs();
            }
        }
    }

    public File createFile() {
        String fileName = "";
        if (rootDir != null) {
            fileName = UUID.randomUUID().toString() + ".png";
        }
        return new File(rootDir, fileName);
    }

    public void clearFile() {
        deleteFolderFile(rootDir, false);
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @return
     */
    private static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDefultDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ImgPicker";
    }

}
