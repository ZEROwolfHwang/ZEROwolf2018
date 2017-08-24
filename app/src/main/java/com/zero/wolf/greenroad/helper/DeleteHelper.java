package com.zero.wolf.greenroad.helper;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.PreviewItemAdapter;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class DeleteHelper {
    /**
     * 清除所有的记录
     * @param context
     * @param typeDraftLite
     * @param adapter
     */
    public static void deleteAllInfos(Context context, String typeDraftLite, PreviewItemAdapter adapter) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("清空本地保存的拍摄数据");
        dialog.setMessage("点击“确定”将删除所有拍摄记录" + "\"" +
                "点击“取消”将取消删除操作");
        dialog.setCancelable(false);
        dialog.setPositiveButton(context.getString(R.string.dialog_messge_OK), (dialog1, which) -> {

            DataSupport.deleteAll(SupportDraftOrSubmit.class, GlobalManager.LITE_CONDITION, typeDraftLite);
          /*  if (mGoodsFile == null) {
                mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                mGoodsFile = new File(mFilePath, "GreenShoot");
                mGoodsFile.mkdirs();
            }
            mGoodsFilePath = mGoodsFile.getPath();

            FileUtils.deleteJpg(new File(mGoodsFilePath));*/
            //顺便将计数清空，进行重新计数
            SPUtils.cancel_count(context, SPUtils.CAR_COUNT);
            SPUtils.cancel_count(context, SPUtils.CAR_NOT_COUNT);

            List<SupportDraftOrSubmit> supportList =
                    DataSupport.where("lite_type=?", typeDraftLite).find(SupportDraftOrSubmit.class);
            adapter.updateListView(supportList);
        });
        dialog.setNegativeButton(context.getString(R.string.dialog_message_Cancel), (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.show();
    }
}
