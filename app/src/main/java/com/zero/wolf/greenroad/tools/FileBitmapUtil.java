package com.zero.wolf.greenroad.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/11.
 */

public class FileBitmapUtil {

    public FileBitmapUtil() {

    }

    public static Bitmap getBitmap(String path) {
        Logger.i(path);
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {
                    InputStream inputStream = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveJPGFile(Bitmap bm, String fileName, String path)
            throws IOException {

        File dirFile = new File(path);
        // 文件夹不存在则创建文件夹
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        Log.v("保存文件函数", "创建文件夹成功");
        File myCaptureFile = new File(path + "/" + fileName);
        Log.v("保存文件函数", "文件路径");

        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            Log.v("保存文件函数", "文件流");
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            Log.v("保存文件函数", "保存成功");
            bos.flush();
            bos.close();
            if (bm.isRecycled() == false) {
                bm.recycle();
                Log.v("Util", "回收bitmap");
            }
        } catch (Exception e) {

        }
    }


    /**
     * 根据全路径获取文件名
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isSpace(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }
}
