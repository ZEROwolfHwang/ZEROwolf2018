package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import com.zero.wolf.greenroad.tools.SPUtils;
import com.zero.wolf.greenroad.tools.ToastUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DraftActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar_draft)
    Toolbar mToolbarPreview;
    @BindView(R.id.recycler_view_preview)
    RecyclerView mRecyclerViewPreview;

    private DraftActivity mActivity;
    private Context mContext;


    private PreviewItemAdapter mAdapter;
    private File mGoodsFile;
    private String mGoodsFilePath;
    private String mFilePath;
    private List<SupportDraftOrSubmit> mDraftList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        ButterKnife.bind(this);

        mActivity = this;
        mContext = this;

        getIntentData();

        initToolbar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initView();
    }

    private void initView() {

        LinearLayoutManager manager = new LinearLayoutManager(mActivity,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPreview.setLayoutManager(manager);

        mRecyclerViewPreview.addItemDecoration(new RecycleViewDivider(mContext,
                LinearLayoutManager.HORIZONTAL, 10, Color.WHITE));

        mAdapter = new PreviewItemAdapter(mContext, mActivity, (ArrayList) mDraftList, support -> {
            PreviewDetailActivity.actionStart(mContext,  support,PreviewDetailActivity.ACTION_DRAFT_ITEM);
        });

        mRecyclerViewPreview.setAdapter(mAdapter);
    }


    private void initData() {

        mDraftList = DataSupport.where("lite_type=?", GlobalManager.TYPE_DRAFT_LITE).find(SupportDraftOrSubmit.class);


        for (int i = 0; i < mDraftList.size(); i++) {
            Logger.i("------------" + mDraftList.get(i).toString());

        }
        SortTime sortDraftTime = new SortTime();

        Collections.sort(mDraftList, sortDraftTime);
       /* for (int i = 0; i < mDraftList.size(); i++) {
            Logger.i("++++++++++++" + mDraftList.get(i).toString());
        }*/

    }

    private void initToolbar() {

        setSupportActionBar(mToolbarPreview);


        TextView title_text_view = ActionBarTool.getInstance(mActivity, 992).getTitle_text_view();
        title_text_view.setText("草稿列表");

        mToolbarPreview.setNavigationIcon(R.drawable.back_up_logo);
        mToolbarPreview.setNavigationOnClickListener(v -> finish());
    }

    /**
     * 创建并传递数据
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DraftActivity.class);
       /* Bundle bundle = new Bundle();
        bundle.putParcelable(MDRAFT_CONFIG, configInfoBean);
        bundle.putParcelable(MAIN2DRAFT_DETAIL, detailInfoBean);*/

        //bundle.putParcelableArrayList("myBitmapList", (ArrayList<? extends Parcelable>) myBitmaps);
        // intent.putExtras(bundle);
        //      intent.setType(type);
        context.startActivity(intent);
    }

    /**
     * 得到从上一个activity中拿到的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
      /*  Bundle bundle = intent.getExtras();
        mConfigInfoBean = bundle.getParcelable(MAIN2DRAFT_CONFIG);
        mDetailInfoBean = bundle.getParcelable(MAIN2DRAFT_DETAIL);
        Logger.i(mDetailInfoBean.toString());
        Logger.i(mConfigInfoBean.toString());*/

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
                DeleteHelper.deleteInfos(this,GlobalManager.TYPE_DRAFT_LITE,SPUtils.MATH_DRAFT_LITE,7,mAdapter);
                break;
            case R.id.delete_preview_15:
                ToastUtils.singleToast("清除15天前记录");
                DeleteHelper.deleteInfos(this,GlobalManager.TYPE_DRAFT_LITE,SPUtils.MATH_DRAFT_LITE,15,mAdapter);
                break;
            case R.id.delete_preview_30:
                ToastUtils.singleToast("清除30天前记录");
                DeleteHelper.deleteInfos(this,GlobalManager.TYPE_DRAFT_LITE,SPUtils.MATH_DRAFT_LITE,30,mAdapter);
                break;
            case R.id.delete_preview_all:
                ToastUtils.singleToast("清空所有记录");
                DeleteHelper.deleteAllInfos(mContext, GlobalManager.TYPE_DRAFT_LITE,SPUtils.MATH_DRAFT_LITE, mAdapter);

                break;
            default:
                break;
        }
        return true;
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
