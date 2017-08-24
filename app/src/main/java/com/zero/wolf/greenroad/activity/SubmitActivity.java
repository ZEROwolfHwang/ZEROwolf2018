package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.PreviewItemAdapter;
import com.zero.wolf.greenroad.adapter.RecycleViewDivider;
import com.zero.wolf.greenroad.helper.DeleteHelper;
import com.zero.wolf.greenroad.helper.SortTime;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;
import com.zero.wolf.greenroad.manager.GlobalManager;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.ImageProcessor;
import com.zero.wolf.greenroad.tools.TimeUtil;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.litepal.crud.DataSupport.findAll;

public class SubmitActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar_draft)
    Toolbar mToolbarPreview;
    @BindView(R.id.recycler_view_preview)
    RecyclerView mRecyclerViewPreview;

    private SubmitActivity mActivity;
    private Context mContext;
    private List<SupportDraftOrSubmit> mSubmitList;

    private PreviewItemAdapter mAdapter;
    private File mGoodsFile;
    private String mGoodsFilePath;
    private String mFilePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        ButterKnife.bind(this);

        mActivity = this;
        mContext = this;

        initToolbar();
        initData();
        initView();
    }


    private void initView() {

        LinearLayoutManager manager = new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPreview.setLayoutManager(manager);

        mRecyclerViewPreview.addItemDecoration(new RecycleViewDivider(mContext,
                LinearLayoutManager.HORIZONTAL, 10, Color.WHITE));

        mAdapter = new PreviewItemAdapter(mContext, mActivity,
                (ArrayList<SupportDraftOrSubmit>) mSubmitList, (support) -> {
            ToastUtils.singleToast("点击了条目");
            PreviewDetailActivity.actionStart(mContext, support,
                    PreviewDetailActivity.ACTION_SUBMIT_ITEM);
        });

        mRecyclerViewPreview.setAdapter(mAdapter);
    }


    private void initData() {
        mSubmitList = DataSupport.
                where(GlobalManager.LITE_CONDITION,GlobalManager.TYPE_SUBMIT_LITE).find(SupportDraftOrSubmit.class);

        for (int i = 0; i < mSubmitList.size(); i++) {
            Logger.i("------------" + mSubmitList.get(i).toString());

        }
        SortTime sortDraftTime = new SortTime();

        Collections.sort(mSubmitList, sortDraftTime);
        for (int i = 0; i < mSubmitList.size(); i++) {
            Logger.i("++++++++++++" + mSubmitList.get(i).toString());
        }


    }

    private void initToolbar() {

        setSupportActionBar(mToolbarPreview);

        TextView title_text_view = ActionBarTool.getInstance(mActivity, 992).getTitle_text_view();
        title_text_view.setText("提交列表");

        mToolbarPreview.setNavigationIcon(R.drawable.back_up_logo);
        mToolbarPreview.setNavigationOnClickListener(v -> finish());
    }

    /**
     * @param photoPath 得到缩小的Bitmap
     * @return
     */
    private Bitmap getBitmap(String photoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);

        ImageProcessor processor = new ImageProcessor(bitmap);
        return processor.scale((float) 0.25);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ACache.get(this).put("preview", (ArrayList<SerializableNumber>) mNumberList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_preview_7:
                ToastUtils.singleToast("清除七天前记录");
                deleteInfos(7);
                break;
            case R.id.delete_preview_15:
                ToastUtils.singleToast("清除15天前记录");
                deleteInfos(15);

                break;
            case R.id.delete_preview_30:
                ToastUtils.singleToast("清除30天前记录");
                deleteInfos(30);
                break;
            case R.id.delete_preview_all:
                ToastUtils.singleToast("清空所有记录");
                DeleteHelper.deleteAllInfos(mContext, GlobalManager.TYPE_SUBMIT_LITE,mAdapter);
                break;

            default:
                break;
        }
        return true;
    }


    /**
     * 清除几天之前的记录
     */
    private void deleteInfos(int day) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("清除" + day + "天之前本地保存的拍摄数据");
        dialog.setMessage("点击“确定”将删除" + day + "天以前的拍摄记录" + "\"" +
                "点击“取消”将取消删除操作");
        dialog.setCancelable(false);
        dialog.setPositiveButton(getString(R.string.dialog_messge_OK), (dialog1, which) -> {
            String currentTimeToDate = TimeUtil.getCurrentTimeToDate();
            List<SupportDraftOrSubmit> photoLiteList = findAll(SupportDraftOrSubmit.class);

            for (int i = 0; i < photoLiteList.size(); i++) {
                String shutTime = photoLiteList.get(i).getCurrent_time();
                int dayGap = TimeUtil.differentDaysByMillisecond(shutTime, currentTimeToDate);
                if (dayGap > day) {
                    DataSupport.deleteAll(SupportDraftOrSubmit.class, "shutTime = ?", shutTime);
                    //删除三张本地照片
                    /*FileUtils.deleteJpgPreview(photoLiteList.get(i).getPhotoPath1());
                    FileUtils.deleteJpgPreview(photoLiteList.get(i).getPhotoPath2());
                    FileUtils.deleteJpgPreview(photoLiteList.get(i).getPhotoPath3());
                    */
                }
            }
            List<SupportDraftOrSubmit> supportDraftList = DataSupport.
                    where("lite_type=?", GlobalManager.TYPE_SUBMIT_LITE).find(SupportDraftOrSubmit.class);

            mAdapter.updateListView(supportDraftList);

        });
        dialog.setNegativeButton(getString(R.string.dialog_message_Cancel), (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview_item_car_number:

                break;

            default:
                break;
        }
    }
}
