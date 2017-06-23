package com.zero.wolf.greenroad.tools;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

/**
 * Created by Administrator on 2017/6/21.
 */

public class SDcardSpace {

    public Context mContext;
    public  String path = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    public  SDcardSpace mSDcardSpace;

    public SDcardSpace(Context context) {
        mContext = context;
    }

 /*   public  SDcardSpace getInstance() {
        if (mSDcardSpace == null) {
            mSDcardSpace = new SDcardSpace();
        }
        return mSDcardSpace;
    }*/

    public String getAvailSpace() {
        StatFs statFs = new StatFs(path);
        long blockSize;
        long totalBlocks;
        long availableBlocks;

        // 由于API18（Android4.3）以后getBlockSize过时并且改为了getBlockSizeLong
// 因此这里需要根据版本号来使用那一套API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            blockSize = statFs.getBlockSizeLong();
            totalBlocks = statFs.getBlockCountLong();
            availableBlocks = statFs.getAvailableBlocksLong();
        }
        else {
            blockSize = statFs.getBlockSize();
            totalBlocks = statFs.getBlockCount();
            availableBlocks = statFs.getAvailableBlocks();
        }

        long result = blockSize * availableBlocks;
        String  availableSize = Formatter.formatFileSize(mContext, result);
        return availableSize;
    }

}
