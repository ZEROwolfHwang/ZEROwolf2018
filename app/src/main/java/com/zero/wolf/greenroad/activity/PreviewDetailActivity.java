package com.zero.wolf.greenroad.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zero.wolf.greenroad.R;
import com.zero.wolf.greenroad.adapter.DetailsRecyclerAdapter;
import com.zero.wolf.greenroad.bean.PathTitleBean;
import com.zero.wolf.greenroad.fragment.MyBitmap;
import com.zero.wolf.greenroad.litepalbean.SupportChecked;
import com.zero.wolf.greenroad.litepalbean.SupportDetail;
import com.zero.wolf.greenroad.litepalbean.SupportDraftOrSubmit;
import com.zero.wolf.greenroad.litepalbean.SupportScan;
import com.zero.wolf.greenroad.tools.ActionBarTool;
import com.zero.wolf.greenroad.tools.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviewDetailActivity extends BaseActivity {

    public static String ACTION_DRAFT_ITEM = "action_draft_item";
    public static String ACTION_SUBMIT_ITEM = "action_submit_item";
    private static String SUPPORTDRAFT_ITEM = "supportdraft_item";
    private static String SUPPORTSUBMIT_ITEM = "supportsubmit_item";
    @BindView(R.id.toolbar_preview_detail)
    Toolbar mToolbarPreviewDetail;
    @BindView(R.id.detail_activity_recycler_photo)
    RecyclerView mDetailActivityRecyclerPhoto;
    @BindView(R.id.export_number)
    TextView mExportNumber;
    @BindView(R.id.text_table_1)
    TextView mTextTable1;
    @BindView(R.id.text_table_2)
    TextView mTextTable2;
    @BindView(R.id.text_table_3)
    TextView mTextTable3;
    @BindView(R.id.text_table_4)
    TextView mTextTable4;
    @BindView(R.id.text_table_5)
    TextView mTextTable5;
    @BindView(R.id.text_table_6)
    TextView mTextTable6;
    @BindView(R.id.text_table_7)
    TextView mTextTable7;
    @BindView(R.id.text_table_8)
    TextView mTextTable8;
    @BindView(R.id.text_table_9)
    TextView mTextTable9;
    @BindView(R.id.text_table_10)
    TextView mTextTable10;
    @BindView(R.id.text_table_11)
    TextView mTextTable11;
    @BindView(R.id.text_table_12)
    TextView mTextTable12;
    @BindView(R.id.checked_conclusion_text)
    TextView mCheckedConclusionText;
    @BindView(R.id.checked_description_text)
    EditText mCheckedDescriptionText;
    @BindView(R.id.pick_001)
    TextView mPick001;
    @BindView(R.id.pick_002)
    TextView mPick002;
    @BindView(R.id.pick_003)
    TextView mPick003;
    @BindView(R.id.pick_004)
    TextView mPick004;
    @BindView(R.id.pick_005)
    TextView mPick005;
    @BindView(R.id.pick_006)
    TextView mPick006;
    @BindView(R.id.check_001)
    TextView mCheck001;
    @BindView(R.id.check_002)
    TextView mCheck002;
    @BindView(R.id.draft_save_time)
    TextView mDraftSaveTime;
    @BindView(R.id.draft_save_edit)
    TextView mDraftSaveEdit;
    private SupportDraftOrSubmit mCurrentSupport;
    private LinearLayoutManager mLayoutManager;
    private DetailsRecyclerAdapter mAdapter;
    private List<PathTitleBean> mPath_cheshen;
    private static ArrayList<MyBitmap> mBitmapArrayList;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_detail);
        ButterKnife.bind(this);

        initToolbar();

        getIntentData();

        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        if (mBitmapArrayList == null) {
            mBitmapArrayList = new ArrayList<>();
        } else {
            mBitmapArrayList.clear();
        }
        List<String> picturePaths = null;
        if (picturePaths == null) {
            picturePaths = mCurrentSupport.getSupportDetail().getPicturePath();
        } else {
            picturePaths.clear();
            picturePaths = mCurrentSupport.getSupportDetail().getPicturePath();
        }
        List<String> pictureTitles = null;
        if (pictureTitles == null) {
            pictureTitles = mCurrentSupport.getSupportDetail().getPictureTitle();
        } else {
            pictureTitles.clear();
            pictureTitles = mCurrentSupport.getSupportDetail().getPictureTitle();
        }
        List<String> finalPicturePaths = picturePaths;
        List<String> finalPictureTitles = pictureTitles;
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mDetailActivityRecyclerPhoto.setLayoutManager(mLayoutManager);
        mAdapter = new DetailsRecyclerAdapter(getContext(), mBitmapArrayList, () -> {

        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (finalPicturePaths != null && finalPicturePaths.size() != 0) {
                    for (int i = 0; i < finalPicturePaths.size(); i++) {
                        Bitmap bitmap = BitmapUtil.convertToBitmap(finalPicturePaths.get(i), 800, 800);
                        String title = finalPictureTitles.get(i);
                        MyBitmap myBitmap = new MyBitmap(finalPicturePaths.get(i), bitmap, title);
                        mBitmapArrayList.add(myBitmap);
                    }
                }
                mDetailActivityRecyclerPhoto.post(() -> {
                    mDetailActivityRecyclerPhoto.setAdapter(mAdapter);
                });
            }
        }).start();
