package com.yiwangrencai.ywkj.tools;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/4/8.
 */

public class FileUtils {
    public static final String CACHE = "cache";
    public static final String ICON = "icon";
    public static final String ROOT = "yiwangkeji";
    /**
     * 获取图片的缓存的路径
     * @return
     */
    public static File getIconDir(){
        return getDir(ICON);

    }
    /**
     * 获取缓存路径
     * @return
     */
    public static File getCacheDir() {
        return getDir(CACHE);
    }
    public static File getDir(String cache) {
        StringBuilder path = new StringBuilder();
        if (isSDAvailable()) {
            path.append(Environment.getExternalStorageDirectory()
                    .getAbsolutePath());// /storage/emulated/0
            path.append(File.separator);// '/'/storage/emulated/0/
            path.append(ROOT);// /storage/emulated/0/yiwangkeji
            path.append(File.separator);// /storage/emulated/0/yiwangkeji/
            path.append(cache);// /storage/emulated/0/yiwangkeji/cache

        }else{
            File filesDir = UiUtils.getContext().getCacheDir();    //  cache  getFileDir file
            path.append(filesDir.getAbsolutePath());// /data/data/com.yiwangrencai.ywkj.YiWangKeJi/cache
            path.append(File.separator);///data/data/com.yiwangrencai.ywkj.YiWangKeJi/cache/
            path.append(cache);///data/data/com.yiwangrencai.ywkj.YiWangKeJi/cache/cache
        }

        File file = new File(path.toString());
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();// 创建文件夹
        }

        return file;

    }

    private static boolean isSDAvailable() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }



}
