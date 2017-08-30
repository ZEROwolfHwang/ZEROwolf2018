package com.zero.wolf.greenroad.helper;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.PreviewItemAdapter;
import com.zero.wolf.greenroad.litepalbean.SupportChecked;
import com.zero.wolf.greenroad.litepalbean.SupportDetail;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;
import com.zero.wolf.greenroad.litepalbean.SupportMedia;
import com.zero.wolf.greenroad.litepalbean.SupportScan;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.TimeUtil;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class DeleteHelper {
    /**
     * 清除所有的记录
     *
     * @param context
     * @param typeLite
     * @param adapter
     */
    public static void deleteAllInfos(Context context, String typeLite, String SpUtil_info, PreviewItemAdapter adapter) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("清空本地保存的拍摄数据");
        dialog.setMessage("点击“确定”将删除所有拍摄记录" + "\"" +
                "点击“取消”将取消删除操作");
        dialog.setCancelable(false);
        dialog.setPositiveButton(context.getString(R.string.dialog_messge_OK), (dialog1, which) -> {

            DataSupport.deleteAll(SupportDraftOrSubmit.class, GlobalManager.LITE_CONDITION, typeLite);
          /*  if (mGoodsFile == null) {
                mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                mGoodsFile = new File(mFilePath, "GreenShoot");
                mGoodsFile.mkdirs();
            }
            mGoodsFilePath = mGoodsFile.getPath();

            FileUtils.deleteJpg(new File(mGoodsFilePath));*/

            List<SupportDraftOrSubmit> supportList =
                    DataSupport.where(GlobalManager.LITE_CONDITION, typeLite).find(SupportDraftOrSubmit.class);
            adapter.updateListView(supportList);
            SPUtils.putAndApply(context, SpUtil_info, 0);
        });
        dialog.setNegativeButton(context.getString(R.string.dialog_message_Cancel), (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.show();
    }

    /**
     * 清除几天之前的记录
     */
    public static void deleteInfos(Context context, String typeLite, String SpUtil_info, int day, PreviewItemAdapter adapter) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("清除" + day + "天之前本地保存的拍摄数据");
        dialog.setMessage("点击“确定”将删除" + day + "天以前的拍摄记录" + "\"" +
                "点击“取消”将取消删除操作");
        dialog.setCancelable(false);
        dialog.setPositiveButton(context.getString(R.string.dialog_messge_OK), (dialog1, which) -> {
            String currentTimeToDate = TimeUtil.getCurrentTimeToDate();
            List<SupportDraftOrSubmit> photoLiteList = DataSupport.where(GlobalManager.LITE_CONDITION, typeLite).
                    find(SupportDraftOrSubmit.class);

            for (int i = 0; i < photoLiteList.size(); i++) {
                String saveTime = photoLiteList.get(i).getCurrent_time();
                int lite_id = photoLiteList.get(i).getLite_ID();
                int dayGap = TimeUtil.differentDaysByMillisecond(saveTime, currentTimeToDate);
                if (dayGap > day) {
                    DataSupport.deleteAll(SupportDraftOrSubmit.class,"lite_ID = ?", String.valueOf(lite_id));
                    DataSupport.deleteAll(SupportDetail.class,"lite_ID = ?", String.valueOf(lite_id));
                    DataSupport.deleteAll(SupportScan.class,"lite_ID = ?", String.valueOf(lite_id));
                    DataSupport.deleteAll(SupportChecked.class,"lite_ID = ?", String.valueOf(lite_id));
                    DataSupport.deleteAll(SupportMedia.class,"lite_ID = ?", String.valueOf(lite_id));
                    //删除三张本地照片
                    /*FileUtils.deleteJpgPreview(photoLiteList.get(i).getPhotoPath1());
                    FileUtils.deleteJpgPreview(photoLiteList.get(i).getPhotoPath2());
                    FileUtils.deleteJpgPreview(photoLiteList.get(i).getPhotoPath3());
                    */
                }
            }
            List<SupportDraftOrSubmit> supportDraftList = DataSupport.
                    where(GlobalManager.LITE_CONDITION, typeLite).find(SupportDraftOrSubmit.class);

            SortTime sortDraftTime = new SortTime();
            Collections.sort(supportDraftList, sortDraftTime);

            adapter.updateListView(supportDraftList);
            SPUtils.putAndApply(context, SpUtil_info, supportDraftList.size());
        });
        dialog.setNegativeButton(context.getString(R.string.dialog_message_Cancel), (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.show();


    }
}