//        mRecyclerViewShootPhoto.scrollToPosition(3);
        // scrollToPosition(mLayoutManager,3);
    }

    private void initView() {
        SupportDetail supportDetail = mCurrentSupport.getSupportDetail();
        SupportChecked supportChecked = mCurrentSupport.getSupportChecked();
        SupportScan supportScan = mCurrentSupport.getSupportScan();

        mDraftSaveTime.setText(mCurrentSupport.getCurrent_time());

        //采集信息的条目
        mPick001.setText(supportDetail.getLane());
        mPick002.setText(supportChecked.getSiteCheck());
        mPick003.setText(supportChecked.getSiteLogin());
        mPick004.setText(supportDetail.getNumber());
        mPick005.setText(supportDetail.getColor());
        mPick006.setText(supportDetail.getGoods());

        //扫描的条目
        mExportNumber.setText(supportScan.getScan_code());
        mTextTable1.setText(supportScan.getScan_01Q());
        mTextTable2.setText(supportScan.getScan_02Q());
        mTextTable3.setText(supportScan.getScan_03Q());
        mTextTable4.setText(supportScan.getScan_04Q());
        mTextTable5.setText(supportScan.getScan_05Q());
        mTextTable6.setText(supportScan.getScan_06Q());
        mTextTable7.setText(supportScan.getScan_07Q());
        mTextTable8.setText(supportScan.getScan_08Q());
        mTextTable9.setText(supportScan.getScan_09Q());
        mTextTable10.setText(supportScan.getScan_10Q());
        mTextTable11.setText(supportScan.getScan_11Q());
        mTextTable12.setText(supportScan.getScan_12Q());

        //检查结论的条目
        mCheck001.setText(supportChecked.getIsRoom() == 0 ? "否" : "是");
        mCheck002.setText(supportChecked.getIsFree() == 0 ? "否" : "是");
        mCheckedConclusionText.setText(supportChecked.getConclusion());
        mCheckedDescriptionText.setText(supportChecked.getDescription());
    }

    private void initToolbar() {
        setSupportActionBar(mToolbarPreviewDetail);
        TextView title_text_view = ActionBarTool.getInstance(this, 991).getTitle_text_view();
        title_text_view.setText("草稿详情页");
        mToolbarPreviewDetail.setNavigationIcon(R.drawable.back_up_logo);
        mToolbarPreviewDetail.setNavigationOnClickListener(v -> finish());

    }


    private void getIntentData() {
        Intent intent = getIntent();
        if (ACTION_DRAFT_ITEM.equals(intent.getAction())) {
            mDraftSaveEdit.setVisibility(View.VISIBLE);
        } else {
            if (ACTION_SUBMIT_ITEM.equals(intent.getAction())) {
                mDraftSaveEdit.setVisibility(View.GONE);
            }
        }
        mCurrentSupport = intent.getParcelableExtra(SUPPORTDRAFT_ITEM);
    }

    public static void actionStart(Context context, SupportDraftOrSubmit support, String action) {
        Intent intent = new Intent(context, PreviewDetailActivity.class);
        intent.setAction(action);
        intent.putExtra(SUPPORTDRAFT_ITEM, support);
        context.startActivity(intent);
    }

    @OnClick(R.id.draft_save_edit)
    public void onClick(View view) {
        SupportDetail supportDetail = mCurrentSupport.getSupportDetail();
        SupportScan supportScan = mCurrentSupport.getSupportScan();
        SupportChecked supportChecked = mCurrentSupport.getSupportChecked();

     /*   for (int i = 0; i < supportMedia.getPaths().size(); i++) {
        Logger.i(supportMedia.getPaths().get(i) + "---" +
                supportMedia.getDurations().get(i) + "---" +
                supportMedia.getHeights().get(i) + "---" +
                supportMedia.getMimeTypes().get(i) + "---" +
                supportMedia.getNums().get(i) + "---" +
                supportMedia.getPictureTypes().get(i) + "---" +
                supportMedia.getPositions().get(i) + "---" +
                supportMedia.getWidths().get(i));
        }*/
        ShowActivity.actionStart(PreviewDetailActivity.this, supportDetail, supportScan, supportChecked, mCurrentSupport.getLite_ID());
    }

    public static void setPictureLisener(PictureListener listener) {
        listener.onPicture(mBitmapArrayList);
    }

    public interface PictureListener {
        void onPicture(List<MyBitmap> myBitmapList);
    }
}
